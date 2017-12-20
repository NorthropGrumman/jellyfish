package com.ngc.seaside.bootstrap.service.impl.repositoryservice;

import com.google.common.base.Preconditions;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.repository.api.IRepositoryService;
import com.ngc.seaside.bootstrap.service.repository.api.RepositoryServiceException;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.building.DefaultSettingsBuilder;
import org.apache.maven.settings.building.DefaultSettingsBuildingRequest;
import org.apache.maven.settings.building.SettingsBuildingException;
import org.apache.maven.settings.io.DefaultSettingsReader;
import org.apache.maven.settings.io.DefaultSettingsWriter;
import org.apache.maven.settings.validation.DefaultSettingsValidator;
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
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This implementation uses maven's .m2 local repository in combination with nexusConsolidated (found in gradle.properties) for the remote repository.
 */
@Component(service = IRepositoryService.class)
public class RepositoryService implements IRepositoryService {
   // <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>,
   private static final Pattern ARTIFACT_IDENTIFIER = Pattern.compile(
      "(?<groupId>[^:\\s@]+)"
         + ":(?<artifactId>[^:\\s@]+)"
         + "(?::(?<extension>[^:\\s@]+)"
         + "(?::(?<classifier>[^:\\s@]+))?)?"
         + ":(?<version>\\d+(?:\\.\\d+)*(?:-SNAPSHOT)?)");
   public static final String MAVEN_ENV = "M2_HOME";
   static final String GRADLE_USER_HOME = "GRADLE_USER_HOME";
   static final String NEXUS_CONSOLIDATED = "nexusConsolidated";
   static final String NEXUS_USERNAME = "nexusUsername";
   static final String NEXUS_PASSWORD = "nexusPassword";

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
      CollectRequest request = new CollectRequest();
      request.setRoot(new Dependency(baseArtifact, null));
      request.setRepositories(remoteRepositories);

      DependencyRequest dependencyRequest = new DependencyRequest(request, null);
      DependencyResult result;
      try {
         result = repositorySystem.resolveDependencies(session, dependencyRequest);
      } catch (Exception e) {
         logService.error(RepositoryService.class, "Unable to retrieve artifact dependencies for " + identifier, e);
         throw new RepositoryServiceException(e);
      }
      result.getCollectExceptions().forEach(exception -> logService.error(RepositoryService.class, exception));
      if (!result.getCollectExceptions().isEmpty()) {
         throw new RepositoryServiceException("Unable to retrieve artifact dependencies for " + identifier);
      }
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
                      .peek(artifactResult -> {
                         if (artifactResult.isMissing() || !artifactResult.isResolved()) {
                            artifactResult.getExceptions()
                                          .forEach(exception -> logService.error(
                                             RepositoryService.class, exception));
                            throw new RepositoryServiceException(
                               "Unable to retrieve transitive dependency " + artifactResult + " for " + identifier);
                         }
                      })
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
      DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
      locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
      locator.addService(TransporterFactory.class, FileTransporterFactory.class);
      locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
      this.repositorySystem = locator.getService(RepositorySystem.class);

      DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
      Optional<Path> mavenLocalRepo = findMavenLocal();
      if (mavenLocalRepo.isPresent()) {
         LocalRepository localRepository = new LocalRepository(mavenLocalRepo.get().toFile());
         session.setLocalRepositoryManager(repositorySystem.newLocalRepositoryManager(session, localRepository));
      } else {
         logService.warn(RepositoryService.class, "Unable to find maven local repository");
      }

      Optional<RemoteRepository> nexusRemoteRepo = findRemoteNexus();
      if (nexusRemoteRepo.isPresent()) {
         remoteRepositories.add(nexusRemoteRepo.get());
      } else {
         logService.warn(RepositoryService.class, "Unable to find Nexus remote repository");
      }

      if (!mavenLocalRepo.isPresent() && !nexusRemoteRepo.isPresent()) {
         logService.error(RepositoryService.class, "Unable to find any local or remote repositories");
         throw new RepositoryServiceException("Unable to find any local or remote repositories");
      }

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

