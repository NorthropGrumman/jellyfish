package com.ngc.seaside.jellyfish.cli.command.createjavaservice;

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
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.IServiceDtoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.ServiceDto;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

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
   private IServiceDtoFactory templateDaoFactory;
   private IProjectNamingService projectNamingService;

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      IModel model = evaluateModelParameter(commandOptions);
      boolean clean = evaluateBooleanParameter(commandOptions, CLEAN_PROPERTY);
      Path outputDir = evaluateOutputDirectory(commandOptions);

      IProjectInformation projectInfo = projectNamingService.getServiceProjectName(commandOptions, model);

      ServiceDto dto = templateDaoFactory.newDto(commandOptions, model);

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>("dto", dto));
      templateService.unpack(CreateJavaServiceCommand.class.getPackage().getName(),
                             parameters,
                             outputDir,
                             clean);

      try {
         parameters.addParameter(new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, outputDir.toString()));
         parameters.addParameter(new DefaultParameter<>(GROUP_ID_PROPERTY, projectInfo.getGroupId()));
         parameters.addParameter(new DefaultParameter<>(ARTIFACT_ID_PROPERTY, projectInfo.getArtifactId()));
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
   public void setTemplateDaoFactory(IServiceDtoFactory ref) {
      this.templateDaoFactory = ref;
   }

   public void removeTemplateDaoFactory(IServiceDtoFactory ref) {
      setTemplateDaoFactory(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC)
   public void setProjectNamingService(IProjectNamingService ref) {
      this.projectNamingService = ref;
   }

   public void removeProjectNamingService(IProjectNamingService ref) {
      setProjectNamingService(null);
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

   private static IUsage createUsage() {
      return new DefaultUsage(
            "Generates the service for a Java application",
            new DefaultParameter<>(GROUP_ID_PROPERTY)
                  .setDescription("The project's group ID. (default: the package in the model)")
                  .setRequired(false),
            new DefaultParameter<>(ARTIFACT_ID_PROPERTY)
                  .setDescription("The project's artifact Id. (default: the model name in lowercase)")
                  .setRequired(false),
            new DefaultParameter<>(MODEL_PROPERTY)
                  .setDescription("The fully qualified path to the model.")
                  .setRequired(true),
            new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY)
                  .setDescription("Base directory in which to output the project")
                  .setRequired(true),
            new DefaultParameter<>(CLEAN_PROPERTY)
                  .setDescription("If true, recursively deletes the domain project before generating the it again")
                  .setRequired(false)
      );
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
