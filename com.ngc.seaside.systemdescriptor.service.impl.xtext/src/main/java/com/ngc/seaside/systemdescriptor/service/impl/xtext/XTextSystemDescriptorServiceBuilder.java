package com.ngc.seaside.systemdescriptor.service.impl.xtext;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;

import com.ngc.blocs.service.log.api.ILogService;
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

/**
 * This is a utility class that is used to created new instances of XText based {@code ISystemDescriptorService}
 * implementation.  It can be used in two ways:
 *
 * <ol> <li>to integrate the service into a larger application which has its own dependency injection (preferred)</li>
 * <li>to be referenced in a small application that does not provide its own dependency injection</li> </ol>
 *
 * Continue reading to learn more.
 *
 * <h1>Integrating with existing applications</h1>
 *
 * Integrating with applications via {@link #forIntegration(Consumer)} is the preferred way to construct the service.
 * With this option, the application itself is responsible for creating and manging the {@code Injector}.  The
 * application passes a {@link Consumer} to the builder that will be used to consume all modules that are required by
 * the service in order for it to be instantiated.  Applications typically consume these modules by adding them to a
 * list.  Finally, construction is complete when a application provides a {@link Supplier} that created the actual
 * {@code Injector}. The builder will notify the {@code Consumer} first, then it will request an {@code Injector} for
 * the supplier.  The service is configured and the original injector is returned to the application.  The service can
 * then be resolved from the injector.
 *
 * In almost all cases, it is used like this:
 *
 * <pre>
 *    {@code Collection<Module> appModules = new ArrayList<>();
 *      // Add any application modules here.
 *      appModules.add(new MyAppModule());
 *      Injector injector = XTextSystemDescriptorServiceBuilder.forIntegration(appModules::add)
 *        .build(() -> Guice.createInjector(appModules));
 *      ISystemDescriptorService service = injector.getInstance(ISystemDescriptorService.class); }
 * </pre>
 *
 * Most of the time the service should be injected into an application component instead of being resolved directly:
 *
 * <pre>
 *    {@code public class MyApplication {
 *       private final ISystemDescriptorService service;
 *
 *       {@literal @}Inject
 *       public MyApplication(ISystemDescriptorService service) {
 *          this.service = service;
 *       }
 *    } }
 * </pre>
 *
 * It is also possible to configure build to load all {@code Module}s via {@link ServiceLoader service loaders} with the
 * {@link IntegrationBuilder#includeServiceLoadedModules(boolean) includeServiceLoadedModules} option.  The default
 * value for this behavior is false.  Only enable this if the application is not doing something like this itself.
 *
 * <h1>Working with smaller applications</h1>
 *
 * As an alternative, {@link #forApplication(ILogService)} can be used to create a builder that will built and instance
 * of the service for an application that doesn't use dependency injection.  This is also useful in tests.  It is used
 * like this:
 *
 * <pre>
 *    {@code ISystemDescriptorService service = XTextSystemDescriptorServiceBuilder.forApplication().build();
 *      // You can also get the injector with this:
 *      Injector injector = XTextSystemDescriptorServiceBuilder.forApplication(logService)
 *        .buildInjector();
 *      // And resolve the service directly:
 *      service = injector.getInstance(ISystemDescriptorService.class); }
 * </pre>
 *
 * An {@code ILogService} implementation must be supplied.
 *
 * <p/>
 *
 * You can also add additional modules to be configured with {@link ForApplicationBuilder#addModule(Module, Module...)
 * addModule}. It's also possible to enable the {@link ServiceLoader service loading} mechanisms described above with
 * {@link ForApplicationBuilder#includeServiceLoadedModules(boolean) includeServiceLoadedModules}.
 */
public class XTextSystemDescriptorServiceBuilder {

   private XTextSystemDescriptorServiceBuilder() {
   }

   /**
    * Creates a builder for an instance of an XText based {@code ISystemDescriptorService} implementation.  This builder
    * used to introduce the service into an existing application and is the preferred way to create an instance of the
    * service.
    *
    * @param moduleConsumer a {@code Consumer} that is invoked to consume all the {@link Module}s needed to create an
    *                       instance of the service
    */
   public static IntegrationBuilder forIntegration(Consumer<Module> moduleConsumer) {
      Preconditions.checkNotNull(moduleConsumer, "moduleConsumer may not be null!");
      return new IntegrationBuilder(moduleConsumer);
   }

   /**
    * Creates a builder for an instance of an XText based {@code ISystemDescriptorService} implementation.  This builder
    * is for use in a simple applications that do not have their own dependency injection mechanism.  This builder is
    * directly responsible for creating the {@code Injector} that will wire up the application.  Note in most case, the
    * creation of the {@code Injector} is the responsibility of the application, not a component within the application.
    * Thus, consider using {@link #forIntegration(Consumer)} to integrate the service into an application instead.
    *
    * @param logService the log service to use when building the service
    * @see #forIntegration(Consumer)
    */
   public static ForApplicationBuilder forApplication(ILogService logService) {
      Preconditions.checkNotNull(logService, "logService may not be null!");
      return new ForApplicationBuilder(logService);
   }

   /**
    * A builder for creating an instance of XText based {@code ISystemDescriptorService} implementation which can be
    * integrated into another application.
    */
   public static class IntegrationBuilder {

