package com.ngc.seaside.jellyfish.cli.command.createjellyfishcommand;

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

public class CreateJellyFishCommandCommandTest {

   private CreateJellyFishCommandCommand cmd = new CreateJellyFishCommandCommand();

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

   @Test
   public void testCommandWithGroup() throws IOException {
      createSettings();

      final String command = "test-command-2";
      final String group = "com.ngc.test";
      final String artifact = String.format(CreateJellyFishCommandCommand.DEFAULT_ARTIFACT_ID_FORMAT,
         command.replace("-", "").toLowerCase());
      final String pkg = group + '.' + artifact;
      final String classname = "TestCommand2Command";
      runCommand(CreateJellyFishCommandCommand.COMMAND_NAME_PROPERTY, command,
         CreateJellyFishCommandCommand.GROUP_ID_PROPERTY, group);
      checkCommandOutput(classname, group, artifact, pkg);
   }

   @Test
   public void testCommandWithArtifact() throws IOException {
      createSettings();

      final String command = "test-command-3";
      final String group = CreateJellyFishCommandCommand.DEFAULT_GROUP_ID;
      final String artifact = "test.artifact.id";
      final String pkg = group + '.' + artifact;
      final String classname = "TestCommand3Command";
      runCommand(CreateJellyFishCommandCommand.COMMAND_NAME_PROPERTY, command,
         CreateJellyFishCommandCommand.ARTIFACT_ID_PROPERTY, artifact);
      checkCommandOutput(classname, group, artifact, pkg);
   }

   @Test
   public void testCommandPackage() throws IOException {
      createSettings();

      final String command = "test-command-4";
      final String group = CreateJellyFishCommandCommand.DEFAULT_GROUP_ID;
      final String artifact = String.format(CreateJellyFishCommandCommand.DEFAULT_ARTIFACT_ID_FORMAT,
         command.replace("-", "").toLowerCase());
      final String pkg = group + '.' + artifact;
      final String classname = "TestCommand4Command";
      runCommand(CreateJellyFishCommandCommand.COMMAND_NAME_PROPERTY, command,
         CreateJellyFishCommandCommand.PACKAGE_PROPERTY, pkg);
      checkCommandOutput(classname, group, artifact, pkg);
   }

   @Test
   public void testCommandWithoutCommandName() throws IOException {
      createSettings();

      final String command = "test-command-5";
      final String group = CreateJellyFishCommandCommand.DEFAULT_GROUP_ID;
      final String artifact = String.format(CreateJellyFishCommandCommand.DEFAULT_ARTIFACT_ID_FORMAT,
         command.replace("-", "").toLowerCase());
      final String pkg = group + '.' + artifact;
      final String classname = "TestCommand5Command";
      Mockito.when(mockPromptService.prompt(Mockito.eq(CreateJellyFishCommandCommand.COMMAND_NAME_PROPERTY),
         Mockito.any(), Mockito.any())).thenReturn(command);
      runCommand();
      checkCommandOutput(classname, group, artifact, pkg);
   }

   @Test
   public void testCommandClassname() throws IOException {
      createSettings();

      final String command = "test-command-6";
      final String group = CreateJellyFishCommandCommand.DEFAULT_GROUP_ID;
      final String artifact = String.format(CreateJellyFishCommandCommand.DEFAULT_ARTIFACT_ID_FORMAT,
         command.replace("-", "").toLowerCase());
      final String pkg = group + '.' + artifact;
      final String classname = "TestName";
      runCommand(CreateJellyFishCommandCommand.COMMAND_NAME_PROPERTY, command,
         CreateJellyFishCommandCommand.CLASSNAME_PROPERTY, classname);
      checkCommandOutput(classname, group, artifact, pkg);
   }

   @Test(expected = Exception.class)
   public void testCommandWithoutSettings() throws IOException {
      final String command = "test-command-7";
      runCommand(CreateJellyFishCommandCommand.COMMAND_NAME_PROPERTY, command);
   }

   @Test
   public void testMultipleSubprojects() throws IOException {
      createSettings();

      final String command1 = "test-command-8";
      final String group1 = CreateJellyFishCommandCommand.DEFAULT_GROUP_ID;
      final String artifact1 = String.format(CreateJellyFishCommandCommand.DEFAULT_ARTIFACT_ID_FORMAT,
         command1.replace("-", "").toLowerCase());
      final String pkg1 = group1 + '.' + artifact1;

      final String command2 = "test-command-9";
      final String group2 = CreateJellyFishCommandCommand.DEFAULT_GROUP_ID;
      final String artifact2 = String.format(CreateJellyFishCommandCommand.DEFAULT_ARTIFACT_ID_FORMAT,
         command2.replace("-", "").toLowerCase());
      final String pkg2 = group2 + '.' + artifact2;

      runCommand(CreateJellyFishCommandCommand.COMMAND_NAME_PROPERTY, command1);
      runCommand(CreateJellyFishCommandCommand.COMMAND_NAME_PROPERTY, command2);

      Assert.assertTrue(
         Files.readAllLines(outputDir.resolve("settings.gradle")).stream().anyMatch(line -> line.contains(pkg1)));
      Assert.assertTrue(
         Files.readAllLines(outputDir.resolve("settings.gradle")).stream().anyMatch(line -> line.contains(pkg2)));

   }

