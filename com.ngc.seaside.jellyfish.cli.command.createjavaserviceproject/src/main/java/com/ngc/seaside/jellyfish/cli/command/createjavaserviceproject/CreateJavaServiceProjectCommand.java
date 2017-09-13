package com.ngc.seaside.jellyfish.cli.command.createjavaserviceproject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(service = IJellyFishCommand.class)
public class CreateJavaServiceProjectCommand implements IJellyFishCommand {

   private IUsage USAGE = null;

   static final String GROUP_ID_PROPERTY = "groupId";
   static final String ARTIFACT_ID_PROPERTY = "artifactId";
   static final String PACKAGE_PROPERTY = "package";
   static final String PROJECT_NAME = "projectName";
   static final String MODEL_PROPERTY = "model";
   static final String OUTPUT_DIRECTORY_PROPERTY = "outputDirectory";
   static final String GENERATED_PROJECT_DIRECTORY_NAME_PROPERTY = "generatedProjectDirectoryName";
   static final String CREATE_SERVICE_DOMAIN_PROPERTY = "createServiceDomain";

   static final String DEFAULT_OUTPUT_DIRECTORY = ".";
   static final String DEFAULT_GENERATED_PROJECT_DIRECTORY_NAME = "generated-projects";

   static final String CREATE_JELLYFISH_GRADLE_PROJECT_COMMAND_NAME = "create-jellyfish-gradle-project";
   static final String CREATE_DOMAIN_COMMAND_NAME = "create-domain";
   static final String CREATE_JAVA_EVENTS_COMMAND_NAME = "create-java-events";
   static final String CREATE_JAVA_CUCUMBER_TESTS_COMMAND_NAME = "create-java-cucumber-tests";
   static final String CREATE_JAVA_DISTRIBUTION_COMMAND_NAME = "create-java-distribution";
   static final String CREATE_JAVA_SERVICE_COMMAND_NAME = "create-java-service";
   static final String CREATE_JAVA_SERVICE_BASE_COMMAND_NAME = "create-java-service-base";
   static final String CREATE_JAVA_PUBSUB_CONNECTOR_COMMAND_NAME = "create-java-pubsub-connector";
   static final String CREATE_JAVA_SERVICE_CONFIG_COMMAND_NAME = "create-java-service-config";
   private static final String[] SUBCOMMANDS = { CREATE_JELLYFISH_GRADLE_PROJECT_COMMAND_NAME,
            CREATE_DOMAIN_COMMAND_NAME, CREATE_JAVA_EVENTS_COMMAND_NAME, CREATE_JAVA_CUCUMBER_TESTS_COMMAND_NAME, CREATE_JAVA_DISTRIBUTION_COMMAND_NAME,
            CREATE_JAVA_SERVICE_BASE_COMMAND_NAME, CREATE_JAVA_PUBSUB_CONNECTOR_COMMAND_NAME,
            CREATE_JAVA_SERVICE_CONFIG_COMMAND_NAME };

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
               
               usageParameters.put(OUTPUT_DIRECTORY_PROPERTY, new DefaultParameter<String>(OUTPUT_DIRECTORY_PROPERTY)
                        .setDescription("Base directory in which to output the project").setRequired(true));
               usageParameters.put(MODEL_PROPERTY, new DefaultParameter<String>(MODEL_PROPERTY)
                        .setDescription("The fully qualified path to the system descriptor model").setRequired(true));
               usageParameters.put(PROJECT_NAME, new DefaultParameter<String>(PROJECT_NAME)
                        .setDescription("The name of the project.").setRequired(false));
               usageParameters.put(GROUP_ID_PROPERTY, new DefaultParameter<String>(GROUP_ID_PROPERTY)
                        .setDescription("The project's group ID").setRequired(false));
               usageParameters.put(ARTIFACT_ID_PROPERTY, new DefaultParameter<String>(ARTIFACT_ID_PROPERTY)
                        .setDescription("The project's version").setRequired(false));
               usageParameters.put(PACKAGE_PROPERTY, new DefaultParameter<String>(PACKAGE_PROPERTY)
                        .setDescription("The project's default package").setRequired(false));
               usageParameters.put(GENERATED_PROJECT_DIRECTORY_NAME_PROPERTY,
                  new DefaultParameter<String>(GENERATED_PROJECT_DIRECTORY_NAME_PROPERTY)
                           .setDescription("The project's folder for generated code").setRequired(false));
               usageParameters.put(CREATE_SERVICE_DOMAIN_PROPERTY,
                  new DefaultParameter<String>(CREATE_SERVICE_DOMAIN_PROPERTY)
                           .setDescription("Whether or not to create the service's domain model").setRequired(false));
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
            new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, ctx.generatedDirectory.getAbsolutePath())
      );
      doRunCommand(CREATE_JAVA_EVENTS_COMMAND_NAME, delegateOptions);
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
            new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, ctx.generatedDirectory.getAbsolutePath())
      );
      doRunCommand(CREATE_JAVA_SERVICE_BASE_COMMAND_NAME, delegateOptions);
   }


   private void createJavaPubsubConnectorProject(CommandInvocationContext ctx) {
      IJellyFishCommandOptions delegateOptions = DefaultJellyFishCommandOptions.mergeWith(
            ctx.standardCommandOptions,
            new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, ctx.generatedDirectory.getAbsolutePath())
      );
      doRunCommand(CREATE_JAVA_PUBSUB_CONNECTOR_COMMAND_NAME, delegateOptions);
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
               promptUserService.prompt(OUTPUT_DIRECTORY_PROPERTY, DEFAULT_OUTPUT_DIRECTORY, null))
               .toFile();
      }
      
      if (commandOptions.getParameters().containsParameter(CREATE_SERVICE_DOMAIN_PROPERTY)) {
         String createDomain = commandOptions.getParameters().getParameter(CREATE_SERVICE_DOMAIN_PROPERTY).getStringValue();
         switch (createDomain.toLowerCase()) {
         case "true":
            ctx.createDomain = true;
            break;
         case "false":
            ctx.createDomain = false;
            break;
         default:
            throw new CommandException(
               "Invalid value for " + CREATE_SERVICE_DOMAIN_PROPERTY + ": " + createDomain + ". Expected either true or false.");
         }
      } else {
         ctx.createDomain = true;
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

      final String generatedDirectory;
      if (commandOptions.getParameters().containsParameter(GENERATED_PROJECT_DIRECTORY_NAME_PROPERTY)) {
         generatedDirectory = commandOptions.getParameters().getParameter(GENERATED_PROJECT_DIRECTORY_NAME_PROPERTY).getStringValue();
      } else {
         generatedDirectory = DEFAULT_GENERATED_PROJECT_DIRECTORY_NAME;
      }
      ctx.generatedDirectory = ctx.projectDirectory.toPath().resolve(generatedDirectory).toFile();
      
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
      File generatedDirectory;

      String projectName;
      String groupId;
      String modelName;

      IModel model;
      
      boolean createDomain;
   }
}
