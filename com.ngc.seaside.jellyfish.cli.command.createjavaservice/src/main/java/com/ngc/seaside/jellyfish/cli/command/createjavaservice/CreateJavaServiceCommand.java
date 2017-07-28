package com.ngc.seaside.jellyfish.cli.command.createjavaservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dao.ITemplateDaoFactory;
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
public class CreateJavaServiceCommand implements IJellyFishCommand {

   static final String GROUP_ID_PROPERTY = "groupId";
   static final String ARTIFACT_ID_PROPERTY = "artifactId";
   static final String MODEL_PROPERTY = "model";
   static final String OUTPUT_DIRECTORY_PROPERTY = "outputDirectory";
   static final String CLEAN_PROPERTY = "clean";

   static final String DEFAULT_OUTPUT_DIRECTORY = ".";

   private static final String NAME = "create-java-service";
   private static final IUsage USAGE = createUsage();

   private ILogService logService;
   private IPromptUserService promptService;
   private ITemplateService templateService;
   private ITemplateDaoFactory templateDaoFactory;

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      IModel model = evaluateModelParameter(commandOptions);
      String groupId = evaluateGroupId(commandOptions, model);
      String artifactId = evaluateArtifactId(commandOptions, model);
      String packagez = evaluatePackage(commandOptions, groupId, artifactId);
      boolean clean = evaluateBooleanParameter(commandOptions, CLEAN_PROPERTY);
      Path outputDir = evaluateOutputDirectory(commandOptions);
      Path projectDir = evaluateProjectDirectory(outputDir, packagez, clean);

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      templateService.unpack(CreateJavaServiceCommand.class.getPackage().getName(),
                             parameters,
                             projectDir,
                             clean);
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
    * Create the usage for this command.
    *
    * @return the usage.
    */
   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IUsage getUsage() {
      return USAGE;
   }

   /**
    * Sets log service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeLogService")
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
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeTemplateService")
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
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removePromptService")
   public void setPromptService(IPromptUserService ref) {
      this.promptService = ref;
   }

   /**
    * Remove prompt service.
    */
   public void removePromptService(IPromptUserService ref) {
      setPromptService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeTemplateDaoFactory")
   public void setTemplateDaoFactory(ITemplateDaoFactory ref) {
      this.templateDaoFactory = ref;
   }

   public void removeTemplateDaoFactory(ITemplateDaoFactory ref) {
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

   private static String evaluateArtifactId(IJellyFishCommandOptions commandOptions, IModel model) {
      String artifactId;
      if (commandOptions.getParameters().containsParameter(ARTIFACT_ID_PROPERTY)) {
         artifactId = commandOptions.getParameters().getParameter(ARTIFACT_ID_PROPERTY).getStringValue();
      } else {
         artifactId = model.getName().toLowerCase();
      }
      return artifactId;
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
      return new DefaultUsage(
            "Generates the service for a Java application",
            new DefaultParameter(GROUP_ID_PROPERTY)
                  .setDescription("The project's group ID. (default: the package in the model)")
                  .setRequired(false),
            new DefaultParameter(ARTIFACT_ID_PROPERTY)
                  .setDescription("The project's artifact Id. (default: the model name in lowercase)")
                  .setRequired(false),
            new DefaultParameter(MODEL_PROPERTY)
                  .setDescription("The fully qualified path to the model.")
                  .setRequired(true),
            new DefaultParameter(OUTPUT_DIRECTORY_PROPERTY)
                  .setDescription("Base directory in which to output the project")
                  .setRequired(true),
            new DefaultParameter(CLEAN_PROPERTY)
                  .setDescription("If true, recursively deletes the domain project before generating the it again")
                  .setRequired(false)
      );
   }

   /**
    * Helper method to delete folder/files
    *
    * @param file file/folder to delete
    */
   private static boolean deleteDir(File file) {
      File[] contents = file.listFiles();
      if (contents != null) {
         for (File f : contents) {
            deleteDir(f);
         }
      }
      return file.delete();
   }

   private static boolean evaluateBooleanParameter(IJellyFishCommandOptions options, String parameter) {
      final boolean booleanValue;
      if (options.getParameters().containsParameter(parameter)) {
         String value = options.getParameters().getParameter(parameter).getStringValue();
         switch (value.toLowerCase()) {
            case "true":
               booleanValue = true;
               break;
            case "false":
               booleanValue = false;
               break;
            default:
               throw new CommandException(
                     "Invalid value for " + parameter + ": " + value + ". Expected either true or false.");
         }
      } else {
         booleanValue = false;
      }
      return booleanValue;
   }
}
