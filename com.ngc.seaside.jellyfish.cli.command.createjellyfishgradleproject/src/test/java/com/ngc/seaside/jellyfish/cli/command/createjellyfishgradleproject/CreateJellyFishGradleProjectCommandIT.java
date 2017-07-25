package com.ngc.seaside.jellyfish.cli.command.createjellyfishgradleproject;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.service.impl.parameterservice.ParameterServiceGuiceWrapper;
import com.ngc.seaside.bootstrap.service.impl.promptuserservice.PromptUserServiceGuiceWrapper;
import com.ngc.seaside.bootstrap.service.impl.propertyservice.PropertyServiceGuiceWrapper;
import com.ngc.seaside.bootstrap.service.impl.templateservice.TemplateServiceGuiceWrapper;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.property.api.IPropertyService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.junit.After;
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
import java.util.List;

public class CreateJellyFishGradleProjectCommandIT {

   private CreateJellyFishGradleProjectCommand cmd = new CreateJellyFishGradleProjectCommand();

   private PrintStreamLogService logger = new PrintStreamLogService();

   private IPromptUserService mockPromptService = Mockito.mock(IPromptUserService.class);

   private Path outputDir;

   @Before
   public void setup() throws IOException {
      outputDir = Files.createTempDirectory(null);
      cmd.setLogService(logger);
      cmd.setPromptService(mockPromptService);
      cmd.setTemplateService(injector.getInstance(ITemplateService.class));
   }

