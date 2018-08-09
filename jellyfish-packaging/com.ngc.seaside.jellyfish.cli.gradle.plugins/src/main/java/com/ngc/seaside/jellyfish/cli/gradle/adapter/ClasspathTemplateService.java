package com.ngc.seaside.jellyfish.cli.gradle.adapter;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Module;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.jellyfish.cli.gradle.RuntimeProperties;
import com.ngc.seaside.jellyfish.service.impl.templateservice.TemplateService;
import com.ngc.seaside.jellyfish.service.property.api.IPropertyService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.service.template.api.TemplateServiceException;

import java.io.InputStream;

/**
 * An implementation of {@code ITemplateService} that loads template resources (IE, ZIP) directly from the classpath.
 */
public class ClasspathTemplateService extends TemplateService {

   @Inject
   public ClasspathTemplateService(ILogService logService,
                                   IResourceService resourceService,
                                   IPropertyService propertyService) {
      super.setLogService(logService);
      super.setResourceService(resourceService);
      super.setPropertyService(propertyService);
      super.activate();
   }

   @Override
   protected InputStream getTemplateInputStream(String templateName) throws TemplateServiceException {
      int index = templateName.indexOf('-');
      String templatePrefix = index < 0 ? templateName : templateName.substring(0, index);
      String templateEnding = index < 0 ? "" : templateName.substring(index);
      InputStream is;
      is = getClass().getClassLoader().getResourceAsStream(String.format("templates/%s-%s-template%s.zip",
                                                                         templatePrefix,
                                                                         RuntimeProperties.getVersion(),
                                                                         templateEnding));
      if (is == null) {
         throw new TemplateServiceException("no template named " + templateName + " found!");
      }
      return is;
   }

   public static final Module MODULE = new AbstractModule() {

      @Override
      protected void configure() {
         bind(ITemplateService.class).to(ClasspathTemplateService.class);
      }
   };
}
