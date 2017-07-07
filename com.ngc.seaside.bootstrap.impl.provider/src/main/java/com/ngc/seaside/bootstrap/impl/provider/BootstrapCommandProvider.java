package com.ngc.seaside.bootstrap.impl.provider;

import com.google.common.base.Preconditions;

import com.ngc.blocs.component.impl.common.DeferredDynamicReference;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.api.DefaultBootstrapCommandOptions;
import com.ngc.seaside.bootstrap.api.IBootstrapCommand;
import com.ngc.seaside.bootstrap.api.IBootstrapCommandOptions;
import com.ngc.seaside.bootstrap.api.IBootstrapCommandProvider;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateOutput;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.bootstrap.service.template.api.TemplateServiceException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.command.api.IUsage;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of the IBootstrapCommandProvider interface.
 */
@Component(service = IBootstrapCommandProvider.class)
public class BootstrapCommandProvider implements IBootstrapCommandProvider {

   private final Map<String, IBootstrapCommand> commandMap = new ConcurrentHashMap<>();
   private ILogService logService;
   private ITemplateService templateService;
   private IParameterService parameterService;

   /**
    * Ensure the dynamic references are added only after the activation of this Component.
    */
   private DeferredDynamicReference<IBootstrapCommand> commands =
         new DeferredDynamicReference<IBootstrapCommand>() {
            @Override
            protected void addPostActivate(IBootstrapCommand command) {
               doAddCommand(command);
            }

            @Override
            protected void removePostActivate(IBootstrapCommand command) {
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
      return null; //TODO this needs to be the usage for all commands in this provider
   }

   @Override
   public void addCommand(IBootstrapCommand command) {
      commands.add(command);
   }

   @Override
   public void removeCommand(IBootstrapCommand command) {
      commands.remove(command);
   }

   @Override
   public void run(String[] arguments) {
      Preconditions.checkNotNull(arguments, "Arguments must not be null.");
      Preconditions.checkArgument(arguments.length > 0, "Arguments must not be empty.");

      String commandName = arguments[0];
      logService.trace(getClass(), "Running command '%s'", commandName);

      IBootstrapCommand command = commandMap.get(commandName);

      if (command == null) {
         logService.error(getClass(), "Unable to find command '%s'", commandName);
         return;
      }

      //the first parameter is the name of the command, strip it off before converting the command input parameters.
      IParameterCollection parameters =
            parameterService.parseParameters(Arrays.asList(
                  Arrays.copyOfRange(arguments, 1,  arguments.length)
            ));

      IParameterCollection templateParameters = unpackTemplate(command, parameters);
      IBootstrapCommandOptions options = createBootstrapCommandOptions(parameters, templateParameters);

      command.run(options);
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
    * Set the bootstrap templateContent service.
    *
    * @param ref the service
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removeTemplateService")
   public void setTemplateService(ITemplateService ref) {
      this.templateService = ref;
   }

   /**
    * Remove the bootstrap templateContent service.
    */
   public void removeTemplateService(ITemplateService ref) {
      setTemplateService(null);
   }

   /**
    * Set the parameter service.
    *
    * @param ref the service
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removeParameterService")
   public void setParameterService(IParameterService ref) {
      this.parameterService = ref;
   }

   /**
    * Remove the bootstrap templateContent service.
    */
   public void removeParameterService(IParameterService ref) {
      setParameterService(null);
   }

   /**
    * This method is required due to an issue when BND tries to resolve the
    * dependencies and the IBootstrapCommand extends an interface that is typed.
    */
   @Reference(unbind = "removeCommandOSGi",
            service = IBootstrapCommand.class,
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC)
   protected void addCommandOSGi(IBootstrapCommand command) {
      addCommand(command);
   }

   protected void removeCommandOSGi(IBootstrapCommand command) {
      removeCommand(command);
   }

   /**
    * Add the command.
    *
    * @param command the command to add.
    */
   protected void doAddCommand(IBootstrapCommand command) {
      Preconditions.checkNotNull(command, "Command is null");
      Preconditions.checkNotNull(command.getName(), "Command name is null %s", command);
      Preconditions
               .checkArgument(!command.getName().isEmpty(), "Command Name is empty %s", command);
      logService.trace(getClass(), "Adding command '%s'", command.getName());
      commandMap.put(command.getName(), command);
   }

   /**
    * Remove the command.
    *
    * @param command the command to remove.
    */
   protected void doRemoveCommand(IBootstrapCommand command) {
      Preconditions.checkNotNull(command, "Command is null");
      Preconditions.checkNotNull(command.getName(), "Command name is null %s", command);
      logService.trace(getClass(), "Removing command '%s'", command.getName());
      commandMap.remove(command.getName());
   }

   /**
    * Create the bootstrap options given the user supplied parameters and the templateContent supplied parameters. The
    * templateContent may be null due to the fact that you can have a command that doesn't have a templateContent.
    *
    * @param userInputParameters the parameters that the user input on the command line
    * @param templateParameters  the parameters that were fulfilled by the templateContent.properties file in the templateContent
    * @return the bootstrap command options. Never null.
    */
   protected IBootstrapCommandOptions createBootstrapCommandOptions(
            IParameterCollection userInputParameters, IParameterCollection templateParameters) {
      DefaultBootstrapCommandOptions options = new DefaultBootstrapCommandOptions();

      if(templateParameters == null) {
         options.setParameters(userInputParameters);
      } else {
         DefaultParameterCollection all = new DefaultParameterCollection();
         all.addParameters(userInputParameters.getAllParameters());
         all.addParameters(templateParameters.getAllParameters());
         options.setParameters(all);
      }

      return options;
   }

   /**
    * Unpack the templateContent if it exists. If not, just return an empty collection of parameters.
    *
    * @param command                 the command.
    * @param userSuppliedParameters  the parameters the user passed in. These should overwrite any properties that
    *                                exists in the templateContent.properties. meaning, if they pass in these parameters they
    *                                should not be prompted!
    * @return the parameters that were required to be input for usage within the templateContent.
    */
   protected IParameterCollection unpackTemplate(
            IBootstrapCommand command,
            IParameterCollection userSuppliedParameters) {
      String templatePrefix = getCommandTemplatePrefix(command);
      /**
       * Unpack the templateContent
       */
      if (templateService.templateExists(templatePrefix)) {
         try {
            Path outputPath = Paths.get(".");
            if(userSuppliedParameters.containsParameter("outputDir")) {
               outputPath = Paths.get(userSuppliedParameters.getParameter("outputDir").getValue());
            }

            logService.trace(getClass(), "Unpacking templateContent for '%s' to '%s'", userSuppliedParameters, outputPath);
            ITemplateOutput templateOutput =
                     templateService.unpack(templatePrefix, userSuppliedParameters, outputPath, false);

            return convertParameters(templateOutput, outputPath);
         } catch (TemplateServiceException e) {
            logService.error(getClass(),
                             e,
                             "Unable to unpack the templateContent for command'%s'. Aborting",
                             command);
         }
      }
      return null;
   }

   /**
    * Return the prefix used in order to look the command's templateContent up within the templates resource directory.
    * Currently this assumes that the naming convention for the command's package includes the same name used for
    * creating the templateContent zip. This is actually done for us using the correct build tools.
    *
    * @param command the command in which to create the prefix
    * @return the String representation of the command's package.
    */
   protected String getCommandTemplatePrefix(IBootstrapCommand command) {
      return command.getClass().getPackage().getName();
   }

   /**
    * Convert the templateContent output to a parameter collection. This includes the templateFinalOutputDir.
    *
    * @param output the templateContent service's output
    * @return the collection of parameters.
    */
   protected IParameterCollection convertParameters(ITemplateOutput output, Path outputPath) {
      IParameterCollection templateParameters = parameterService.parseParameters(output.getProperties());

      DefaultParameterCollection collection = new DefaultParameterCollection();
      DefaultParameter outputDir = new DefaultParameter("outputDirectory")
               .setValue(outputPath.toString());
      DefaultParameter templateOutputDir = new DefaultParameter("templateFinalOutputDirectory")
               .setValue(output.getOutputPath().toString());
      collection.addParameter(outputDir);
      collection.addParameter(templateOutputDir);

      for (IParameter templateParameter : templateParameters.getAllParameters()) {
         collection.addParameter(templateParameter);
      }

      return collection;
   }

}
