/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.cli.gradle.adapter;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClasspathTemplateServiceTest {

   @Test
   public void testCanFindTemplatesInJar() throws Exception {
      Path exampleJar = Paths.get("src", "test", "resources", "example-classpath-templates.jar");
      Collection<String> templateNames = ClasspathTemplateService.getTemplateNames(exampleJar.toUri());
      assertEquals(4, templateNames.size());
      assertTrue(templateNames.contains("com.example-2.21.0-SNAPSHOT-template.zip"));
      assertTrue(templateNames.contains("com.example-2.21.0-SNAPSHOT-template-build.zip"));
      assertTrue(templateNames.contains("com.example-2.23.1-template-build.zip"));
      assertTrue(templateNames.contains("com.example2-1.5-template.zip"));
   }

   @Test
   public void testCanFindTemplateWithDifferentVersion() {
      Collection<String> templates = Arrays.asList("com.example-2.21.0-SNAPSHOT-template.zip",
               "com.example-2.21.0-SNAPSHOT-template-build.zip", "com.example-2.23.1-template-build.zip",
               "com.example2-1.5-template.zip");
      String template = ClasspathTemplateService.findTemplate(templates, "com.example");
      assertEquals("com.example-2.21.0-SNAPSHOT-template.zip", template);
      
      template = ClasspathTemplateService.findTemplate(templates, "com.example-build");
      assertEquals("com.example-2.23.1-template-build.zip", template);
      
      template = ClasspathTemplateService.findTemplate(templates, "com.example2");
      assertEquals("com.example2-1.5-template.zip", template);
   }

}
