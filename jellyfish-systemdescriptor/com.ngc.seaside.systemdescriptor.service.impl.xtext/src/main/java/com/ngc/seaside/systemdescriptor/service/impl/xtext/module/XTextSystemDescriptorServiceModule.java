package com.ngc.seaside.systemdescriptor.service.impl.xtext.module;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.SystemDescriptorRuntimeModule;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.XTextSystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing.ParsingDelegate;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.XTextSourceLocatorService;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.validation.ScenarioStepValidator;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.validation.ValidationDelegate;
import com.ngc.seaside.systemdescriptor.source.api.ISourceLocatorService;
import com.ngc.seaside.systemdescriptor.validation.api.ISystemDescriptorValidator;

import org.eclipse.xtext.common.TerminalsStandaloneSetup;

import java.lang.reflect.Constructor;

/**
 * The Guice module configuration for the service.  Only use the {@link #XTextSystemDescriptorServiceModule() default
 * constructor} of this module if the service is being used within Eclipse.  Otherwise, use {@link
 * #forStandaloneUsage()} to create the module.
 */
public class XTextSystemDescriptorServiceModule extends AbstractModule {

   private final Constructor<XTextSystemDescriptorService> constructor;
   private final boolean isStandalone;

   /**
    * Creates a new instance of this module that is configured to provide a {@code ISystemDescriptorService} for use
    * within Eclipse.  If the service is being used outside of Eclipse use {@link #forStandaloneUsage()} instead to
    * create this module.
    */
   public XTextSystemDescriptorServiceModule() {
      this(getEclipseIntegratedConstructor(), false);
   }

   /**
    * Private constructor used to specify the constructor of the service which will be bound..
    */
   private XTextSystemDescriptorServiceModule(Constructor<XTextSystemDescriptorService> constructor,
                                              boolean isStandalone) {
      this.constructor = constructor;
      this.isStandalone = isStandalone;
   }

   @Override
   protected void configure() {
      // Use either the standalone or embedded constructor to create the service.
      bind(ISystemDescriptorService.class).toConstructor(constructor).asEagerSingleton();
      bind(ISourceLocatorService.class).to(XTextSourceLocatorService.class).in(Singleton.class);
      bind(ParsingDelegate.class).in(Singleton.class);
      bind(ValidationDelegate.class).in(Singleton.class);
      bindDefaultValidators();

      // If in standalone mode, include the DSL module so the client does not have to.
      if (isStandalone) {
         install(new SystemDescriptorRuntimeModule());
      }
   }

   /**
    * Invoked to bind the default {@link ISystemDescriptorValidator}s.
    */
   protected void bindDefaultValidators() {
      Multibinder<ISystemDescriptorValidator> multibinder = Multibinder.newSetBinder(
            binder(),
            ISystemDescriptorValidator.class);
      multibinder.addBinding().to(ScenarioStepValidator.class);
   }

   /**
    * Prepares the {@code XTextSystemDescriptorService} for use outside of Eclipse and returns the {@code Module} that
    * can be included with the Guice configuration.  Use this option when using the service outside of Eclipse.  If
    * using the service inside Eclipse use the {@link #XTextSystemDescriptorServiceModule() public constructor}.
    */
   public static Module forStandaloneUsage() {
      // Perform XText setup.
      TerminalsStandaloneSetup.doSetup();
      return new XTextSystemDescriptorServiceModule(getStandaloneConstructor(), true);
   }

   private static Constructor<XTextSystemDescriptorService> getEclipseIntegratedConstructor() {
      try {
         return XTextSystemDescriptorService.class.getConstructor(ILogService.class,
                                                                  ParsingDelegate.class,
                                                                  ValidationDelegate.class,
                                                                  XTextSystemDescriptorService.StepsHolder.class);
      } catch (NoSuchMethodException e) {
         // This shouldn't happen unless the XTextSystemDescriptorService is refactored to have different constructors.
         throw new RuntimeException(e.getMessage(), e);
      }
   }

   private static Constructor<XTextSystemDescriptorService> getStandaloneConstructor() {
      try {
         return XTextSystemDescriptorService.class.getConstructor(Injector.class,
                                                                  ILogService.class,
                                                                  ParsingDelegate.class,
                                                                  ValidationDelegate.class,
                                                                  XTextSystemDescriptorService.StepsHolder.class);
      } catch (NoSuchMethodException e) {
         // This shouldn't happen unless the XTextSystemDescriptorService is refactored to have different constructors.
         throw new RuntimeException(e.getMessage(), e);
      }
   }
}
