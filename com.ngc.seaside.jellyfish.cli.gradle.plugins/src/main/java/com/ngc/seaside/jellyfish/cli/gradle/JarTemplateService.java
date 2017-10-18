package com.ngc.seaside.jellyfish.cli.gradle;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Module;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.seaside.bootstrap.service.impl.templateservice.TemplateService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.property.api.IPropertyService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.bootstrap.service.template.api.TemplateServiceException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JarTemplateService extends TemplateService {

   private final static String CONFIG_PROPERTIES_FILE = "com.ngc.seaside.jellyfish.cli.gradle.config.properties";
   private final static String VERSION_PROPERTY = "version";

   private final String version;

   @Inject
   public JarTemplateService(ILogService logService,
                             IResourceService resourceService,
                             IPromptUserService promptUserService,
                             IPropertyService propertyService) {
      version = loadVersion();
      super.setLogService(logService);
      super.setResourceService(resourceService);
      super.setPromptUserService(promptUserService);
      super.setPropertyService(propertyService);
      super.activate();
   }

   @Override
   protected InputStream getTemplateInputStream(String templateName) throws IOException, TemplateServiceException {
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
      try (InputStream is = JarTemplateService.class.getClassLoader().getResourceAsStream(CONFIG_PROPERTIES_FILE)) {
         Properties props = new Properties();
         props.load(is);
         version = props.getProperty(VERSION_PROPERTY);
         Preconditions.checkState(version != null, "version not set in configuration properties!");
      } catch (IOException e) {
         throw new IllegalStateException("failed to load configuration properties from classpath!", e);
      }
      return version;
   }

   public final static Module MODULE = new AbstractModule() {

      @Override
      protected void configure() {
         bind(ITemplateService.class).to(JarTemplateService.class);
      }
   };
}
