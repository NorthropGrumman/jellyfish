package com.ngc.seaside.jellyfish.impl.provider;

import com.google.common.base.Preconditions;

import com.ngc.blocs.component.impl.common.DeferredDynamicReference;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateOutput;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.bootstrap.service.template.api.TemplateServiceException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.DefaultJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
// TODO TH: the help command should not be imported into this project.
import com.ngc.seaside.jellyfish.cli.command.help.HelpCommand;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.api.ParsingException;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of the IJellyFishCommandProvider interface.
 */
@Component(service = IJellyFishCommandProvider.class)
public class JellyFishCommandProvider implements IJellyFishCommandProvider {

   private final Map<String, IJellyFishCommand> commandMap = new ConcurrentHashMap<>();
   private ILogService logService;
   private IParameterService parameterService;
   private ISystemDescriptorService sdService;
   private ITemplateService templateService;
   private HelpCommand helpCommand;

   /**
    * Ensure the dynamic references are added only after the activation of this
    * Component.
    */
   private DeferredDynamicReference<IJellyFishCommand> commands = new DeferredDynamicReference<IJellyFishCommand>() {
      @Override
      protected void addPostActivate(IJellyFishCommand command) {
         doAddCommand(command);
      }

      @Override
      protected void removePostActivate(IJellyFishCommand command) {
         doRemoveCommand(command);
      }
   };

   @Activate
   public void activate() {
      commands.markActivated();
   }

   @Deactivate
   public void deactivate() {

   }

   @Override
   public IUsage getUsage() {
      return new DefaultUsage("JellyFish Description",
                              Collections.singletonList(new DefaultParameter("inputDir").setRequired(false)));
   }

   @Override
   public IJellyFishCommand getCommand(String commandName) {
      IJellyFishCommand command = commandMap.get(commandName);
      if (command == null) {
         logService.error(getClass(), "Unable to find command '%s'", commandName);
         return null;
      } else {
         return command;
      }
   }

   @Override
   public void addCommand(IJellyFishCommand command) {
      commands.add(command);
   }

   @Override
   public void removeCommand(IJellyFishCommand command) {
      commands.remove(command);
   }

   @Override
   public void run(String[] arguments) {
      Preconditions.checkNotNull(arguments, "Arguments must not be null.");

      String[] validatedArgs;

      // If no input directory is provided, look in working directory
      if (arguments.length == 0) {
         throw new IllegalArgumentException("No command provided");
      } else {
         if (arguments.length == 1) {
            validatedArgs = new String[]{arguments[0], "-DinputDir=" + System.getProperty("user.dir")};
         } else {
            validatedArgs = arguments;
         }
      }

      String commandName = validatedArgs[0];
      logService.trace(getClass(), "Running command '%s'", commandName);

      IJellyFishCommand command = lookupCommand(validatedArgs[0]);
      if (command == null) {
         logService.error(getClass(), "Unable to find command '%s'", commandName);
         return;
      }

      IParameterCollection userInputParameters = parameterService
               .parseParameters(Arrays.asList(validatedArgs).subList(1, validatedArgs.length));
      IParameterCollection templateParameters = unpackTemplate(command, userInputParameters);

      IJellyFishCommandOptions jellyFishCommandOptions = createCommandOptions(userInputParameters, templateParameters);

      command.run(jellyFishCommandOptions);
   }

