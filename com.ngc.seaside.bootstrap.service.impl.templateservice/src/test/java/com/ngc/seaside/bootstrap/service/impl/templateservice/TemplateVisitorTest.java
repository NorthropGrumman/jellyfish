package com.ngc.seaside.bootstrap.service.impl.templateservice;

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
