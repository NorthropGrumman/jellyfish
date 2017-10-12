package com.ngc.seaside.jellyfish.cli.command.createjavaserviceproject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(service = IJellyFishCommand.class)
public class CreateJavaServiceProjectCommand implements IJellyFishCommand {

   private IUsage USAGE = null;

   static final String GROUP_ID_PROPERTY = CommonParameters.GROUP_ID.getName();
   static final String PROJECT_NAME_PROPERTY = "projectName";
   static final String MODEL_PROPERTY = CommonParameters.MODEL.getName();
   static final String OUTPUT_DIRECTORY_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   static final String CREATE_SERVICE_DOMAIN_PROPERTY = "createServiceDomain";

   static final String DEFAULT_OUTPUT_DIRECTORY = ".";

   static final String CREATE_JELLYFISH_GRADLE_PROJECT_COMMAND_NAME = "create-jellyfish-gradle-project";
   static final String CREATE_DOMAIN_COMMAND_NAME = "create-domain";
   static final String CREATE_JAVA_EVENTS_COMMAND_NAME = "create-java-events";
   static final String CREATE_JAVA_CUCUMBER_TESTS_COMMAND_NAME = "create-java-cucumber-tests";
   static final String CREATE_JAVA_DISTRIBUTION_COMMAND_NAME = "create-java-distribution";
   static final String CREATE_JAVA_SERVICE_COMMAND_NAME = "create-java-service";
   static final String CREATE_JAVA_SERVICE_BASE_COMMAND_NAME = "create-java-service-base";
   static final String CREATE_JAVA_PUBSUB_CONNECTOR_COMMAND_NAME = "create-java-pubsub-connector";
   static final String CREATE_JAVA_SERVICE_CONFIG_COMMAND_NAME = "create-java-service-config";
   static final String CREATE_PROTOCOLBUFFER_MESSAGES_COMMAND_NAME = "create-protocolbuffer-messages";
   private static final String[] SUBCOMMANDS = {CREATE_JELLYFISH_GRADLE_PROJECT_COMMAND_NAME,
                                                CREATE_DOMAIN_COMMAND_NAME, CREATE_JAVA_EVENTS_COMMAND_NAME,
                                                CREATE_JAVA_CUCUMBER_TESTS_COMMAND_NAME,
                                                CREATE_JAVA_DISTRIBUTION_COMMAND_NAME,
                                                CREATE_JAVA_SERVICE_BASE_COMMAND_NAME,
                                                CREATE_JAVA_PUBSUB_CONNECTOR_COMMAND_NAME,
                                                CREATE_JAVA_SERVICE_CONFIG_COMMAND_NAME,
                                                CREATE_PROTOCOLBUFFER_MESSAGES_COMMAND_NAME};

   public static final String NAME = "create-java-service-project";

   private ILogService logService;
   private IJellyFishCommandProvider jellyFishCommandProvider;
   private ITemplateService templateService;
   private IProjectNamingService projectNamingService;

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IUsage getUsage() {
      if (USAGE == null) {
         synchronized (CreateJavaServiceProjectCommand.class) {
            if (USAGE == null) {
               Map<String, IParameter<?>> usageParameters = new HashMap<>();

               for (String subcommand : SUBCOMMANDS) {
                  List<IParameter<?>> parameters = jellyFishCommandProvider.getCommand(subcommand).getUsage()
                        .getAllParameters();
                  for (IParameter<?> parameter : parameters) {
                     if (parameter.getName() != null && parameter.getDescription() != null) {
                        usageParameters.put(parameter.getName(), parameter);
                     }
                  }
               }

               usageParameters.put(OUTPUT_DIRECTORY_PROPERTY, CommonParameters.OUTPUT_DIRECTORY.required());
               usageParameters.put(MODEL_PROPERTY, CommonParameters.MODEL.required());
               usageParameters.put(PROJECT_NAME_PROPERTY, new DefaultParameter<String>(PROJECT_NAME_PROPERTY)
                     .setDescription("The name of the project.").setRequired(false));
               usageParameters.put(CREATE_SERVICE_DOMAIN_PROPERTY,
                                   new DefaultParameter<String>(CREATE_SERVICE_DOMAIN_PROPERTY)
                                         .setDescription("Whether or not to create the service's domain model")
                                         .setRequired(false));
               IParameter<?>[] parameters = usageParameters.values().toArray(new IParameter<?>[usageParameters.size()]);
               USAGE = new DefaultUsage("Create a new Java service project for a particular model.", parameters);
            }
         }
      }
      return USAGE;
   }

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      CommandInvocationContext ctx = buildContext(commandOptions);

      createJellyFishGradleProject(ctx);
      if (ctx.createDomain) {
         createDomainProject(ctx);
      }
      createEventsProject(ctx);
      createCucumberTestsProject(ctx);
      createDistributionProject(ctx);
      createJavaServiceProject(ctx);
      createJavaServiceConfigProject(ctx);

