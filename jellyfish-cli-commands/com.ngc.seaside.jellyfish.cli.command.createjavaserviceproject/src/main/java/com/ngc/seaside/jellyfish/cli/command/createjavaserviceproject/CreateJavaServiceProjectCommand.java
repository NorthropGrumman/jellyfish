package com.ngc.seaside.jellyfish.cli.command.createjavaserviceproject;

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

public class CreateJavaServiceProjectCommand extends AbstractJellyfishCommand {

   static final String GROUP_ID_PROPERTY = CommonParameters.GROUP_ID.getName();
   static final String PROJECT_NAME_PROPERTY = CommonParameters.PROJECT_NAME.getName();
   static final String MODEL_PROPERTY = CommonParameters.MODEL.getName();
   static final String DEPLOYMENT_MODEL_PROPERTY = CommonParameters.DEPLOYMENT_MODEL.getName();
   static final String OUTPUT_DIRECTORY_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   static final String CREATE_SERVICE_DOMAIN_PROPERTY = "createServiceDomain";

   static final String GRADLE_JELLYFISH_COMMAND_PARAMETER_NAME = "gradleJellyfishCommand";

   static final String CREATE_JELLYFISH_GRADLE_PROJECT_COMMAND_NAME = "create-jellyfish-gradle-project";
   static final String CREATE_DOMAIN_COMMAND_NAME = "create-domain";
   static final String CREATE_JAVA_EVENTS_COMMAND_NAME = "create-java-events";
   static final String CREATE_JAVA_CUCUMBER_TESTS_COMMAND_NAME = "create-java-cucumber-tests";
   static final String CREATE_JAVA_CUCUMBER_TESTS_CONFIG_COMMAND_NAME = "create-java-cucumber-tests-config";
   static final String CREATE_JAVA_DISTRIBUTION_COMMAND_NAME = "create-java-distribution";
   static final String CREATE_JAVA_SERVICE_COMMAND_NAME = "create-java-service";
   static final String CREATE_JAVA_SERVICE_BASE_COMMAND_NAME = "create-java-service-base";
   static final String CREATE_JAVA_PUBSUB_CONNECTOR_COMMAND_NAME = "create-java-protobuf-connector";
   static final String CREATE_JAVA_SERVICE_CONFIG_COMMAND_NAME = "create-java-service-config";
   static final String CREATE_JAVA_SERVICE_PUBSUB_BRIDGE_COMMAND_NAME = "create-java-service-pubsub-bridge";
   static final String CREATE_JAVA_SERVICE_GENERATED_CONFIG_COMMAND_NAME = "create-java-service-generated-config";
   static final String CREATE_PROTOCOLBUFFER_MESSAGES_COMMAND_NAME = "create-protocolbuffer-messages";

   private static final String[] SUBCOMMANDS = {CREATE_JELLYFISH_GRADLE_PROJECT_COMMAND_NAME,
                                                CREATE_DOMAIN_COMMAND_NAME, CREATE_JAVA_EVENTS_COMMAND_NAME,
                                                CREATE_JAVA_CUCUMBER_TESTS_COMMAND_NAME,
                                                CREATE_JAVA_CUCUMBER_TESTS_CONFIG_COMMAND_NAME,
                                                CREATE_JAVA_DISTRIBUTION_COMMAND_NAME,
                                                CREATE_JAVA_SERVICE_BASE_COMMAND_NAME,
                                                CREATE_JAVA_PUBSUB_CONNECTOR_COMMAND_NAME,
                                                CREATE_JAVA_SERVICE_CONFIG_COMMAND_NAME,
                                                CREATE_JAVA_SERVICE_PUBSUB_BRIDGE_COMMAND_NAME,
                                                CREATE_JAVA_SERVICE_GENERATED_CONFIG_COMMAND_NAME,
                                                CREATE_PROTOCOLBUFFER_MESSAGES_COMMAND_NAME};

   private static final String NAME = "create-java-service-project";

   private IJellyFishCommandProvider jellyFishCommandProvider;

   public CreateJavaServiceProjectCommand() {
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

      usageParameters.put(CREATE_SERVICE_DOMAIN_PROPERTY, new DefaultParameter<>(CREATE_SERVICE_DOMAIN_PROPERTY, 
               "Whether or not to create the service's domain model"));
      usageParameters.put(MODEL_PROPERTY, CommonParameters.MODEL.required());
      usageParameters.put(OUTPUT_DIRECTORY_PROPERTY, CommonParameters.OUTPUT_DIRECTORY.required());
      usageParameters.put(PROJECT_NAME_PROPERTY, CommonParameters.PROJECT_NAME);

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
      return new DefaultUsage("Creates a new Java micro-service project for a service model", parameters);
   }

   @Override
   protected void doRun() {
      CommandInvocationContext ctx = buildContext();

      if (ctx.createDomain) {
         createDomainProject(ctx);
      }

      createCucumberTestsProject(ctx);
      createDistributionProject(ctx);
      createJavaServiceProject(ctx);
      createJavaServiceConfigProject(ctx);

      createJavaServiceBaseProject(ctx);
      createJavaPubsubConnectorProject(ctx);
      createJavaPubsubPubsubBridgeProject(ctx);
      createEventsProject(ctx);
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

   private void createDomainProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = generateDelegateOptions(ctx);
      doRunCommand(CREATE_DOMAIN_COMMAND_NAME, delegateOptions);
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

   private void createJavaServiceProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = generateDelegateOptions(ctx);
      doRunCommand(CREATE_JAVA_SERVICE_COMMAND_NAME, delegateOptions);
   }

   private void createJavaServiceConfigProject(CommandInvocationContext ctx) {
      final String command;
      if (ctx.generatedConfigProjectUsed) {
         command = CREATE_JAVA_SERVICE_GENERATED_CONFIG_COMMAND_NAME;
      } else {
         command = CREATE_JAVA_SERVICE_CONFIG_COMMAND_NAME;
      }
      IJellyFishCommandOptions delegateOptions = generateDelegateOptions(ctx);
      doRunCommand(command, delegateOptions);
   }

   private void createJavaServiceBaseProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = generateDelegateOptions(ctx);
      doRunCommand(CREATE_JAVA_SERVICE_BASE_COMMAND_NAME, delegateOptions);
   }

   private void createEventsProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = generateDelegateOptions(ctx);
      doRunCommand(CREATE_JAVA_EVENTS_COMMAND_NAME, delegateOptions);
   }

   private void createProtocolBufferMessagesProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = generateDelegateOptions(ctx);
      doRunCommand(CREATE_PROTOCOLBUFFER_MESSAGES_COMMAND_NAME, delegateOptions);
   }

   //TODO Is this needed now?
   private void createJavaPubsubConnectorProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = generateDelegateOptions(ctx);
      doRunCommand(CREATE_JAVA_PUBSUB_CONNECTOR_COMMAND_NAME, delegateOptions);
   }
   
   //TODO Is this needed now?
   private void createJavaPubsubPubsubBridgeProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = generateDelegateOptions(ctx);
      doRunCommand(CREATE_JAVA_SERVICE_PUBSUB_BRIDGE_COMMAND_NAME, delegateOptions);
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

      ctx.createDomain = CommonParameters.evaluateBooleanParameter(getOptions().getParameters(),
                                                                   CREATE_SERVICE_DOMAIN_PROPERTY,
                                                                   true);

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
            new DefaultParameter<>(MODEL_PROPERTY, ctx.modelName));

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

      boolean createDomain;
   }
}
