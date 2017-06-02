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
      Injector injector = XTextSystemDescriptorServiceBuilder.forApplication().buildInjector();
      assertNotNull("injector is null!",
                    injector);
      ISystemDescriptorService service = injector.getInstance(ISystemDescriptorService.class);
      assertNotNull("service is null!",
                    service);
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
}
