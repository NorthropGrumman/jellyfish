package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.template.MockedTemplateService;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class CreateJavaCucumberTestsCommandIT {

   private CreateJavaCucumberTestsCommand command = new CreateJavaCucumberTestsCommand();

   private MockedTemplateService templateService;

   private DefaultParameterCollection parameters;

   @Rule
   public final TemporaryFolder tempFolder = new TemporaryFolder();

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
      outputDirectory = tempFolder.newFolder().toPath();

      templateService = new MockedTemplateService().useRealPropertyService().setTemplateDirectory(
         CreateJavaCucumberTestsCommandIT.class.getPackage().getName(),
         Paths.get("src", "main", "template"));

      parameters = new DefaultParameterCollection();
      when(options.getParameters()).thenReturn(parameters);

      // Setup mock system descriptor
      when(options.getSystemDescriptor()).thenReturn(systemDescriptor);

      // Setup mock model
      when(model.getParent()).thenReturn(mock(IPackage.class));

      command.setLogService(logService);
      command.setPromptService(promptUserService);
      command.setTemplateService(templateService);
      command.activate();
   }

   private void setupModel(Path inputDirectory, String pkg, String name) {
      when(options.getSystemDescriptorProjectPath()).thenReturn(inputDirectory);
      when(systemDescriptor.findModel(pkg + '.' + name)).thenReturn(Optional.of(model));
      when(model.getParent().getName()).thenReturn(pkg);
      when(model.getName()).thenReturn(name);
      when(model.getFullyQualifiedName()).thenReturn(pkg + '.' + name);
   }

   @Test
   public void testDoesGenerateANewProjectAndCopyFeatureFiles() throws IOException, URISyntaxException {
      Path inputDir = Paths.get("src", "test", "resources");
      setupModel(inputDir, "com.ngc.seaside.testeval", "HamburgerService");

      parameters.addParameter(new DefaultParameter<>(CreateJavaCucumberTestsCommand.MODEL_PROPERTY,
         model.getFullyQualifiedName()));
      parameters.addParameter(new DefaultParameter<>(CreateJavaCucumberTestsCommand.OUTPUT_DIRECTORY_PROPERTY,
         outputDirectory.toString()));

      command.run(options);

      Path projectDir = outputDirectory.resolve(model.getFullyQualifiedName().toLowerCase() + ".tests");

      Path javaDir = projectDir.resolve(
         Paths.get("src", "main", "java", model.getFullyQualifiedName().replace('.', File.separatorChar), "tests"));
      Assert.assertTrue(Files.isDirectory(javaDir));

      Path featureDir = projectDir.resolve(Paths.get("src",
         "main",
         "resources",
         model.getParent().getName().replace('.', File.separatorChar)));
      Assert.assertTrue(Files.isDirectory(featureDir));

      Path addBacon = featureDir.resolve("HamburgerService.addBacon.feature");
      Path removeTheCheese = featureDir.resolve("HamburgerService.removeTheCheese.feature");
      Assert.assertTrue(Files.isRegularFile(addBacon));
      Assert.assertTrue(Files.isRegularFile(removeTheCheese));
   }

   @Test
   public void testDoesRefreshFeatureFilesOnly() throws Throwable {
      Path inputDir = Paths.get("src", "test", "resources");
      setupModel(inputDir, "com.ngc.seaside.testeval", "HamburgerService");

      parameters.addParameter(new DefaultParameter<>(CreateJavaCucumberTestsCommand.MODEL_PROPERTY,
         model.getFullyQualifiedName()));
      parameters.addParameter(new DefaultParameter<>(CreateJavaCucumberTestsCommand.OUTPUT_DIRECTORY_PROPERTY,
         outputDirectory.toString()));
      parameters.addParameter(
         new DefaultParameter<>(CreateJavaCucumberTestsCommand.REFRESH_FEATURE_FILES_PROPERTY, "true"));

      command.run(options);

      Path projectDir = outputDirectory.resolve(model.getFullyQualifiedName().toLowerCase() + ".tests");

      Path javaDir = projectDir.resolve(
         Paths.get("src", "main", "java"));
      Assert.assertFalse(Files.isDirectory(javaDir));

      Path featureDir = projectDir.resolve(Paths.get("src",
         "main",
         "resources",
         model.getParent().getName().replace('.', File.separatorChar)));
      Assert.assertTrue(Files.isDirectory(featureDir));

      Path addBacon = featureDir.resolve("HamburgerService.addBacon.feature");
      Path removeTheCheese = featureDir.resolve("HamburgerService.removeTheCheese.feature");
      Assert.assertTrue(Files.isRegularFile(addBacon));
      Assert.assertTrue(Files.isRegularFile(removeTheCheese));

   }

}