   @Test
   public void testCommand() throws IOException {
      final String projectSimpleName = "test-project-1";
      final String group             = CreateJellyFishGradleProjectCommand.DEFAULT_GROUP_ID;
      final String artifact          = String.format(CreateJellyFishGradleProjectCommand.DEFAULT_ARTIFACT_ID_FORMAT, projectSimpleName.replace("-", "").toLowerCase());
      final String projectName       = group + "." + artifact;  
      final String version           = "1.0";
      
      runCommand(CreateJellyFishGradleProjectCommand.PROJECT_NAME_PROPERTY, projectName,
		         CreateJellyFishGradleProjectCommand.VERSION_PROPERTY,      version);
      checkCommandOutput(projectName, group, artifact, version);
   }

//   @Test
//   public void testCommandWithGroup() throws IOException {
//	  final String projectSimpleName = "test-project-2";
//      final String group             = "com.ngc.test";
//      final String artifact          = String.format(CreateJellyFishGradleProjectCommand.DEFAULT_ARTIFACT_ID_FORMAT, projectSimpleName.replace("-", "").toLowerCase());
//      final String projectName       = group + '.' + artifact;
//      final String version           = "1.0";
//
//      runCommand(CreateJellyFishGradleProjectCommand.PROJECT_NAME_PROPERTY, projectName,
//		         CreateJellyFishGradleProjectCommand.GROUP_ID_PROPERTY,     group,
//		         CreateJellyFishGradleProjectCommand.VERSION_PROPERTY,      version);
//      checkCommandOutput(projectName, group, artifact, version);
//   }
//
//   @Test
//   public void testCommandWithArtifact() throws IOException {
//	  final String projectSimpleName = "test-project-2";
//      final String group             = "com.ngc.test";
//      final String artifact          = String.format(CreateJellyFishGradleProjectCommand.DEFAULT_ARTIFACT_ID_FORMAT, projectSimpleName.replace("-", "").toLowerCase());
//      final String projectName       = group + '.' + artifact;
//      final String version           = "1.0";
//
//      runCommand(CreateJellyFishGradleProjectCommand.PROJECT_NAME_PROPERTY, projectName,
//		         CreateJellyFishGradleProjectCommand.ARTIFACT_ID_PROPERTY,  artifact,
//		         CreateJellyFishGradleProjectCommand.VERSION_PROPERTY,      version);
//      checkCommandOutput(projectName, group, artifact, version);
//   }

//   @Test
//   public void testCommandPackage() throws IOException {
//      final String command     = "test-command-4";
//      final String group       = CreateJellyFishGradleProjectCommand.DEFAULT_GROUP_ID;
//      final String artifact    = String.format(CreateJellyFishGradleProjectCommand.DEFAULT_ARTIFACT_ID_FORMAT, command.replace("-", "").toLowerCase());
//      final String projectName = group + '.' + artifact;
//      final String version           = "1.0";
//
//      runCommand(CreateJellyFishGradleProjectCommand.COMMAND_NAME_PROPERTY, command, CreateJellyFishGradleProjectCommand.PACKAGE_PROPERTY, pkg);
//      checkCommandOutput(classname, group, artifact, pkg);
//   }
//
//   @Test
//   public void testCommandWithoutCommandName() throws IOException {
//      createSettings();
//
//      final String command = "test-command-5";
//      final String group = CreateJellyFishGradleProjectCommand.DEFAULT_GROUP_ID;
//      final String artifact = String.format(CreateJellyFishGradleProjectCommand.DEFAULT_ARTIFACT_ID_FORMAT,
//         command.replace("-", "").toLowerCase());
//      final String pkg = group + '.' + artifact;
//      final String classname = "TestCommand5Command";
//      Mockito.when(mockPromptService.prompt(Mockito.eq(CreateJellyFishGradleProjectCommand.COMMAND_NAME_PROPERTY),
//         Mockito.any(), Mockito.any())).thenReturn(command);
//      runCommand();
//      checkCommandOutput(classname, group, artifact, pkg);
//   }
//
//   @Test
//   public void testCommandClassname() throws IOException {
//      createSettings();
//
//      final String command = "test-command-6";
//      final String group = CreateJellyFishGradleProjectCommand.DEFAULT_GROUP_ID;
//      final String artifact = String.format(CreateJellyFishGradleProjectCommand.DEFAULT_ARTIFACT_ID_FORMAT,
//         command.replace("-", "").toLowerCase());
//      final String pkg = group + '.' + artifact;
//      final String classname = "TestName";
//      runCommand(CreateJellyFishGradleProjectCommand.COMMAND_NAME_PROPERTY, command,
//         CreateJellyFishGradleProjectCommand.CLASSNAME_PROPERTY, classname);
//      checkCommandOutput(classname, group, artifact, pkg);
//   }
//
//   @Test(expected = Exception.class)
//   public void testCommandWithoutSettings() throws IOException {
//      final String command = "test-command-7";
//      runCommand(CreateJellyFishGradleProjectCommand.COMMAND_NAME_PROPERTY, command);
//   }
//
//   @Test
//   public void testMultipleSubprojects() throws IOException {
//      createSettings();
//
//      final String command1 = "test-command-8";
//      final String group1 = CreateJellyFishGradleProjectCommand.DEFAULT_GROUP_ID;
//      final String artifact1 = String.format(CreateJellyFishGradleProjectCommand.DEFAULT_ARTIFACT_ID_FORMAT,
//         command1.replace("-", "").toLowerCase());
//      final String pkg1 = group1 + '.' + artifact1;
//
//      final String command2 = "test-command-9";
//      final String group2 = CreateJellyFishGradleProjectCommand.DEFAULT_GROUP_ID;
//      final String artifact2 = String.format(CreateJellyFishGradleProjectCommand.DEFAULT_ARTIFACT_ID_FORMAT,
//         command2.replace("-", "").toLowerCase());
//      final String pkg2 = group2 + '.' + artifact2;
//
//      runCommand(CreateJellyFishGradleProjectCommand.COMMAND_NAME_PROPERTY, command1);
//      runCommand(CreateJellyFishGradleProjectCommand.COMMAND_NAME_PROPERTY, command2);
//
//      Assert.assertTrue(
//         Files.readAllLines(outputDir.resolve("settings.gradle")).stream().anyMatch(line -> line.contains(pkg1)));
//      Assert.assertTrue(
//         Files.readAllLines(outputDir.resolve("settings.gradle")).stream().anyMatch(line -> line.contains(pkg2)));
//
//   }
//
//   @Test
//   public void testWithoutClean() throws IOException {
//      createSettings();
//
//      final String command1 = "test-command-8";
//      final String group1 = CreateJellyFishGradleProjectCommand.DEFAULT_GROUP_ID;
//      final String artifact1 = String.format(CreateJellyFishGradleProjectCommand.DEFAULT_ARTIFACT_ID_FORMAT,
//         command1.replace("-", "").toLowerCase());
//      final String pkg1 = group1 + '.' + artifact1;
//      final String classname1 = "Test1";
//      final String classname2 = "Test2";
//      runCommand(CreateJellyFishGradleProjectCommand.COMMAND_NAME_PROPERTY, command1,
//         CreateJellyFishGradleProjectCommand.CLASSNAME_PROPERTY, classname1, CreateJellyFishGradleProjectCommand.CLEAN_PROPERTY,
//         "false");
//      runCommand(CreateJellyFishGradleProjectCommand.COMMAND_NAME_PROPERTY, command1,
//         CreateJellyFishGradleProjectCommand.CLASSNAME_PROPERTY, classname2, CreateJellyFishGradleProjectCommand.CLEAN_PROPERTY,
//         "false");
//      checkCommandOutput(classname1, group1, artifact1, pkg1);
//      checkCommandOutput(classname2, group1, artifact1, pkg1);
//   }
//
//   @Test
//   public void testWithClean() throws IOException {
//      createSettings();
//
//      final String command1 = "test-command-8";
//      final String group1 = CreateJellyFishGradleProjectCommand.DEFAULT_GROUP_ID;
//      final String artifact1 = String.format(CreateJellyFishGradleProjectCommand.DEFAULT_ARTIFACT_ID_FORMAT,
//         command1.replace("-", "").toLowerCase());
//      final String pkg1 = group1 + '.' + artifact1;
//      final String classname1 = "Test1";
//      final String classname2 = "Test2";
//      runCommand(CreateJellyFishGradleProjectCommand.COMMAND_NAME_PROPERTY, command1,
//         CreateJellyFishGradleProjectCommand.CLASSNAME_PROPERTY, classname1, CreateJellyFishGradleProjectCommand.CLEAN_PROPERTY,
//         "true");
//      runCommand(CreateJellyFishGradleProjectCommand.COMMAND_NAME_PROPERTY, command1,
//         CreateJellyFishGradleProjectCommand.CLASSNAME_PROPERTY, classname2, CreateJellyFishGradleProjectCommand.CLEAN_PROPERTY,
//         "true");
//      try {
//         checkCommandOutput(classname1, group1, artifact1, pkg1);
//         Assert.fail("file was not cleaned");
//      } catch (AssertionError a) {
//      }
//      checkCommandOutput(classname2, group1, artifact1, pkg1);
//   }
//
   private void runCommand(String... keyValues) throws IOException {
      IJellyFishCommandOptions mockOptions = Mockito.mock(IJellyFishCommandOptions.class);
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter<String>(keyValues[n]).setValue(keyValues[n + 1]));
      }

	  DefaultParameter<String> outputDirectory = new DefaultParameter<>(CreateJellyFishGradleProjectCommand.OUTPUT_DIR_PROPERTY).setValue(outputDir.toString());
      collection.addParameter(outputDirectory);

      Mockito.when(mockOptions.getParameters()).thenReturn(collection);

      cmd.run(mockOptions);
   }

   private void checkCommandOutput(String expectedProjectName, String expectedGroupId, String expectedArtifactId, String expectedVersion)
      throws IOException {
	  // Check project directory
      Assert.assertTrue("project directory not created", Files.isDirectory(outputDir.resolve(expectedProjectName)));
      
      // Check gradle files existence
      Assert.assertTrue("gradlew was not found",               outputDir.resolve(Paths.get(expectedProjectName, "gradlew")).toFile().exists());
      Assert.assertTrue("gradlew.bat was not found",           outputDir.resolve(Paths.get(expectedProjectName, "gradlew.bat")).toFile().exists());
      Assert.assertTrue("build.gradle was not found",          outputDir.resolve(Paths.get(expectedProjectName, "build.gradle")).toFile().exists());
      Assert.assertTrue("settings.gradle was not found",       outputDir.resolve(Paths.get(expectedProjectName, "settings.gradle")).toFile().exists());
      
      // Check gradle wrapper files existence
      Assert.assertTrue("gradle-wrapper.jar not found",        outputDir.resolve(Paths.get(expectedProjectName, "gradle", "wrapper", "gradle-wrapper.jar")).toFile().exists());
      Assert.assertTrue("gradle-wrapper.properties not found", outputDir.resolve(Paths.get(expectedProjectName, "gradle", "wrapper", "gradle-wrapper.properties")).toFile().exists());

      // Check build.gradle file content
      Path buildFilePath = outputDir.resolve(Paths.get(expectedProjectName, "build.gradle"));
      List<String> buildFileContent = Files.readAllLines(buildFilePath);
      String versionStringToMatch = "version = '" + expectedVersion + "'";
      boolean versionMatch = buildFileContent.stream().anyMatch(line -> line.contains(versionStringToMatch));
      Assert.assertTrue("build.gradle version is incorrect", versionMatch);
      
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

   private static Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
         IResourceService resourceService = Mockito.mock(IResourceService.class);
         Mockito.when(resourceService.getResourceRootPath()).thenReturn(Paths.get("src", "main", "resources"));

         bind(IResourceService.class).toInstance(resourceService);
         bind(ILogService.class).to(PrintStreamLogService.class);
         bind(IParameterService.class).to(ParameterServiceGuiceWrapper.class);
         bind(IPromptUserService.class).to(PromptUserServiceGuiceWrapper.class);
         bind(ITemplateService.class).to(TemplateServiceGuiceWrapper.class);
         bind(IPropertyService.class).to(PropertyServiceGuiceWrapper.class);
      }
   });

}
