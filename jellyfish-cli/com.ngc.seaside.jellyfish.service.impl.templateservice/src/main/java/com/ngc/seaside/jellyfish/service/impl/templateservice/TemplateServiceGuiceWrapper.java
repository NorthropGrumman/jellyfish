/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.jellyfish.service.impl.templateservice;

import java.nio.file.Path;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.jellyfish.service.property.api.IPropertyService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateOutput;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.service.template.api.TemplateServiceException;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;

/**
 * Wrap the service using Guice Injection
 */
@Singleton
public class TemplateServiceGuiceWrapper implements ITemplateService {

   private TemplateService delegate = new TemplateService();

   @Inject
   public TemplateServiceGuiceWrapper(ILogService logService,
                                      IPromptUserService promptUserService,
                                      IPropertyService propertyService) {
      delegate.setLogService(logService);
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