   /**
    * Returns a remote repository to Nexus. This repository is found using the variable {@value #NEXUS_CONSOLIDATED} with optionally {@value #NEXUS_USERNAME} and {@value #NEXUS_PASSWORD}. These
    * variables are
    * determined using the rules in the following order:
    * 
    * <ol>
    * <li>From {@link System#getProperty(String)}</li>
    * <li>From {@code gradle.properties} located in the current working directory</li>
    * <li>From {@code gradle.properties} located from the property {@value #GRADLE_USER_HOME}
    * <li>From {@code gradle.properties} located in the {@code <user.home>/.gradle}</li>
    * <li>From {@link System#getenv(String)}</li>
    * </ol>
    * 
    * @return the remote repository to Nexus, or {@link Optional#empty()} if it cannot be determined
    */
   Optional<RemoteRepository> findRemoteNexus() {
      Properties properties = new Properties();
      String gradleUserHome = System.getProperty(GRADLE_USER_HOME, System.getenv(GRADLE_USER_HOME));
      Path gradlePropertiesFile = null;
      if (gradleUserHome == null) {
         String userHome = System.getProperty("user.home");
         if (userHome != null) {
            gradlePropertiesFile = Paths.get(userHome, ".gradle", "gradle.properties");
         }
      } else {
         gradlePropertiesFile = Paths.get(gradleUserHome, "gradle.properties");
      }
      if (gradlePropertiesFile != null && Files.isRegularFile(gradlePropertiesFile)) {
         try {
            properties.load(Files.newBufferedReader(gradlePropertiesFile));
         } catch (IOException e) {
            logService.warn(RepositoryService.class,
               "Unable to load " + gradlePropertiesFile + ": " + e.getMessage());
         }
      }
      Path cwdPropertiesFile = Paths.get("gradle.properties");
      if (Files.isRegularFile(cwdPropertiesFile)) {
         try {
            properties.load(Files.newBufferedReader(cwdPropertiesFile));
         } catch (IOException e) {
            logService.warn(RepositoryService.class, "Unable to load " + cwdPropertiesFile + ": " + e.getMessage());
         }
      }

      String nexusConsolidated = System.getProperty(NEXUS_CONSOLIDATED,
         properties.getProperty(NEXUS_CONSOLIDATED, System.getenv(NEXUS_CONSOLIDATED)));
      String nexusUsername = System.getProperty(NEXUS_USERNAME,
         properties.getProperty(NEXUS_USERNAME, System.getenv(NEXUS_USERNAME)));
      String nexusPassword = System.getProperty(NEXUS_PASSWORD,
         properties.getProperty(NEXUS_PASSWORD, System.getenv(NEXUS_PASSWORD)));

      if (nexusConsolidated == null) {
         logService.warn(RepositoryService.class,
            "Unable to find " + NEXUS_CONSOLIDATED
               + " from system properties, gradle.properties, or system environment variables");
         return Optional.empty();
      }

      RemoteRepository.Builder builder = new RemoteRepository.Builder("central", "default", nexusConsolidated);
      if (nexusUsername != null && nexusPassword != null) {
         builder.setAuthentication(new AuthenticationBuilder().addUsername(nexusUsername)
                                                              .addPassword(nexusPassword)
                                                              .build());
      }
      return Optional.of(builder.build());
   }

   /**
    * Returns the path to the maven local directory. This directory is determined using the rules in the following order:
    * 
    * <ol>
    * <li>From maven user settings.xml found in {@code <user.home>/.m2</li>
    * <li>From maven global settings.xml found in {@code <M2_HOME>/conf}</li>
    * <li>{@code <user.home>/.m2/repository}</li>
    * </ol>
    * 
    * @return the path to the local maven repository, or {@link Optional#empty()} if it cannot be determined
    */
   Optional<Path> findMavenLocal() {
      DefaultSettingsBuildingRequest settingsRequest = new DefaultSettingsBuildingRequest();
      String m2Home = System.getProperty(MAVEN_ENV, System.getenv(MAVEN_ENV));
      if (m2Home != null) {
         Path globalMavenSettings = Paths.get(m2Home, "conf", "settings.xml");
         if (Files.isRegularFile(globalMavenSettings)) {
            settingsRequest.setGlobalSettingsFile(globalMavenSettings.toFile());
         }
      }
      String userHome = System.getProperty("user.home");
      if (userHome != null) {
         Path userMavenSettings = Paths.get(userHome, ".m2", "settings.xml");
         if (Files.isRegularFile(userMavenSettings)) {
            settingsRequest.setUserSettingsFile(userMavenSettings.toFile());
         }
      }
      settingsRequest.setUserProperties(System.getProperties());
      Settings settings;
      try {
         settings = new DefaultSettingsBuilder().setSettingsReader(new DefaultSettingsReader())
                                                .setSettingsWriter(new DefaultSettingsWriter())
                                                .setSettingsValidator(new DefaultSettingsValidator())
                                                .build(settingsRequest)
                                                .getEffectiveSettings();
      } catch (SettingsBuildingException e) {
         return Optional.empty();
      }
      String localRepository = settings.getLocalRepository();
      Path userMavenRepo;
      if (localRepository == null) {
         if (userHome != null) {
            userMavenRepo = Paths.get(userHome, ".m2", "repository");
         } else {
            userMavenRepo = null;
         }
      } else {
         userMavenRepo = Paths.get(localRepository);
      }
      if (userMavenRepo == null || !Files.isDirectory(userMavenRepo)) {
         return Optional.empty();
      }

      return Optional.of(userMavenRepo);
   }

}
