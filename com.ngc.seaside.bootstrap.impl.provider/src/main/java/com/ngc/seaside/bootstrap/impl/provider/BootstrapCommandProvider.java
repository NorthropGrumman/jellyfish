package com.ngc.seaside.bootstrap.impl.provider;

import com.google.common.base.Preconditions;

import com.ngc.blocs.component.impl.common.DeferredDynamicReference;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.DefaultBootstrapCommandOptions;
import com.ngc.seaside.bootstrap.IBootstrapCommand;
import com.ngc.seaside.bootstrap.IBootstrapCommandProvider;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateOutput;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.bootstrap.service.template.api.TemplateServiceException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
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
               addCommand(command);
            }

            @Override
            protected void removePostActivate(IBootstrapCommand command) {
               removeCommand(command);
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
      Preconditions.checkNotNull(command, "Command is null");
      Preconditions.checkNotNull(command.getName(), "Command name is null %s", command);
      Preconditions
            .checkArgument(!command.getName().isEmpty(), "Command Name is empty %s", command);

      logService.trace(getClass(), "Adding command '%s'", command.getName());
      commandMap.put(command.getName(), command);
   }

   @Override
   public void removeCommand(IBootstrapCommand command) {
      Preconditions.checkNotNull(command, "Command is null");
      Preconditions.checkNotNull(command.getName(), "Command name is null %s", command);
      logService.trace(getClass(), "Removing command '%s'", command.getName());
      commandMap.remove(command.getName());
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

      DefaultBootstrapCommandOptions options = new DefaultBootstrapCommandOptions();

      IParameterCollection parameters =
            parameterService.parseParameters(Arrays.asList(
                  Arrays.copyOfRange(arguments, 1,  arguments.length)
            ));

      String templatePrefix = command.getClass().getPackage().getName();

      /**
       * Unpack the template
       */
      if (templateService.templateExists(templatePrefix)) {
         try {
            Path outputPath = Paths.get(".");
            if(parameters.containsParameter("outputDirectory")) {
               outputPath = Paths.get(parameters.getParameter("outputDirectory").getValue());
            }

            logService.trace(getClass(),
                             "Unpacking template for '%s' to '%s'", commandName, outputPath);
            ITemplateOutput templateOutput =
                  templateService.unpack(commandName, outputPath, false);


            options.setParameters(convertParameters(templateOutput));
         } catch (TemplateServiceException e) {
            logService.error(getClass(),
                             e,
                             "Unable to unpack the template for command'%s'. Aborting",
                             command);
            return;
         }
      }




      if (command != null) {
         //TODO this will be updated with parameter service output as well
         command.run(options);
      }
   }

   /**
    * TODO this should be in the parameter service.
    *
    * @param output the template service's output
    * @return the collection of parameters.
    */
   private IParameterCollection convertParameters(ITemplateOutput output) {
      DefaultParameterCollection collection = new DefaultParameterCollection();
//      DefaultParameter outputDir = new DefaultParameter("outputDirectory", true);
//      outputDir.setValue(output.getOutputPath().toString());
//      collection.addParameter(outputDir);
//
//      for (Map.Entry<String, String> entry : output.getProperties().entrySet()) {
//         DefaultParameter parameter = new DefaultParameter(entry.getKey(), true);
//         parameter.setValue(entry.getValue());
//         collection.addParameter(parameter);
//      }

      return collection;
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
    * Set the bootstrap template service.
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
    * Remove the bootstrap template service.
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
    * Remove the bootstrap template service.
    */
   public void removeParameterService(IParameterService ref) {
      setParameterService(null);
   }
}
