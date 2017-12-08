package com.ngc.seaside.jellyfish.cli.command.createjellyfishgradleproject;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
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

public class CreateJellyFishGradleProjectCommandIT {

   private static final String DEFAULT_GROUP = CreateJellyFishGradleProjectCommand.DEFAULT_GROUP_ID;

   private CreateJellyFishGradleProjectCommand cmd = new CreateJellyFishGradleProjectCommand();
   private PrintStreamLogService logger = new PrintStreamLogService();
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
      cmd.setTemplateService(mockTemplateService);
   }

   @Test
   public void testCommand() throws IOException {
      final String projectName = "test-project-1";
      final String version = "1.0";
      final String sdgav = "com.ngc.seasid.system1:system.descriptor:1.0-SNAPSHOT";
      final String model = "com.ngc.seaside.Model1";

      runCommand(CreateJellyFishGradleProjectCommand.PROJECT_NAME_PROPERTY, projectName,
                 CreateJellyFishGradleProjectCommand.VERSION_PROPERTY, version,
                 CreateJellyFishGradleProjectCommand.SYSTEM_DESCRIPTOR_GAV_PROPERTY, sdgav,
                 CreateJellyFishGradleProjectCommand.MODEL_NAME_PROPERTY, model);
      checkCommandOutput(projectName, DEFAULT_GROUP, version, sdgav, model);
      checkJellyfishGradlePluginsVersion(projectName, "1.2.3"); // 1.2.3 is loaded from test/resources/properties file.
   }

   @Test
   public void testCommandWithJellyfishGradlePluginsVersion() throws Throwable {
      final String projectName = "test-project-1";
      final String version = "1.0";
      final String sdgav = "com.ngc.seasid.system1:system.descriptor:1.0-SNAPSHOT";
      final String model = "com.ngc.seaside.Model1";
      final String jfPluginsVersion = "1.6.0";

      runCommand(CreateJellyFishGradleProjectCommand.PROJECT_NAME_PROPERTY, projectName,
                 CreateJellyFishGradleProjectCommand.VERSION_PROPERTY, version,
                 CreateJellyFishGradleProjectCommand.SYSTEM_DESCRIPTOR_GAV_PROPERTY, sdgav,
                 CreateJellyFishGradleProjectCommand.MODEL_NAME_PROPERTY, model,
                 CreateJellyFishGradleProjectCommand.JELLYFISH_GRADLE_PLUGINS_VERSION_PROPERTY, jfPluginsVersion);
      checkCommandOutput(projectName, DEFAULT_GROUP, version, sdgav, model);
      checkJellyfishGradlePluginsVersion(projectName, jfPluginsVersion);
   }

   @After
   public void cleanup() throws IOException {
      FileUtils.deleteQuietly(outputDir.toFile());
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
                                   String expectedGavId, String expectedModelName) throws IOException {
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

      String artifactStringToMatch = "project = '" + expectedGavId + "'";
      boolean artifactNameMatch = buildFileContent.stream().anyMatch(line -> line.contains(artifactStringToMatch));
      Assert.assertTrue("build.gradle system descriptor project name is incorrect", artifactNameMatch);

      String modelStringToMatch = "model = '" + expectedModelName + "'";
      boolean modelStringMatch = buildFileContent.stream().anyMatch(line -> line.contains(modelStringToMatch));
      Assert.assertTrue("build.gradle model name is incorrect", modelStringMatch);

      // Check settings.gradle content
      Path settingsFilePath = outputDir.resolve(Paths.get(expectedProjectName, "settings.gradle"));
      List<String> settingsFileContent = Files.readAllLines(settingsFilePath);
      String projectNameToMatch = "rootProject.name = '" + expectedProjectName + "'";
      boolean projectNameMatch = settingsFileContent.stream().anyMatch(line -> line.contains(projectNameToMatch));
      Assert.assertTrue("settings.gradle root project name is incorrect", projectNameMatch);
   }

   private void checkJellyfishGradlePluginsVersion(String expectedProjectName, String pluginsVersion)
         throws IOException {
      Path buildFilePath = outputDir.resolve(Paths.get(expectedProjectName, "build.gradle"));
      List<String> buildFileContent = Files.readAllLines(buildFilePath);
      String lineToMatch = String.format("classpath 'com.ngc.seaside:jellyfish.cli.gradle.plugins:%s'", pluginsVersion);
      Assert.assertTrue("did not use correct Jellyfish plugins version!",
                        buildFileContent.stream().anyMatch(l -> l.contains(lineToMatch)));
   }
}
