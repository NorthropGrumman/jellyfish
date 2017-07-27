package com.ngc.seaside.jellyfish.cli.command.createjavaserviceconnectorcommand;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.service.impl.templateservice.TemplateServiceGuiceModule;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.jellyfish.cli.command.test.template.MockedTemplateService;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.osgi.service.component.annotations.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import org.mockito.Mockito;
import static org.mockito.Mockito.mock;

@Component(service = IJellyFishCommand.class)
public class CreateJavaServiceConnectorCommandIT {

   private IJellyFishCommand cmd = injector.getInstance(CreateJavaServiceConnectorCommandGuiceWrapper.class);
   private IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class);

   private Path outputDir;

   @Before
   public void setup() throws IOException {
      outputDir = Files.createDirectories(Paths.get("build/test-template"));

      Path sdDir = Paths.get("src", "test", "sd");
      PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.sd");
      Collection<Path> sdFiles = Files.walk(sdDir).filter(matcher::matches).collect(Collectors.toSet());
      ISystemDescriptorService sdService = injector.getInstance(ISystemDescriptorService.class);
      IParsingResult result = sdService.parseFiles(sdFiles);
      Assert.assertTrue(result.getIssues().toString(), result.isSuccessful());
      ISystemDescriptor sd = result.getSystemDescriptor();
      Mockito.when(options.getSystemDescriptor()).thenReturn(sd);

      createSettings();
   }

   /*
   * This test tests that the CreateJavaServiceConnectorCommand does run with
   * minimal parameters and is ultimately the base test.
   */
   @Test
   public void testCommandWithoutOptionalParameters() throws IOException {
      final String model = "com.ngc.seaside.threateval.EngagementTrackPriorityService";
      final String expectedGroupId = "com.ngc.seaside.threateval";
      final String expectedArtifactId = "engagementtrackpriorityservice.connector";
      final String expectedClassName = "EngagementTrackPriorityServiceConnector";

      runCommand(CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
                 CreateJavaServiceConnectorCommand.MODEL_PROPERTY, model);
      checkCommandOutput(expectedGroupId, expectedArtifactId, expectedClassName);

      Path expectedConnectorFile = Paths.get("src", "test", "resources", "expectedfiles", "EngagementTrackPriorityServiceConnector.java");
      Path actualConnectorFile = Paths.get(outputDir.toAbsolutePath().toString(),expectedGroupId + '.' + expectedArtifactId, "src", "main", "java", (expectedGroupId + '.' + expectedArtifactId).replace('.', File.separatorChar), expectedClassName + ".java");
      checkOutputAgainstExpectedFile(expectedConnectorFile ,actualConnectorFile);
   }

   /*
    * This test tests that the CreateJavaServiceConnectorCommand does run with
    * optional parameters and that they do have an impact on the generated project.
    */
   @Test
   public void testCommandWithOptionalParameters() throws IOException {
      final String model = "com.ngc.seaside.threateval.EngagementTrackPriorityService";
      final String group = "com.ngc.seaside.threateval.test";
      final String artifact = "engagementtrackpriorityservicetest.connector";

      final String expectedClassName = "EngagementTrackPriorityServiceConnector";

      runCommand(CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
                 CreateJavaServiceConnectorCommand.MODEL_PROPERTY, model,
                 CreateJavaServiceConnectorCommand.GROUP_ID_PROPERTY, group,
                 CreateJavaServiceConnectorCommand.ARTIFACT_ID_PROPERTY, artifact);

      checkCommandOutput(group, artifact, expectedClassName);
   }

   private void checkOutputAgainstExpectedFile(Path expectedFile, Path actualFile) throws IOException {
      Assert.assertTrue("expectedFile does not exist " + expectedFile.toAbsolutePath().toString(), expectedFile.toFile().exists());
      Assert.assertTrue("actualFile does not exist " + actualFile.toAbsolutePath().toString(), actualFile.toFile().exists());

      List<String> expectedLines = Files.readAllLines(expectedFile);
      expectedLines.replaceAll(String::trim);
      List<String> actualLines = Files.readAllLines(actualFile);
      actualLines.replaceAll(String::trim);

      for (int i = 0; i < expectedLines.size(); i++){
         String expectedLine = expectedLines.get(i);
         if(!actualLines.contains(expectedLine)){
            //If the assertTrue were to fail, let's use a more descriptive error message...
            Assert.assertEquals("actualFile does not contain line from expectedFile. Expected line number "
                                + (i+1) + " missing. May correlate to actual line", expectedLine, actualLines.get(i));
         } else {
            Assert.assertTrue("actualFile does not contain line from expectedFile ", actualLines.contains(expectedLine));
         }
      }
   }

   private void runCommand(String... keyValues) throws IOException {
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter<>(keyValues[n]).setValue(keyValues[n + 1]));
      }

      Mockito.when(options.getParameters()).thenReturn(collection);

      cmd.run(options);

   }

   private void checkCommandOutput(String expectedGroupId, String expectedArtifactId, String expectedClassName) throws IOException {
      String expectedBundle = expectedGroupId + '.' + expectedArtifactId;
      String projectPathExpected = expectedBundle.replace('.', File.separatorChar);

      Path actualBundle = outputDir.resolve(expectedBundle).toAbsolutePath();
      Path packagePath  = Paths.get(actualBundle.toString(), "src", "main", "java", projectPathExpected);
      Path expectedJavaModelFilePath = Paths.get(packagePath.toString(), expectedClassName + ".java");
      Path expectedGradleBuildFile = Paths.get(actualBundle.toString(), "build.gradle" );

      String expectedJavaFileModelName = expectedClassName + ".java".trim();
      String actualBundleName = actualBundle.getFileName().toString();

      verifyGeneratedProjectStructure(packagePath, expectedGradleBuildFile, actualBundle, expectedBundle);
      verifyActualArtifactAndGroupId(expectedGroupId, expectedArtifactId, actualBundleName);
      verifyActualModelName(expectedJavaModelFilePath, expectedJavaFileModelName);

      checkGradleBuildFile(expectedGradleBuildFile);
      checkGradleSettingsFile(expectedGradleBuildFile, expectedArtifactId, actualBundleName);
   }

   private void verifyGeneratedProjectStructure(Path projectPath, Path actualBundle, Path expectedGradleBuildFile, String expectedBundle){
      //Checks that the outputDirectory is correctly created
      Assert.assertTrue("Output Directory does not exist", outputDir.toFile().exists());
      Assert.assertTrue("Output Directory is not a directory", outputDir.toFile().isDirectory());

      //Checks the basic structure of the output directory
      Assert.assertTrue("Project folder was not created " + actualBundle.toString(),
                        actualBundle.toFile().exists());
      Assert.assertTrue("build.gradle was not created",
                        expectedGradleBuildFile.toFile().exists());
      Assert.assertTrue("Src/Main/Java folder was not created",
                        outputDir.resolve(Paths.get(expectedBundle, "src", "main", "java" )).toFile().exists());
      Assert.assertTrue("Bundle was not created: " + projectPath.toString(),
                        projectPath.toFile().exists());

      Assert.assertTrue("Actual bundle doesn't exist! " + actualBundle.toString(), actualBundle.toFile().exists());
   }

   private void verifyActualModelName(Path expectedJavaModelFilePath, String expectedJavaFileModelName){
      //Check that the modelName is correct
      Assert.assertTrue("Java model file was expected but not found " + expectedJavaModelFilePath.toString(), expectedJavaModelFilePath.toFile().exists() );
      Assert.assertEquals("Generated model java file names doesn't match expected result " + expectedJavaFileModelName,expectedJavaFileModelName, expectedJavaModelFilePath.toFile().getName().trim());
   }

   private void verifyActualArtifactAndGroupId(String expectedGroupId, String expectedArtifactId, String actualBundleName){
      String[] actualArtifactId = actualBundleName.split(expectedGroupId);
      String[] actualGroupId = actualBundleName.split(expectedArtifactId);
      //Check that the artifactId is correct, and assuming it has a leading '.'
      Assert.assertTrue("Actual artifactId is not correct. String split returned \'"+actualArtifactId.length+"\'strings instead of 2", actualArtifactId.length == 2);
      Assert.assertEquals("Expected groupId does not match the actual artifactId", '.' + expectedArtifactId, actualArtifactId[1]);
      //Check that the groupId is correct, and assuming it has a trailing '.'
      Assert.assertTrue("Actual artifactId is not correct. String split returned \'"+actualGroupId.length+"\'strings instead of 1", actualGroupId.length == 1);
      Assert.assertEquals("Expected groupId does not match the actual groupId", expectedGroupId + '.', actualGroupId[0]);

   }
   private void checkGradleBuildFile(Path gradleFile) throws IOException {
      //Check that the gradle file contains all necessary blocs files
      Assert.assertTrue("build.gradle does not contain blocs service api", Files.readAllLines(gradleFile).stream()
            .anyMatch(line -> line.contains("\"com.ngc.blocs:service.api:$blocsCoreVersion\"")));
      Assert.assertTrue("build.gradle does not contain blocs test utilities", Files.readAllLines(gradleFile).stream()
            .anyMatch(line -> line.contains("\"com.ngc.blocs:test.impl.common.testutilities:$blocsCoreVersion\"")));
      Assert.assertTrue("build.gradle does not contain osgi core", Files.readAllLines(gradleFile).stream()
            .anyMatch(line -> line.contains("\"org.osgi:osgi.core:$osgiVersion\"")));
      Assert.assertTrue("build.gradle does not contain osgi enterprise", Files.readAllLines(gradleFile).stream()
            .anyMatch(line -> line.contains("\"org.osgi:osgi.enterprise:$osgiVersion\"")));
   }

   private void checkGradleSettingsFile(Path gradleFile, String expectedArtifactId, String actualBundleName) throws IOException {
      //Check that the settings.gradle file contains the project
      String contents = new String(Files.readAllBytes(outputDir.resolve("settings.gradle")));
      Assert.assertTrue("Project is not included in settings.gradle",contents.contains("include \'"+actualBundleName+"\'"));
      Assert.assertTrue("Project is not named in settings.gradle",contents.contains("project(\':" + actualBundleName + "\').name = \'" + expectedArtifactId + "\'"));
   }

   private void createSettings() throws IOException {
      try {
         Files.createFile(outputDir.resolve("settings.gradle"));
      } catch (FileAlreadyExistsException e) {
         // ignore
      }
   }

   private static final Module TEST_SERVICE_MODULE = new AbstractModule() {
      @Override
      protected void configure() {
         bind(ILogService.class).to(PrintStreamLogService.class);
         MockedTemplateService mockedTemplateService = new MockedTemplateService().useRealPropertyService().useDefaultUserValues(true)
               .setTemplateDirectory(CreateJavaServiceConnectorCommand.class.getPackage().getName(),
                                     Paths.get("src", "main", "template"));
         bind(ITemplateService.class).toInstance(mockedTemplateService);
      }
   };

   private static Collection<Module> getModules() {
      Collection<Module> modules = new ArrayList<>();
      modules.add(TEST_SERVICE_MODULE);
      for (Module dynamicModule : ServiceLoader.load(Module.class)) {
         if (!(dynamicModule instanceof TemplateServiceGuiceModule || dynamicModule instanceof PrintStreamLogService)) {
            modules.add(dynamicModule);
         }
      }

      modules.removeIf(m -> m instanceof XTextSystemDescriptorServiceModule);
      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
      return modules;
   }

   private static final Injector injector = Guice.createInjector(getModules());

}
