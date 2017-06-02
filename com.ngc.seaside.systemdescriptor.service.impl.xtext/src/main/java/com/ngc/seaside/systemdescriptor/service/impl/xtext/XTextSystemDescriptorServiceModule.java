package com.ngc.seaside.systemdescriptor.service.impl.xtext;

import com.google.inject.AbstractModule;

import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing.ParsingDelegate;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.validation.ValidationDelegate;

/**
 * The Guice module configuration for the service.
 */
public class XTextSystemDescriptorServiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(ISystemDescriptorService.class).to(XTextSystemDescriptorService.class);
      bind(ParsingDelegate.class);
      bind(ValidationDelegate.class);
   }
}
