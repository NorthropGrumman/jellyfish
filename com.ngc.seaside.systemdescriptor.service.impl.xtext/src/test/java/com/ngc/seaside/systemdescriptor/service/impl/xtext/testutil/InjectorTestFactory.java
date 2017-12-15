package com.ngc.seaside.systemdescriptor.service.impl.xtext.testutil;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.repository.api.IRepositoryService;
import com.ngc.seaside.systemdescriptor.SystemDescriptorRuntimeModule;
import com.ngc.seaside.systemdescriptor.SystemDescriptorStandaloneSetup;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

import org.eclipse.xtext.common.TerminalsStandaloneSetup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.Mockito.*;

/**
 * Maintains singleton access to the {@code Injector} that manages the XText related resources and the system descriptor
 * service.  We have to do this because the XText stuff cannot be instantiated more than once in a VM.
 */
public class InjectorTestFactory {

   private static Injector injector;

   private InjectorTestFactory() {
   }

   /**
    * Gets the shared {@code Injector} that should be used by <b>all</b> tests.
    */
   public synchronized static Injector getSharedInstance() {
      if (injector == null) {
         TerminalsStandaloneSetup.doSetup();

         ILogService logService = mock(ILogService.class);
         IRepositoryService repositoryService = mock(IRepositoryService.class);
         when(repositoryService.getArtifactDependencies(any(), anyBoolean())).thenReturn(Collections.emptySet());
         Collection<Module> modules = new ArrayList<>();
         modules.add(new AbstractModule() {
            @Override
            protected void configure() {
               bind(ILogService.class).toInstance(logService);
               bind(IRepositoryService.class).toInstance(repositoryService);
            }
         });
         modules.add(new SystemDescriptorRuntimeModule());
         // Disable scenario validation.
         modules.add(new XTextSystemDescriptorServiceModule() {
            @Override
            protected void bindDefaultValidators() {
               // Do nothing.
            }
         });
         injector = Guice.createInjector(modules);
         new SystemDescriptorStandaloneSetup().register(injector);
      }
      return injector;
   }
}
