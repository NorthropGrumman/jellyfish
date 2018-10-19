/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.cli.command.createjellyfishgradleproject;

import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IParameter;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.cli.command.createjellyfishgradleproject.dto.GradleProjectDto;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.CommonDependencies;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.DependencyScope;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildDependency;
import com.ngc.seaside.jellyfish.service.execution.api.JellyfishExecutionException;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.utilities.command.AbstractJellyfishCommand;
import com.ngc.seaside.jellyfish.utilities.command.FileHeader;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

@Component(service = IJellyFishCommand.class)
public class CreateJellyFishGradleProjectCommand extends AbstractJellyfishCommand {

   private static final String NAME = "create-jellyfish-gradle-project";

   public static final String OUTPUT_DIR_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   public static final String GROUP_ID_PROPERTY = CommonParameters.GROUP_ID.getName();
   public static final String SYSTEM_DESCRIPTOR_GAV_PROPERTY = CommonParameters.GROUP_ARTIFACT_VERSION.getName();
   public static final String MODEL_NAME_PROPERTY = CommonParameters.MODEL.getName();
   public static final String DEPLOYMENT_MODEL_NAME_PROPERTY = CommonParameters.DEPLOYMENT_MODEL.getName();

   public static final String PROJECT_NAME_PROPERTY = CommonParameters.PROJECT_NAME.getName();
   public static final String VERSION_PROPERTY = CommonParameters.VERSION.getName();
   public static final String JELLYFISH_GRADLE_PLUGINS_VERSION_PROPERTY = "jellyfishGradlePluginsVersion";
   public static final String DEFAULT_GROUP_ID = "com.ngc.seaside";
   public static final String LICENSE_FILE_NAME = "LICENSE";

   public static final String GRADLE_USER_HOME_ENVIRONMENT_VARIABLE = "GRADLE_USER_HOME";

   public static final String DEFAULT_NEXUS_CONSOLIDATED = "http://10.207.42.137/nexus/repository/maven-public";
   public static final String DEFAULT_GRADLE_DISTRIBUTION = "4.9";

   /**
    * The name of the property in $JELLYFISH_USER_HOME/jellyfish.properties for identifying the default
    * Gradle distribution URL to use in generated Gradle projects.
    */
   public static final String JELLYFISH_USER_HOME_GRADLE_URL_PROPERTY = "jellyfish.generated.gradle.url";

   public CreateJellyFishGradleProjectCommand() {
      super(NAME);
   }

   @Activate
   public void activate() {
      logService.trace(getClass(), "Activated");
   }

   @Deactivate
   public void deactivate() {
      logService.trace(getClass(), "Deactivated");
   }

   @Override
   protected IUsage createUsage() {
      return new DefaultUsage(
            "Generates the root Gradle files for a new Jellyfish project",
            CommonParameters.OUTPUT_DIRECTORY.required(),
            CommonParameters.PROJECT_NAME.optional(),
            new DefaultParameter<>(JELLYFISH_GRADLE_PLUGINS_VERSION_PROPERTY)
                  .setDescription("The version of the Jellyfish Gradle plugins to use when generating the script."
                                  + "  Defaults to the current version of Jellyfish.")
                  .advanced(),
            CommonParameters.GROUP_ID.advanced(),
            CommonParameters.VERSION.optional(),
            CommonParameters.GROUP_ARTIFACT_VERSION.required(),
            CommonParameters.MODEL.required(),
            CommonParameters.DEPLOYMENT_MODEL.optional(),
            CommonParameters.HEADER_FILE.advanced(),
            CommonParameters.CLEAN.optional());
   }

   @Override
   protected void doRun() {
      IJellyFishCommandOptions commandOptions = getOptions();
      DefaultParameterCollection collection = new DefaultParameterCollection();
      collection.addParameters(commandOptions.getParameters().getAllParameters());

      final String projectName = collection.getParameter(PROJECT_NAME_PROPERTY).getStringValue();

      // Create project directory.
      final Path outputDirectory = Paths.get(collection.getParameter(OUTPUT_DIR_PROPERTY).getStringValue());
      final Path projectDirectory = outputDirectory.resolve(projectName);
      try {
         Files.createDirectories(projectDirectory);
      } catch (IOException e) {
         logService.error(CreateJellyFishGradleProjectCommand.class, e);
         throw new CommandException(e);
      }

      registerRequiredDependencies(commandOptions);

      String groupId = collection.containsParameter(GROUP_ID_PROPERTY)
                       ? collection.getParameter(GROUP_ID_PROPERTY).getStringValue()
                       : DEFAULT_GROUP_ID;
      GradleProjectDto dto = new GradleProjectDto()
            .setGroupId(groupId)
            .setProjectName(collection.getParameter(PROJECT_NAME_PROPERTY).getStringValue())
            .setSystemDescriptorGav(collection.getParameter(SYSTEM_DESCRIPTOR_GAV_PROPERTY).getStringValue())
            .setModelName(collection.getParameter(MODEL_NAME_PROPERTY).getStringValue())
            .setDeploymentModelName(getDeploymentModel(commandOptions))
            .setGradleDistributionUrl(getGradleDistributionUrl())
            .setBuildScriptDependencies(getBuildScriptDependencies(commandOptions))
            .setVersionProperties(getVersionProperties(commandOptions))
            .setProjects(getProjects(commandOptions))
               .setSystem(CommonParameters.evaluateBooleanParameter(commandOptions.getParameters(),
                        CommonParameters.SYSTEM.getName(), false));
      
      IParameter<?> version = collection.getParameter(VERSION_PROPERTY);
      if (version == null || version.getStringValue() == null || version.getStringValue().isEmpty()) {
         dto.setVersion("1.0.0-SNAPSHOT");
      } else {
         dto.setVersion(version.getStringValue());
      }
      configModelParts(dto);
      collection.addParameter(new DefaultParameter<>("dto", dto));

      boolean clean = CommonParameters.evaluateBooleanParameter(collection, CommonParameters.CLEAN.getName(), false);
      unpackDefaultTemplate(collection, projectDirectory, clean);

      @SuppressWarnings("unchecked")
      IParameter<FileHeader> headerParam = (IParameter<FileHeader>) addDefaultUnpackParameters(collection)
               .getParameter(AbstractJellyfishCommand.FILE_HEADER_TEMPLATE_VARIABLE);
      FileHeader header = headerParam == null ? null : headerParam.getValue();
      if (header != null) {
         Path license = projectDirectory.resolve(LICENSE_FILE_NAME);
         try {
            Files.write(license, header.getPlain().getBytes(StandardCharsets.UTF_8));
         } catch (IOException e) {
            throw new JellyfishExecutionException("Unable to create " + LICENSE_FILE_NAME + " file", e);
         }
      }
   }

