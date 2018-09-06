package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertestsconfig;

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
import com.ngc.seaside.jellyfish.cli.command.createjavacucumbertestsconfig.dto.GeneratedTestConfigDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto.GeneratedServiceConfigDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationType;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.IConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.IConfigurationPlugin.DependencyType;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.IConfigurationTemplatePlugin;
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
public class CreateJavaCucumberTestsConfigCommand extends AbstractMultiphaseJellyfishCommand
         implements IJellyFishCommand {

   static final String CONFIG_GENERATED_BUILD_TEMPLATE_SUFFIX = "genbuild";
   static final String CONFIG_BUILD_TEMPLATE_SUFFIX = "build";

   static final String MODEL_PROPERTY = CommonParameters.MODEL.getName();
   static final String DEPLOYMENT_MODEL_PROPERTY = CommonParameters.DEPLOYMENT_MODEL.getName();
   static final String OUTPUT_DIRECTORY_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   static final String CLEAN_PROPERTY = CommonParameters.CLEAN.getName();

   private static final String NAME = "create-java-cucumber-tests-config";

   private Set<IConfigurationPlugin> plugins = new LinkedHashSet<>();

   protected CreateJavaCucumberTestsConfigCommand() {
      super(NAME);
   }

   @Override
   protected void runDefaultPhase() {
      IModel model = getModel();
      Path outputDirectory = getOutputDirectory();
      boolean clean = getBooleanParameter(CLEAN_PROPERTY);

      IProjectInformation projectInfo = projectNamingService.getCucumberTestsConfigProjectName(getOptions(), model);
      GeneratedServiceConfigDto dto = new GeneratedServiceConfigDto(buildManagementService, getOptions());
      dto.setProjectDirectoryName(projectInfo.getDirectoryName());

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>("dto", dto));
      unpackSuffixedTemplate(CONFIG_BUILD_TEMPLATE_SUFFIX, parameters, outputDirectory, clean);

      registerProject(projectInfo);
   }

   @Override
   protected void runDeferredPhase() {
      IJellyFishCommandOptions options = getOptions();
      IModel model = getModel();
      boolean clean = getBooleanParameter(CLEAN_PROPERTY);
      Path outputDir = getOutputDirectory();

      IProjectInformation projectInfo = projectNamingService.getCucumberTestsConfigProjectName(options, model);
      Path projectDir = evaluateProjectDirectory(outputDir, projectInfo.getDirectoryName(), clean);

      GeneratedTestConfigDto dto = new GeneratedTestConfigDto(buildManagementService, options)
               .setBaseProjectName(projectNamingService.getBaseServiceProjectName(options, model).getArtifactId())
               .setProjectDirectoryName(outputDir.relativize(projectDir).toString());

      ConfigurationContext context = new ConfigurationContext();
      context.setBasePackage(packageNamingService.getCucumberTestsConfigPackageName(options, model));
      context.setConfigurationType(ConfigurationType.TEST);
      context.setModel(model);
      context.setDeploymentModel(getDeploymentModel());
      context.setOptions(options);
      context.setProjectInformation(projectInfo);
      Set<IConfigurationTemplatePlugin<?>> templates = new LinkedHashSet<>();
      for (IConfigurationPlugin plugin : plugins) {
         if (plugin.isValid(context)) {
            dto.addCompileDependencies(plugin.getDependencies(context, DependencyType.COMPILE));
            dto.addDefaultModules(plugin.getDependencies(context, DependencyType.MODULE));

            if (plugin instanceof IConfigurationTemplatePlugin<?>) {
               templates.add((IConfigurationTemplatePlugin<?>) plugin);
            }
         }
      }

      DefaultParameterCollection parameters = new DefaultParameterCollection();
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

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      super.setLogService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removeTemplateService")
   public void setTemplateService(ITemplateService ref) {
      super.setTemplateService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removeProjectNamingService")
   public void setProjectNamingService(IProjectNamingService ref) {
      super.setProjectNamingService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removePackageNamingService")
   public void setPackageNamingService(IPackageNamingService ref) {
      super.setPackageNamingService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
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
         DefaultParameterCollection parameters = new DefaultParameterCollection();
         parameters.addParameter(new DefaultParameter<>("dto", dto.get()));
         for (Entry<String, Object> entry : plugin.getExtraTemplateParameters(context).entrySet()) {
            parameters.addParameter(new DefaultParameter<>(entry.getKey(), entry.getValue()));
         }
         templateService.unpack(template, parameters, outputDirectory, false);
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
      //                "Generates the generated service configuration for a Cucumber tests",
      return new DefaultUsage(
               "Generates a Gradle project responsible for transport configuration "
               + "and other configurations for a service's acceptance tests",
               CommonParameters.GROUP_ID,
               CommonParameters.ARTIFACT_ID,
               CommonParameters.MODEL.required(),
               CommonParameters.DEPLOYMENT_MODEL.required(),
               CommonParameters.OUTPUT_DIRECTORY.required(),
               CommonParameters.CLEAN);
   }
}