   @Override
   public void run(String command, IJellyFishCommandOptions commandOptions) {
      Preconditions.checkNotNull(command, "command may not be null!");
      Preconditions.checkArgument(!command.trim().isEmpty(), "command may not be empty!");
      Preconditions.checkNotNull(commandOptions, "commandOptions may not be null!");
      IJellyFishCommand c = lookupCommand(command);
      Preconditions.checkArgument(c != null, "could not find command named %s!", command);
      c.run(commandOptions);
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
    * Sets log service.
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
    * Remove log service.
    */
   public void removeTemplateService(ITemplateService ref) {
      setTemplateService(null);
   }

   /**
    * Set the IParameterService.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removeParameterService")
   public void setParameterService(IParameterService ref) {
      this.parameterService = ref;
   }

   /**
    * Remove paramater service.
    */
   public void removeParameterService(IParameterService ref) {
      setParameterService(null);
   }

   /**
    * Set the ISystemDescriptorService.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removeSystemDescriptorService")
   public void setSystemDescriptorService(ISystemDescriptorService ref) {
      this.sdService = ref;
   }

   /**
    * Remove the system descriptor service.
    */
   public void removeSystemDescriptorService(ISystemDescriptorService ref) {
      setSystemDescriptorService(null);
   }

   /**
    * Add the command.
    *
    * @param command the command to add.
    */
   protected void doAddCommand(IJellyFishCommand command) {
      Preconditions.checkNotNull(command, "Command is null");
      Preconditions.checkNotNull(command.getName(), "Command name is null %s", command);
      Preconditions.checkArgument(!command.getName().isEmpty(), "Command Name is empty %s", command);
      logService.trace(getClass(), "Adding command '%s'", command.getName());
      if (command instanceof HelpCommand) {
         helpCommand = (HelpCommand) command;
         for (IJellyFishCommand cmd : commandMap.values()) {
            helpCommand.addCommand(cmd);
         }
      }
      commandMap.put(command.getName(), command);
      if (helpCommand != null) {
         helpCommand.addCommand(command);
      }
   }

   /**
    * Remove the command.
    *
    * @param command the command to remove.
    */
   protected void doRemoveCommand(IJellyFishCommand command) {
      Preconditions.checkNotNull(command, "Command is null");
      Preconditions.checkNotNull(command.getName(), "Command name is null %s", command);
      logService.trace(getClass(), "Removing command '%s'", command.getName());
      commandMap.remove(command.getName());
      if (helpCommand != null) {
         helpCommand.removeCommand(command);
         if (command instanceof HelpCommand) {
            helpCommand = null;
         }
      }
   }

   /**
    * This method is required due to an issue when BND tries to resolve the
    * dependencies and the IBootstrapCommand extends an interface that is
    * typed.
    */
   @Reference(unbind = "removeCommandOSGi",
            service = IJellyFishCommand.class,
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC)
   protected void addCommandOSGi(IJellyFishCommand command) {
      addCommand(command);
   }

   protected void removeCommandOSGi(IJellyFishCommand command) {
      removeCommand(command);
   }

   /**
    * Unpack the templateContent if it exists. If not, just return an empty
    * collection of parameters.
    *
    * @param command                the command.
    * @param userSuppliedParameters the parameters the user passed in. These should overwrite any properties that exists
    *                               in the templateContent.properties. meaning, if they pass in these parameters they
    *                               should not be prompted!
    * @return the parameters that were required to be input for usage within the templateContent.
    */
   protected IParameterCollection unpackTemplate(
            IJellyFishCommand command, IParameterCollection userSuppliedParameters) {
      String templatePrefix = getCommandTemplatePrefix(command);
      /**
       * Unpack the templateContent
       */
      if (templateService.templateExists(templatePrefix)) {
         try {
            Path outputPath = Paths.get(".");
            if (userSuppliedParameters.containsParameter("outputDir")) {
               outputPath = Paths.get(userSuppliedParameters.getParameter("outputDir").getStringValue());
            }

            logService.trace(getClass(),
                             "Unpacking templateContent for '%s' to '%s'",
                             userSuppliedParameters, outputPath);
            ITemplateOutput templateOutput =
                     templateService.unpack(templatePrefix, userSuppliedParameters, outputPath, false);

            return convertParameters(templateOutput, outputPath);
         } catch (TemplateServiceException e) {
            logService.error(getClass(), e,
                             "Unable to unpack the templateContent for command'%s'. Aborting",
                             command);
         }
      }
      return null;
   }

   /**
    * Convert the templateContent output to a parameter collection. This
    * includes the templateFinalOutputDir.
    *
    * @param output the templateContent service's output
    * @return the collection of parameters.
    */
   protected IParameterCollection convertParameters(ITemplateOutput output, Path outputPath) {
      IParameterCollection templateParameters = parameterService.parseParameters(output.getProperties());

      DefaultParameterCollection collection = new DefaultParameterCollection();
      DefaultParameter outputDir = new DefaultParameter<>("outputDirectory", outputPath.toString());
      DefaultParameter templateOutputDir = new DefaultParameter<>("templateFinalOutputDirectory",
                                                                  output.getOutputPath().toString());
      collection.addParameter(outputDir);
      collection.addParameter(templateOutputDir);

      for (IParameter templateParameter : templateParameters.getAllParameters()) {
         collection.addParameter(templateParameter);
      }

      return collection;
   }

   /**
    * Return the prefix used in order to look the command's templateContent up
    * within the templates resource directory. Currently this assumes that the
    * naming convention for the command's package includes the same name used
    * for creating the templateContent zip. This is actually done for us using
    * the correct build tools.
    *
    * @param command the command in which to create the prefix
    * @return the String representation of the command's package.
    */
   protected String getCommandTemplatePrefix(IJellyFishCommand command) {
      return command.getClass().getPackage().getName();
   }

   /**
    * This method converts into an {@link IJellyFishCommandOptions} object. The input directory should
    * be the root directory of a system descriptor project. At minimum, the project root should contain
    * the directories of src/main/sd and src/test/gherkin. If these requirements are met and the
    * system descriptor files are valid syntactically, the {@link ISystemDescriptor} model will be loaded
    * into the {@link IJellyFishCommandOptions} object. Otherwise, the application will exit with a
    * thrown exception of {@link IllegalArgumentException} for illegal directory structure
    *
    * @param userInputParameters the parameters that the user input on the command line
    * @param templateParameters  the parameters that were fulfilled by the templateContent.properties file in the
    *                            templateContent
    * @return the JellyFish command options
    */
   private IJellyFishCommandOptions createCommandOptions(
            IParameterCollection userInputParameters, IParameterCollection templateParameters) {

      DefaultJellyFishCommandOptions options = new DefaultJellyFishCommandOptions();

      if (templateParameters == null) {
         options.setParameters(userInputParameters);
      } else {
         DefaultParameterCollection all = new DefaultParameterCollection();
         all.addParameters(userInputParameters.getAllParameters());
         all.addParameters(templateParameters.getAllParameters());
         options.setParameters(all);
      }

      IParameter inputDir = userInputParameters.getParameter("inputDir");
      Path path;
      if (inputDir == null) {
         path = Paths.get(System.getProperty("user.dir"));
      } else {
         path = Paths.get(inputDir.getStringValue());
      }

      try {
         if (!path.toFile().isDirectory()) {
            throw new IllegalArgumentException(
                     String.format("Unable to use %s as it does not exist as a directory", path));
         }
         options.setSystemDescriptor(getSystemDescriptor(path));
      } catch (IllegalArgumentException | ParsingException e) {
         logService.warn(getClass(), e.getMessage());
         options.setSystemDescriptor(null);
      }
      return options;
   }

   /**
    * This method uses the {@link ISystemDescriptorService} to parse the provided project.
    * If errors occur, a {@link ParsingException} is thrown along with a list of issues.
    *
    * @param path system descriptor project path
    * @return the system descriptor
    */
   private ISystemDescriptor getSystemDescriptor(Path path) {
      IParsingResult result = sdService.parseProject(path);
      if (!result.isSuccessful()) {
         result.getIssues().forEach(issue -> logService.error(this.getClass(), issue));
         throw new ParsingException("Error occurred parsing project");
      }
      return result.getSystemDescriptor();
   }

   /**
    * This method looks up the {@link IJellyFishCommand} corresponding with the given
    * string.
    *
    * @param cmd the string representation of a JellyFish command
    * @return the JellyFish command
    */
   private IJellyFishCommand lookupCommand(String cmd) {
      return commandMap.get(cmd);
   }

}
