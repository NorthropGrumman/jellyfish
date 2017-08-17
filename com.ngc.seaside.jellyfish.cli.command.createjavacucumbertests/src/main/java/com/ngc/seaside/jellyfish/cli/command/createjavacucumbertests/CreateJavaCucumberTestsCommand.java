package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.bootstrap.utilities.file.FileUtilitiesException;
import com.ngc.seaside.bootstrap.utilities.file.GradleSettingsUtilities;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

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

@Component(service = IJellyFishCommand.class)
public class CreateJavaCucumberTestsCommand implements IJellyFishCommand {

   private static final String NAME = "create-java-cucumber-tests";
   private static final IUsage USAGE = createUsage();

   public static final String GROUP_ID_PROPERTY = "groupId";
   public static final String ARTIFACT_ID_PROPERTY = "artifactId";
   public static final String OUTPUT_DIRECTORY_PROPERTY = "outputDirectory";
   public static final String MODEL_PROPERTY = "model";
   public static final String CLEAN_PROPERTY = "clean";
   public static final String REFRESH_FEATURE_FILES_PROPERTY = "refreshFeatureFiles";

   public static final String MODEL_OBJECT_PROPERTY = "modelObject";
   //private static final String DEFAULT_PACKAGE_SUFFIX = "tests";
   private static final String PACKAGE_PROPERTY = "package";
   private static final String OUTPUT_PROPERTY = "output";

   private ILogService logService;
   private IPromptUserService promptService;
   private ITemplateService templateService;

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameters(commandOptions.getParameters().getAllParameters());

      if (!parameters.containsParameter(MODEL_PROPERTY)) {
         String modelId = promptService.prompt(MODEL_PROPERTY, null, null);
         parameters.addParameter(new DefaultParameter<>(MODEL_PROPERTY, modelId));
      }

      ISystemDescriptor systemDescriptor = commandOptions.getSystemDescriptor();
      String modelId = parameters.getParameter(MODEL_PROPERTY).getStringValue();
      final IModel model = systemDescriptor.findModel(modelId)
            .orElseThrow(() -> new CommandException("Unknown model:" + modelId));

      parameters.addParameter(new DefaultParameter<>(MODEL_OBJECT_PROPERTY, model));

