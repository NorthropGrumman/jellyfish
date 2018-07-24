package com.ngc.seaside.jellyfish.service.impl.templateservice;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.jellyfish.service.property.api.IPropertyService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateOutput;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.service.template.api.TemplateServiceException;

import java.nio.file.Path;

/**
 * Wrap the service using Guice Injection
 */
@Singleton
public class TemplateServiceGuiceWrapper implements ITemplateService {

   private TemplateService delegate = new TemplateService();

   @Inject
   public TemplateServiceGuiceWrapper(ILogService logService,
                                      IResourceService resourceService,
                                      IPromptUserService promptUserService,
                                      IPropertyService propertyService) {
      delegate.setLogService(logService);
      delegate.setResourceService(resourceService);
      delegate.setPromptUserService(promptUserService);
      delegate.setPropertyService(propertyService);
      delegate.activate();
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
}
