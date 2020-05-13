/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.systemdescriptor.service.impl.xtext.testutil;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.SystemDescriptorRuntimeModule;
import com.ngc.seaside.systemdescriptor.SystemDescriptorStandaloneSetup;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.module.CucumberGherkinServiceGuiceModule;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;
import com.ngc.seaside.systemdescriptor.service.repository.api.IRepositoryService;

import org.eclipse.xtext.common.TerminalsStandaloneSetup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
   public static synchronized Injector getSharedInstance() {
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
         modules.add(new CucumberGherkinServiceGuiceModule());
         injector = Guice.createInjector(modules);
         new SystemDescriptorStandaloneSetup().register(injector);
      }
      return injector;
   }
}
