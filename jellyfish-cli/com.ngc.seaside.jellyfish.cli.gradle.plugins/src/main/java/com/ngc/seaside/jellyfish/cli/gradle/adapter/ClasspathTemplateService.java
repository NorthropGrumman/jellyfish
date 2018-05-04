package com.ngc.seaside.jellyfish.cli.gradle.adapter;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Module;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.jellyfish.service.impl.templateservice.TemplateService;
import com.ngc.seaside.jellyfish.service.property.api.IPropertyService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.service.template.api.TemplateServiceException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * An implementation of {@code ITemplateService} that loads template resources (IE, ZIP) directly from the classpath.
 */
public class ClasspathTemplateService extends TemplateService {

   private static final String CONFIG_PROPERTIES_FILE = "com.ngc.seaside.jellyfish.cli.gradle.config.properties";
   private static final String VERSION_PROPERTY = "version";

   private final String version;

   @Inject
   public ClasspathTemplateService(ILogService logService,
                                   IResourceService resourceService,
                                   IPropertyService propertyService) {
      version = loadVersion();
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
                                                                         version,
                                                                         templateEnding));
      if (is == null) {
         throw new TemplateServiceException("no template named " + templateName + " found!");
      }
      return is;
   }

   private static String loadVersion() {
      String version;
      try (InputStream is = ClasspathTemplateService.class.getClassLoader()
            .getResourceAsStream(CONFIG_PROPERTIES_FILE)) {
         Properties props = new Properties();
         props.load(is);
         version = props.getProperty(VERSION_PROPERTY);
         Preconditions.checkState(version != null, "version not set in configuration properties!");
      } catch (IOException e) {
         throw new IllegalStateException("failed to load configuration properties from classpath!", e);
      }
      return version;
   }

   public static final Module MODULE = new AbstractModule() {

      @Override
      protected void configure() {
         bind(ITemplateService.class).to(ClasspathTemplateService.class);
      }
   };
}
