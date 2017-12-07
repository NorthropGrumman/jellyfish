package com.ngc.seaside.bootstrap.service.impl.repositoryservice;

import com.google.common.base.Preconditions;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.repository.api.IRepositoryService;
import com.ngc.seaside.bootstrap.service.repository.api.RepositoryServiceException;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This implementation uses maven's .m2 local repository in combination with nexusConsolidated (found in gradle.properties) for the remote repository.
 */
@Component(service = IRepositoryService.class)
public class RepositoryService implements IRepositoryService {
//<groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>, 
   private static final Pattern ARTIFACT_IDENTIFIER = Pattern.compile("[^:\\s@]+:[^:\\s@]+(?:[^:\\s@]+(?:[^:\\s@]+)):\\d+(?:\\.\\d+)*(?:-SNAPSHOT)?");
   private static final String NEXUS_CONSOLIDATED = "nexusConsolidated";
   private static final String NEXUS_USERNAME = "nexusUsername";
   private static final String NEXUS_PASSWORD = "nexusPassword";
   private static final Path MAVEN_LOCAL_REPO = Paths.get(System.getProperty("user.home"), ".m2", "repository");
   private static final Path GRADLE_USER_DIR = Paths.get(System.getProperty("user.home"), ".gradle", "gradle.properties");

   private ILogService logService;
   private final List<RemoteRepository> remoteRepositories = new ArrayList<>();
   private RepositorySystem repositorySystem;
   private RepositorySystemSession session;

   @Override
   public Path getArtifact(String identifier) {
      Preconditions.checkNotNull(identifier, "identifier may not be null!");
      Preconditions.checkArgument(ARTIFACT_IDENTIFIER.matcher(identifier).matches(),
         "invalid identifier: " + identifier);

      ArtifactRequest request = new ArtifactRequest();
      request.setArtifact(new DefaultArtifact(identifier));
      request.setRepositories(remoteRepositories);

      ArtifactResult result;
      try {
         result = repositorySystem.resolveArtifact(session, request);
      } catch (Exception e) {
         throw new RepositoryServiceException(e);
      }

      Artifact artifact = result.getArtifact();
      if (artifact == null || artifact.getFile() == null) {
         result.getExceptions().forEach(exception -> logService.error(RepositoryService.class, exception));
         throw new RepositoryServiceException("Unable to resolve artifact " + identifier);
      }

      return result.getArtifact().getFile().toPath();
   }

   @Override
   public Set<Path> getArtifactDependencies(String identifier, boolean transitive) {
      Preconditions.checkNotNull(identifier, "identifier may not be null!");
      Preconditions.checkArgument(ARTIFACT_IDENTIFIER.matcher(identifier).matches(),
         "invalid identifier: " + identifier);
      Artifact baseArtifact = new DefaultArtifact(identifier);
      logService.info(getClass(), "********** %s, %s", baseArtifact, baseArtifact.getFile());
      CollectRequest request = new CollectRequest();
      request.setRoot(new Dependency(baseArtifact, null));
      request.setRepositories(remoteRepositories);

      DependencyRequest dependencyRequest = new DependencyRequest(request, null);
      DependencyResult result;
      try {
         result = repositorySystem.resolveDependencies(session, dependencyRequest);
      } catch (Exception e) {
         throw new RepositoryServiceException(e);
      }
      logService.info(getClass(), "######### %s, %s", baseArtifact, baseArtifact.getFile());
      result.getCollectExceptions().forEach(exception -> logService.error(RepositoryService.class, exception));
      if (transitive) {
         return result.getArtifactResults()
                      .stream()
                      .filter(artifactResult -> artifactResult.getArtifact() != null)
                      .filter(artifactResult -> {
                         Artifact artifact = artifactResult.getArtifact();
                         return !(Objects.equals(artifact.getGroupId(), baseArtifact.getGroupId()) &&
                         Objects.equals(artifact.getArtifactId(), baseArtifact.getArtifactId()) &&
                         Objects.equals(artifact.getClassifier(), baseArtifact.getClassifier()) &&
                         Objects.equals(artifact.getExtension(), baseArtifact.getExtension()));
                      })
                      .peek(artifactResult -> artifactResult.getExceptions()
                                                            .forEach(exception -> logService.error(
                                                               RepositoryService.class, exception)))
                      .map(ArtifactResult::getArtifact)
                      .map(Artifact::getFile)
                      .filter(file -> file != null)
                      .map(File::toPath)
                      .collect(Collectors.toCollection(LinkedHashSet::new));
      } else {
         return result.getRoot()
                      .getChildren()
                      .stream()
                      .map(DependencyNode::getArtifact)
                      .filter(artifact -> artifact != null)
                      .map(Artifact::getFile)
                      .filter(file -> file != null)
                      .map(File::toPath)
                      .collect(Collectors.toCollection(LinkedHashSet::new));
      }
   }

   @Activate
   public void activate() {
      Properties properties = new Properties();
      try {
         properties.load(Files.newBufferedReader(GRADLE_USER_DIR));

         String nexusConsolidated = properties.getProperty(NEXUS_CONSOLIDATED);

         if (nexusConsolidated == null) {
            logService.warn(RepositoryService.class,
               "Missing " + NEXUS_CONSOLIDATED + " property from " + GRADLE_USER_DIR);
         } else {
            String nexusUsername = properties.getProperty(NEXUS_USERNAME);
            String nexusPassword = properties.getProperty(NEXUS_PASSWORD);
            RemoteRepository.Builder builder = new RemoteRepository.Builder("central", "default", nexusConsolidated);
            if (nexusUsername != null && nexusPassword != null) {
               builder.setAuthentication(new AuthenticationBuilder().addUsername(nexusUsername)
                                                                    .addPassword(nexusPassword)
                                                                    .build());
            }
            remoteRepositories.add(builder.build());
         }

      } catch (IOException e) {
         logService.warn(RepositoryService.class, "Unable to load " + GRADLE_USER_DIR);
      }
      
      DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
      locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
      locator.addService(TransporterFactory.class, FileTransporterFactory.class);
      locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
      this.repositorySystem = locator.getService(RepositorySystem.class);

      DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
      LocalRepository localRepository = new LocalRepository(MAVEN_LOCAL_REPO.toFile());
      session.setLocalRepositoryManager(repositorySystem.newLocalRepositoryManager(session, localRepository));
      this.session = session;
      
      logService.trace(getClass(), "activated");
   }

   @Deactivate
   public void deactivate() {
      this.remoteRepositories.clear();
      this.repositorySystem = null;
      this.session = null;
      logService.trace(getClass(), "deactivated");
   }

   /**
    * Sets log service.
    */
   @Reference(unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

}