      if (!parameters.containsParameter(OUTPUT_DIRECTORY_PROPERTY)) {
         String input = promptService.prompt(OUTPUT_DIRECTORY_PROPERTY, null, null);
         parameters.addParameter(new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, input));
      }
      final Path outputDirectory = Paths.get(parameters.getParameter(OUTPUT_DIRECTORY_PROPERTY).getStringValue());

      doCreateDirectories(outputDirectory);

      if (!parameters.containsParameter(GROUP_ID_PROPERTY)) {
         parameters.addParameter(new DefaultParameter<>(GROUP_ID_PROPERTY, model.getParent().getName()));
      }
      final String groupId = parameters.getParameter(GROUP_ID_PROPERTY).getStringValue();

      if (!parameters.containsParameter(ARTIFACT_ID_PROPERTY)) {
         String artifact = model.getName().toLowerCase().concat(".connector");
         parameters.addParameter(new DefaultParameter<String>(ARTIFACT_ID_PROPERTY, artifact));
      }
      final String artifactId = parameters.getParameter(ARTIFACT_ID_PROPERTY).getStringValue();
      parameters.addParameter(new DefaultParameter<String>(PACKAGE_PROPERTY, groupId + "." + artifactId));

      try {
         Files.createDirectories(outputDirectory);
      } catch (IOException e) {
         logService.error(CreateJavaCucumberTestsCommand.class, e);
         throw new CommandException(e);
      }

      final boolean refreshFeature;
      if (parameters.containsParameter(REFRESH_FEATURE_FILES_PROPERTY)) {
         String value = parameters.getParameter(REFRESH_FEATURE_FILES_PROPERTY).getStringValue();
         switch (value.toLowerCase()) {
            case "true":
               refreshFeature = true;
               //TODO add feature files requirement
               break;
            case "false":
               refreshFeature = false;
               break;
            default:
               throw new CommandException("Invalid value for clean: " + value + ". Expected either true or false.");
         }
      } else {
         refreshFeature = false;
      }

      final boolean clean;
      if (parameters.containsParameter(CLEAN_PROPERTY)) {
         String value = parameters.getParameter(CLEAN_PROPERTY).getStringValue();
         switch (value.toLowerCase()) {
            case "true":
               clean = true;
               break;
            case "false":
               clean = false;
               break;
            default:
               throw new CommandException("Invalid value for clean: " + value + ". Expected either true or false.");
         }
      } else {
         clean = true;
      }

      doAddProject(parameters);

      templateService.unpack(CreateJavaCucumberTestsCommand.class.getPackage().getName(), parameters, outputDirectory, clean);
      logService.info(CreateJavaCucumberTestsCommand.class, "%s project successfully created", model.getName());
   }

   /**
    * Create the usage for this command.
    *
    * @return the usage.
    */
   @SuppressWarnings("rawtypes")
   private static IUsage createUsage() {
      return new DefaultUsage("Generates the gradle distribution project for a Java application",
                              new DefaultParameter(GROUP_ID_PROPERTY)
                                    .setDescription("The project's group ID. (default: the package in the model)")
                                    .setRequired(false),
                              new DefaultParameter(ARTIFACT_ID_PROPERTY).setDescription(
                                    "The project's artifact ID. (default: model name in lowercase + '.distribution')")
                                    .setRequired(false),
                              new DefaultParameter(OUTPUT_DIRECTORY_PROPERTY)
                                    .setDescription("Base directory in which to output the project")
                                    .setRequired(true),
                              new DefaultParameter(MODEL_PROPERTY)
                                    .setDescription("The fully qualified path to the system descriptor model")
                                    .setRequired(true),
                              new DefaultParameter(CLEAN_PROPERTY).setDescription(
                                    "If true, recursively deletes the domain project (if it already exists), before generating the it again")
                                    .setRequired(false),
                              new DefaultParameter(REFRESH_FEATURE_FILES_PROPERTY).setDescription(
                                    "If false, copy the feature files and resources from the system descriptor project into src/main/resources.")
                                    .setRequired(false));
   }
   private Path getAbsoluteOutputPath(String output, IJellyFishCommandOptions commandOptions) {
      Path path = Paths.get(output);
      if (!path.isAbsolute()) {
         path = commandOptions.getSystemDescriptorProjectPath().toAbsolutePath().resolve(output).toAbsolutePath();
      }
      return path;
   }

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IUsage getUsage() {
      return USAGE;
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
    * Sets prompt user service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removePromptService")
   public void setPromptService(IPromptUserService ref) {
      this.promptService = ref;
   }

   /**
    * Remove prompt user service.
    */
   public void removePromptService(IPromptUserService ref) {
      setPromptService(null);
   }

   protected void doAddProject(IParameterCollection parameters) {
      try {
         if (!GradleSettingsUtilities.tryAddProject(parameters)) {
            logService.warn(getClass(), "Unable to add the new project to settings.gradle.");
         }
      } catch (FileUtilitiesException e) {
         logService.warn(getClass(), e, "Unable to add the new project to settings.gradle.");
         throw new CommandException(e);
      }
   }

   /**
    * Returns the boolean value of the given parameter if it was set, false otherwise.
    *
    * @param parameters command parameters
    * @param parameter  name of parameter
    * @return the boolean value of the parameter
    * @throws CommandException if the value is invalid
    */
   private static boolean getCleanProperty(IParameterCollection parameters, String parameter) {
      final boolean booleanValue;
      if (parameters.containsParameter(parameter)) {
         String value = parameters.getParameter(parameter).getStringValue();
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
   protected void doCreateDirectories(Path outputDirectory) {
      try {
         Files.createDirectories(outputDirectory);
      } catch (IOException e) {
         logService.error(CreateJavaCucumberTestsCommand.class, e);
         throw new CommandException(e);
      }
   }

   /**
    * Retrieve the output property value based on user input. Default is standard output
    *
    * @param commandOptions Jellyfish command options containing user params
    */
   protected Path evaluateOutput(IJellyFishCommandOptions commandOptions) {
      Path output;
      if (commandOptions.getParameters().containsParameter(REFRESH_FEATURE_FILES_PROPERTY)) {
         String outputUri = commandOptions.getParameters().getParameter(OUTPUT_PROPERTY).getStringValue();
         output = Paths.get(outputUri);

         if (!output.isAbsolute()) {
            output = commandOptions.getSystemDescriptorProjectPath().toAbsolutePath().resolve(outputUri);
         }

         return output.toAbsolutePath();
      } else {
         return null;
      }
   }
}

