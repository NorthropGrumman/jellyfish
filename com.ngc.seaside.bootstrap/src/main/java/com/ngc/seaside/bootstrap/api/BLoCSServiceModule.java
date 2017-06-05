package com.ngc.seaside.bootstrap.api;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.log.impl.common.LogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.blocs.service.resource.impl.common.ResourceService;

/**
 * Register the BLoCS based services within a Guice module.
 * This class should use the Guice binding to match the interface to the
 * default implementation and call the activate method on the implementation.
 *
 * @author justan.provence@ngc.com
 */
public class BLoCSServiceModule extends AbstractModule {

   private LogService logService;
   private ResourceService resourceService;

   @Override
   protected void configure() {
      //The listener let's us know when the service has been bound
      //and then we call activate on the service.
      //TODO figure out how to deactivate the component and make this generic for all OSGi services
      bindListener(new AbstractMatcher<TypeLiteral>() {
         @Override
         public boolean matches(TypeLiteral literal) {
            return literal.getRawType().equals(LogService.class) ||
                     literal.getRawType().equals(ResourceService.class);
         }
      }, new TypeListener() {
         @Override
         public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
            encounter.register((InjectionListener<I>) i -> {
               if(i instanceof LogService) {
                  logService = (LogService) i;
                  logService.activate();
                  if(resourceService != null) {
                     resourceService.setLogService(logService);
                     resourceService.activate();
                  }
               } else if(i instanceof ResourceService) {
                  resourceService = (ResourceService) i;
                  if(logService != null) {
                     resourceService.setLogService(logService);
                     resourceService.activate();
                  }
               }
            });
         }
      });

      //bind the interface to the implementation
      bind(ILogService.class).to(LogService.class);
      bind(IResourceService.class).to(ResourceService.class);
   }



}
