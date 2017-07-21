package com.ngc.seaside.jellyfish.cli.command.createjavadistribution;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
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

@Component(service = IJellyFishCommand.class)
public class CreateJavaDistributionCommand implements IJellyFishCommand {

   public static final String GROUP_ID_PROPERTY = "groupId";
   public static final String ARTIFACT_ID_PROPERTY = "artifactId";
   public static final String OUTPUT_DIRECTORY_PROPERTY = "outputDirectory";
   public static final String MODEL_PROPERTY = "model";
   public static final String CLASSNAME_PROPERTY = "classname";
   public static final String CLEAN_PROPERTY = "clean";
   private static final String NAME = "create-java-distribution";
   private static final IUsage USAGE = createUsage();
   private ILogService logService;
   private IPromptUserService promptService;

   /**
    * Create the usage for this command.
    *
    * @return the usage.
    */
   private static IUsage createUsage() {
      return new DefaultUsage("Generate the gradle distribution project for a Java application",
                              new DefaultParameter(GROUP_ID_PROPERTY).setDescription("The project's group ID")
                                       .setRequired(false),
                              new DefaultParameter(ARTIFACT_ID_PROPERTY).setDescription("The project's artifact ID")
                                       .setRequired(false),
                              new DefaultParameter(OUTPUT_DIRECTORY_PROPERTY)
                                       .setDescription("Base directory in which to output the project")
                                       .setRequired(true),
                              new DefaultParameter(MODEL_PROPERTY)
                                       .setDescription("The fully qualified path to the system descriptor model")
                                       .setRequired(true),
                              new DefaultParameter(CLEAN_PROPERTY).setDescription(
                                       "If true, recursively deletes the domain project (if it already exists), before generating the it again")
                                       .setRequired(false)
      );
   }

   /**
    * Returns the groupId for the domain project.
    *
    * @param parameters command parameters
    * @param model      domain model
    * @return the groupId for the domain project
    */
   private static String getGroupIdProperty(IParameterCollection parameters, IModel model) {
      final String groupId;
      if (parameters.containsParameter(GROUP_ID_PROPERTY)) {
         groupId = parameters.getParameter(GROUP_ID_PROPERTY).getStringValue();
      } else {
         groupId = model.getParent().getName();
      }
      return groupId;
   }

   /**
    * Returns the artifactId for the domain project.
    *
    * @param parameters command parameters
    * @param model      domain model
    * @return the artifactId for the domain project
    */
   private static String getArtifactIdProperty(IParameterCollection parameters, IModel model) {
      final String artifactId;
      if (parameters.containsParameter(ARTIFACT_ID_PROPERTY)) {
         artifactId = parameters.getParameter(ARTIFACT_ID_PROPERTY).getStringValue();
      } else {
         artifactId = model.getName().toLowerCase() + "distribution";
      }
      return artifactId;
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
      IParameterCollection parameters = commandOptions.getParameters();
      final IModel model = getModelProperty(commandOptions, parameters);
      final String groupId = getGroupIdProperty(parameters, model);
      final String artifactId = getArtifactIdProperty(parameters, model);
      final boolean clean = getCleanProperty(parameters, CLEAN_PROPERTY);
      //final Path outputDir = getOutputDirectoryProperty(parameters, model, clean);

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

   /**
    * Returns the {@link IModel} associated with the value of the {@link #MODEL_PROPERTY}.
    *
    * @param commandOptions command options
    * @return the {@link IModel}
    * @throws CommandException if the model name is invalid or missing
    */
   IModel getModelProperty(IJellyFishCommandOptions commandOptions, IParameterCollection parameters) {
      ISystemDescriptor systemDescriptor = commandOptions.getSystemDescriptor();
      final String modelName;
      if (parameters.containsParameter(MODEL_PROPERTY)) {
         modelName = parameters.getParameter(MODEL_PROPERTY).getStringValue();
      } else {
         modelName = promptService.prompt(MODEL_PROPERTY, null, null);
      }
      return systemDescriptor.findModel(modelName)
               .orElseThrow(() -> new CommandException("Unknown model:" + modelName));
   }

   /**
    * Creates and returns the path to the domain project directory.
    *
    * @param parameters command parameters
    * @param pkg        domain package
    * @param clean      whether or not to delete the contents of the directory
    * @return the path to the domain project directory
    * @throws CommandException if an error occurred in creating the project directory
    */
   private Path getOutputDirectoryProperty(IParameterCollection parameters, String pkg, boolean clean) {
      final Path outputDir;

      if (parameters.containsParameter(OUTPUT_DIRECTORY_PROPERTY)) {
         outputDir = Paths.get(parameters.getParameter(OUTPUT_DIRECTORY_PROPERTY).getStringValue());
      } else {
         String input = promptService.prompt(OUTPUT_DIRECTORY_PROPERTY, null, null);
         outputDir = Paths.get(input);
      }
      final Path projectDir = outputDir.resolve(pkg);
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
}
