package com.ngc.seaside.systemdescriptor.service.impl.xtext;

import com.google.common.base.Preconditions;
import com.google.inject.Injector;
import com.google.inject.Module;

import com.ngc.seaside.systemdescriptor.SystemDescriptorRuntimeModule;
import com.ngc.seaside.systemdescriptor.SystemDescriptorStandaloneSetup;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;

import org.eclipse.xtext.common.TerminalsStandaloneSetup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class XTextSystemDescriptorServiceBuilder {

   private XTextSystemDescriptorServiceBuilder() {
   }

   public static ForApplicationBuilder forApplication() {
      return new ForApplicationBuilder();
   }

   public static IntegrationBuilder forIntegration(Consumer<Module> moduleConsumer) {
      Preconditions.checkNotNull(moduleConsumer, "moduleConsumer may not be null!");
      return new IntegrationBuilder(moduleConsumer);
   }

   public static class IntegrationBuilder {

      private final Consumer<Module> moduleConsumer;
      private boolean includeServiceLoadedModules = false;

      private IntegrationBuilder(Consumer<Module> moduleConsumer) {
         this.moduleConsumer = moduleConsumer;
      }

      public IntegrationBuilder includeServiceLoadedModules(boolean includeServiceLoadedModules) {
         this.includeServiceLoadedModules = includeServiceLoadedModules;
         return this;
      }

      public Injector build(Supplier<Injector> injectorSupplier) {
         Preconditions.checkNotNull(injectorSupplier, "injectorSupplier may not be null!");

         getDefaultModules().forEach(moduleConsumer);
         if (includeServiceLoadedModules) {
            loadModules().forEach(moduleConsumer);
         }

         TerminalsStandaloneSetup.doSetup();
         Injector injector = injectorSupplier.get();
         new SystemDescriptorStandaloneSetup().register(injector);
         return injector;
      }
   }

   public static class ForApplicationBuilder {

      private final Collection<Module> modules = new ArrayList<>();
      private boolean includeServiceLoadedModules = false;

      private ForApplicationBuilder() {
         modules.addAll(getDefaultModules());
      }

      public ForApplicationBuilder addModule(Module module, Module... more) {
         modules.add(Preconditions.checkNotNull(module, "module may not be null!"));
         if (more != null) {
            Collections.addAll(modules, more);
         }
         return this;
      }

      public ForApplicationBuilder addModules(Collection<Module> modules) {
         Preconditions.checkNotNull(modules, "modules may not be null!");
         this.modules.addAll(modules);
         return this;
      }

      public ForApplicationBuilder includeServiceLoadedModules(boolean includeServiceLoadedModules) {
         this.includeServiceLoadedModules = includeServiceLoadedModules;
         return this;
      }

      public ISystemDescriptorService build() {
         Injector injector = buildInjector();
         return injector.getInstance(ISystemDescriptorService.class);
      }

      public Injector buildInjector() {
         if (includeServiceLoadedModules) {
            modules.addAll(loadModules());
         }
         return new SystemDescriptorStandaloneSetup().createInjectorAndDoEMFRegistration(modules, false);
      }
   }

   private static Collection<Module> getDefaultModules() {
      return Arrays.asList(new SystemDescriptorRuntimeModule(),
                           new XTextSystemDescriptorServiceModule());
   }

   private static Collection<Module> loadModules() {
      Collection<Module> dynamicModules = new ArrayList<>();
      // Use the ServiceLoader to find all Guice modules and include them in the list modules.
      for (Module m : ServiceLoader.load(Module.class)) {
         dynamicModules.add(m);
      }
      return dynamicModules;
   }
}
