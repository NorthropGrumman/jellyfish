package com.ngc.seaside.jellyfish.cli.command.createjavaserviceconnectorcommand;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
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

public class CreateJavaServiceConnectorCommandTest {

   private CreateJavaServiceConnectorCommand cmd = new CreateJavaServiceConnectorCommand();

   private PrintStreamLogService logger = new PrintStreamLogService();

   private IPromptUserService mockPromptService = Mockito.mock(IPromptUserService.class);

   private Path outputDir;

   @Before
   public void setup() throws IOException {
      outputDir = Files.createTempDirectory(null);
      cmd.setLogService(logger);
      cmd.setPromptService(mockPromptService);
   }

   /*
   * This test tests that the CreateJavaServiceConnectorCommand does run with
   * minimal parameters and is ultimately the base test.
   */
   @Test
   public void testCommand() throws IOException {
      createSettings();

      String parameters = {CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
                           CreateJavaServiceConnectorCommand.MODEL_PROPERTY, "com.ngc.seaside.test1.Model1"};

      final String outputDir = "test";
      final String group = CreateJavaServiceConnectorCommand.DEFAULT_GROUP_ID;
      final String artifact = "testmodel.model";
      final String bundle = group + '.' + artifact;
      final String model = "TestCommand1Command";
      runCommand(CreateJavaServiceConnectorCommand.COMMAND_NAME_PROPERTY, parameters);
      checkCommandOutput(outputDir, model, group, artifact, bundle);
   }

   /*
    * This test tests that the CreateJavaServiceConnectorCommand does run with
    * optional parameters and that they do have an impact on the generated project.
    */
   @Test
   public void testCommandWithOptionalParameters() throws IOException {
      createSettings();

      String parameters = {CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
                           CreateJavaServiceConnectorCommand.MODEL_PROPERTY, "com.ngc.seaside.test1.Model1",
                           CreateJavaServiceConnectorCommand.GROUPID_PROPERTY, "com.ngc.seaside.test1.Model1",
                           CreateJavaServiceConnectorCommand.ARTIFACT_PROPERTY, "com.ngc.seaside.test1.Model1"};

      final String outputDir = "test";
      final String group = CreateJavaServiceConnectorCommand.DEFAULT_GROUP_ID;
      final String artifact = "testmodel.model";
      final String bundle = group + '.' + artifact;
      final String model = "TestCommand1Command";
      runCommand(CreateJavaServiceConnectorCommand.COMMAND_NAME_PROPERTY, parameters);
      checkCommandOutput(outputDir, model, group, artifact, bundle);
   }

   /*
    * This test tests that the CreateJavaServiceConnectorCommand does prompt
    * the user to enter values if the required parameters aren't set.
    */
   @Test
   public void testCommandDoesPromptWithoutRequiredParameters() throws IOException {
      createSettings();

      String parameters1 = {CreateJavaServiceConnectorCommand.MODEL_PROPERTY, "com.ngc.seaside.test1.Model1",
                           CreateJavaServiceConnectorCommand.GROUPID_PROPERTY, "com.ngc.seaside.test1.Model1",
                           CreateJavaServiceConnectorCommand.ARTIFACT_PROPERTY, "com.ngc.seaside.test1.Model1"};

      String parameters2 = {CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
                            CreateJavaServiceConnectorCommand.GROUPID_PROPERTY, "com.ngc.seaside.test1.Model1",
                            CreateJavaServiceConnectorCommand.ARTIFACT_PROPERTY, "com.ngc.seaside.test1.Model1"};

      String parameters3 = {CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
                            CreateJavaServiceConnectorCommand.MODEL_PROPERTY, "com.ngc.seaside.test1.Model1",
                            CreateJavaServiceConnectorCommand.GROUPID_PROPERTY, "com.ngc.seaside.test1.Model1",
                            CreateJavaServiceConnectorCommand.ARTIFACT_PROPERTY, "com.ngc.seaside.test1.Model1"};

      final String outputDir = "test";
      final String group = CreateJavaServiceConnectorCommand.DEFAULT_GROUP_ID;
      final String artifact = "testmodel.model";
      final String bundle = group + '.' + artifact;
      final String model = "TestCommand1Command";
      runCommand(CreateJavaServiceConnectorCommand.COMMAND_NAME_PROPERTY, parameters1);
      //TODO Check if prompt occurs
      runCommand(CreateJavaServiceConnectorCommand.COMMAND_NAME_PROPERTY, parameters2);
      //TODO Check if prompt occurs
      runCommand(CreateJavaServiceConnectorCommand.COMMAND_NAME_PROPERTY, parameters3);
      //TODO Check if prompt occurs
      checkCommandOutput(outputDir, model, group, artifact, bundle);
   }

