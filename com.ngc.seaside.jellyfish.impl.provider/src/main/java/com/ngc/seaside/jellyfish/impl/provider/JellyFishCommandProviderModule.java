package com.ngc.seaside.jellyfish.impl.provider;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.log.impl.common.LogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.blocs.service.resource.impl.common.ResourceService;
import com.ngc.seaside.bootstrap.api.IBootstrapCommandProvider;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;

import java.util.Set;

/**
 * Guice wrapper around the {@link JellyFishCommandProvider} implementation.
 *
 * TODO abstract the isReady and Update technique to a reusable software pattern
 */
public class JellyFishCommandProviderModule extends AbstractModule implements IJellyFishCommandProvider {

   private final JellyFishCommandProvider delegate = new JellyFishCommandProvider();

   private boolean logServiceSet = false;
   private boolean bootstrapCommandProviderSet = false;
   private boolean parameterCollectionSet = false;
   private boolean systemDescriptorServiceSet = false;
   private Set<IJellyFishCommand> temporaryCommands;

   @Override
   protected void configure() {
      bindListener(new AbstractMatcher<TypeLiteral<?>>() {
         @Override
         public boolean matches(TypeLiteral<?> literal) {
            return literal.getRawType().equals(JellyFishCommandProviderModule.class);
         }
      }, new TypeListener() {
         @Override
         public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
            // this Component requires that the activate method be called.
            encounter.register((InjectionListener<I>) i -> delegate.activate());
         }
      });
   }

   public void run(String[] args) {
      delegate.run(args);
   }

   @Override
   public IUsage getUsage() {
      return delegate.getUsage();
   }

   @Override
   public IJellyFishCommand getCommand(String commandName) {
      return delegate.getCommand(commandName);
   }

   @Override
   public void addCommand(IJellyFishCommand command) {
      delegate.addCommand(command);

   }

   @Override
   public void removeCommand(IJellyFishCommand command) {
      delegate.removeCommand(command);

   }

   @Inject
   public void addCommands(Set<IJellyFishCommand> commands) {
      if (isReady()) {
         commands.forEach(this::addCommand);
      } else {
         temporaryCommands = commands;
      }
   }

   @Inject
   public void setLogService(ILogService ref) {
      delegate.setLogService(ref);
      logServiceSet = true;
      update();
   }

   @Inject
   public void setIBootstrapCommandProvider(IBootstrapCommandProvider ref) {
      delegate.setIBootstrapCommandProvider(ref);
      bootstrapCommandProviderSet = true;
      update();
   }

   @Inject
   public void setIParameterService(IParameterService ref) {
      delegate.setIParameterService(ref);
      parameterCollectionSet = true;
      update();
   }

   @Inject
   public void setISystemDescriptorService(ISystemDescriptorService ref) {
      delegate.setISystemDescriptorService(ref);
      systemDescriptorServiceSet = true;
      update();
   }

   /**
    * Update the delegate with the commands.
    */
   private void update() {
      if (isReady() && temporaryCommands != null) {
         addCommands(temporaryCommands);
         temporaryCommands = null;
      }
   }

   /**
    * Determine if the required services exists and the commands can be added.
    *
    * @return true if the services exists.
    */
   private boolean isReady() {
      return bootstrapCommandProviderSet && logServiceSet && parameterCollectionSet && systemDescriptorServiceSet;
   }

}
