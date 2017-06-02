package com.ngc.seaside.systemdescriptor.service.impl.xtext;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;

public class XTextSystemDescriptorServiceBuilderIT {

   @Test
   public void testDoesBuildServiceForSimpleApplication() throws Throwable {
      ISystemDescriptorService service =
            XTextSystemDescriptorServiceBuilder.forApplication().build();
      assertNotNull("service is null!",
                    service);
   }

   @Test
   public void testDoesBuildInjectorForSimpleApplication() throws Throwable {
      Injector injector = XTextSystemDescriptorServiceBuilder.forApplication()
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
      Injector injector = XTextSystemDescriptorServiceBuilder.forApplication()
            .includeServiceLoadedModules(true)
            .buildInjector();
      assertNotNull("did not add load modules via service loader!",
                    injector.getInstance(TestableModule.ITestableComponent.class));
   }

   @Test
   public void testDoesIntegrateWithApplication() throws Throwable {
      Collection<Module> appModules = new ArrayList<>();
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
