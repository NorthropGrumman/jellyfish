package com.ngc.seaside.bootstrap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for the TemplateGenerator class.
 */
public class TemplateGeneratorTests {
//   private Path templateFolder;
//   private Path outputFolder;
//   private Map<String, String> parametersAndValues;
//
//   private static final String GROUP_ID = "com.ngc.seaside.starfish";
//   private static final String ARTIFACT_ID = "sample";
//   private static final String VERSION = "1.0-SNAPSHOT";
//
//   @Before
//   public void setup() throws URISyntaxException, IOException {
//      templateFolder =
//               Paths.get(getClass().getClassLoader().getResource("TemplateExample").toURI()).resolve("template");
//      outputFolder = Files.createTempDirectory(null);
//      templateFolder.toFile().deleteOnExit();
//      outputFolder.toFile().deleteOnExit();
//      parametersAndValues = new HashMap<>();
//      parametersAndValues.put("groupId", GROUP_ID);
//      parametersAndValues.put("artifactId", ARTIFACT_ID);
//      parametersAndValues.put("version", VERSION);
//   }
//
//   @Test
//   public void testGenerator() throws IOException {
//      TemplateGenerator gen = new TemplateGenerator(parametersAndValues, templateFolder, outputFolder, false);
//      Files.walkFileTree(templateFolder, gen);
//
//      Path base = outputFolder.resolve(GROUP_ID);
//      Assert.assertTrue(Files.isDirectory(base));
//
//      Path gradle = base.resolve("build.gradle");
//      Assert.assertTrue(Files.isRegularFile(gradle));
//
//      Path mainJavaFolder = base.resolve(Paths.get("src", "main", "java"));
//      Assert.assertTrue(Files.isDirectory(mainJavaFolder));
//
//      Path groupFolder = mainJavaFolder.resolve(GROUP_ID.replace(".", FileSystems.getDefault().getSeparator()));
//      Assert.assertTrue(Files.isDirectory(groupFolder));
//
//      Path artifactFolder = groupFolder.resolve(ARTIFACT_ID);
//      Assert.assertTrue(Files.isDirectory(artifactFolder));
//
//      Path sampleFile = artifactFolder.resolve("Sample.java");
//      Assert.assertTrue(Files.isRegularFile(sampleFile));
//
//      Path testJavaFolder = base.resolve(Paths.get("src", "test", "java"));
//      Assert.assertTrue(Files.isDirectory(mainJavaFolder));
//
//      Path testGroupFolder = testJavaFolder.resolve(GROUP_ID.replace(".", FileSystems.getDefault().getSeparator()));
//      Assert.assertTrue(Files.isDirectory(testGroupFolder));
//
//      Path testArtifactFolder = testGroupFolder.resolve(ARTIFACT_ID);
//      Assert.assertTrue(Files.isDirectory(testArtifactFolder));
//
//      Path testSampleFile = testArtifactFolder.resolve("SampleTest.java");
//      Assert.assertTrue(Files.isRegularFile(testSampleFile));
//
//      for (Map.Entry<String, String> entry : parametersAndValues.entrySet()) {
//         String key = entry.getKey();
//         Assert.assertTrue(checkFile(gradle, "$" + key, "${" + key + "}"));
//         Assert.assertTrue(checkFile(sampleFile, "$" + key, "${" + key + "}"));
//         Assert.assertTrue(checkFile(testSampleFile, "$" + key, "${" + key + "}"));
//      }
//   }
//
//   @Test
//   public void testGeneratorWithClean() throws IOException {
//      TemplateGenerator gen = new TemplateGenerator(parametersAndValues, templateFolder, outputFolder, true);
//      Files.createFile(outputFolder.resolve("should_not_be_deleted"));
//      Files.walkFileTree(templateFolder, gen);
//
//      Files.createDirectories(outputFolder.resolve(GROUP_ID));
//      Files.createFile(outputFolder.resolve(Paths.get(GROUP_ID, "test.txt")));
//
//      final String ARTIFACT_ID2 = "sample2";
//
//      parametersAndValues.put("artifactId", ARTIFACT_ID2);
//
//      gen = new TemplateGenerator(parametersAndValues, templateFolder, outputFolder, true);
//      Files.walkFileTree(templateFolder, gen);
//
//      Path base = outputFolder.resolve(GROUP_ID);
//      Assert.assertTrue(Files.isDirectory(base));
//
//      Path gradle = base.resolve("build.gradle");
//      Assert.assertTrue(Files.isRegularFile(gradle));
//
//      Path falseFile = base.resolve("test.txt");
//      Assert.assertFalse(Files.exists(falseFile));
//
//      Path mainJavaFolder = base.resolve(Paths.get("src", "main", "java"));
//      Assert.assertTrue(Files.isDirectory(mainJavaFolder));
//
//      Path groupFolder = mainJavaFolder.resolve(GROUP_ID.replace(".", FileSystems.getDefault().getSeparator()));
//      Assert.assertTrue(Files.isDirectory(groupFolder));
//
//      Path falseArtifactFolder = groupFolder.resolve(ARTIFACT_ID);
//      Assert.assertFalse(Files.exists(falseArtifactFolder));
//
//      Path artifactFolder = groupFolder.resolve(ARTIFACT_ID2);
//      Assert.assertTrue(Files.isDirectory(artifactFolder));
//
//      Path sampleFile = artifactFolder.resolve("Sample.java");
//      Assert.assertTrue(Files.isRegularFile(sampleFile));
//
//      Path testJavaFolder = base.resolve(Paths.get("src", "test", "java"));
//      Assert.assertTrue(Files.isDirectory(mainJavaFolder));
//
//      Path testGroupFolder = testJavaFolder.resolve(GROUP_ID.replace(".", FileSystems.getDefault().getSeparator()));
//      Assert.assertTrue(Files.isDirectory(testGroupFolder));
//
//      Path falseTestArtifactFolder = testGroupFolder.resolve(ARTIFACT_ID);
//      Assert.assertFalse(Files.exists(falseTestArtifactFolder));
//
//      Path testArtifactFolder = testGroupFolder.resolve(ARTIFACT_ID2);
//      Assert.assertTrue(Files.isDirectory(testArtifactFolder));
//
//      Path testSampleFile = testArtifactFolder.resolve("SampleTest.java");
//      Assert.assertTrue(Files.isRegularFile(testSampleFile));
//
//      for (Map.Entry<String, String> entry : parametersAndValues.entrySet()) {
//         String key = entry.getKey();
//         Assert.assertTrue(checkFile(gradle, "$" + key, "${" + key + "}"));
//         Assert.assertTrue(checkFile(sampleFile, "$" + key, "${" + key + "}"));
//         Assert.assertTrue(checkFile(testSampleFile, "$" + key, "${" + key + "}"));
//      }
//
//      Assert.assertTrue(Files.exists(outputFolder.resolve("should_not_be_deleted")));
//   }
//
//   @After
//   public void cleanUp() throws IOException {
//      TemplateGenerator.deleteRecursive(outputFolder, false);
//   }
//
//   public static boolean checkFile(Path file, String... undesirableStrings) throws IOException {
//      for (String line : Files.readAllLines(file)) {
//         for (String undesirableString : undesirableStrings) {
//            if (line.contains(undesirableString)) {
//               return false;
//            }
//         }
//      }
//      return true;
//   }
}
