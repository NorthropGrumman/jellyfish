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

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 *
 */
public class TemplateIgnoreComponentTest {

   private ILogService logService;
   private TemplateIgnoreComponent fixture;

   @Before
   public void setup() throws URISyntaxException {
      URL ignoreComponentResourceURL = getClass().getClassLoader().getResource("ignoreComponentResource");

      Path templateDir = Paths.get(ignoreComponentResourceURL.toURI());
      Path template = templateDir.resolve("templateContent");

      logService = new PrintStreamLogService();
      fixture = new TemplateIgnoreComponent(template, "templateContent", logService);
   }

   @Test
   public void testParse() throws IOException {

      fixture.parse();

      List<String> keys = fixture.getAllKeys();

      assertEquals("${groupId}.${artifactId}/src/main/template/template.ignore", keys.get(0));
      assertEquals("[${groupId}.${artifactId}]/src/main/template/templateContent/${groupId}.${artifactId}",
                   keys.get(1));
   }

}
