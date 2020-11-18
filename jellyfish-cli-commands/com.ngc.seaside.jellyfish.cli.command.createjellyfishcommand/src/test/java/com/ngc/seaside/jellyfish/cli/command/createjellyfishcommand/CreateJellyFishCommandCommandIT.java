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
package com.ngc.seaside.jellyfish.cli.command.createjellyfishcommand;

import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedTemplateService;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;

public class CreateJellyFishCommandCommandIT {

   private CreateJellyFishCommandCommand cmd = new CreateJellyFishCommandCommand();

   private Path outputDir;

   @Before
   public void setup() throws IOException {
      MockedTemplateService mockedTemplateService = new MockedTemplateService().useRealPropertyService()
            .useDefaultUserValues(true)
            .setTemplateDirectory(
                  CreateJellyFishCommandCommand.class.getPackage().getName(),
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
      ICommandOptions mockOptions = Mockito.mock(ICommandOptions.class);
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
