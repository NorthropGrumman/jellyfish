package com.ngc.seaside.bootstrap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Permission;
import java.util.HashMap;
import java.util.Map;

/**
 * Test for the Main class
 */
public class MainTests {
   private static final String GROUP_ID = "com.ngc.seaside";
   private static final String ARTIFACT_ID = "sample";
   private static final String VERSION = "1.0-SNAPSHOT";

   private static PrintStream SYSTEM_OUT;
   private static InputStream SYSTEM_IN;
   private Path outputFolder;
   private Path templateFile;

   @BeforeClass
   public static void setupClass() {
//      System.setSecurityManager(new NoExitSecurityManager(System.getSecurityManager()));
//      SYSTEM_OUT = System.out;
//      SYSTEM_IN = System.in;
   }

   @Before
   public void setup() throws Exception {
//      templateFile =
//               Paths.get(getClass().getClassLoader().getResource("templates/example/TemplateExample.zip").toURI())
//                        .toAbsolutePath();
//      outputFolder = Files.createTempDirectory(null).toAbsolutePath();
//      final String input = GROUP_ID + "\n" + ARTIFACT_ID + "\n" + VERSION + "\n";
//      InputStream in = new ByteArrayInputStream(input.getBytes());
//      System.setIn(in);
//      System.setOut(new PrintStream(new ByteArrayOutputStream()));
//      Field f = CommandLine.class.getDeclaredField("sc");
//      f.setAccessible(true);
//      f.set(null, null);
   }

   @Test
   public void mainTest() throws IOException {
//      Map<String, String> parametersAndValues = new HashMap<>();
//      parametersAndValues.put("groupId", GROUP_ID);
//      parametersAndValues.put("artifactId", ARTIFACT_ID);
//      parametersAndValues.put("version", VERSION);
//
//      try {
//         Main.main(new String[] { templateFile.toString(), "-o", outputFolder.toString() });
//      } catch (SecurityException e) {
//         Assert.assertEquals("exitVM.0", e.getMessage());
//      }
//
//      Path base = outputFolder.resolve(ARTIFACT_ID);
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
//         Assert.assertTrue(TemplateGeneratorTests.checkFile(gradle, "$" + key, "${" + key + "}"));
//         Assert.assertTrue(TemplateGeneratorTests.checkFile(sampleFile, "$" + key, "${" + key + "}"));
//         Assert.assertTrue(TemplateGeneratorTests.checkFile(testSampleFile, "$" + key, "${" + key + "}"));
//      }
   }

   @After
   public void cleanUp() throws IOException {
//      try {
//         TemplateGenerator.deleteRecursive(outputFolder, false);
//      } catch (Exception e) {
//         System.err.println("Unable to clean up temporary directory " + outputFolder + ": " + e.getMessage());
//      }
   }

   @AfterClass
   public static void cleanUpClass() {
//      System.setSecurityManager(((NoExitSecurityManager) System.getSecurityManager()).baseSecurityManager);
//      System.setOut(SYSTEM_OUT);
//      System.setIn(SYSTEM_IN);
   }

   private static class NoExitSecurityManager extends SecurityManager {

      private SecurityManager baseSecurityManager;

      private NoExitSecurityManager(SecurityManager baseSecurityManager) {
         this.baseSecurityManager = baseSecurityManager;
      }

      @Override
      public void checkPermission(Permission permission) {
         if (permission.getName().startsWith("exitVM")) {
            throw new SecurityException(permission.getName());
         }
         if (baseSecurityManager != null) {
            baseSecurityManager.checkPermission(permission);
         }
      }
   }
}
