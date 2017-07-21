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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateJavaServiceConnectorCommandTest {

   private CreateJavaServiceConnectorCommand cmd = new CreateJavaServiceConnectorCommand();

   private PrintStreamLogService logger = new PrintStreamLogService();

   private IPromptUserService mockPromptService = Mockito.mock(IPromptUserService.class);

   private Path outputDir;

   private String propertyModelName;

   @Before
   public void setup() throws IOException {
      outputDir = Files.createTempDirectory(null);
      propertyModelName = "models.testModelsProp";
      cmd.setLogService(logger);
      cmd.setPromptService(mockPromptService);
      //cmd.setTemplateService(injector.getInstance(ITemplateService.class));
   }

   /*
   * This test tests that the CreateJavaServiceConnectorCommand does run with
   * minimal parameters and is ultimately the base test.
   */
   @Test
   public void testCommand() throws IOException {
      createProperties();

      final String model = "models.testModels";

      runCommand(CreateJavaServiceConnectorCommand.COMMAND_NAME_PROPERTY,
                 CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
                 CreateJavaServiceConnectorCommand.MODEL_PROPERTY, model);
      checkCommandOutput(model, CreateJavaServiceConnectorCommand.DEFAULT_GROUP_ID, CreateJavaServiceConnectorCommand.DEFAULT_ARTIFACT_ID);
   }

   /*
    * This test tests that the CreateJavaServiceConnectorCommand does run with
    * optional parameters and that they do have an impact on the generated project.
    */
   @Test
   public void testCommandWithOptionalParameters() throws IOException {
      createProperties();

      final String group = "com.ngc.seaside";
      final String artifact = "testArtifact.connector";
      final String model = "models.testModels";

      runCommand(CreateJavaServiceConnectorCommand.COMMAND_NAME_PROPERTY,
                 CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
                 CreateJavaServiceConnectorCommand.MODEL_PROPERTY, model,
                 CreateJavaServiceConnectorCommand.GROUP_ID_PROPERTY, group,
                 CreateJavaServiceConnectorCommand.ARTIFACT_ID_PROPERTY, artifact);

      checkCommandOutput(model, group, artifact);
   }

   /*
    * This test tests that the CreateJavaServiceConnectorCommand does prompt
    * the user to enter values if the required parameters aren't set.
    */
   @Test
   public void testCommandDoesPromptWithoutRequiredParameters() throws IOException {
      createProperties();
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream ps = new PrintStream(outputStream);
      System.setOut(ps);

      final String group = "com.ngc.seaside";
      final String artifact = "testmodel.model";
      final String model = "TestCommand1Command";

      runCommand(CreateJavaServiceConnectorCommand.COMMAND_NAME_PROPERTY,
                 CreateJavaServiceConnectorCommand.MODEL_PROPERTY, model,
                 CreateJavaServiceConnectorCommand.GROUP_ID_PROPERTY, group,
                 CreateJavaServiceConnectorCommand.ARTIFACT_ID_PROPERTY, artifact);
      outputStream.flush();
      Assert.assertEquals("Expected prompt for empty required parameter didn't match","Enter value for " + CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, outputStream.toString());
      outputStream.reset();
      runCommand(CreateJavaServiceConnectorCommand.COMMAND_NAME_PROPERTY,
                 CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
                 CreateJavaServiceConnectorCommand.GROUP_ID_PROPERTY, group,
                 CreateJavaServiceConnectorCommand.ARTIFACT_ID_PROPERTY, artifact);
      outputStream.flush();
      Assert.assertEquals("Expected prompt for empty required parameter didn't match","Enter value for " + CreateJavaServiceConnectorCommand.MODEL_PROPERTY, outputStream.toString());
      outputStream.reset();
      runCommand(CreateJavaServiceConnectorCommand.COMMAND_NAME_PROPERTY,
                 CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
                 CreateJavaServiceConnectorCommand.MODEL_PROPERTY, model,
                 CreateJavaServiceConnectorCommand.GROUP_ID_PROPERTY, group,
                 CreateJavaServiceConnectorCommand.ARTIFACT_ID_PROPERTY, artifact);
      checkCommandOutput(model, group, artifact);
   }

   /*
    * This test tests that the CreateJavaServiceConnectorCommand does not prompt
    * the user to enter values if the required parameters aren't set, but are in
    * the jellyfish.properties file.
    */
   @Test
   public void testCommandDoesNotPromptWithJellyfishProperties() throws IOException {
      createProperties();
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream ps = new PrintStream(outputStream);
      System.setOut(ps);

      runCommand(CreateJavaServiceConnectorCommand.COMMAND_NAME_PROPERTY, "");
      outputStream.flush();
      Assert.assertNotEquals("No output expected!", "Enter value for " + CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, outputStream.toString());
      Assert.assertNotEquals("No output expected!", "Enter value for " + CreateJavaServiceConnectorCommand.MODEL_PROPERTY, outputStream.toString());
      outputStream.reset();
      checkCommandOutput(propertyModelName, null, null);
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

      DefaultParameter outputDirectory = new DefaultParameter(CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY)
            .setValue(outputDir.toString());
      collection.addParameter(outputDirectory);

      Mockito.when(mockOptions.getParameters()).thenReturn(collection);

      cmd.run(mockOptions);

   }

   private void createProperties() throws IOException {
      String jellyfishProperties = ("outputDir="+outputDir.toString()+"\n" +
                                    "model="+propertyModelName);
      try {
         Path propertiesFile = Files.createFile(outputDir.resolve("src/main/resources/jellyfish.properties"));
         Files.write(propertiesFile, jellyfishProperties.getBytes());
      } catch (FileAlreadyExistsException e) {
         // ignore
      }
   }

   private void checkCommandOutput(String expectedModelName, String expectedGroupId, String expectedArtifactId)
         throws IOException {
      String expectedBundle = expectedGroupId + '.' + expectedArtifactId;
      String directoryTreeExpectedBundleName = expectedBundle.replace('.', File.separatorChar);
      String expectedJavaFileModelName = expectedModelName.replaceAll("\\.","") + ".java";
      Path bundlePath = Paths.get(expectedBundle, "src", "main", "java", directoryTreeExpectedBundleName);
      Path expectedJavaModelFilePath = Paths.get(expectedBundle, "src", "main", "java", directoryTreeExpectedBundleName, expectedArtifactId + ".java");

      //Checks that the outputDirectory is correctly created
      Assert.assertTrue("Output Directory does not exist", outputDir.toFile().exists());
      Assert.assertTrue("Output Directory is not a directory", outputDir.toFile().isDirectory());

      //Checks the basic structure of the output directory
      Assert.assertTrue("Project folder was not created",
                        outputDir.resolve(Paths.get(expectedBundle)).toFile().exists());
      Assert.assertTrue("build.gradle was not created",
                        outputDir.resolve(Paths.get(expectedBundle, "build.gradle")).toFile().exists());
      Assert.assertTrue("Src/Main/Java folder was not created",
                        outputDir.resolve(Paths.get(expectedBundle, "src", "main", "java" )).toFile().exists());
      Assert.assertTrue("Bundle was not created: " + bundlePath.toString(),
                        outputDir.resolve(bundlePath).toFile().exists());

      String actualBundle = outputDir.resolve(bundlePath).getFileName().toString();
      String[] actualArtifactId = actualBundle.split(expectedGroupId);
      String[] actualGroupId = actualBundle.split(expectedArtifactId);
      //Check that the artifactId is correct, and assuming it has a leading '.'
      Assert.assertTrue("Actual artifactId is not correct. String split returned \'"+actualArtifactId.length+"\'strings instead of 1", actualArtifactId.length == 1);
      Assert.assertEquals("Expected groupId does not match the actual artifactId", '.' + expectedArtifactId, actualArtifactId[0]);
      //Check that the groupId is correct, and assuming it has a trailing '.'
      Assert.assertTrue("Actual artifactId is not correct. String split returned \'"+actualGroupId.length+"\'strings instead of 1", actualGroupId.length == 1);
      Assert.assertEquals("Expected groupId does not match the actual groupId", expectedGroupId + '.', actualGroupId[0]);

      //Check that the modelName is correct
      Assert.assertTrue("Java model file was expected but not found " +expectedJavaModelFilePath.toString(), expectedJavaModelFilePath.toFile().exists() );
      Assert.assertEquals("Generated model java file names doesn't match expected result " + expectedJavaFileModelName, expectedJavaModelFilePath.toFile().getName());

      //Check that the gradle file contains all necessary blocs files
      Assert.assertTrue("build.gradle does not contain blocs service api", Files.readAllLines(outputDir.resolve("settings.gradle")).stream()
            .anyMatch(line -> line.contains("\"com.ngc.blocs:service.api:$blocsCoreVersion\"")));
      Assert.assertTrue("build.gradle does not contain blocs test utilities", Files.readAllLines(outputDir.resolve("settings.gradle")).stream()
            .anyMatch(line -> line.contains("\"com.ngc.blocs:test.impl.common.testutilities:$blocsCoreVersion\"")));
      Assert.assertTrue("build.gradle does not contain osgi core", Files.readAllLines(outputDir.resolve("settings.gradle")).stream()
            .anyMatch(line -> line.contains("\"org.osgi:osgi.core:$osgiVersion\"")));
      Assert.assertTrue("build.gradle does not contain osgi enterprise", Files.readAllLines(outputDir.resolve("settings.gradle")).stream()
            .anyMatch(line -> line.contains("\"org.osgi:osgi.enterprise:$osgiVersion\"")));

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
