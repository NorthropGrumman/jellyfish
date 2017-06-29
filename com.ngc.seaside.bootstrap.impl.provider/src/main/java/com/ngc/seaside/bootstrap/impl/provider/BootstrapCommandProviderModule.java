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
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.IUsage;

import java.util.Set;

/**
 * Guice wrapper around the {@link BootstrapCommandProvider} implementation.
 */
public class BootstrapCommandProviderModule extends AbstractModule implements IBootstrapCommandProvider {

   private final BootstrapCommandProvider delegate = new BootstrapCommandProvider();

   @Inject
   public BootstrapCommandProviderModule(
            ILogService logService,
            ITemplateService templateService,
            IParameterService parameterService,
            Set<IBootstrapCommand> commands) {
       delegate.setLogService(logService);
       delegate.setTemplateService(templateService);
       delegate.setParameterService(parameterService);
       commands.forEach(delegate::addCommand);
   }

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

//   @Inject
//   public void addCommands(Set<IBootstrapCommand> commands) {
//      commands.forEach(this::addCommand);
//   }
//
//   @Inject
//   public void setLogService(ILogService ref) {
//      delegate.setLogService(ref);
//   }
//
//   @Inject
//   public void setBootstrapTemplateService(ITemplateService ref) {
//      delegate.setTemplateService(ref);
//   }
//
//   @Inject
//   public void setParameterService(IParameterService ref) {
//      delegate.setParameterService(ref);
//   }

}
