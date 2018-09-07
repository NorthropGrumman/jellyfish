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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.GeneratedServiceConfigDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationType;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.IConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.IConfigurationPlugin.DependencyType;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.IConfigurationTemplatePlugin;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.CommonDependencies;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.utilities.command.AbstractMultiphaseJellyfishCommand;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.apache.commons.io.FileUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

@Component(service = IJellyFishCommand.class)
public class CreateJavaServiceGeneratedConfigCommand extends AbstractMultiphaseJellyfishCommand {

   static final String CONFIG_GENERATED_BUILD_TEMPLATE_SUFFIX = "genbuild";
   static final String CONFIG_BUILD_TEMPLATE_SUFFIX = "build";

   static final String MODEL_PROPERTY = CommonParameters.MODEL.getName();
   static final String DEPLOYMENT_MODEL_PROPERTY = CommonParameters.DEPLOYMENT_MODEL.getName();
   static final String OUTPUT_DIRECTORY_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   static final String CLEAN_PROPERTY = CommonParameters.CLEAN.getName();

   static final String NAME = "create-java-service-generated-config";

   private Set<IConfigurationPlugin> plugins = new LinkedHashSet<>();

   public CreateJavaServiceGeneratedConfigCommand() {
      super(NAME);
   }

   @Override
   protected void runDefaultPhase() {
      IJellyFishCommandOptions options = getOptions();
      IModel model = getModel();
      Path outputDirectory = getOutputDirectory();
      boolean clean = getBooleanParameter(CLEAN_PROPERTY);

      IProjectInformation projectInfo = projectNamingService.getGeneratedConfigProjectName(options, model);
      GeneratedServiceConfigDto dto = new GeneratedServiceConfigDto(buildManagementService, options);
      dto.setProjectDirectoryName(projectInfo.getDirectoryName());

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>("dto", dto));
      unpackSuffixedTemplate(CONFIG_BUILD_TEMPLATE_SUFFIX, parameters, outputDirectory, clean);
      buildManagementService.registerDependency(options, CommonDependencies.SPARK_CORE.getGropuId(),
               CommonDependencies.SPARK_CORE.getArtifactId());
      registerProject(projectInfo);
   }

   @Override
   protected void runDeferredPhase() {
      IJellyFishCommandOptions options = getOptions();
      IModel model = getModel();
      boolean clean = getBooleanParameter(CLEAN_PROPERTY);
      Path outputDir = getOutputDirectory();

      IProjectInformation projectInfo = projectNamingService.getGeneratedConfigProjectName(options, model);
      Path projectDir = evaluateProjectDirectory(outputDir, projectInfo.getDirectoryName(), clean);

      GeneratedServiceConfigDto dto = new GeneratedServiceConfigDto(buildManagementService, options)
               .setBaseProjectName(projectNamingService.getBaseServiceProjectName(options, model).getArtifactId())
               .setProjectDirectoryName(outputDir.relativize(projectDir).toString());

      ConfigurationContext context = new ConfigurationContext();
      context.setBasePackage(packageNamingService.getConfigPackageName(options, model));
      context.setConfigurationType(ConfigurationType.SERVICE);
      context.setModel(model);
      context.setDeploymentModel(getDeploymentModel());
      context.setOptions(options);
      context.setProjectInformation(projectInfo);
      Set<IConfigurationTemplatePlugin<?>> templates = new LinkedHashSet<>();
      for (IConfigurationPlugin plugin : plugins) {
         if (plugin.isValid(context)) {
            dto.addCompileDependencies(plugin.getDependencies(context, DependencyType.COMPILE));
            dto.addDefaultBundles(plugin.getDependencies(context, DependencyType.BUNDLE));

            if (plugin instanceof IConfigurationTemplatePlugin<?>) {
               templates.add((IConfigurationTemplatePlugin<?>) plugin);
            }
         }
      }

      DefaultParameterCollection parameters = new DefaultParameterCollection(getOptions().getParameters());
      parameters.addParameter(new DefaultParameter<>("dto", dto));
      unpackSuffixedTemplate(CONFIG_GENERATED_BUILD_TEMPLATE_SUFFIX, parameters, outputDir, clean);

      for (IConfigurationTemplatePlugin<?> template : templates) {
         configurePlugin(context, template, outputDir);
      }

      registerProject(projectInfo);
   }

   @Activate
   public void activate() {
      logService.trace(getClass(), "Activated");
   }

   @Deactivate
   public void deactivate() {
      logService.trace(getClass(), "Deactivated");
   }

   @Reference
   public void setLogService(ILogService ref) {
      super.setLogService(ref);
   }

   @Reference
   public void setTemplateService(ITemplateService ref) {
      super.setTemplateService(ref);
   }

   @Reference
   public void setProjectNamingService(IProjectNamingService ref) {
      super.setProjectNamingService(ref);
   }

   @Reference
   public void setPackageNamingService(IPackageNamingService ref) {
      super.setPackageNamingService(ref);
   }

   @Reference
   public void setBuildManagementService(IBuildManagementService ref) {
      super.setBuildManagementService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
   public void addConfigurationPlugin(IConfigurationPlugin ref) {
      plugins.add(ref);
   }

   public void removeConfigurationPlugin(IConfigurationPlugin ref) {
      plugins.remove(ref);
   }

   private <T> void configurePlugin(ConfigurationContext context, IConfigurationTemplatePlugin<T> plugin,
            Path outputDirectory) {
      String template = plugin.getTemplate(context);
      Optional<T> dto = plugin.getConfigurationDto(context);
      if (template != null && dto.isPresent()) {
         DefaultParameterCollection parameters = new DefaultParameterCollection(getOptions().getParameters());
         parameters.addParameter(new DefaultParameter<>("dto", dto.get()));
         for (Entry<String, Object> entry : plugin.getExtraTemplateParameters(context).entrySet()) {
            parameters.addParameter(new DefaultParameter<>(entry.getKey(), entry.getValue()));
         }
         templateService.unpack(template, addDefaultUnpackParameters(parameters), outputDirectory, false);
      }
   }

   /**
    * Creates and returns the path to the domain project directory.
    *
    * @param outputDir output directory
    * @param projDirName project directory name
    * @param clean whether or not to delete the contents of the directory
    * @return the path to the domain project directory
    * @throws CommandException if an error occurred in creating the project directory
    */
   private Path evaluateProjectDirectory(Path outputDir, String projDirName, boolean clean) {
      final Path projectDir = outputDir.resolve(Paths.get(projDirName));
      try {
         Files.createDirectories(outputDir);
         if (clean) {
            FileUtils.deleteQuietly(projectDir.toFile());
         }
         Files.createDirectories(projectDir);
      } catch (IOException e) {
         throw new CommandException(e);
      }
      return projectDir;
   }

   private IModel getDeploymentModel() {
      ISystemDescriptor sd = getOptions().getSystemDescriptor();
      IParameterCollection parameters = getOptions().getParameters();
      final String modelName = parameters.getParameter(CommonParameters.DEPLOYMENT_MODEL.getName()).getStringValue();
      return sd.findModel(modelName)
               .orElseThrow(() -> new CommandException("Deployment model not found: " + modelName));
   }

   @Override
   protected IUsage createUsage() {
      return new DefaultUsage(
               "Generates a Gradle project that automatically handles "
               + "transport configuration and other configurations for a service using the service's deployment model",
               CommonParameters.GROUP_ID,
               CommonParameters.ARTIFACT_ID,
               CommonParameters.MODEL.required(),
               CommonParameters.DEPLOYMENT_MODEL.required(),
               CommonParameters.OUTPUT_DIRECTORY.required(),
               CommonParameters.HEADER_FILE,
               CommonParameters.CLEAN);
   }
}
