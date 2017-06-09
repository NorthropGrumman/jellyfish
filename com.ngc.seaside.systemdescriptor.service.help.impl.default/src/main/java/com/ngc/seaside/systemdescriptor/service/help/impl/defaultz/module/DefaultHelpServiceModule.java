package com.ngc.seaside.systemdescriptor.service.help.impl.defaultz.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import com.ngc.seaside.systemdescriptor.service.help.api.IHelpService;
import com.ngc.seaside.systemdescriptor.service.help.impl.defaultz.DefaultHelpService;

public class DefaultHelpServiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(IHelpService.class).to(DefaultHelpService.class).in(Singleton.class);
   }
}
