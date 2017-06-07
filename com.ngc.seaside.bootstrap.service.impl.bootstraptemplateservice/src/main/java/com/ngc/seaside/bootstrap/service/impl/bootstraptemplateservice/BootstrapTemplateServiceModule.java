package com.ngc.seaside.bootstrap.service.impl.bootstraptemplateservice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.property.api.IPropertyService;
import com.ngc.seaside.bootstrap.service.template.api.BootstrapTemplateException;
import com.ngc.seaside.bootstrap.service.template.api.IBootstrapTemplateService;

import java.nio.file.Path;

/**
 * Guice wrapper around the BootstrapTemplateService class.
 */
public class BootstrapTemplateServiceModule extends AbstractModule implements IBootstrapTemplateService {

   private BootstrapTemplateService delegate = new BootstrapTemplateService();

   @Override
   protected void configure() {
     bind(IBootstrapTemplateService.class).toInstance(this);
   }

   @Override
   public boolean templateExists(String templateName) {
      return delegate.templateExists(templateName);
   }

   @Override
   public Path unpack(String templateName, Path outputDirectory, boolean clean) throws BootstrapTemplateException {
      return delegate.unpack(templateName, outputDirectory, clean);
   }

   @Inject
   public void setLogService(ILogService ref) {
      delegate.setLogService(ref);
   }

   @Inject
   public void setResourceService(IResourceService ref) {
      delegate.setResourceService(ref);
   }

   @Inject
   public void setPromptUserService(IPromptUserService ref) {
      delegate.setPromptUserService(ref);
   }

   @Inject
   public void setPropertyService(IPropertyService ref) {
      delegate.setPropertyService(ref);
   }
}
