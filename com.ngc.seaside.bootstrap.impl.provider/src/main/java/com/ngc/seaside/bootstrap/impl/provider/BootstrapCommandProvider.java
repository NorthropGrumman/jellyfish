package com.ngc.seaside.bootstrap.impl.provider;

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

import java.util.Set;

/**
 * @author justan.provence@ngc.com
 */
@Component(service = IBootstrapCommandProvider.class)
public class BootstrapCommandProvider implements IBootstrapCommandProvider {

   private final BootstrapCommandProviderDelegate delegate = new BootstrapCommandProviderDelegate();

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
      delegate.setLogService(ref);
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   @Override
   public IUsage getUsage() {
      return delegate.getUsage();
   }

   @Override
   public void addCommand(IBootstrapCommand command) {
      delegate.addCommand(command);
   }

   @Override
   public void removeCommand(IBootstrapCommand command) {
      delegate.removeCommand(command);
   }

   @Override
   public void run(String[] arguments) {
      delegate.run(arguments);
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
