package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.bootstrap.utilities.file.FileUtilitiesException;
import com.ngc.seaside.bootstrap.utilities.file.GradleSettingsUtilities;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.ITemplateDtoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.BaseServiceTemplateDto;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component(service = IJellyFishCommand.class)
public class CreateJavaServiceBaseCommand implements IJellyFishCommand {

   static final String GROUP_ID_PROPERTY = CommonParameters.GROUP_ID.getName();
   static final String ARTIFACT_ID_PROPERTY = CommonParameters.ARTIFACT_ID.getName();
   static final String MODEL_PROPERTY = CommonParameters.MODEL.getName();
   static final String OUTPUT_DIRECTORY_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   static final String CLEAN_PROPERTY = CommonParameters.CLEAN.getName();
   
   static final String ARTIFACT_ID_SUFFIX_PROPERTY = "artifactIdSuffix";
   static final String DEFAULT_ARTIFACT_ID_SUFFIX = "base";
   static final String DEFAULT_OUTPUT_DIRECTORY = ".";
   
   private static final String NAME = "create-java-service-base";
   private static final IUsage USAGE = createUsage();

   private ILogService logService;
   private IPromptUserService promptService;
   private ITemplateService templateService;
   private ITemplateDtoFactory templateDaoFactory;

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IUsage getUsage() {
      return USAGE;
   }

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      IModel model = evaluateModelParameter(commandOptions);
      String groupId = evaluateGroupId(commandOptions, model);
      String artifactIdWithoutSuffix = evaluateArtifactIdWithoutSuffix(commandOptions, model);
      String artifactId = evaluateArtifactId(commandOptions, model, artifactIdWithoutSuffix);
      String packagez = evaluatePackage(commandOptions, groupId, artifactId);
      boolean clean = CommonParameters.evaluateBooleanParameter(commandOptions.getParameters(), CLEAN_PROPERTY);
      Path outputDir = evaluateOutputDirectory(commandOptions);
      Path projectDir = evaluateProjectDirectory(outputDir, packagez, clean);

