package com.ngc.seaside.jellyfish.cli.command.createjavadistribution;

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
import java.util.regex.Pattern;

@Component(service = IJellyFishCommand.class)
public class CreateJavaDistributionCommand implements IJellyFishCommand {

   public static final String GROUP_ID_PROPERTY = "groupId";
   public static final String ARTIFACT_ID_PROPERTY = "artifactId";
   public static final String OUTPUT_DIRECTORY_PROPERTY = "outputDirectory";

   public static final String MODEL_PROPERTY = "model";
   public static final String MODEL_OBJECT_PROPERTY = "modelObject";

   public static final String PACKAGE_PROPERTY = "package";
   public static final String DEFAULT_PACKAGE_SUFFIX = "distribution";
   public static final String CLEAN_PROPERTY = "clean";

   private static final String NAME = "create-java-distribution";
   private static final IUsage USAGE = createUsage();
   private static final Pattern JAVA_QUALIFIED_IDENTIFIER = Pattern
            .compile("[a-zA-Z$_][a-zA-Z$_0-9]*(?:\\.[a-zA-Z$_][a-zA-Z$_0-9]*)*");
   private ILogService logService;
   private IPromptUserService promptService;
   private ITemplateService templateService;

   /**
    * Create the usage for this command.
    *
    * @return the usage.
    */
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
                                       .setRequired(false)
      );
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
      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameters(commandOptions.getParameters().getAllParameters());

      // Resolve model properties
      if (!parameters.containsParameter(MODEL_PROPERTY)) {
         String modelId = promptService.prompt(MODEL_PROPERTY, null, null);
         parameters.addParameter(new DefaultParameter<>(MODEL_PROPERTY, modelId));
      }

      ISystemDescriptor systemDescriptor = commandOptions.getSystemDescriptor();
      String modelId = parameters.getParameter(MODEL_PROPERTY).getStringValue();
      final IModel model = systemDescriptor.findModel(modelId)
               .orElseThrow(() -> new CommandException("Unknown model:" + modelId));

      parameters.addParameter(new DefaultParameter<>(MODEL_OBJECT_PROPERTY, model));

      // Resolve output directory
      if (!parameters.containsParameter(OUTPUT_DIRECTORY_PROPERTY)) {
         String input = promptService.prompt(OUTPUT_DIRECTORY_PROPERTY, null, null);
         parameters.addParameter(new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, input));
      }
      final Path outputDirectory = Paths.get(parameters.getParameter(OUTPUT_DIRECTORY_PROPERTY).getStringValue());

      doCreateDirectories(outputDirectory);

      // Resolve groupId
      if (!parameters.containsParameter(GROUP_ID_PROPERTY)) {
         parameters.addParameter(new DefaultParameter<>(GROUP_ID_PROPERTY, model.getParent().getName()));
      }

      // Resolve artifactId
      if (!parameters.containsParameter(ARTIFACT_ID_PROPERTY)) {
         parameters.addParameter(
                  new DefaultParameter<>(ARTIFACT_ID_PROPERTY,
                                         model.getName().toLowerCase() + '.' + DEFAULT_PACKAGE_SUFFIX));
      }

      // Resolve clean property
      final boolean clean = getCleanProperty(parameters, CLEAN_PROPERTY);

      // Assign package name
      final String group = parameters.getParameter(GROUP_ID_PROPERTY).getStringValue();
      final String artifact = parameters.getParameter(ARTIFACT_ID_PROPERTY).getStringValue();
      parameters.addParameter(new DefaultParameter<>(PACKAGE_PROPERTY, group + '.' + artifact));
      String pkg = parameters.getParameter(PACKAGE_PROPERTY).getStringValue();
      if (!JAVA_QUALIFIED_IDENTIFIER.matcher(pkg).matches()) {
         throw new CommandException("Invalid package name: " + pkg);
      }

      doAddProject(parameters);

      templateService.unpack(CreateJavaDistributionCommand.class.getPackage().getName(),
                             parameters,
                             outputDirectory,
                             clean);
      logService.info(CreateJavaDistributionCommand.class, "%s distribution project successfully created",
                      model.getName());
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

   protected void doCreateDirectories(Path outputDirectory) {
      try {
         Files.createDirectories(outputDirectory);
      } catch (IOException e) {
         logService.error(CreateJavaDistributionCommand.class, e);
         throw new CommandException(e);
      }
   }
}

