/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.service.impl.m2repositoryservice;

import com.google.common.base.Preconditions;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.service.repository.api.IRepositoryService;
import com.ngc.seaside.systemdescriptor.service.repository.api.RepositoryServiceException;

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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This implementation uses maven's .m2 local repository in combination with nexusConsolidated
 * (found in gradle.properties) for the remote repository.
 */
public class RepositoryService implements IRepositoryService {

   static final String NEXUS_CONSOLIDATED = "nexusConsolidated";
   static final String SYSTEM_PROPERTY_PREFIX = "systemProp.";
   private static final String TRUST_STORE_PROPERTY = "javax.net.ssl.trustStore";

   // <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>,
   private static final Pattern ARTIFACT_IDENTIFIER = Pattern.compile(
         "(?<groupId>[^:\\s@]+)"
         + ":(?<artifactId>[^:\\s@]+)"
         + "(?::(?<extension>[^:\\s@]+)"
         + "(?::(?<classifier>[^:\\s@]+))?)?"
         + ":(?<version>\\d+(?:\\.\\d+)*(?:-SNAPSHOT)?)");
   private static final String MAVEN_ENV = "M2_HOME";
   private static final String MAVEN_PROPERTY_NAME = "maven.home";
   private static final String NEXUS_USERNAME = "nexusUsername";
   private static final String NEXUS_PASSWORD = "nexusPassword";
   private static final String USER_HOME_PROPERTY_NAME = "user.home";

   private final List<RemoteRepository> remoteRepositories = new ArrayList<>();
   private boolean initialized = false;
   private RepositorySystem repositorySystem;
   private RepositorySystemSession session;
   private GradlePropertiesService propertiesService;

   private ILogService logService;

   @Override
   public Path getArtifact(String identifier) {
      Preconditions.checkNotNull(identifier, "identifier may not be null!");
      Preconditions.checkArgument(ARTIFACT_IDENTIFIER.matcher(identifier).matches(),
                                  "invalid identifier: " + identifier);

      // Defer initialization until necessary.  We do this because this service might be needed for dependencies
      // but might not actually be used.  This is the case if this bundle is deployed in Eclipse.
      initalizeIfNecessary();

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

      // Defer initialization until necessary.  We do this because this service might be needed for dependencies
      // but might not actually be used.  This is the case if this bundle is deployed in Eclipse.
      initalizeIfNecessary();

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
                  return !(Objects.equals(artifact.getGroupId(), baseArtifact.getGroupId())
                           && Objects.equals(artifact.getArtifactId(), baseArtifact.getArtifactId())
                           && Objects.equals(artifact.getClassifier(), baseArtifact.getClassifier())
                           && Objects.equals(artifact.getExtension(), baseArtifact.getExtension()));
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

   /**
    * Activates this component.
    */
   public void activate() {
      logService.trace(getClass(), "activated");
   }

   /**
    * Deactivates this component.
    */
   public void deactivate() {
      this.remoteRepositories.clear();
      this.repositorySystem = null;
      this.session = null;
      logService.trace(getClass(), "deactivated");
   }

   /**
    * Sets the log service this component will use.
    *
    * @param ref the log service this component will use
    */
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   /**
    * Removes the log service this component will use.
    *
    * @param ref the log service this component will use
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   /**
    * Sets the property service.
    *
    * @param ref the property service
    */
   public void setPropertiesService(GradlePropertiesService ref) {
      this.propertiesService = ref;
   }

   /**
    * Removes the property service.
    *
    * @param ref the property service
    */
   public void removePropertiesService(GradlePropertiesService ref) {
      setPropertiesService(null);
   }

   /**
    * Returns a remote repository to Nexus. This repository is found using the variable {@value #NEXUS_CONSOLIDATED}
    * with optionally {@value #NEXUS_USERNAME} and {@value #NEXUS_PASSWORD}. These variables are resolved using
    * {@link GradlePropertiesService#getProperty(String)}.
    *
    * @return the remote repository to Nexus, or {@link Optional#empty()} if it cannot be determined
    */
   Optional<RemoteRepository> findRemoteNexus() {
      Optional<String> nexusConsolidated = propertiesService.getProperty(NEXUS_CONSOLIDATED);
      Optional<String> nexusUsername = propertiesService.getProperty(NEXUS_USERNAME);
      Optional<String> nexusPassword = propertiesService.getProperty(NEXUS_PASSWORD);

      if (!nexusConsolidated.isPresent()) {
         logService.warn(RepositoryService.class, "Unable to find value for " + NEXUS_CONSOLIDATED);
         return Optional.empty();
      }

      RemoteRepository.Builder builder = new RemoteRepository.Builder("central", "default", nexusConsolidated.get());
      if (nexusUsername.isPresent() && nexusPassword.isPresent()) {
         builder.setAuthentication(new AuthenticationBuilder().addUsername(nexusUsername.get())
                                         .addPassword(nexusPassword.get())
                                         .build());
      }

      return Optional.of(builder.build());
   }

   /**
    * Returns the path to the maven local directory. This directory is determined using the rules in the following
    * order:
    * <ol>
    * <li>From maven user settings.xml found in {@value #USER_HOME_PROPERTY_NAME}/.m2</li>
    * <li>From maven global settings.xml found in {@value #MAVEN_PROPERTY_NAME}/conf</li>
    * <li>From maven global settings.xml found in {@value #MAVEN_ENV}/conf</li>
    * <li>{@value #USER_HOME_PROPERTY_NAME}/.m2/repository</li>
    * </ol>
    *
    * @return the path to the local maven repository, or {@link Optional#empty()} if it cannot be determined
    */
   Optional<Path> findMavenLocal() {
      DefaultSettingsBuildingRequest settingsRequest = new DefaultSettingsBuildingRequest();
      String m2Home = System.getProperty(MAVEN_PROPERTY_NAME, System.getProperty(MAVEN_ENV, System.getenv(MAVEN_ENV)));
      if (m2Home != null) {
         Path globalMavenSettings = Paths.get(m2Home, "conf", "settings.xml");
         if (Files.isRegularFile(globalMavenSettings)) {
            settingsRequest.setGlobalSettingsFile(globalMavenSettings.toFile());
         }
      }
      String userHome = System.getProperty(USER_HOME_PROPERTY_NAME);
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

   private synchronized void initalizeIfNecessary() {
      if (!this.initialized) {
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

         Optional<String> trustStore = propertiesService.getProperty(SYSTEM_PROPERTY_PREFIX + TRUST_STORE_PROPERTY);
         if (trustStore.isPresent() && System.getProperty(TRUST_STORE_PROPERTY) == null) {
            System.setProperty(TRUST_STORE_PROPERTY, trustStore.get());
         }

         this.session = session;
         this.initialized = true;
      }
   }
}
