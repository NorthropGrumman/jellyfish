package com.ngc.seaside.jellyfish.cli.command.createjellyfishcommand;

import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class CreateJellyFishCommandTest {

   private CreateJellyFishCommand cmd = new CreateJellyFishCommand();

   private Path outputDir;

   @Before
   public void setup() throws IOException {
      outputDir = Files.createTempDirectory(null);
   }

   @Test
   public void testCommand() throws IOException {
      testCommand(null, "commandName", "test-command");
   }

   @Test
   public void testCommandWithGroup() throws IOException {
      testCommand(null, "commandName", "test-command-2", "groupId", "com.ngc.test");
   }

   @Test
   public void testCommandWithArtifact() throws IOException {
      testCommand(null, "commandName", "test-command-3", "groupId", "com.ngc.test", "artifactId", "test.artifact.id");
   }

   @Test
   public void testCommandWithoutCommandName() throws IOException {
      final String name = "test-command-4";
      IPromptUserService mockPromptService = Mockito.mock(IPromptUserService.class);
      Mockito.when(mockPromptService.prompt(Mockito.eq("commandName"), Mockito.any(), Mockito.any())).thenReturn(name);
      testCommand(name);
   }

   private void testCommand(String commandName, String... keyValues) throws IOException {
      IJellyFishCommandOptions mockOptions = Mockito.mock(IJellyFishCommandOptions.class);
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n < keyValues.length; n += 2) {
         DefaultParameter param = new DefaultParameter(keyValues[n], false);
         param.setValue(keyValues[n + 1]);
         collection.addParameter(param);
      }

      DefaultParameter outputDirectory = new DefaultParameter("outputDirectory", false);
      outputDirectory.setValue(outputDir.toString());
      collection.addParameter(outputDirectory);

      Mockito.when(mockOptions.getParameters()).thenReturn(collection);

      cmd.run(mockOptions);

      if (commandName == null) {
         commandName = Optional.ofNullable(collection.getParameter("commandName")).map(IParameter::getValue).orElse(null);
      }
      String groupId = Optional.ofNullable(collection.getParameter("groupId")).map(IParameter::getValue).orElse(null);
      String artifactId = Optional.ofNullable(collection.getParameter("groupId")).map(IParameter::getValue).orElse(null);

      checkCommandOutput(commandName, outputDir, groupId, artifactId);
   }

   private static void checkCommandOutput(String commandName, Path outputDir, String groupId, String artifactId) throws IOException {
      if (groupId == null) {
         groupId = CreateJellyFishCommand.DEFAULT_GROUP_ID;
      }
      if (artifactId == null) {
         artifactId = String.format(CreateJellyFishCommand.DEFAULT_ARTIFACT_ID_FORMAT, commandName.replace("-", ""));
      }

      StringBuilder commandNameJavaBuilder = new StringBuilder();
      StringBuilder commandNamePackageBuilder = new StringBuilder();
      for (int n = 0; n < commandName.length(); n++) {
         if (commandName.charAt(n) == '-') {
            if (n + 1 < commandName.length()) {
               commandNameJavaBuilder.append(Character.toUpperCase(commandName.charAt(n + 1)));
               commandNamePackageBuilder.append(commandName.charAt(n + 1));
               n++;
            }
         } else {
            commandNameJavaBuilder.append(commandName.charAt(n));
            commandNamePackageBuilder.append(commandName.charAt(n));
         }
      }

      final String commandNameJava = commandNameJavaBuilder.toString();
      final String commandNamePackage = commandNamePackageBuilder.toString();

      Assert.assertTrue(
         outputDir.resolve(Paths.get("src", "main", "java", groupId.replace('.', File.separatorChar), artifactId.replace('.', File.separatorChar), commandNameJava + ".java")).toFile().exists());
      Assert.assertTrue(outputDir.resolve(Paths.get("src", "main", "resources")).toFile().exists());
      Assert.assertTrue(outputDir.resolve(Paths.get("src", "test", "java")).toFile().exists());
      Assert.assertTrue(outputDir.resolve(Paths.get("build.gradle")).toFile().exists());
   }

   @After
   public void cleanup() throws IOException {
      FileUtils.deleteQuietly(outputDir.toFile());
   }

}
