package com.ngc.seaside.jellyfish.service.impl.templateservice;

import com.google.inject.AbstractModule;

import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;

/**
 * Configure the service for use in Guice
 */
public class TemplateServiceGuiceModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(ITemplateService.class).to(TemplateServiceGuiceWrapper.class);
   }

}
