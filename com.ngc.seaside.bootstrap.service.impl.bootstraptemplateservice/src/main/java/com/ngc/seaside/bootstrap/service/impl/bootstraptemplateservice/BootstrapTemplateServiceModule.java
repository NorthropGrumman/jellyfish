package com.ngc.seaside.bootstrap.service.impl.bootstraptemplateservice;

import com.google.inject.AbstractModule;

import com.ngc.seaside.bootstrap.service.template.api.IBootstrapTemplateService;

/**
 * @author justan.provence@ngc.com
 */
public class BootstrapTemplateServiceModule extends AbstractModule {
   @Override
   protected void configure() {
     bind(IBootstrapTemplateService.class).to(BootstrapTemplateServiceDelegate.class);
   }
}
