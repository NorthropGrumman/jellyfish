package com.ngc.seaside.bootstrap.impl.provider;

import com.google.common.base.Preconditions;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.IBootstrapCommand;
import com.ngc.seaside.bootstrap.IBootstrapCommandProvider;
import com.ngc.seaside.bootstrap.service.template.api.BootstrapTemplateException;
import com.ngc.seaside.bootstrap.service.template.api.IBootstrapTemplateService;
import com.ngc.seaside.command.api.IUsage;

import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author justan.provence@ngc.com
 */
public class BootstrapCommandProviderDelegate implements IBootstrapCommandProvider {

   private final Map<String, IBootstrapCommand> commandMap = new ConcurrentHashMap<>();
   private ILogService logService;
   private IBootstrapTemplateService bootstrapTemplateService;

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
   public void setBootstrapTemplateService(IBootstrapTemplateService ref) {
      this.bootstrapTemplateService = ref;
   }

   /**
    * Remove the bootstrap template service.
    */
   public void removeBootstrapTemplateService(IBootstrapTemplateService ref) {
      setBootstrapTemplateService(null);
   }

   @Override
   public IUsage getUsage() {
      return null;
   }

   @Override
   public void addCommand(IBootstrapCommand command) {
      Preconditions.checkNotNull(command, "Command is null");
      Preconditions.checkNotNull(command.getName(), "Command name is null %s", command);
      Preconditions
               .checkArgument(!command.getName().isEmpty(), "Command Name is empty %s", command);
      commandMap.put(command.getName(), command);
   }

   @Override
   public void removeCommand(IBootstrapCommand command) {
      commandMap.remove(command.getName());
   }

   @Override
   public void run(String[] arguments) {
      Preconditions.checkNotNull(arguments, "Arguments must not be null.");
      Preconditions.checkArgument(arguments.length > 0, "Arguments must not be empty.");

      String commandName = arguments[0];

      IBootstrapCommand command = commandMap.get(commandName);

      boolean templateExists = bootstrapTemplateService.templateExists(commandName);

      if (command == null && !templateExists) {
         logService.error(getClass(),
                          "Unable to find a command or template by the name '%s'", commandName);
         return;
      }

      if (templateExists) {
         try {
            //TODO update with the output directory instead of Paths.
            //This will require the parameter service
            logService.info(getClass(), "Unpacking template");
            bootstrapTemplateService.unpack(commandName, Paths.get("."), false);
         } catch (BootstrapTemplateException e) {
            logService.error(getClass(),
                             e,
                             "Unable to unpack the template for command'%s'. Aborting",
                             command);
            return;
         }
      }

      if (command != null) {
         //TODO this will be updated with parameter service output
         command.run(null);
      }
   }
}
