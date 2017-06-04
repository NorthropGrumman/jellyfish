package com.ngc.seaside.bootstrap.impl.provider;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import com.ngc.blocs.component.impl.common.DeferredDynamicReference;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.IBootstrapCommand;
import com.ngc.seaside.bootstrap.IBootstrapCommandProvider;
import com.ngc.seaside.command.api.IUsage;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author justan.provence@ngc.com
 */
@Component(service = IBootstrapCommandProvider.class)
public class BootstrapCommandProvider implements IBootstrapCommandProvider {
   private final Map<String, IBootstrapCommand> commandMap = new ConcurrentHashMap<>();

   private ILogService logService;

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
      logService.trace(getClass(), "Activated");
      commands.markActivated();
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
   @Inject
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   @Override
   public IUsage getUsage() {
      return null;
   }

   @Override
   public void addCommand(IBootstrapCommand command) {
      Preconditions.checkNotNull(command, "Command is nullS");
      Preconditions.checkNotNull(command.getName(), "Command name is null %s", command);
      Preconditions.checkArgument(!command.getName().isEmpty(), "Command Name is empty %s", command);
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
      if(command == null) {
         logService.error(getClass(), "Unable to find a command by the name of %s", commandName);
         return;
      }

      command.run(null);
   }

   @Inject
   public void addCommands(Set<IBootstrapCommand> commands) {
      commands.forEach(this::addCommand);
   }

   @Override
   public String toString() {
      return getClass().getName();
   }
}
