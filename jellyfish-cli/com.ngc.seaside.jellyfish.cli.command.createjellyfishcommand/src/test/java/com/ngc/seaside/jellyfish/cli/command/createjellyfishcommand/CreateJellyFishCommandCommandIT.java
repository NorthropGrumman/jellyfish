package com.ngc.seaside.jellyfish.cli.command.createjellyfishcommand;

import static org.mockito.Mockito.mock;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedTemplateService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateJellyFishCommandCommandIT {

   private CreateJellyFishCommandCommand cmd = new CreateJellyFishCommandCommand();

   private Path outputDir;

   @Before
   public void setup() throws IOException {
      MockedTemplateService mockedTemplateService = new MockedTemplateService().useRealPropertyService()
                                                                               .useDefaultUserValues(true)
                                                                               .setTemplateDirectory(
                                                                                  CreateJellyFishCommandCommand.class.getPackage()
                                                                                                                     .getName(),
                                                                                  Paths.get("src/main/template"));

      outputDir = Files.createTempDirectory(null);
      outputDir.toFile().deleteOnExit();
      cmd.setLogService(mock(ILogService.class));
      cmd.setTemplateService(mockedTemplateService);
   }

   @Test
   public void testCommand() throws IOException {
      createSettings();

      final String command = "test-command-1";
      final String group = CreateJellyFishCommandCommand.DEFAULT_GROUP_ID;
      final String artifact = String.format(CreateJellyFishCommandCommand.DEFAULT_ARTIFACT_ID_FORMAT,
         command.replace("-", "").toLowerCase());
      final String pkg = group + '.' + artifact;
      final String classname = "TestCommand1Command";
      runCommand(CreateJellyFishCommandCommand.COMMAND_NAME_PROPERTY, command);
      checkCommandOutput(classname, group, artifact, pkg);
   }

   private void runCommand(String... keyValues) throws IOException {
      IJellyFishCommandOptions mockOptions = Mockito.mock(IJellyFishCommandOptions.class);
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter<>(keyValues[n], keyValues[n + 1]));
      }

      DefaultParameter<String> outputDirectory = new DefaultParameter<>(
         CreateJellyFishCommandCommand.OUTPUT_DIR_PROPERTY,
         outputDir.toString());
      collection.addParameter(outputDirectory);

      Mockito.when(mockOptions.getParameters()).thenReturn(collection);

      cmd.run(mockOptions);

   }

   private void createSettings() throws IOException {
      try {
         Files.createFile(outputDir.resolve("settings.gradle"));
      } catch (FileAlreadyExistsException e) {
         // ignore
      }
   }

   private void checkCommandOutput(String expectedClassname, String expectedGroupId, String expectedArtifactId,
            String expectedPackage)
      throws IOException {
      String projectName = expectedGroupId + '.' + expectedArtifactId;
      if (expectedPackage == null) {
         expectedPackage = projectName;
      }
      expectedPackage = expectedPackage.replace('.', File.separatorChar);

      Assert.assertTrue("settings.gradle not updated", Files.readAllLines(outputDir.resolve("settings.gradle"))
                                                            .stream()
                                                            .anyMatch(line -> line.contains(projectName)));
      Path expectedPath = Paths.get(projectName, "src", "main", "java", expectedPackage, expectedClassname + ".java");
      Assert.assertTrue("command was not created: " + expectedPath, outputDir.resolve(expectedPath).toFile().exists());
      Path actualPath = outputDir.resolve(expectedPath).toRealPath();
      Assert.assertEquals("Filename was not capitalized correctly",
         outputDir.toRealPath().resolve(expectedPath).toString(),
         actualPath.toString());
      Assert.assertTrue("resources folder was not created",
         outputDir.resolve(Paths.get(projectName, "src", "main", "resources")).toFile().exists());
      Assert.assertTrue("test folder was not created",
         outputDir.resolve(Paths.get(projectName, "src", "test", "java")).toFile().exists());
      Assert.assertTrue("build.gradle was not created",
         outputDir.resolve(Paths.get(projectName, "build.gradle")).toFile().exists());
   }

}
