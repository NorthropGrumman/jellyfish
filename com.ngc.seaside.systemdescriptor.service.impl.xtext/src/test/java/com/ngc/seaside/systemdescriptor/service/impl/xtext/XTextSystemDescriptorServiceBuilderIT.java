package com.ngc.seaside.systemdescriptor.service.impl.xtext;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class XTextSystemDescriptorServiceBuilderIT {

   @Mock
   private ILogService logService;

   @Test
   public void testDoesBuildServiceForSimpleApplication() throws Throwable {
      ISystemDescriptorService service =
            XTextSystemDescriptorServiceBuilder.forApplication(logService).build();
      assertNotNull("service is null!",
                    service);
   }

   @Test
   public void testDoesBuildInjectorForSimpleApplication() throws Throwable {
      Injector injector = XTextSystemDescriptorServiceBuilder.forApplication(logService)
            .addModule(new TestableModule())
            .buildInjector();
      assertNotNull("injector is null!",
                    injector);
      ISystemDescriptorService service = injector.getInstance(ISystemDescriptorService.class);
      assertNotNull("service is null!",
                    service);
      assertNotNull("did not add extra modules!",
                    injector.getInstance(TestableModule.ITestableComponent.class));
   }

   @Test
   public void testDoesUseServiceLoadersForSimpleApplication() throws Throwable {
      Injector injector = XTextSystemDescriptorServiceBuilder.forApplication(logService)
            .includeServiceLoadedModules(true)
            .buildInjector();
      assertNotNull("did not add load modules via service loader!",
                    injector.getInstance(TestableModule.ITestableComponent.class));
   }

   @Test
   public void testDoesIntegrateWithApplication() throws Throwable {
      Collection<Module> appModules = new ArrayList<>();
      appModules.add(new AbstractModule() {
         @Override
         protected void configure() {
            bind(ILogService.class).toInstance(logService);
         }
      });
      Injector injector = XTextSystemDescriptorServiceBuilder.forIntegration(appModules::add)
            .build(() -> Guice.createInjector(appModules));

      assertNotNull("injector is null!",
                    injector);

      ISystemDescriptorService service = injector.getInstance(ISystemDescriptorService.class);
      assertNotNull("service is null!",
                    service);
   }

   @Test
   public void testDoesUseServiceLoadersWhenIntegratingWithApplication() throws Throwable {
      Collection<Module> appModules = new ArrayList<>();
      appModules.add(new AbstractModule() {
         @Override
         protected void configure() {
            bind(ILogService.class).toInstance(logService);
         }
      });
      Injector injector = XTextSystemDescriptorServiceBuilder.forIntegration(appModules::add)
            .includeServiceLoadedModules(true)
            .build(() -> Guice.createInjector(appModules));

      assertNotNull("injector is null!",
                    injector);

      ISystemDescriptorService service = injector.getInstance(ISystemDescriptorService.class);
      assertNotNull("service is null!",
                    service);
      assertNotNull("did not add load modules via service loader!",
                    injector.getInstance(TestableModule.ITestableComponent.class));
   }
}
