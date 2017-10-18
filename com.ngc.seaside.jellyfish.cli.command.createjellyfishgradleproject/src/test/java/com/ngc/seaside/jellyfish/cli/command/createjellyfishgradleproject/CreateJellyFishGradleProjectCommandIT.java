package com.ngc.seaside.jellyfish.cli.command.createjellyfishgradleproject;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.template.MockedTemplateService;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;

public class CreateJellyFishGradleProjectCommandIT {

   private static final String DEFAULT_GROUP = CreateJellyFishGradleProjectCommand.DEFAULT_GROUP_ID;

   private CreateJellyFishGradleProjectCommand cmd = new CreateJellyFishGradleProjectCommand();
   private PrintStreamLogService logger = new PrintStreamLogService();
   private IPromptUserService mockPromptService = Mockito.mock(IPromptUserService.class);
   private ITemplateService mockTemplateService = new MockedTemplateService()
         .useRealPropertyService()
         .useDefaultUserValues(true)
         .setTemplateDirectory(CreateJellyFishGradleProjectCommandIT.class.getPackage().getName(),
                               Paths.get("src/main/template"));


   private Path outputDir;

   @Before
   public void setup() throws IOException {
      outputDir = Files.createTempDirectory(null);
      cmd.setLogService(logger);
      cmd.setPromptService(mockPromptService);
      cmd.setTemplateService(mockTemplateService);
   }

   @Test
   public void testCommand() throws IOException {
      final String projectName = "test-project-1";
      final String version = "1.0";
      final String sdgave = "com.ngc.seasid.system1:system.descriptor:1.0-SNAPSHOT@zip";
      final String model = "com.ngc.seaside.Model1";

      runCommand(CreateJellyFishGradleProjectCommand.PROJECT_NAME_PROPERTY, projectName,
                 CreateJellyFishGradleProjectCommand.VERSION_PROPERTY, version,
                 CreateJellyFishGradleProjectCommand.SYSTEM_DESCRIPTOR_GAVE_PROPERTY, sdgave,
                 CreateJellyFishGradleProjectCommand.MODEL_NAME_PROPERTY, model);
      checkCommandOutput(projectName, DEFAULT_GROUP, version, sdgave, model);
   }

   @Test
   public void testCommandWithGroup() throws IOException {
      final String projectName = "test-project-2";
      final String version = "1.0";
      final String group = "com.ngc.test";
      final String sdgave = "com.ngc.seasid.system2:system.descriptor:1.0-SNAPSHOT@zip";
      final String model = "com.ngc.seaside.Model2";

      runCommand(CreateJellyFishGradleProjectCommand.PROJECT_NAME_PROPERTY, projectName,
                 CreateJellyFishGradleProjectCommand.GROUP_ID_PROPERTY, group,
                 CreateJellyFishGradleProjectCommand.VERSION_PROPERTY, version,
                 CreateJellyFishGradleProjectCommand.SYSTEM_DESCRIPTOR_GAVE_PROPERTY, sdgave,
                 CreateJellyFishGradleProjectCommand.MODEL_NAME_PROPERTY, model);
      checkCommandOutput(projectName, group, version, sdgave, model);
   }

   @Test(expected = Exception.class)
   public void testCommandWithoutSettings() throws IOException {
      final String projectName = "test-project-3";

      runCommand(CreateJellyFishGradleProjectCommand.PROJECT_NAME_PROPERTY, projectName);
   }

   @Test
   public void testWithoutClean() throws IOException {
      final String projectNameA = "test-project-4a";
      final String projectNameB = "test-project-4b";
      final String version = "1.0";
      final String sdgave = "com.ngc.seasid.system4:system.descriptor:1.0-SNAPSHOT@zip";
      final String model = "com.ngc.seaside.Model4";

      runCommand(CreateJellyFishGradleProjectCommand.PROJECT_NAME_PROPERTY, projectNameA,
                 CreateJellyFishGradleProjectCommand.VERSION_PROPERTY, version,
                 CreateJellyFishGradleProjectCommand.CLEAN_PROPERTY, "false",
                 CreateJellyFishGradleProjectCommand.SYSTEM_DESCRIPTOR_GAVE_PROPERTY, sdgave,
                 CreateJellyFishGradleProjectCommand.MODEL_NAME_PROPERTY, model);

      runCommand(CreateJellyFishGradleProjectCommand.PROJECT_NAME_PROPERTY, projectNameB,
                 CreateJellyFishGradleProjectCommand.VERSION_PROPERTY, version,
                 CreateJellyFishGradleProjectCommand.CLEAN_PROPERTY, "false",
                 CreateJellyFishGradleProjectCommand.SYSTEM_DESCRIPTOR_GAVE_PROPERTY, sdgave,
                 CreateJellyFishGradleProjectCommand.MODEL_NAME_PROPERTY, model);

      checkCommandOutput(projectNameA, DEFAULT_GROUP, version, sdgave, model);
      checkCommandOutput(projectNameB, DEFAULT_GROUP, version, sdgave, model);
   }

