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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;

/**
 *
 */
public class TemplateVisitorTest {

   private TemplateVisitor fixture;
   private TemplateIgnoreComponent templateIgnoreComponent;
   private Path inputFolder;
   private Path outputFolder;

   @Rule
   public TemporaryFolder testFolder = new TemporaryFolder();

   @Before
   public void setup() throws IOException, URISyntaxException {
      templateIgnoreComponent = mock(TemplateIgnoreComponent.class);

      Map<String, Object> parameterAndValues = new LinkedHashMap<>();
      parameterAndValues.put("classname", "MyClass");
      parameterAndValues.put("groupId", "com.ngc.seaside");
      parameterAndValues.put("artifactId", "mybundle");
      parameterAndValues.put("commandName", "my-bundle");
      parameterAndValues.put("package", "com.ngc.seaside.mybundle");
      parameterAndValues.put("pojo", new TestablePojo("Bob", "Smith"));

      File outputDirectory = testFolder.newFolder("output");
      outputFolder = Paths.get(outputDirectory.getAbsolutePath());

      URL visitorResourceURL = getClass().getClassLoader().getResource("visitorResource");
      Path visitorResourcePath = Paths.get(visitorResourceURL.toURI());

      inputFolder = Paths.get(visitorResourcePath.toString(), "templateContent");

      fixture = new TemplateVisitor(parameterAndValues,
                                    inputFolder,
                                    outputFolder,
                                    false,
                                    templateIgnoreComponent);

   }

   @Test
   public void testTemplate() throws IOException {
      Files.walkFileTree(inputFolder, fixture);

      Path topLevel = fixture.getTopLevelFolder();
      Path expected = Paths.get(outputFolder.toString(), "com.ngc.seaside.mybundle");
      assertEquals(expected, topLevel);
   }

   @Test
   public void testAsPath() {
      String dots = "com.ngc.seaside.testme";
      String extraDots = "extra.test";

      String expected = dots + "." + extraDots;
      expected = expected.replace(".", FileSystems.getDefault().getSeparator());

      assertEquals(expected, TemplateVisitor.asPath(dots, extraDots));

   }

}