      private final Consumer<Module> moduleConsumer;
      private boolean includeServiceLoadedModules = false;

      private IntegrationBuilder(Consumer<Module> moduleConsumer) {
         this.moduleConsumer = moduleConsumer;
      }

      /**
       * If true, Guice {@link Module}s will be loaded via {@link ServiceLoader service loaders} when building the
       * {@code Injector}.  Use this option to include additional JellyFish plugins when initializing the service and
       * the application is not during type of work itself.  If this value is true, the provided {@link Consumer module
       * consumer} will also be invoked to cosume any modules obtained via the service loader.  The default value is
       * false.
       *
       * @param includeServiceLoadedModules if true, {@code Modules} are loaded via a service loader
       * @return this builder
       */
      public IntegrationBuilder includeServiceLoadedModules(boolean includeServiceLoadedModules) {
         this.includeServiceLoadedModules = includeServiceLoadedModules;
         return this;
      }

      /**
       * Uses a {@link Supplier} to get an {@code Injector} and configures the XText {@code ISystemDescriptorService}
       * implementation.  Note the {@link Consumer module consumer} is invoked to consume all modules required by the
       * service <i>before</i> the {@code Supplier} is invoked.  The original {@code Injector} as provided by the
       * supplier is returned.
       *
       * @param injectorSupplier a supplier that will be invoked to create the {@code Injector}
       * @return the {@code Injector} created via the {@code Supplier}
       */
      public Injector build(Supplier<Injector> injectorSupplier) {
         Preconditions.checkNotNull(injectorSupplier, "injectorSupplier may not be null!");

         // Note we are using a callback based way to perform the creation of the Injector because we need to make
         // sure XText related operations happen in the right order.  In particular, we need to setup the
         // terminals, create the injector, register the EMF models (in that order).

         // Have the consumer accept the default modules.
         getDefaultModules().forEach(moduleConsumer);
         // If configured, have the consumer also accept any loaded modules.
         if (includeServiceLoadedModules) {
            loadModules().forEach(moduleConsumer);
         }

         // XText setup.
         TerminalsStandaloneSetup.doSetup();
         // Request the injector be created
         Injector injector = injectorSupplier.get();
         new SystemDescriptorStandaloneSetup().register(injector);
         return injector;
      }
   }

   /**
    * A builder for creating a standalone instance of an XText based {@code ISystemDescriptorService} implementation
    * which can be used by a lightwight application.
    *
    * @see #forApplication(ILogService)
    */
   public static class ForApplicationBuilder {

      private final Collection<Module> modules = new ArrayList<>();
      private final ILogService logService;
      private boolean includeServiceLoadedModules = false;

      private ForApplicationBuilder(ILogService logService) {
         this.logService = logService;
         modules.addAll(getDefaultModules());
         // Add the log service binding.
         modules.add(new AbstractModule() {
            @Override
            protected void configure() {
               bind(ILogService.class).toInstance(logService);
            }
         });
      }

      /**
       * Includes the given modules when building the {@code Injector} to resolve the XText based {@code
       * ISystemDescriptorService}.
       *
       * @param module the additional module to include when building the {@code Injector}
       * @param more   additional modules to include when building the {@code Injector}
       * @return this builder
       */
      public ForApplicationBuilder addModule(Module module, Module... more) {
         modules.add(Preconditions.checkNotNull(module, "module may not be null!"));
         if (more != null) {
            Collections.addAll(modules, more);
         }
         return this;
      }

      /**
       * Includes the given modules when building the {@code Injector} to resolve the XText based {@code
       * ISystemDescriptorService}.
       *
       * @param modules additional modules to include when building the {@code Injector}
       * @return this builder
       */
      public ForApplicationBuilder addModules(Collection<Module> modules) {
         Preconditions.checkNotNull(modules, "modules may not be null!");
         this.modules.addAll(modules);
         return this;
      }

      /**
       * If true, Guice {@link Module}s will be loaded via {@link ServiceLoader service loaders} when building the
       * {@code Injector}.  Use this option to include additional JellyFish plugins when initializing the service.  The
       * default value is false.
       *
       * @param includeServiceLoadedModules if true, {@code Modules} are loaded via a service loader
       * @return this builder
       */
      public ForApplicationBuilder includeServiceLoadedModules(boolean includeServiceLoadedModules) {
         this.includeServiceLoadedModules = includeServiceLoadedModules;
         return this;
      }

      /**
       * Gets an XText based implementation of {@code ISystemDescriptorService}.
       *
       * @return an implementation of {@code ISystemDescriptorService}
       */
      public ISystemDescriptorService build() {
         Injector injector = buildInjector();
         return injector.getInstance(ISystemDescriptorService.class);
      }

      /**
       * Gets an {@code Injector} that can be used to resolve a reference to an XText based {@code
       * ISystemDescriptorService} implementation.
       *
       * @return the {@code Injector}
       */
      public Injector buildInjector() {
         // Do we need to include any modules from service loaders?
         if (includeServiceLoadedModules) {
            modules.addAll(loadModules());
         }
         return new SystemDescriptorStandaloneSetup().createInjectorAndDoEMFRegistration(modules, false);
      }
   }

   private static Collection<Module> getDefaultModules() {
      // These modules are always needed.
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
