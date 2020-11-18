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
package com.ngc.seaside.jellyfish.cli.gradle.adapter;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.ngc.seaside.jellyfish.service.impl.templateservice.TemplateService;
import com.ngc.seaside.jellyfish.service.property.api.IPropertyService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.service.template.api.TemplateServiceException;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;

/**
 * An implementation of {@code ITemplateService} that loads template resources (IE, ZIP) directly from the classpath.
 */
public class ClasspathTemplateService extends TemplateService {

   private static final String TEMPLATES_DIRECTORY_NAME = "templates";

   private static final Comparator<String> VERSION_COMPARATOR = (v1, v2) -> {
      String[] p1 = v1.split("\\.|-");
      String[] p2 = v2.split("\\.|-");
      for (int i = 0; i < Math.min(p1.length, p2.length); i++) {
         String e1 = p1[i];
         String e2 = p2[i];
         int comp;
         try {
            int i1 = Integer.parseInt(e1);
            int i2 = Integer.parseInt(e2);
            comp = Integer.compare(i1, i2);

         } catch (NumberFormatException e) {
            comp = e1.compareTo(e2);
         }
         if (comp != 0) {
            return comp;
         }
      }
      return Integer.compare(p1.length, p2.length);
   };

   private final List<String> templateNames = new ArrayList<>();

   @Inject
   public ClasspathTemplateService(ILogService logService,
                                   IPropertyService propertyService) {
      super.setLogService(logService);
      super.setPropertyService(propertyService);
      super.activate();
      try {
         findTemplates();
      } catch (Exception e) {
         throw new IllegalStateException("unable to find templates on classpath", e);
      }
   }

   @Override
   protected InputStream getTemplateInputStream(String templateName) throws TemplateServiceException {
      String template = findTemplate(templateNames, templateName);
      InputStream is;
      is = getClass().getClassLoader().getResourceAsStream(TEMPLATES_DIRECTORY_NAME + "/" + template);
      return is;
   }

   /**
    * Finds the templates found in this project's classpath in the templates folder. This implementation
    * is complicated by the fact that normally the templates are located within the project's .jar, but
    * for tests are located in a folder separate from the compiled source code.
    */
   private void findTemplates() throws Exception {
      Enumeration<URL> templateFolders =
               ClasspathTemplateService.class.getClassLoader().getResources(TEMPLATES_DIRECTORY_NAME);
      while (templateFolders.hasMoreElements()) {
         URL templatesFolder = templateFolders.nextElement();
         URI location = templatesFolder.toURI();
         String baseLocation = location.toString();
         baseLocation = baseLocation.substring(0, baseLocation.length() - TEMPLATES_DIRECTORY_NAME.length() - 1);
         if (baseLocation.endsWith("!")) {
            baseLocation = baseLocation.substring(0, baseLocation.length() - 1);
         }
         location = URI.create(baseLocation);
         templateNames.addAll(getTemplateNames(location));
      }
   }

   static String findTemplate(Collection<String> templates, String templateName) {
      int index = templateName.indexOf('-');
      String templatePrefix = index < 0 ? templateName : templateName.substring(0, index);
      String templateEnding = index < 0 ? "" : templateName.substring(index);
      Pattern templatePattern =
               Pattern.compile(templatePrefix + "-(?<version>.+?)-template" + templateEnding + ".zip");
      TreeMap<String, String> matchingTemplates = new TreeMap<>(VERSION_COMPARATOR);
      for (String template : templates) {
         Matcher m = templatePattern.matcher(template);
         if (m.matches()) {
            String version = m.group("version");
            matchingTemplates.put(version, template);
         }
      }
      if (matchingTemplates.isEmpty()) {
         throw new TemplateServiceException("no template named " + templateName + " found!");
      }
      return matchingTemplates.lastEntry().getValue();
   }

   static Collection<String> getTemplateNames(URI location) throws Exception {
      Path templatesDirectory;
      FileSystem fs = null;
      if (location.toString().toLowerCase().endsWith(".jar")) {
         URI jarLocation = location;
         if (!jarLocation.toString().toLowerCase().startsWith("jar:")) {
            jarLocation = URI.create("jar:" + jarLocation.toString());
         }
         try {
            fs = FileSystems.newFileSystem(jarLocation, Collections.emptyMap());
         } catch (FileSystemAlreadyExistsException e) {
            fs = FileSystems.getFileSystem(jarLocation);
         }
         templatesDirectory = fs.getPath(TEMPLATES_DIRECTORY_NAME);
      } else {
         templatesDirectory = Paths.get(location).resolve(TEMPLATES_DIRECTORY_NAME);
      }
      List<String> templates =
               Files.list(templatesDirectory).map(Path::getFileName).map(Path::toString).collect(Collectors.toList());
      if (fs != null) {
         fs.close();
      }
      return templates;
   }

   public static final Module MODULE = new AbstractModule() {

      @Override
      protected void configure() {
         bind(ITemplateService.class).to(ClasspathTemplateService.class);
      }
   };
}