      createJavaServiceBaseProject(ctx);
      createJavaPubsubConnectorProject(ctx);
      createProtocolBufferMessagesProject(ctx);
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
         policy = ReferencePolicy.STATIC)
   public void setTemplateService(ITemplateService ref) {
      this.templateService = ref;
   }

   public void removeTemplateService(ITemplateService ref) {
      setTemplateService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC)
   public void setProjectNamingService(IProjectNamingService ref) {
      this.projectNamingService = ref;
   }

   public void removeProjectNamingService(IProjectNamingService ref) {
      setProjectNamingService(null);
   }

   private void createJellyFishGradleProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = DefaultJellyFishCommandOptions.mergeWith(
            ctx.standardCommandOptions,
            new DefaultParameter<>(PROJECT_NAME_PROPERTY, ctx.projectName),
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
      generateGradleBuildFile(ctx, projectNamingService.getEventsProjectName(delegateOptions, ctx.model));
   }

   private void createCucumberTestsProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = DefaultJellyFishCommandOptions.mergeWith(
            ctx.standardCommandOptions,
            new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, ctx.projectDirectory.getAbsolutePath())
      );
      doRunCommand(CREATE_JAVA_CUCUMBER_TESTS_COMMAND_NAME, delegateOptions);
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

   private void createJavaServiceConfigProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = DefaultJellyFishCommandOptions.mergeWith(
            ctx.standardCommandOptions,
            new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, ctx.projectDirectory.getAbsolutePath())
      );
      doRunCommand(CREATE_JAVA_SERVICE_CONFIG_COMMAND_NAME, delegateOptions);
   }

   private void createJavaServiceBaseProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = DefaultJellyFishCommandOptions.mergeWith(
            ctx.standardCommandOptions,
            new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, ctx.projectDirectory.getAbsolutePath())
      );
      doRunCommand(CREATE_JAVA_SERVICE_BASE_COMMAND_NAME, delegateOptions);
      generateGradleBuildFile(ctx, projectNamingService.getBaseServiceProjectName(delegateOptions, ctx.model));
   }

   private void createProtocolBufferMessagesProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = DefaultJellyFishCommandOptions.mergeWith(
            ctx.standardCommandOptions,
            new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, ctx.projectDirectory.getAbsolutePath())
      );
      doRunCommand(CREATE_PROTOCOLBUFFER_MESSAGES_COMMAND_NAME, delegateOptions);
      generateGradleBuildFile(ctx, projectNamingService.getMessageProjectName(delegateOptions, ctx.model));
   }

   private void createJavaPubsubConnectorProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = DefaultJellyFishCommandOptions.mergeWith(
            ctx.standardCommandOptions,
            new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, ctx.projectDirectory.getAbsolutePath())
      );
      doRunCommand(CREATE_JAVA_PUBSUB_CONNECTOR_COMMAND_NAME, delegateOptions);
      generateGradleBuildFile(ctx, projectNamingService.getConnectorProjectName(delegateOptions, ctx.model));
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
      ctx.modelName = commandOptions.getParameters().getParameter(MODEL_PROPERTY).getStringValue();

      // Find the actual model.
      ctx.model = commandOptions.getSystemDescriptor()
            .findModel(ctx.modelName)
            .orElseThrow(() -> new CommandException(String.format("model %s not found!", ctx.modelName)));

      // Get the directory that will contain the project directory.
      ctx.rootOutputDirectory = Paths.get(
              commandOptions.getParameters().getParameter(OUTPUT_DIRECTORY_PROPERTY).getStringValue())
              .toFile();

      ctx.createDomain =
            CommonParameters
                  .evaluateBooleanParameter(commandOptions.getParameters(), CREATE_SERVICE_DOMAIN_PROPERTY, true);

      // Get the group ID.
      if (commandOptions.getParameters().containsParameter(GROUP_ID_PROPERTY)) {
         ctx.groupId = commandOptions.getParameters().getParameter(GROUP_ID_PROPERTY).getStringValue();
      } else {
         // Compute group ID from model package.
         ctx.groupId = ctx.model.getParent().getName();
      }

      // Get the actual project name.
      if (commandOptions.getParameters().containsParameter(PROJECT_NAME_PROPERTY)) {
         ctx.projectName = commandOptions.getParameters().getParameter(PROJECT_NAME_PROPERTY).getStringValue();
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

   private void generateGradleBuildFile(CommandInvocationContext ctx, IProjectInformation projectInfo) {
      boolean clean = CommonParameters.evaluateBooleanParameter(ctx.originalCommandOptions.getParameters(),
                                                                CommonParameters.CLEAN.getName(),
                                                                false);
      templateService.unpack(CreateJavaServiceProjectCommand.class.getPackage().getName(),
                             ctx.standardCommandOptions.getParameters(),
                             ctx.projectDirectory.toPath().resolve(projectInfo.getDirectoryName()),
                             clean);
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

      boolean createDomain;
   }
}
