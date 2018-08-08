package com.ngc.seaside.jellyfish.cli.command.createjavasystemproject;

import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.api.IParameter;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.utilities.command.AbstractJellyfishCommand;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CreateJavaSystemProjectCommand extends AbstractJellyfishCommand {

   static final String GROUP_ID_PROPERTY = CommonParameters.GROUP_ID.getName();
   static final String PROJECT_NAME_PROPERTY = "projectName";
   static final String MODEL_PROPERTY = CommonParameters.MODEL.getName();
   static final String DEPLOYMENT_MODEL_PROPERTY = CommonParameters.DEPLOYMENT_MODEL.getName();
   static final String OUTPUT_DIRECTORY_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   static final String GAV_PROPERTY = CommonParameters.GROUP_ARTIFACT_VERSION.getName();

   static final String GRADLE_JELLYFISH_COMMAND_PARAMETER_NAME = "gradleJellyfishCommand";

   static final String CREATE_JELLYFISH_GRADLE_PROJECT_COMMAND_NAME = "create-jellyfish-gradle-project";
   static final String CREATE_JAVA_CUCUMBER_TESTS_COMMAND_NAME = "create-java-cucumber-tests";
   static final String CREATE_JAVA_CUCUMBER_TESTS_CONFIG_COMMAND_NAME = "create-java-cucumber-tests-config";
   static final String CREATE_JAVA_DISTRIBUTION_COMMAND_NAME = "create-java-distribution";
   static final String CREATE_JAVA_SERVICE_BASE_COMMAND_NAME = "create-java-service-base";
   static final String CREATE_PROTOCOLBUFFER_MESSAGES_COMMAND_NAME = "create-protocolbuffer-messages";

   private static final String[] SUBCOMMANDS = {CREATE_JELLYFISH_GRADLE_PROJECT_COMMAND_NAME,
                                                CREATE_JAVA_CUCUMBER_TESTS_COMMAND_NAME,
                                                CREATE_JAVA_CUCUMBER_TESTS_CONFIG_COMMAND_NAME,
                                                CREATE_JAVA_DISTRIBUTION_COMMAND_NAME,
                                                CREATE_JAVA_SERVICE_BASE_COMMAND_NAME,
                                                CREATE_PROTOCOLBUFFER_MESSAGES_COMMAND_NAME};

   private static final String NAME = "create-java-system-project";

   private IJellyFishCommandProvider jellyFishCommandProvider;

   public CreateJavaSystemProjectCommand() {
      super(NAME);
   }

   @Override
   public void activate() {
      logService.trace(getClass(), "Activated");
   }

   @Override
   public void deactivate() {
      logService.trace(getClass(), "Deactivated");
   }

   public void setJellyFishCommandProvider(IJellyFishCommandProvider ref) {
      this.jellyFishCommandProvider = ref;
   }

   public void removeJellyFishCommandProvider(IJellyFishCommandProvider ref) {
      setJellyFishCommandProvider(null);
   }

   @Override
   protected IUsage createUsage() {
      Map<String, IParameter<?>> usageParameters = new HashMap<>();

      usageParameters.put(OUTPUT_DIRECTORY_PROPERTY, CommonParameters.OUTPUT_DIRECTORY.required());
      usageParameters.put(MODEL_PROPERTY, CommonParameters.MODEL.required());
      usageParameters.put(DEPLOYMENT_MODEL_PROPERTY, CommonParameters.DEPLOYMENT_MODEL);
      usageParameters.put(PROJECT_NAME_PROPERTY, new DefaultParameter<String>(PROJECT_NAME_PROPERTY)
            .setDescription("The name of the project.").setRequired(false));
      usageParameters.put(GAV_PROPERTY, CommonParameters.GROUP_ARTIFACT_VERSION);

      for (String subcommand : SUBCOMMANDS) {
         List<IParameter<?>> parameters = jellyFishCommandProvider.getCommand(subcommand).getUsage()
               .getAllParameters();
         for (IParameter<?> parameter : parameters) {
            if (parameter.getName() != null && parameter.getDescription() != null) {
               IParameter<?> previous = usageParameters.get(parameter.getName());
               if (previous == null || !previous.isRequired()) {
                  usageParameters.put(parameter.getName(), parameter);
               }
            }
         }
      }

      // This is explicitly optional since the config command will run if it's not provided
      usageParameters.put(DEPLOYMENT_MODEL_PROPERTY, CommonParameters.DEPLOYMENT_MODEL);

      IParameter<?>[] parameters = usageParameters.values().toArray(new IParameter<?>[usageParameters.size()]);
      return new DefaultUsage("Create a new Java service project for a particular model.", parameters);
   }

   @Override
   protected void doRun() {
      CommandInvocationContext ctx = buildContext();

      createCucumberTestsProject(ctx);
      createDistributionProject(ctx);

      createJavaServiceBaseProject(ctx);
      createProtocolBufferMessagesProject(ctx);

      // Do this last after all the other commands have run.  We do this because the build mgmt service builds up
      // state as the commands are run.  The next command will read this state, so we want to make sure all the other
      // commands are finished.
      createJellyFishGradleProject(ctx);
   }

   private IJellyFishCommandOptions generateDelegateOptions(CommandInvocationContext ctx) {
      // default: gradleStyle==false
      return generateDelegateOptions(ctx, false);
   }

   private IJellyFishCommandOptions generateDelegateOptions(CommandInvocationContext ctx, boolean gradleStyle) {
      IJellyFishCommandOptions delegateOptions;
      if (gradleStyle) {
         delegateOptions = DefaultJellyFishCommandOptions.mergeWith(
               ctx.standardCommandOptions,
               new DefaultParameter<>(PROJECT_NAME_PROPERTY, ctx.projectName),
               new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, ctx.rootOutputDirectory.getAbsolutePath())
         );
      } else {
         delegateOptions = DefaultJellyFishCommandOptions.mergeWith(
               ctx.standardCommandOptions,
               new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, ctx.projectDirectory.getAbsolutePath())
         );
      }
      return delegateOptions;
   }

   private void createJellyFishGradleProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = generateDelegateOptions(ctx, true);
      doRunCommand(CREATE_JELLYFISH_GRADLE_PROJECT_COMMAND_NAME, delegateOptions);
   }

   private void createCucumberTestsProject(CommandInvocationContext ctx) {
      String command;
      if (ctx.generatedConfigProjectUsed) {
         command = CREATE_JAVA_CUCUMBER_TESTS_CONFIG_COMMAND_NAME;
         IJellyFishCommandOptions delegateOptions = generateDelegateOptions(ctx);
         doRunCommand(command, delegateOptions);
      }
      command = CREATE_JAVA_CUCUMBER_TESTS_COMMAND_NAME;
      IJellyFishCommandOptions delegateOptions = generateDelegateOptions(ctx);
      doRunCommand(command, delegateOptions);
   }

   private void createDistributionProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = generateDelegateOptions(ctx);
      doRunCommand(CREATE_JAVA_DISTRIBUTION_COMMAND_NAME, delegateOptions);
   }

   private void createJavaServiceBaseProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = generateDelegateOptions(ctx);
      doRunCommand(CREATE_JAVA_SERVICE_BASE_COMMAND_NAME, delegateOptions);
   }

   private void createProtocolBufferMessagesProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = generateDelegateOptions(ctx);
      doRunCommand(CREATE_PROTOCOLBUFFER_MESSAGES_COMMAND_NAME, delegateOptions);
   }

   private void doRunCommand(String commandName, IJellyFishCommandOptions delegateOptions) {
      logService.debug(CreateJavaSystemProjectCommand.class,
                       "--------------------------------------------------");
      logService.debug(CreateJavaSystemProjectCommand.class,
                       "Running %s", commandName);
      logService.debug(CreateJavaSystemProjectCommand.class,
                       "--------------------------------------------------");
      jellyFishCommandProvider.run(commandName, delegateOptions);
   }

   private CommandInvocationContext buildContext() {
      CommandInvocationContext ctx = new CommandInvocationContext();
      ctx.originalCommandOptions = getOptions();

      // Get the fully qualified model name.
      ctx.modelName = getOptions().getParameters().getParameter(MODEL_PROPERTY).getStringValue();

      // Find the actual model.
      ctx.model = getOptions().getSystemDescriptor()
            .findModel(ctx.modelName)
            .orElseThrow(() -> new CommandException(String.format("model %s not found!", ctx.modelName)));

      // Get the fully qualified deployment model name.
      ctx.deploymentModelName =
            Optional.ofNullable(getOptions().getParameters().getParameter(DEPLOYMENT_MODEL_PROPERTY))
                  .map(IParameter::getStringValue);

      // Find the actual deployment model.
      // Note the explicit <CommandException> is to work around a JDK bug:
      // https://bugs.openjdk.java.net/browse/JDK-8054569 that is impacting some JDKs.
      ctx.deploymentModel = ctx.deploymentModelName.map(name -> getOptions().getSystemDescriptor()
            .findModel(name)
            .<CommandException>orElseThrow(() -> new CommandException(String.format("deployment model %s not found!",
                                                                                    ctx.deploymentModelName))));

      // Whether or not the configuration should use the generated config command
      ctx.generatedConfigProjectUsed = ctx.deploymentModel.isPresent();

      // Get the directory that will contain the project directory.
      ctx.rootOutputDirectory = Paths.get(
            getOptions().getParameters().getParameter(OUTPUT_DIRECTORY_PROPERTY).getStringValue())
            .toFile();

      // Get the group ID.
      if (getOptions().getParameters().containsParameter(GROUP_ID_PROPERTY)) {
         ctx.groupId = getOptions().getParameters().getParameter(GROUP_ID_PROPERTY).getStringValue();
      } else {
         // Compute group ID from model package.
         ctx.groupId = ctx.model.getParent().getName();
      }

      // Get the actual project name.
      if (getOptions().getParameters().containsParameter(PROJECT_NAME_PROPERTY)) {
         ctx.projectName = getOptions().getParameters().getParameter(PROJECT_NAME_PROPERTY).getStringValue();
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
            new DefaultParameter<>(MODEL_PROPERTY, ctx.modelName),
            new DefaultParameter<>(CommonParameters.SYSTEM.getName(), true));

      return ctx;
   }

   private static class CommandInvocationContext {

      IJellyFishCommandOptions originalCommandOptions;
      IJellyFishCommandOptions standardCommandOptions;

      File rootOutputDirectory;
      File projectDirectory;

      String projectName;
      String groupId;
      String modelName;
      Optional<String> deploymentModelName;
      boolean generatedConfigProjectUsed;

      IModel model;
      Optional<IModel> deploymentModel;
   }
}