   @Test
   public void testWithoutClean() throws IOException {
      createSettings();

      final String command1 = "test-command-8";
      final String group1 = CreateJellyFishCommandCommand.DEFAULT_GROUP_ID;
      final String artifact1 = String.format(CreateJellyFishCommandCommand.DEFAULT_ARTIFACT_ID_FORMAT,
         command1.replace("-", "").toLowerCase());
      final String pkg1 = group1 + '.' + artifact1;
      final String classname1 = "Test1";
      final String classname2 = "Test2";
      runCommand(CreateJellyFishCommandCommand.COMMAND_NAME_PROPERTY, command1,
         CreateJellyFishCommandCommand.CLASSNAME_PROPERTY, classname1, CreateJellyFishCommandCommand.CLEAN_PROPERTY,
         "false");
      runCommand(CreateJellyFishCommandCommand.COMMAND_NAME_PROPERTY, command1,
         CreateJellyFishCommandCommand.CLASSNAME_PROPERTY, classname2, CreateJellyFishCommandCommand.CLEAN_PROPERTY,
         "false");
      checkCommandOutput(classname1, group1, artifact1, pkg1);
      checkCommandOutput(classname2, group1, artifact1, pkg1);
   }

   @Test
   public void testWithClean() throws IOException {
      createSettings();

      final String command1 = "test-command-8";
      final String group1 = CreateJellyFishCommandCommand.DEFAULT_GROUP_ID;
      final String artifact1 = String.format(CreateJellyFishCommandCommand.DEFAULT_ARTIFACT_ID_FORMAT,
         command1.replace("-", "").toLowerCase());
      final String pkg1 = group1 + '.' + artifact1;
      final String classname1 = "Test1";
      final String classname2 = "Test2";
      runCommand(CreateJellyFishCommandCommand.COMMAND_NAME_PROPERTY, command1,
         CreateJellyFishCommandCommand.CLASSNAME_PROPERTY, classname1, CreateJellyFishCommandCommand.CLEAN_PROPERTY,
         "true");
      runCommand(CreateJellyFishCommandCommand.COMMAND_NAME_PROPERTY, command1,
         CreateJellyFishCommandCommand.CLASSNAME_PROPERTY, classname2, CreateJellyFishCommandCommand.CLEAN_PROPERTY,
         "true");
      try {
         checkCommandOutput(classname1, group1, artifact1, pkg1);
         Assert.fail("file was not cleaned");
      } catch (AssertionError a) {
      }
      checkCommandOutput(classname2, group1, artifact1, pkg1);
   }

   private void runCommand(String... keyValues) throws IOException {
      IJellyFishCommandOptions mockOptions = Mockito.mock(IJellyFishCommandOptions.class);
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter<>(keyValues[n], keyValues[n + 1]));
      }

      DefaultParameter outputDirectory = new DefaultParameter<>(CreateJellyFishCommandCommand.OUTPUT_DIR_PROPERTY,
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

      Assert.assertTrue("settings.gradle not updated", Files.readAllLines(outputDir.resolve("settings.gradle")).stream()
               .anyMatch(line -> line.contains(projectName)));
      Path expectedPath = Paths.get(projectName, "src", "main", "java", expectedPackage, expectedClassname + ".java");
      Assert.assertTrue("command was not created: " + expectedPath, outputDir.resolve(expectedPath).toFile().exists());
      Path actualPath = outputDir.resolve(expectedPath).toRealPath();
      Assert.assertEquals("Filename was not capitalized correctly", outputDir.toRealPath().resolve(expectedPath).toString(), actualPath.toString());
      Assert.assertTrue("resources folder was not created",
         outputDir.resolve(Paths.get(projectName, "src", "main", "resources")).toFile().exists());
      Assert.assertTrue("test folder was not created",
         outputDir.resolve(Paths.get(projectName, "src", "test", "java")).toFile().exists());
      Assert.assertTrue("build.gradle was not created",
         outputDir.resolve(Paths.get(projectName, "build.gradle")).toFile().exists());
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
