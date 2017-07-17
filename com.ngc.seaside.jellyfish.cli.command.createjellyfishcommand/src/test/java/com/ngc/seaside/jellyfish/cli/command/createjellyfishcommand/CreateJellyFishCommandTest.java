package com.ngc.seaside.jellyfish.cli.command.createjellyfishcommand;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
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

   private PrintStreamLogService logger = new PrintStreamLogService();

   private IPromptUserService mockPromptService = Mockito.mock(IPromptUserService.class);

   private Path outputDir;

   @Before
   public void setup() throws IOException {
      outputDir = Files.createTempDirectory(null);
      cmd.setLogService(logger);
      cmd.setPromptService(mockPromptService);
   }

   private void createSettings() throws IOException {
      Files.createFile(outputDir.resolve("settings.gradle"));
   }
   
   @Test
   public void testCommand() throws IOException {
      createSettings();
      testCommand(null, CreateJellyFishCommand.COMMAND_NAME_PROPERTY, "test-command");
   }

   @Test
   public void testCommandWithGroup() throws IOException {
      createSettings();
      testCommand(null, CreateJellyFishCommand.COMMAND_NAME_PROPERTY, "test-command-2", "groupId", "com.ngc.test");
   }

   @Test
   public void testCommandWithArtifact() throws IOException {
      createSettings();
      testCommand(null, CreateJellyFishCommand.COMMAND_NAME_PROPERTY, "test-command-3", "groupId", "com.ngc.test", "artifactId", "test.artifact.id");
   }

   @Test
   public void testCommandWithoutCommandName() throws IOException {
      final String name = "test-command-4";
      Mockito.when(mockPromptService.prompt(Mockito.eq(CreateJellyFishCommand.COMMAND_NAME_PROPERTY), Mockito.any(), Mockito.any())).thenReturn(name);

      createSettings();
      testCommand(name);
   }
   
   @Test(expected = Exception.class)
   public void testCommandWithoutSettings() throws IOException {
      testCommand(null, CreateJellyFishCommand.COMMAND_NAME_PROPERTY, "test-command-5");
   }

   private void testCommand(String commandName, String... keyValues) throws IOException {
      IJellyFishCommandOptions mockOptions = Mockito.mock(IJellyFishCommandOptions.class);
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter(keyValues[n]).setValue(keyValues[n + 1]));
      }

      DefaultParameter outputDirectory = new DefaultParameter(CreateJellyFishCommand.OUTPUT_DIR_PROPERTY).setValue(outputDir.toString());
      collection.addParameter(outputDirectory);

      Mockito.when(mockOptions.getParameters()).thenReturn(collection);

      cmd.run(mockOptions);

      if (commandName == null) {
         commandName = Optional.ofNullable(collection.getParameter(CreateJellyFishCommand.COMMAND_NAME_PROPERTY)).map(IParameter::getValue).orElse(null);
      }
      String groupId = Optional.ofNullable(collection.getParameter(CreateJellyFishCommand.GROUP_ID_PROPERTY)).map(IParameter::getValue).orElse(null);
      String artifactId = Optional.ofNullable(collection.getParameter(CreateJellyFishCommand.ARTIFACT_ID_PROPERTY)).map(IParameter::getValue).orElse(null);

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
      for (int n = 0; n < commandName.length(); n++) {
         if (commandName.charAt(n) == '-') {
            if (n + 1 < commandName.length()) {
               commandNameJavaBuilder.append(Character.toUpperCase(commandName.charAt(n + 1)));
               n++;
            }
         } else {
            commandNameJavaBuilder.append(commandName.charAt(n));
         }
      }

      final String commandNameJava = commandNameJavaBuilder.toString();

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