      BaseServiceTemplateDto dto = (BaseServiceTemplateDto) templateDaoFactory.newDto(model, packagez);
      dto.setProjectDirectoryName(projectDir.getFileName().toString());

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>("dto", dto));
      templateService.unpack(CreateJavaServiceBaseCommand.class.getPackage().getName(),
         parameters,
         outputDir,
         clean);

      try {
         parameters.addParameter(new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, outputDir.toString()));
         parameters.addParameter(new DefaultParameter<>(GROUP_ID_PROPERTY, groupId));
         parameters.addParameter(new DefaultParameter<>(ARTIFACT_ID_PROPERTY, artifactId));
         if (!GradleSettingsUtilities.tryAddProject(parameters)) {
            logService.warn(getClass(), "Unable to add the new project to settings.gradle.");
         }
      } catch (FileUtilitiesException e) {
         throw new CommandException("failed to update settings.gradle!", e);
      }
   }

   @Activate
   public void activate() {
      logService.trace(getClass(), "Activated");
   }

   @Deactivate
   public void deactivate() {
      logService.trace(getClass(), "Deactivated");
   }

   /**
    * Sets log service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeLogService")
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
    * Sets template service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeTemplateService")
   public void setTemplateService(ITemplateService ref) {
      this.templateService = ref;
   }

   /**
    * Remove template service.
    */
   public void removeTemplateService(ITemplateService ref) {
      setTemplateService(null);
   }

   /**
    * Sets prompt service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removePromptService")
   public void setPromptService(IPromptUserService ref) {
      this.promptService = ref;
   }

   /**
    * Remove prompt service.
    */
   public void removePromptService(IPromptUserService ref) {
      setPromptService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeTemplateDaoFactory")
   public void setTemplateDaoFactory(ITemplateDtoFactory ref) {
      this.templateDaoFactory = ref;
   }

   public void removeTemplateDaoFactory(ITemplateDtoFactory ref) {
      setTemplateDaoFactory(null);
   }

   private IModel evaluateModelParameter(IJellyFishCommandOptions commandOptions) {
      // Get the fully qualified model name.
      String modelName;
      if (commandOptions.getParameters().containsParameter(MODEL_PROPERTY)) {
         modelName = commandOptions.getParameters().getParameter(MODEL_PROPERTY).getStringValue();
      } else {
         modelName = promptService.prompt(MODEL_PROPERTY,
            null,
            m -> commandOptions.getSystemDescriptor().findModel(m).isPresent());
      }
      // Find the actual model.
      return commandOptions.getSystemDescriptor()
                           .findModel(modelName)
                           .orElseThrow(() -> new CommandException(String.format("model %s not found!", modelName)));
   }

   private Path evaluateOutputDirectory(IJellyFishCommandOptions commandOptions) {
      Path outputDirectory;
      if (commandOptions.getParameters().containsParameter(OUTPUT_DIRECTORY_PROPERTY)) {
         outputDirectory = Paths.get(commandOptions.getParameters()
                                                   .getParameter(OUTPUT_DIRECTORY_PROPERTY)
                                                   .getStringValue());
      } else {
         // Ask the user if needed.
         outputDirectory = Paths.get(promptService.prompt(OUTPUT_DIRECTORY_PROPERTY, DEFAULT_OUTPUT_DIRECTORY, null));
      }
      return outputDirectory;
   }

   private static String evaluateGroupId(IJellyFishCommandOptions commandOptions, IModel model) {
      String groupId;
      if (commandOptions.getParameters().containsParameter(GROUP_ID_PROPERTY)) {
         groupId = commandOptions.getParameters().getParameter(GROUP_ID_PROPERTY).getStringValue();
      } else {
         groupId = model.getParent().getName();
      }
      return groupId;
   }

   private static String evaluateArtifactIdWithoutSuffix(IJellyFishCommandOptions commandOptions, IModel model) {
      String artifactId;
      if (commandOptions.getParameters().containsParameter(ARTIFACT_ID_PROPERTY)) {
         artifactId = commandOptions.getParameters().getParameter(ARTIFACT_ID_PROPERTY).getStringValue();
      } else {
         artifactId = model.getName().toLowerCase();
      }
      return artifactId;
   }

   private static String evaluateArtifactId(IJellyFishCommandOptions commandOptions, IModel model,
            String artifactIdWithoutSuffix) {
      String artifactIdSuffix;
      if (commandOptions.getParameters().containsParameter(ARTIFACT_ID_SUFFIX_PROPERTY)) {
         artifactIdSuffix = commandOptions.getParameters().getParameter(ARTIFACT_ID_SUFFIX_PROPERTY).getStringValue();
         if (!artifactIdSuffix.isEmpty() && !artifactIdSuffix.startsWith(".")) {
            artifactIdSuffix = '.' + artifactIdSuffix;
         }
      } else {
         artifactIdSuffix = '.' + DEFAULT_ARTIFACT_ID_SUFFIX;
      }
      return artifactIdWithoutSuffix + artifactIdSuffix;
   }

   private static String evaluatePackage(IJellyFishCommandOptions commandOptions, String groupId, String artifactId) {
      return String.format("%s.%s", groupId, artifactId);
   }

   private static Path evaluateProjectDirectory(Path outputDir, String packagez, boolean clean) {
      Path projectDir = outputDir.resolve(packagez);
      File projectDirFile = projectDir.toFile();
      if (clean && projectDirFile.exists() && projectDirFile.isDirectory()) {
         deleteDir(projectDirFile);
      }
      return projectDir;
   }

   private static IUsage createUsage() {
      return new DefaultUsage("Generates the base abstract service for a Java application",
         CommonParameters.GROUP_ID,
         CommonParameters.ARTIFACT_ID,
         CommonParameters.OUTPUT_DIRECTORY.required(),
         CommonParameters.MODEL.required(),
         CommonParameters.CLEAN);
   }

   /**
    * Helper method to delete folder/files
    *
    * @param file file/folder to delete
    */
   private static void deleteDir(File file) {
      File[] contents = file.listFiles();
      if (contents != null) {
         for (File f : contents) {
            deleteDir(f);
         }
      }
      file.delete();
   }
}
