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