   /*
    * This test tests that the CreateJavaServiceConnectorCommand does not prompt
    * the user to enter values if the required parameters aren't set, but are in
    * the jellyfish.properties file.
    */
   @Test
   public void testCommandDoesNotPromptWithJellyfishProperties() throws IOException {
      createSettings();

      String parameters = {CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
                           CreateJavaServiceConnectorCommand.MODEL_PROPERTY, "com.ngc.seaside.test1.Model1"};

      final String outputDir = "test";
      final String group = CreateJavaServiceConnectorCommand.DEFAULT_GROUP_ID;
      final String artifact = "testmodel.model";
      final String bundle = group + '.' + artifact;
      final String model = "TestCommand1Command";
      runCommand(CreateJavaServiceConnectorCommand.COMMAND_NAME_PROPERTY, parameters);
      checkCommandOutput(outputDir, model, group, artifact, bundle);
   }

   @After
   public void cleanup() throws IOException {
      FileUtils.deleteQuietly(outputDir.toFile());
   }

   private void runCommand(String... keyValues) throws IOException {
      IJellyFishCommandOptions mockOptions = Mockito.mock(IJellyFishCommandOptions.class);
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter(keyValues[n]).setValue(keyValues[n + 1]));
      }

      DefaultParameter outputDirectory = new DefaultParameter(CreateJavaServiceConnectorCommand.OUTPUT_DIR_PROPERTY)
            .setValue(outputDir.toString());
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

   private void checkCommandOutput(String expectedOutputDir, String expectedModelName, String expectedGroupId, String expectedArtifactId,
                                   String expectedBundle)
         throws IOException {
      String projectName = expectedGroupId + '.' + expectedArtifactId;
      if (expectedBundle == null) {
         expectedBundle = projectName;
      }
      expectedBundle = expectedBundle.replace('.', File.separatorChar);

      Assert.assertTrue("settings.gradle not updated", Files.readAllLines(outputDir.resolve("settings.gradle")).stream()
            .anyMatch(line -> line.contains(projectName)));
      Path expectedPath = Paths.get(projectName, "src", "main", "java", expectedBundle, expectedModelName + ".java");
      Assert.assertTrue("command was not created: " + expectedPath, outputDir.resolve(expectedPath).toFile().exists());
      Path actualPath = outputDir.resolve(expectedPath).toRealPath();
      Assert.assertEquals("Filename was not capitalized correctly", outputDir.toRealPath().resolve(expectedPath).toString(), actualPath.toString());
      Assert.assertTrue("resources folder was not created",
                        outputDir.resolve(Paths.get(projectName, "src", "main", "resources")).toFile().exists());
      Assert.assertTrue("test folder was not created",
                        outputDir.resolve(Paths.get(projectName, "src", "test", "java")).toFile().exists());
      Assert.assertTrue("build.gradle was not created",
                        outputDir.resolve(Paths.get(projectName, "build.gradle")).toFile().exists());

      //TODO Check bundle name is ${groupId}.${artifactId}
      //TODO Check Gradle file contains all necessary blocs files
      //TODO Check expectedOutputDir
      //TODO Check expectedModelName
      //TODO Check expectedGroupId
      //TODO Check expectedArtifactId
   }

   private static Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
         IResourceService resourceService = Mockito.mock(IResourceService.class);
         Mockito.when(resourceService.getResourceRootPath()).thenReturn(Paths.get("src", "main", "resources"));

         bind(IResourceService.class).toInstance(resourceService);
         bind(ILogService.class).to(PrintStreamLogService.class);
//         bind(IParameterService.class).to(ParameterServiceGuiceWrapper.class);
//         bind(IPromptUserService.class).to(PromptUserServiceGuiceWrapper.class);
//         bind(ITemplateService.class).to(TemplateServiceGuiceWrapper.class);
//         bind(IPropertyService.class).to(PropertyServiceGuiceWrapper.class);
      }
   });
   
}
