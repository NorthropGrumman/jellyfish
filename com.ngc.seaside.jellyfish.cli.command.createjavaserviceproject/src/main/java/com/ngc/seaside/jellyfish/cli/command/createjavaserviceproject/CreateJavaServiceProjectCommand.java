package com.ngc.seaside.jellyfish.cli.command.createjavaserviceproject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.DefaultJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.File;
import java.nio.file.Paths;

@Component(service = IJellyFishCommand.class)
public class CreateJavaServiceProjectCommand implements IJellyFishCommand {

   private static final IUsage USAGE = createUsage();

   static final String GROUP_ID_PROPERTY = "groupId";
   static final String ARTIFACT_ID_PROPERTY = "artifactId";
   static final String PACKAGE_PROPERTY = "package";
   static final String PROJECT_NAME = "projectName";
   static final String MODEL_PROPERTY = "model";
   static final String OUTPUT_DIRECTORY_PROPERTY = "outputDirectory";

   static final String DEFAULT_OUTPUT_DIRECTOY = ".";

   static final String CREATE_JELLYFISH_GRADLE_PROJECT_COMMAND_NAME = "create-jellyfish-gradle-project";
   static final String CREATE_DOMAIN_COMMAND_NAME = "create-domain";
   static final String CREATE_JAVA_EVENTS_COMMAND_NAME = "create-java-events";
   static final String CREATE_JAVA_DISTRIBUTION_COMMAND_NAME = "create-java-distribution";
   static final String CREATE_JAVA_SERVICE_COMMAND_NAME = "create-java-service";
   static final String CREATE_JAVA_SERVICE_BASE_COMMAND_NAME = "create-java-service-base";
   static final String CREATE_JAVA_SERVICE_CONNECTOR_COMMAND_ANME = "create-java-service-connector";

   public static final String NAME = "create-java-service-project";

   private ILogService logService;
   private IPromptUserService promptUserService;
   private IJellyFishCommandProvider jellyFishCommandProvider;

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
      CommandInvocationContext ctx = buildContext(commandOptions);

      createJellyFishGradleProject(ctx);
      createDomainProject(ctx);
      createEventsProject(ctx);
      createDistributionProject(ctx);
      createJavaServiceProject(ctx);

      // TODO TH: put these in the generated-projects directory.
      // Allow user to specific the name of the directory for generated projects.
      createJavaServiceBaseProject(ctx);
      createJavaServiceConnectorProject(ctx);
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

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeJellyFishCommandProvider")
   public void setJellyFishCommandProvider(IJellyFishCommandProvider ref) {
      this.jellyFishCommandProvider = ref;
   }

   public void removeJellyFishCommandProvider(IJellyFishCommandProvider ref) {
      setJellyFishCommandProvider(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removePromptUserService")
   public void setPromptUserService(IPromptUserService ref) {
      this.promptUserService = ref;
   }

   public void removePromptUserService(IPromptUserService ref) {
      setPromptUserService(null);
   }

   private void createJellyFishGradleProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = DefaultJellyFishCommandOptions.mergeWith(
            ctx.standardCommandOptions,
            new DefaultParameter<>(PROJECT_NAME, ctx.projectName),
            new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, ctx.rootOutputDirectory.getAbsolutePath())
      );
      doRunCommand(CREATE_JELLYFISH_GRADLE_PROJECT_COMMAND_NAME, delegateOptions);
   }

