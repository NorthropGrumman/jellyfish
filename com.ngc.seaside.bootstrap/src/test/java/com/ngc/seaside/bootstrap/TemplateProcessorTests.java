package com.ngc.seaside.bootstrap;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for the TemplateProcessor class.
 */
public class TemplateProcessorTests {
//   private Path zipFolder;
//   private int fileCount;
//   private Path templatePath;
//
//   @Before
//   public void setup() throws URISyntaxException {
//      zipFolder = Paths.get(getClass().getClassLoader().getResource(
//               "templates/example/templates.example.TemplateExample.zip").toURI());
//      templatePath = Paths.get(getClass().getClassLoader().getResource("templates.example.TemplateExample").toURI());
//      fileCount = 0;
//   }
//
//   @Test
//   public void testUnzip() {
//      Path results;
//
//      /*
//       * Test 1: Valid zip folder
//       */
//
//      // Setup expected
//      Set<String> expectedFiles = new HashSet<>();
//      expectedFiles.add(".gitignore");
//      expectedFiles.add("settings.gradle");
//      expectedFiles.add("build.gradle");
//      expectedFiles.add("Sample.java");
//      expectedFiles.add("SampleTest.java");
//      expectedFiles.add("template.properties");
//      expectedFiles.add("gradlew");
//      expectedFiles.add("gradlew.bat");
//      expectedFiles.add("gradle-wrapper.jar");
//      expectedFiles.add("gradle-wrapper.properties");
//
//      int expectedFileCount = expectedFiles.size();
//
//      // Method to test
//      try {
//         results = TemplateProcessor.unzip(zipFolder);
//
//         assertNotNull(results);
//
//         Files.walkFileTree(results, new SimpleFileVisitor<Path>() {
//            @Override
//            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//               assertTrue("Unexpected File: " + file, expectedFiles.contains(file.getFileName().toString()));
//               fileCount++;
//               return FileVisitResult.CONTINUE;
//            }
//         });
//
//         assertEquals(expectedFileCount, fileCount);
//         TemplateGenerator.deleteRecursive(results, false);
//      } catch (IOException e) {
//         fail("Error processing file");
//      }
//
//      /*
//       * Test 2: Invalid zip folder
//       */
//      results = null;
//
//      try {
//         results = TemplateProcessor.unzip(templatePath);
//      } catch (IOException e) {
//         // This exception is expected
//         return;
//      }
//   }
//
//   @Test
//   public void testValidateTemplate() throws URISyntaxException {
//      /*
//       * Test 1: Valid template
//       */
//      try {
//         TemplateProcessor.validateTemplate(templatePath);
//      } catch (ExitException e) {
//         fail();
//      }
//
//      /*
//       * Test 2: Invalid template: template.properties not found
//       */
//
//      // Setup
//      templatePath = Paths.get(getClass().getClassLoader().getResource(
//               "templates.InvalidTemplates").toURI()).resolve("NoProperties");
//
//      try {
//         TemplateProcessor.validateTemplate(templatePath);
//      } catch (ExitException e) {
//         assertTrue(e.failed());
//      }
//
//      /*
//       * Test 3: Invalid template: template.properties must be a file
//       */
//
//      // Setup
//      templatePath = Paths.get(getClass().getClassLoader().getResource(
//               "templates.InvalidTemplates").toURI()).resolve("NotAFile");
//
//      try {
//         TemplateProcessor.validateTemplate(templatePath);
//      } catch (ExitException e) {
//         assertTrue(e.failed());
//      }
//
//      /*
//       * Test 4: Invalid template: template folder not found
//       */
//
//      // Setup
//      templatePath =
//               Paths.get(getClass().getClassLoader().getResource(
//                        "templates.InvalidTemplates").toURI()).resolve("NoFolderFound");
//
//      try {
//         TemplateProcessor.validateTemplate(templatePath);
//      } catch (ExitException e) {
//         assertTrue(e.failed());
//      }
//
//      /*
//       * Test 5: Invalid template: template must be a folder
//       */
//
//      // Setup
//      templatePath =
//               Paths.get(getClass().getClassLoader().getResource(
//                        "templates.InvalidTemplates").toURI()).resolve("NotAFolder");
//
//      try {
//         TemplateProcessor.validateTemplate(templatePath);
//      } catch (ExitException e) {
//         assertTrue(e.failed());
//      }
//   }
//
//   @Test
//   public void testParseTemplateProperties() throws URISyntaxException {
//      LinkedHashMap<String, String> actual;
//      LinkedHashMap<String, String> expected;
//
//      /**
//       * Test 1: Verify properties are placed in the hashmap
//       */
//
//      // Setup
//      templatePath =
//               Paths.get(getClass().getClassLoader().getResource("templates.example.TemplateExample").toURI())
//                        .resolve("template.properties");
//      expected = new LinkedHashMap<>();
//
//      expected.put("groupId", "com.ngc.seaside.starfish");
//      expected.put("artifactId", "sample");
//      expected.put("version", "1.0-SNAPSHOT");
//
//      // Method to test
//      try {
//         actual = TemplateProcessor.parseTemplateProperties(templatePath);
//         assertEquals(expected.size(), actual.size());
//
//         for (Entry<String, String> entry : actual.entrySet()) {
//
//            if (!expected.containsKey(entry.getKey())) {
//               fail();
//            }
//            assertEquals(expected.get(entry.getKey()), entry.getValue());
//         }
//      } catch (IOException e) {
//         fail();
//      }
//   }
}
