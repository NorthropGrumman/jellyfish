/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
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
