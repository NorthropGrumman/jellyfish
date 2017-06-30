package com.ngc.blocs.guice.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.log.impl.common.LogService;

/**
 * Wrap the BLoCS LogService implementation using a Guice Module.
 */
public class LogServiceModule extends AbstractModule {

   @Override
   protected void configure() {

      bindListener(new AbstractMatcher<TypeLiteral>() {
         @Override
         public boolean matches(TypeLiteral literal) {
            return literal.getRawType().equals(LogService.class);
         }
      }, new TypeListener() {
         @Override
         public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
            //this Component requires that the activate method be called.
            encounter.register(new InjectionListener<I>() {
               @Override
               public void afterInjection(I injectee) {
                  ((LogService) injectee).activate();
               }
            });
         }
      });

      bind(ILogService.class).to(LogService.class).in(Scopes.SINGLETON);
   }
}
