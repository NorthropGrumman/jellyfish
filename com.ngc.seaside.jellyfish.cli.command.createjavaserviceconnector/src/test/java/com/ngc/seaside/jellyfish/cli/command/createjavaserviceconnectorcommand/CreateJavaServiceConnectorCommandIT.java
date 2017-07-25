package com.ngc.seaside.jellyfish.cli.command.createjavaserviceconnectorcommand;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;


import com.ngc.blocs.guice.module.LogServiceModule;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.service.impl.templateservice.TemplateServiceGuiceModule;
import com.ngc.seaside.bootstrap.service.property.api.IPropertyService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.jellyfish.cli.command.test.template.MockedTemplateService;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.basic.SystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import com.ngc.seaside.bootstrap.service.impl.templateservice.TemplateServiceGuiceWrapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Component(service = IJellyFishCommand.class)
public class CreateJavaServiceConnectorCommandIT {

   private IJellyFishCommand cmd = injector.getInstance(CreateJavaServiceConnectorCommand.class);
   private IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class);


   private Path outputDir;

   @Before
   public void setup() throws IOException {
      outputDir = Files.createTempDirectory(null);
      outputDir.toFile().deleteOnExit();

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
      final String model = "com.ngc.seaside.test1.threateval.EngagementTrackPriorityService";
      final String expectedGroupId = "com.ngc.seaside.test1.threateval";
      final String expectedArtifactId = "engagementtrackpriorityservice.connector";
      final String expectedClassName = "EngagementTrackPriorityServiceConnector";

      runCommand(CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
                 CreateJavaServiceConnectorCommand.MODEL_PROPERTY, model);
      checkCommandOutput(expectedGroupId, expectedArtifactId, expectedClassName);
   }

   /*
    * This test tests that the CreateJavaServiceConnectorCommand does run with
    * optional parameters and that they do have an impact on the generated project.
    */
   @Test
   public void testCommandWithOptionalParameters() throws IOException {
      final String model = "com.ngc.seaside.test.TestService";
      final String group = "com.ngc.seaside.otherthing";
      final String artifact = "TestServiceArtifact.connector";

      final String expectedGroupId = "com.ngc.seaside.otherthing";
      final String expectedArtifactId = "TestServiceArtifact.connector";
      final String expectedClassName = "TestServiceConnector";

      runCommand(CreateJavaServiceConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
                 CreateJavaServiceConnectorCommand.MODEL_PROPERTY, model,
                 CreateJavaServiceConnectorCommand.GROUP_ID_PROPERTY, group,
                 CreateJavaServiceConnectorCommand.ARTIFACT_ID_PROPERTY, artifact);

      checkCommandOutput(expectedGroupId, expectedArtifactId, expectedClassName);
   }

   @After
   public void cleanup() throws IOException {
     FileUtils.deleteQuietly(outputDir.toFile());
   }

   private void runCommand(String... keyValues) throws IOException {
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter<>(keyValues[n]).setValue(keyValues[n + 1]));
      }

      Mockito.when(options.getParameters()).thenReturn(collection);

      cmd.run(options);

   }

   private void checkCommandOutput(String expectedGroupId, String expectedArtifactId, String expectedClassName)
         throws IOException {
      String expectedBundle = expectedGroupId + '.' + expectedArtifactId;
      String directoryTreeExpectedBundleName = expectedBundle.replace('.', File.separatorChar);
      String expectedJavaFileModelName = expectedClassName + ".java".trim();
      Path bundlePath = Paths.get(expectedBundle, "src", "main", "java", directoryTreeExpectedBundleName);
      Path expectedJavaModelFilePath = Paths.get(outputDir.toAbsolutePath().toString(),expectedBundle, "src", "main", "java", directoryTreeExpectedBundleName, expectedClassName + ".java");
      Path expectedGradleBuildFile = Paths.get(outputDir.toAbsolutePath().toString(), expectedBundle, "build.gradle" );

      //Checks that the outputDirectory is correctly created
      Assert.assertTrue("Output Directory does not exist", outputDir.toFile().exists());
      Assert.assertTrue("Output Directory is not a directory", outputDir.toFile().isDirectory());

      //Checks the basic structure of the output directory
      Assert.assertTrue("Project folder was not created " + outputDir.resolve(Paths.get(expectedBundle)).toString(),
                        outputDir.resolve(Paths.get(expectedBundle)).toFile().exists());
      Assert.assertTrue("build.gradle was not created",
                        outputDir.resolve(Paths.get(expectedBundle, "build.gradle")).toFile().exists());
      Assert.assertTrue("Src/Main/Java folder was not created",
                        outputDir.resolve(Paths.get(expectedBundle, "src", "main", "java" )).toFile().exists());
      Assert.assertTrue("Bundle was not created: " + bundlePath.toString(),
                        outputDir.resolve(bundlePath).toFile().exists());

      Path actualBundle = outputDir.resolve(expectedBundle).toAbsolutePath();
      String actualBundleName = actualBundle.getFileName().toString();
      Assert.assertTrue("Actual bundle doesn't exist! " + actualBundle.toString(), actualBundle.toFile().exists());
      String[] actualArtifactId = actualBundleName.split(expectedGroupId);
      String[] actualGroupId = actualBundleName.split(expectedArtifactId);
      //Check that the artifactId is correct, and assuming it has a leading '.'
      Assert.assertTrue("Actual artifactId is not correct. String split returned \'"+actualArtifactId.length+"\'strings instead of 2", actualArtifactId.length == 2);
      Assert.assertEquals("Expected groupId does not match the actual artifactId", '.' + expectedArtifactId, actualArtifactId[1]);
      //Check that the groupId is correct, and assuming it has a trailing '.'
      Assert.assertTrue("Actual artifactId is not correct. String split returned \'"+actualGroupId.length+"\'strings instead of 1", actualGroupId.length == 1);
      Assert.assertEquals("Expected groupId does not match the actual groupId", expectedGroupId + '.', actualGroupId[0]);

      //Check that the modelName is correct
      Assert.assertTrue("Java model file was expected but not found " + expectedJavaModelFilePath.toString(), expectedJavaModelFilePath.toFile().exists() );
      Assert.assertEquals("Generated model java file names doesn't match expected result " + expectedJavaFileModelName,expectedJavaFileModelName, expectedJavaModelFilePath.toFile().getName().trim());

      //Check that the gradle file contains all necessary blocs files
      Assert.assertTrue("build.gradle does not contain blocs service api", Files.readAllLines(expectedGradleBuildFile).stream()
            .anyMatch(line -> line.contains("\"com.ngc.blocs:service.api:$blocsCoreVersion\"")));
      Assert.assertTrue("build.gradle does not contain blocs test utilities", Files.readAllLines(expectedGradleBuildFile).stream()
            .anyMatch(line -> line.contains("\"com.ngc.blocs:test.impl.common.testutilities:$blocsCoreVersion\"")));
      Assert.assertTrue("build.gradle does not contain osgi core", Files.readAllLines(expectedGradleBuildFile).stream()
            .anyMatch(line -> line.contains("\"org.osgi:osgi.core:$osgiVersion\"")));
      Assert.assertTrue("build.gradle does not contain osgi enterprise", Files.readAllLines(expectedGradleBuildFile).stream()
            .anyMatch(line -> line.contains("\"org.osgi:osgi.enterprise:$osgiVersion\"")));

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
         MockedTemplateService mockedTemplateService = new MockedTemplateService();
         mockedTemplateService = new MockedTemplateService().useRealPropertyService().useDefaultUserValues(true)
               .setTemplateDirectory(CreateJavaServiceConnectorCommand.class.getPackage().getName(),
                                     Paths.get("src", "main", "template"));

         bind(ITemplateService.class).toInstance(mockedTemplateService);
      }
   };

   private static Collection<Module> getModules() {
      Collection<Module> modules = new ArrayList<>();
      modules.add(TEST_SERVICE_MODULE);
      for (Module dynamicModule : ServiceLoader.load(Module.class)) {
         if (!(dynamicModule instanceof TemplateServiceGuiceModule || dynamicModule instanceof LogServiceModule)) {
            modules.add(dynamicModule);
         }
      }

      modules.removeIf(m -> m instanceof XTextSystemDescriptorServiceModule);
      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
      return modules;
   }

   private static final Injector injector = Guice.createInjector(getModules());

}
