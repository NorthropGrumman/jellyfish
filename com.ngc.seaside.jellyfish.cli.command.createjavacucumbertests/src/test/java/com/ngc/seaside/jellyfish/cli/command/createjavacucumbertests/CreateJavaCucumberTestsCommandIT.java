package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.template.MockedTemplateService;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.ngc.seaside.jellyfish.cli.command.test.files.TestingFiles.assertFileLinesEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateJavaCucumberTestsCommandIT {

   private CreateJavaCucumberTestsCommand command;

   private MockedTemplateService templateService;

   private DefaultParameterCollection parameters;

//   @Rule
//   public final TemporaryFolder outputDirectory = new TemporaryFolder();

   // TODO: remove this, use the rule above.
   private Path outputDirectory;

   @Mock
   private IPromptUserService promptUserService;

   @Mock
   private IJellyFishCommandOptions options;

   @Mock
   private ISystemDescriptor systemDescriptor;

   @Mock
   private IModel model;

   @Mock
   private ILogService logService;

   @Before
   public void setup() throws IOException {
      // TODO: remove this
      outputDirectory = Files.createDirectories(Paths.get("build/test-template"));

      command = new CreateJavaCucumberTestsCommand();

      templateService = new MockedTemplateService()
            .useRealPropertyService()
            .setTemplateDirectory(CreateJavaCucumberTestsCommandIT.class.getPackage().getName(),
                                  Paths.get("src", "main", "template"));

      parameters = new DefaultParameterCollection();
      when(options.getParameters()).thenReturn(parameters);

      // Setup mock system descriptor
      when(options.getSystemDescriptor()).thenReturn(systemDescriptor);
      when(systemDescriptor.findModel("com.ngc.seaside.test.Model")).thenReturn(Optional.of(model));

      // Setup mock model
      when(model.getParent()).thenReturn(mock(IPackage.class));
      when(model.getParent().getName()).thenReturn("com.ngc.seaside.test");
      when(model.getName()).thenReturn("Model");
      when(model.getFullyQualifiedName()).thenReturn("com.ngc.seaside.test.Model");

      command.setLogService(logService);
      command.setPromptService(promptUserService);
      command.setTemplateService(templateService);
      command.activate();
   }

   @Test
   public void testDoesGenerateANewProjectAndCopyFeatureFiles() throws IOException {
      parameters.addParameter(new DefaultParameter<>(CreateJavaCucumberTestsCommand.MODEL_PROPERTY,
                                                     model.getFullyQualifiedName()));
      parameters.addParameter(new DefaultParameter<>(CreateJavaCucumberTestsCommand.OUTPUT_DIRECTORY_PROPERTY,
                                                     outputDirectory.toString()));
      parameters.addParameter(new DefaultParameter<>(CreateJavaCucumberTestsCommand.REFRESH_FEATURE_FILES_PROPERTY,
                                                     "false"));

      command.run(options);

      // TODO: check that files exists.
      assertFileLinesEquals("some error messages",
                            Paths.get(null), // expected file
                            Paths.get(null)); // the file that was generated.

//      runCommand(CreateJavaCucumberTestsCommand.MODEL_PROPERTY, "com.ngc.seaside.test.Model",
//                 CreateJavaCucumberTestsCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString());
//
//      Mockito.verify(options, Mockito.times(1)).getParameters();
//      Mockito.verify(options, Mockito.times(1)).getSystemDescriptor();
//      System.out.println(printDirectoryTree(outputDir.toFile()));
//      checkGradleBuild(outputDir);
//      checkLogContents(outputDir);
   }

   @Test
   public void testDoesRefreshFeatureFilesOnly() throws Throwable {
      // TODO: create some feature files before running the command.
      fail("not implemented");
      // TODO: check the old feature files where removed and the updated ones where copied.
   }

   @Test
   public void testDoesRefreshFeatureFilesOnlyDefaultToFalse() throws Throwable {
      fail("not implemented");
   }

//   private void runCommand(String... keyValues) {
//      DefaultParameterCollection collection = new DefaultParameterCollection();
//
//      for (int n = 0; n + 1 < keyValues.length; n += 2) {
//         collection.addParameter(new DefaultParameter<String>(keyValues[n]).setValue(keyValues[n + 1]));
//      }
//
//      Mockito.when(options.getParameters()).thenReturn(collection);
//
//      cmd.run(options);
//   }
//
//   private void checkLogContents(Path projectDir) throws IOException {
//      PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**log4j.xml");
//      Collection<Path> gradleFiles = Files.walk(projectDir).filter(matcher::matches).collect(Collectors.toSet());
//
//      // There should only be one log4j.xml file generated
//      Assert.assertEquals(1, gradleFiles.size());
//      Path generatedFile = Paths.get(gradleFiles.toArray()[0].toString());
//      Assert.assertTrue("log4j.xml is missing", Files.isRegularFile(generatedFile));
//      String actualContents = new String(Files.readAllBytes(generatedFile));
//
//      Path expectedFile = Paths.get("src/test/resources/expectedfiles/log4j.xml.expected");
//      String expectedContents = new String(Files.readAllBytes(expectedFile));
//
//      Assert.assertEquals(expectedContents, actualContents);
//   }
//
//   private void checkGradleBuild(Path projectDir) throws IOException {
//      PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**build.gradle");
//      Collection<Path> gradleFiles = Files.walk(projectDir).filter(matcher::matches).collect(Collectors.toSet());
//
//      // There should only be one build.gradle generated
//      Assert.assertEquals(1,gradleFiles.size());
//      Path generatedFile = Paths.get(gradleFiles.toArray()[0].toString());
//      Assert.assertTrue("build.gradle is missing", Files.isRegularFile(generatedFile));
//      String actualContents = new String(Files.readAllBytes(generatedFile));
//
//      Path expectedFile = Paths.get("src/test/resources/expectedfiles/build.gradle.expected");
//      String expectedContents = new String(Files.readAllBytes(expectedFile));
//
//      Assert.assertEquals(expectedContents, actualContents);
//   }
//
//   private void createSettings() throws IOException {
//      try {
//         Files.createFile(outputDir.resolve("settings.gradle"));
//      } catch (FileAlreadyExistsException e) {
//         // ignore
//      }
//   }
}
