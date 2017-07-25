package com.ngc.seaside.jellyfish.cli.command.createjavaservicecommand;

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
public class CreateJavaServiceCommand implements IJellyFishCommand {


   public static final String GROUP_ID_PROPERTY = "groupId";
   public static final String ARTIFACT_ID_PROPERTY = "artifactId";
   public static final String MODEL_PROPERTY = "model";
   public static final String MODELNAME_PROPERTY = "modelname";
   public static final String OUTPUT_DIRECTORY_PROPERTY = "outputDirectory";
   public static final String DEFAULT_PACKAGE_SUFFIX = "distribution";
   public static final String GENERATE_BASE_PROPERTY = "base";
   public static final String GENERATE_DELEGATE_PROPERTY = "delegate";
   public static final String PACKAGE_PROPERTY = "package";
   public static final String CLEAN_PROPERTY = "clean";

   private static final String NAME = "create-java-service";
   private static final IUsage USAGE = createUsage();
   private static final Pattern JAVA_QUALIFIED_IDENTIFIER = Pattern
         .compile("[a-zA-Z$_][a-zA-Z$_0-9]*(?:\\.[a-zA-Z$_][a-zA-Z$_0-9]*)*");
   private ILogService logService;
   private IPromptUserService promptService;
   private ITemplateService templateService;

   private static IUsage createUsage() {
      return new DefaultUsage("Generates the service for a Java application",
                              new DefaultParameter(GROUP_ID_PROPERTY).setDescription
                                    ("The project's group ID. (default: the package in the model)")
                                    .setRequired(false),
                              new DefaultParameter(ARTIFACT_ID_PROPERTY).setDescription
                                    ("The project's artifact Id. (default: the model name in lowercase + '.distribution')")
                                    .setRequired(false),
                              new DefaultParameter(MODEL_PROPERTY).setDescription
                                    ("The fully qualified path to the model.")
                                    .setRequired(true),
                              new DefaultParameter(OUTPUT_DIRECTORY_PROPERTY).setDescription
                                    ("Base directory in which to output the project")
                                    .setRequired(true),
                              new DefaultParameter(GENERATE_BASE_PROPERTY).setDescription
                                    ("This will allow you to generate the abstraction layer for the project")
                                    .setRequired(false),
                              new DefaultParameter(GENERATE_DELEGATE_PROPERTY).setDescription
                                    ("This is the class that the developer will edit")
                                    .setRequired(false),
                              new DefaultParameter(CLEAN_PROPERTY).setDescription
                                    ("If true, recursively deletes the domain project (if it already exists), before generating the it again")
                                    .setRequired(false)
      );
   }

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      // TODO Auto-generated method stub
      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameters(commandOptions.getParameters().getAllParameters());

      // Resolve model
      if (!parameters.containsParameter(MODEL_PROPERTY)) {
         String modelId = promptService.prompt(MODEL_PROPERTY, null, null);
         parameters.addParameter(new DefaultParameter<>(MODEL_PROPERTY, modelId));
      }

      ISystemDescriptor systemDescriptor = commandOptions.getSystemDescriptor();
      String modelId = parameters.getParameter(MODEL_PROPERTY).getStringValue();
      final IModel model = systemDescriptor.findModel(modelId)
            .orElseThrow(() -> new CommandException("Unknown model:" + modelId));

      parameters.addParameter(new DefaultParameter<>(MODELNAME_PROPERTY, model.getName()));

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
      // Resolve outputDirectory
      if (!parameters.containsParameter(OUTPUT_DIRECTORY_PROPERTY)) {
         String input = promptService.prompt(OUTPUT_DIRECTORY_PROPERTY, null, null);
         parameters.addParameter(new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, input));
      }
      final Path outputDirectory = Paths.get(parameters.getParameter(OUTPUT_DIRECTORY_PROPERTY).getStringValue());
      try {
         Files.createDirectories(outputDirectory);
      } catch (IOException e) {
         logService.error(CreateJavaServiceCommand.class, e);
      }

      // Resolve base
      if (!parameters.containsParameter(GENERATE_BASE_PROPERTY)) {
         parameters.addParameter(new DefaultParameter<String>(GENERATE_BASE_PROPERTY, "true"));
      }

      // Resolve delegate
      if (!parameters.containsParameter(GENERATE_DELEGATE_PROPERTY)) {
         parameters.addParameter(new DefaultParameter<String>(GENERATE_DELEGATE_PROPERTY, "true"));
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
//
//      templateService.unpack(CreateJavaServiceCommand.class.getPackage().getName(),
//                             parameters,
//                             outputDirectory,
//                             clean);
//      logService.info(CreateJavaServiceCommand.class, "%s service project successfully created",
//                      model.getName());
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

   protected void doAddProject(IParameterCollection parameters) {
      try {
         GradleSettingsUtilities.addProject(parameters);
      } catch (FileUtilitiesException e) {
         logService.warn(getClass(), e, "Unable to add the new project to settings.gradle.");
         throw new CommandException(e);
      }
   }

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

}
