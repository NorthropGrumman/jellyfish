package com.ngc.seaside.systemdescriptor.service.impl.xtext.module;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Singleton;

import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.XTextSystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing.ParsingDelegate;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.validation.ValidationDelegate;

import org.eclipse.xtext.common.TerminalsStandaloneSetup;

/**
 * The Guice module configuration for the service.  Use {@link #prepare()} to get an instance of this module.
 */
public class XTextSystemDescriptorServiceModule extends AbstractModule {

   private XTextSystemDescriptorServiceModule() {
   }

   @Override
   protected void configure() {
      bind(ISystemDescriptorService.class).to(XTextSystemDescriptorService.class).in(Singleton.class);
      bind(ParsingDelegate.class).in(Singleton.class);
      bind(ValidationDelegate.class).in(Singleton.class);
   }

   /**
    * Prepares the {@code XTextSystemDescriptorService} for use and returns the {@code Module} that can be included with
    * the Guice configuration.
    */
   public static Module prepare() {
      // Perform XText setup.
      TerminalsStandaloneSetup.doSetup();
      return new XTextSystemDescriptorServiceModule();
   }
}
