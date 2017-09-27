package com.ngc.seaside.jellyfish.cli.gradle;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Module;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.bootstrap.service.impl.templateservice.TemplateService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.property.api.IPropertyService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;

public class JarTemplateService extends TemplateService {

   @Inject
   public JarTemplateService(ILogService logService,
                             IResourceService resourceService,
                             IPromptUserService promptUserService,
                             IPropertyService propertyService) {
      super.setLogService(logService);
      super.setResourceService(resourceService);
      super.setPromptUserService(promptUserService);
      super.setPropertyService(propertyService);
      super.activate();
   }

   public final static Module MODULE = new AbstractModule() {

      @Override
      protected void configure() {
         bind(ITemplateService.class).to(JarTemplateService.class);
      }
   };
}
