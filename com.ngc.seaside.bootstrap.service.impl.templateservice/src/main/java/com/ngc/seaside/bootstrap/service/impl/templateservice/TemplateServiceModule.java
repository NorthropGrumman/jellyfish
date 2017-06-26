package com.ngc.seaside.bootstrap.service.impl.templateservice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.property.api.IPropertyService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateOutput;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.bootstrap.service.template.api.TemplateServiceException;
import com.ngc.seaside.command.api.IParameterCollection;

import java.nio.file.Path;

/**
 * Guice wrapper around the TemplateService class.
 */
public class TemplateServiceModule extends AbstractModule implements ITemplateService {

   private TemplateService delegate = new TemplateService();

   @Override
   protected void configure() {
      bind(ITemplateService.class).toInstance(this);
   }

   @Override
   public boolean templateExists(String templateName) {
      return delegate.templateExists(templateName);
   }

   @Override
   public ITemplateOutput unpack(String templateName,
                                 IParameterCollection parameters,
                                 Path outputDirectory,
                                 boolean clean)
            throws TemplateServiceException {
      return delegate.unpack(templateName, parameters, outputDirectory, clean);
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