   @Test
   public void testWithClean() throws IOException {
      final String projectNameA = "test-project-5a";
      final String projectNameB = "test-project-5b";
      final String version = "1.0";
      final String sdgave = "com.ngc.seasid.system5:system.descriptor:1.0-SNAPSHOT@zip";
      final String model = "com.ngc.seaside.Model5";

      runCommand(CreateJellyFishGradleProjectCommand.PROJECT_NAME_PROPERTY, projectNameA,
                 CreateJellyFishGradleProjectCommand.VERSION_PROPERTY, version,
                 CreateJellyFishGradleProjectCommand.CLEAN_PROPERTY, "true",
                 CreateJellyFishGradleProjectCommand.SYSTEM_DESCRIPTOR_GAVE_PROPERTY, sdgave,
                 CreateJellyFishGradleProjectCommand.MODEL_NAME_PROPERTY, model);

      runCommand(CreateJellyFishGradleProjectCommand.PROJECT_NAME_PROPERTY, projectNameB,
                 CreateJellyFishGradleProjectCommand.VERSION_PROPERTY, version,
                 CreateJellyFishGradleProjectCommand.CLEAN_PROPERTY, "true",
                 CreateJellyFishGradleProjectCommand.SYSTEM_DESCRIPTOR_GAVE_PROPERTY, sdgave,
                 CreateJellyFishGradleProjectCommand.MODEL_NAME_PROPERTY, model);

      try {
         checkCommandOutput(projectNameA, DEFAULT_GROUP, version, sdgave, model);
         Assert.fail("file was not cleaned");
      } catch (AssertionError a) {
         // expected case
      }

      checkCommandOutput(projectNameB, DEFAULT_GROUP, version, sdgave, model);
   }

   private void runCommand(String... keyValues) throws IOException {
      IJellyFishCommandOptions mockOptions = Mockito.mock(IJellyFishCommandOptions.class);
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter<String>(keyValues[n]).setValue(keyValues[n + 1]));
      }

      DefaultParameter<String> outputDirectory = new DefaultParameter<>(
            CreateJellyFishGradleProjectCommand.OUTPUT_DIR_PROPERTY,
            outputDir.toString());
      collection.addParameter(outputDirectory);

      Mockito.when(mockOptions.getParameters()).thenReturn(collection);

      cmd.run(mockOptions);
   }

   private void checkCommandOutput(String expectedProjectName, String expectedGroupId, String expectedVersion,
		   String expectedGaveId, String expectedModelName) throws IOException {
      // Check project directory
      Assert.assertTrue("project directory not created", Files.isDirectory(outputDir.resolve(expectedProjectName)));

      // Check gradle files existence
      Assert.assertTrue("gradlew was not found",
                        outputDir.resolve(Paths.get(expectedProjectName, "gradlew")).toFile().exists());
      Assert.assertTrue("gradlew.bat was not found",
                        outputDir.resolve(Paths.get(expectedProjectName, "gradlew.bat")).toFile().exists());
      Assert.assertTrue("build.gradle was not found",
                        outputDir.resolve(Paths.get(expectedProjectName, "build.gradle")).toFile().exists());
      Assert.assertTrue("settings.gradle was not found",
                        outputDir.resolve(Paths.get(expectedProjectName, "settings.gradle")).toFile().exists());

      // Check gradle wrapper files existence
      Assert.assertTrue("gradle-wrapper.jar not found",
                        outputDir.resolve(Paths.get(expectedProjectName, "gradle", "wrapper", "gradle-wrapper.jar"))
                              .toFile()
                              .exists());
      Assert.assertTrue(
            "gradle-wrapper.properties not found",
            outputDir.resolve(Paths.get(expectedProjectName, "gradle", "wrapper", "gradle-wrapper.properties"))
                  .toFile()
                  .exists());

      // Check build.gradle file content
      Path buildFilePath = outputDir.resolve(Paths.get(expectedProjectName, "build.gradle"));
      List<String> buildFileContent = Files.readAllLines(buildFilePath);

      String versionStringToMatch = "version = '" + expectedVersion + "'";
      boolean versionMatch = buildFileContent.stream().anyMatch(line -> line.contains(versionStringToMatch));
      Assert.assertTrue("build.gradle version is incorrect", versionMatch);

      String groupStringToMatch = "group = '" + expectedGroupId + "'";
      boolean groupMatch = buildFileContent.stream().anyMatch(line -> line.contains(groupStringToMatch));
      Assert.assertTrue("build.gradle group is incorrect", groupMatch);

      Matcher matcher = CreateJellyFishGradleProjectCommand.GAVE_REGEX.matcher(expectedGaveId);
      matcher.matches();
      String artifactStringToMatch = "systemDescriptorProjectName = '" + matcher.group(2) + "'";
      boolean artifactNameMatch = buildFileContent.stream().anyMatch(line -> line.contains(artifactStringToMatch));
      Assert.assertTrue("build.gradle system descriptor project name is incorrect", artifactNameMatch);
      
      String SDversionStringToMatch = "systemDescriptorProjectVersion = '" + matcher.group(3) + "'";
      boolean SDversionMatch = buildFileContent.stream().anyMatch(line -> line.contains(SDversionStringToMatch));
      Assert.assertTrue("build.gradle system descriptor version is incorrect", SDversionMatch);

      String modelStringToMatch = "modelName = '" + expectedModelName + "'";
      boolean modelStringMatch = buildFileContent.stream().anyMatch(line -> line.contains(modelStringToMatch));
      Assert.assertTrue("build.gradle model name is incorrect", modelStringMatch);

      String depStringToMatch = "generate \"" + expectedGaveId + "\"";
      boolean depStringMatch = buildFileContent.stream().anyMatch(line -> line.contains(depStringToMatch));
      Assert.assertTrue("build.gradle generate dependencies is incorrect", depStringMatch);

      // Check settings.gradle content
      Path settingsFilePath = outputDir.resolve(Paths.get(expectedProjectName, "settings.gradle"));
      List<String> settingsFileContent = Files.readAllLines(settingsFilePath);
      String projectNameToMatch = "rootProject.name = '" + expectedProjectName + "'";
      boolean projectNameMatch = settingsFileContent.stream().anyMatch(line -> line.contains(projectNameToMatch));
      Assert.assertTrue("settings.gradle root project name is incorrect", projectNameMatch);
   }

   @After
   public void cleanup() throws IOException {
      FileUtils.deleteQuietly(outputDir.toFile());
   }
}