   private String getGradleDistributionUrl() {
      String url = jellyfishUserService.getJellyfishUserProperties().get(JELLYFISH_USER_HOME_GRADLE_URL_PROPERTY);
      if (url == null) {
         String gradleUserHome = System.getProperty("GRADLE_USER_HOME_ENVIRONMENT_VARIABLE");
         if (gradleUserHome == null) {
            gradleUserHome = System.getenv(GRADLE_USER_HOME_ENVIRONMENT_VARIABLE);
            if (gradleUserHome == null) {
               gradleUserHome = System.getProperty("user.home") + File.separatorChar + ".gradle";
            }
         }
         Path gradleProperties = Paths.get(gradleUserHome, "gradle.properties");
         String nexusConsolidated = null;
         if (Files.isRegularFile(gradleProperties)) {
            Properties properties = new Properties();
            try {
               properties.load(Files.newBufferedReader(gradleProperties));
            } catch (IOException e) {
               // ignore
            }
            nexusConsolidated = properties.getProperty("nexusConsolidated");
         }
         if (nexusConsolidated == null) {
            nexusConsolidated = DEFAULT_NEXUS_CONSOLIDATED;
         }
         return nexusConsolidated + "/gradle/gradle/" + DEFAULT_GRADLE_DISTRIBUTION + "/gradle-"
                  + DEFAULT_GRADLE_DISTRIBUTION + "-bin.zip";
      }
      return url;
   }

   private void registerRequiredDependencies(IJellyFishCommandOptions commandOptions) {
      // These dependencies are required but are not registered directly by a template or command, so we do this here.
      buildManagementService.registerDependency(commandOptions,
                                                CommonDependencies.JELLYFISH_GRADLE_PLUGINS.getGropuId(),
                                                CommonDependencies.JELLYFISH_GRADLE_PLUGINS.getArtifactId());
      buildManagementService.registerDependency(commandOptions,
                                                CommonDependencies.SEASIDE_GRADLE_PLUGINS.getGropuId(),
                                                CommonDependencies.SEASIDE_GRADLE_PLUGINS.getArtifactId());
      buildManagementService.registerDependency(commandOptions,
                                                CommonDependencies.SONARQUBE_GRADLE_PLUGIN.getGropuId(),
                                                CommonDependencies.SONARQUBE_GRADLE_PLUGIN.getArtifactId());
   }

   private Collection<IBuildDependency> getBuildScriptDependencies(IJellyFishCommandOptions commandOptions) {
      return buildManagementService.getRegisteredDependencies(commandOptions, DependencyScope.BUILDSCRIPT);
   }

   private SortedMap<String, String> getVersionProperties(IJellyFishCommandOptions commandOptions) {
      SortedMap<String, String> versions = new TreeMap<>();

      EnumSet<DependencyScope> scopes = EnumSet.complementOf(EnumSet.of(DependencyScope.BUILDSCRIPT));
      for (DependencyScope scope : scopes) {
         for (IBuildDependency dependency : buildManagementService.getRegisteredDependencies(commandOptions, scope)) {
            versions.put(dependency.getVersionPropertyName(), dependency.getVersion());
         }
      }

      return versions;
   }

   private Collection<IProjectInformation> getProjects(IJellyFishCommandOptions commandOptions) {
      return buildManagementService.getRegisteredProjects();
   }

   private String getDeploymentModel(IJellyFishCommandOptions options) {
      IParameter<?> deploymentParameter = options.getParameters().getParameter(DEPLOYMENT_MODEL_NAME_PROPERTY);
      if (deploymentParameter == null) {
         return null;
      } else {
         return deploymentParameter.getStringValue();
      }
   }

   private void configModelParts(GradleProjectDto dto) {
      Collection<IModelReferenceField> parts = getModel().getParts();
      if (parts != null) {
         for (IModelReferenceField part : parts) {
            IModel partModel = part.getType();
            IProjectInformation project = projectNamingService.getDistributionProjectName(getOptions(), partModel);
            String name = partModel.getFullyQualifiedName();
            String distributionGav = project.getGroupId() + ":" + project.getArtifactId() + ":$"
                     + project.getVersionPropertyName() + "@zip";
            dto.addModelPart(name, distributionGav, project.getVersionPropertyName());
         }
      }
   }
}
