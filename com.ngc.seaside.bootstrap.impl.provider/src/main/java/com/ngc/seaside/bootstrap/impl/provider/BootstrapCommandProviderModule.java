package com.ngc.seaside.bootstrap.impl.provider;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.IBootstrapCommand;
import com.ngc.seaside.bootstrap.IBootstrapCommandProvider;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.IUsage;

import java.util.Set;

/**
 * Guice wrapper around the {@link BootstrapCommandProvider} implementation.
 *
 * TODO abstract the isReady and Update technique to a reusable software pattern
 */
public class BootstrapCommandProviderModule extends AbstractModule implements IBootstrapCommandProvider {

   private final BootstrapCommandProvider delegate = new BootstrapCommandProvider();

   private boolean logServiceSet = false;
   private boolean templateServiceSet = false;
   private Set<IBootstrapCommand> temporaryCommands;

   @Override
   protected void configure() {
      bindListener(new AbstractMatcher<TypeLiteral>() {
         @Override
         public boolean matches(TypeLiteral literal) {
            return literal.getRawType().equals(BootstrapCommandProviderModule.class);
         }
      }, new TypeListener() {
         @Override
         public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
            //this Component requires that the activate method be called.
            encounter.register((InjectionListener<I>) i -> delegate.activate());
         }
      });

      bind(IBootstrapCommandProvider.class).toInstance(this);
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
      if(isReady()) {
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
   public void setBootstrapTemplateService(ITemplateService ref) {
      delegate.setTemplateService(ref);
      templateServiceSet = true;
      update();
   }

   /**
    * Update the delegate with the commands.
    */
   private void update() {
      if(isReady() && temporaryCommands != null) {
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
      return templateServiceSet && logServiceSet;
   }
}
