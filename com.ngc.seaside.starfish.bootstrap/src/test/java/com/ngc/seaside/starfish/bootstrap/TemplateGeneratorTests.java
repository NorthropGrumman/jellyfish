package com.ngc.seaside.starfish.bootstrap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class TemplateGeneratorTests
{

   private Path templateFolder;
   private Path outputFolder;
   private Map<String, String> parametersAndValues;

   private static final String GROUP_ID = "com.ngc.seaside.starfish";
   private static final String ARTIFACT_ID = "sample";
   private static final String VERSION = "1.0-SNAPSHOT";

   @Before
   public void setup() throws URISyntaxException, IOException
   {
      templateFolder = Paths.get(getClass().getClassLoader().getResource("TemplateExample").toURI());
      outputFolder = Files.createTempDirectory(null);
      parametersAndValues = new HashMap<>();
      parametersAndValues.put("groupId", GROUP_ID);
      parametersAndValues.put("artifactId", ARTIFACT_ID);
      parametersAndValues.put("version", VERSION);
   }

   @Test
   public void testGenerator() throws IOException
   {
      TemplateGenerator gen = new TemplateGenerator(parametersAndValues, outputFolder, false);
      Files.walkFileTree(templateFolder, gen);
      Path temp;

      Path base = outputFolder.resolve(GROUP_ID);
      Assert.assertTrue(Files.isDirectory(base));

      Path mainJavaFolder = base.resolve(Paths.get("src", "main", "java"));
      Assert.assertTrue(Files.isDirectory(mainJavaFolder));

      Path groupFolder = mainJavaFolder.resolve(GROUP_ID.replace(".", FileSystems.getDefault().getSeparator()));
      Assert.assertTrue(Files.isDirectory(groupFolder));

      Path artifactFolder = groupFolder.resolve(ARTIFACT_ID);
      Assert.assertTrue(Files.isDirectory(artifactFolder));

      Path sampleFile = artifactFolder.resolve("Sample.java");
      Assert.assertTrue(Files.isRegularFile(sampleFile));

      Path testJavaFolder = base.resolve(Paths.get("src", "test", "java"));
      Assert.assertTrue(Files.isDirectory(mainJavaFolder));

      Path testGroupFolder = testJavaFolder.resolve(GROUP_ID.replace(".", FileSystems.getDefault().getSeparator()));
      Assert.assertTrue(Files.isDirectory(testGroupFolder));

      Path testArtifactFolder = testGroupFolder.resolve(ARTIFACT_ID);
      Assert.assertTrue(Files.isDirectory(testArtifactFolder));

      Path testSampleFile = testArtifactFolder.resolve("SampleTest.java");
      Assert.assertTrue(Files.isRegularFile(testSampleFile));
   }

   @After
   public void cleanUp() throws IOException
   {
      try {
         Files.walkFileTree(outputFolder, new SimpleFileVisitor<Path>()
         {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
            {
               Files.delete(file);
               return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
            {
               Files.delete(dir);
               return FileVisitResult.CONTINUE;
            }

         });
      }
      catch (IOException e) {
         System.err.println("Failed to clean up temporary test folder");
      }
   }

}