   private void createDomainProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = DefaultJellyFishCommandOptions.mergeWith(
            ctx.standardCommandOptions,
            new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, ctx.projectDirectory.getAbsolutePath())
      );
      doRunCommand(CREATE_DOMAIN_COMMAND_NAME, delegateOptions);
   }

   private void createEventsProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = DefaultJellyFishCommandOptions.mergeWith(
            ctx.standardCommandOptions,
            new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, ctx.projectDirectory.getAbsolutePath())
      );
      doRunCommand(CREATE_JAVA_EVENTS_COMMAND_NAME, delegateOptions);
   }

   private void createDistributionProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = DefaultJellyFishCommandOptions.mergeWith(
            ctx.standardCommandOptions,
            new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, ctx.projectDirectory.getAbsolutePath())
      );
      doRunCommand(CREATE_JAVA_DISTRIBUTION_COMMAND_NAME, delegateOptions);
   }

   private void createJavaServiceProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = DefaultJellyFishCommandOptions.mergeWith(
            ctx.standardCommandOptions,
            new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, ctx.projectDirectory.getAbsolutePath())
      );
      doRunCommand(CREATE_JAVA_SERVICE_COMMAND_NAME, delegateOptions);
   }

   private void createJavaServiceBaseProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = DefaultJellyFishCommandOptions.mergeWith(
            ctx.standardCommandOptions,
            new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, ctx.projectDirectory.getAbsolutePath())
      );
      doRunCommand(CREATE_JAVA_SERVICE_BASE_COMMAND_NAME, delegateOptions);
   }


   private void createJavaServiceConnectorProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = DefaultJellyFishCommandOptions.mergeWith(
            ctx.standardCommandOptions,
            new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, ctx.projectDirectory.getAbsolutePath())
      );
      doRunCommand(CREATE_JAVA_SERVICE_CONNECTOR_COMMAND_ANME, delegateOptions);
   }

   private void doRunCommand(String commandName, IJellyFishCommandOptions delegateOptions) {
      logService.debug(CreateJavaServiceProjectCommand.class,
                       "--------------------------------------------------");
      logService.debug(CreateJavaServiceProjectCommand.class,
                       "Running %s", commandName);
      logService.debug(CreateJavaServiceProjectCommand.class,
                       "--------------------------------------------------");
      jellyFishCommandProvider.run(commandName, delegateOptions);
   }

   private CommandInvocationContext buildContext(IJellyFishCommandOptions commandOptions) {
      CommandInvocationContext ctx = new CommandInvocationContext();
      ctx.originalCommandOptions = commandOptions;

      // Get the fully qualified model name.
      if (commandOptions.getParameters().containsParameter(MODEL_PROPERTY)) {
         ctx.modelName = commandOptions.getParameters().getParameter(MODEL_PROPERTY).getStringValue();
      } else {
         ctx.modelName = promptUserService.prompt(MODEL_PROPERTY,
                                                  null,
                                                  m -> commandOptions.getSystemDescriptor().findModel(m).isPresent());
      }
      // Find the actual model.
      ctx.model = commandOptions.getSystemDescriptor()
            .findModel(ctx.modelName)
            .orElseThrow(() -> new CommandException(String.format("model %s not found!", ctx.modelName)));

      // Get the directory that will contain the project directory.
      if (commandOptions.getParameters().containsParameter(OUTPUT_DIRECTORY_PROPERTY)) {
         ctx.rootOutputDirectory = Paths.get(
               commandOptions.getParameters().getParameter(OUTPUT_DIRECTORY_PROPERTY).getStringValue())
               .toFile();
      } else {
         // Ask the user if needed.
         ctx.rootOutputDirectory = Paths.get(
               promptUserService.prompt(OUTPUT_DIRECTORY_PROPERTY, DEFAULT_OUTPUT_DIRECTOY, null))
               .toFile();
      }

      // Get the group ID.
      if (commandOptions.getParameters().containsParameter(GROUP_ID_PROPERTY)) {
         ctx.groupId = commandOptions.getParameters().getParameter(GROUP_ID_PROPERTY).getStringValue();
      } else {
         // Compute group ID from model package.
         ctx.groupId = ctx.model.getParent().getName();
      }

      // Get the actual project name.
      if (commandOptions.getParameters().containsParameter(PROJECT_NAME)) {
         ctx.projectName = commandOptions.getParameters().getParameter(PROJECT_NAME).getStringValue();
      } else {
         // Compute the project name from the groupID and the model name.
         ctx.projectName = ctx.groupId + "." + ctx.model.getName().toLowerCase();
      }

      // Make the output directory if needed.
      if (!ctx.rootOutputDirectory.exists()) {
         ctx.rootOutputDirectory.mkdir();
      }
      if (!ctx.rootOutputDirectory.isDirectory()) {
         throw new CommandException(String.format("outputDirectory %s is not a directory!", ctx.rootOutputDirectory));
      }

      // This is the directory that will contain the actual projects.  Its ${outputDirectory}/${projectName}.
      ctx.projectDirectory = ctx.rootOutputDirectory.toPath().resolve(ctx.projectName).toFile();

      // Build a set of standard options that will be used by all commands.  Note the actual output directories will be
      // different depending on each command.
      ctx.standardCommandOptions = DefaultJellyFishCommandOptions.mergeWith(
            ctx.originalCommandOptions,
            new DefaultParameter<>(GROUP_ID_PROPERTY, ctx.groupId),
            new DefaultParameter<>(MODEL_PROPERTY, ctx.modelName));

      return ctx;
   }


   /**
    * Create the usage for this command.
    *
    * @return the usage.
    */
   private static IUsage createUsage() {
      return new DefaultUsage(
            "Create a new Java service project for a particular model.",
            new DefaultParameter<String>(OUTPUT_DIRECTORY_PROPERTY)
                  .setDescription("Base directory in which to output the project")
                  .setRequired(true),
            new DefaultParameter<String>(MODEL_PROPERTY)
                  .setDescription("The fully qualified path to the system descriptor model")
                  .setRequired(true),
            new DefaultParameter<String>(PROJECT_NAME)
                  .setDescription("The name of the project.")
                  .setRequired(false),
            new DefaultParameter<String>(GROUP_ID_PROPERTY)
                  .setDescription("The project's group ID")
                  .setRequired(false),
            new DefaultParameter<String>(ARTIFACT_ID_PROPERTY)
                  .setDescription("The project's version")
                  .setRequired(false),
            new DefaultParameter<String>(PACKAGE_PROPERTY)
                  .setDescription("The project's default package")
                  .setRequired(false));
   }

   private static class CommandInvocationContext {

      IJellyFishCommandOptions originalCommandOptions;
      IJellyFishCommandOptions standardCommandOptions;

      File rootOutputDirectory;
      File projectDirectory;

      String projectName;
      String groupId;
      String modelName;

      IModel model;
   }
}
