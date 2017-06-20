package com.ngc.seaside.jellyfish.impl.provider;

import com.google.common.base.Preconditions;
import com.ngc.blocs.component.impl.common.DeferredDynamicReference;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.IBootstrapCommandProvider;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of the IJellyFishCommandProvider interface.
 */
@Component(service = IJellyFishCommandProvider.class)
public class JellyFishCommandProvider implements IJellyFishCommandProvider {
   private final Map<String, IJellyFishCommand> commandMap = new ConcurrentHashMap<>();
   private ILogService logService;
   private IBootstrapCommandProvider bootstrapCommandProvider;
   
   /**
    * Ensure the dynamic references are added only after the activation of this Component.
    */
   private DeferredDynamicReference<IJellyFishCommand> commands =
            new DeferredDynamicReference<IJellyFishCommand>() {
               @Override
               protected void addPostActivate(IJellyFishCommand command) {
                  addCommand(command);
               }

               @Override
               protected void removePostActivate(IJellyFishCommand command) {
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
   public void addCommand(IJellyFishCommand command) {
      Preconditions.checkNotNull(command, "Command is null");
      Preconditions.checkNotNull(command.getName(), "Command name is null %s", command);
      Preconditions
               .checkArgument(!command.getName().isEmpty(), "Command Name is empty %s", command);

      logService.trace(getClass(), "Adding command '%s'", command.getName());
      commandMap.put(command.getName(), command);
   }

   @Override
   public void removeCommand(IJellyFishCommand command) {
      Preconditions.checkNotNull(command, "Command is null");
      Preconditions.checkNotNull(command.getName(), "Command name is null %s", command);
      logService.trace(getClass(), "Removing command '%s'", command.getName());
      commandMap.remove(command.getName()); 
   }

   @Override
   public void run(String[] arguments) {
      // TODO Call IBootstrapCommandProvider from here???
      
      bootstrapCommandProvider.run(arguments);
      
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
    * Set the IBootstrapCommandProvider.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.STATIC,
            unbind = "removeIBootstrapCommandProvider")
   public void setIBootstrapCommandProvider(IBootstrapCommandProvider ref) {
      this.bootstrapCommandProvider = ref;
   }

   /**
    * Remove log service.
    */
   public void removeIBootstrapCommandProvider(IBootstrapCommandProvider ref) {
      setIBootstrapCommandProvider(null);
   }


}
