package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.template.MockedTemplateService;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.EnumDto;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureInformation;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
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
import java.util.TreeMap;

@RunWith(MockitoJUnitRunner.class)
public class CreateJavaCucumberTestsCommandIT {

   private CreateJavaCucumberTestsCommand command = new CreateJavaCucumberTestsCommand();

   private MockedTemplateService templateService;

   private DefaultParameterCollection parameters;

   @Rule
   public final TemporaryFolder tempFolder = new TemporaryFolder();

   private Path outputDirectory;

   @Mock
   private IJellyFishCommandOptions options;

   @Mock
   private ISystemDescriptor systemDescriptor;

   @Mock
   private IModel model;

   @Mock
   private ILogService logService;
   
   @Mock
   private IJavaServiceGenerationService generationService;
   
   @Mock
   private IProjectNamingService projectService;
   
   @Mock
   private IPackageNamingService packageService;
   
   @Mock
   private IFeatureService featureService;
   
   
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
      command.setTemplateService(templateService);
      command.setProjectNamingService(projectService);
      command.setPackageNamingService(packageService);
      command.setJavaServiceGenerationService(generationService);
      command.setFeatureService(featureService);
      command.activate();
   }

   @SuppressWarnings("unchecked")
   private void setupModel(Path inputDirectory, String pkg, String name) {
      when(options.getSystemDescriptorProjectPath()).thenReturn(inputDirectory);
      when(systemDescriptor.findModel(pkg + '.' + name)).thenReturn(Optional.of(model));
      when(model.getParent().getName()).thenReturn(pkg);
      when(model.getName()).thenReturn(name);
      when(model.getFullyQualifiedName()).thenReturn(pkg + '.' + name);

      IProjectInformation info = mock(IProjectInformation.class);
      when(projectService.getCucumberTestsProjectName(any(), eq(model))).thenReturn(info);
      when(info.getDirectoryName()).thenReturn(pkg + "." + name.toLowerCase() + ".tests");
      when(info.getArtifactId()).thenReturn(name.toLowerCase() + ".tests");
      when(info.getGroupId()).thenReturn(pkg);
      
      info = mock(IProjectInformation.class);
      when(projectService.getBaseServiceProjectName(any(), eq(model))).thenReturn(info);
      when(info.getArtifactId()).thenReturn(name.toLowerCase() + ".base.impl");

      info = mock(IProjectInformation.class);
      when(projectService.getMessageProjectName(any(), eq(model))).thenReturn(info);
      when(info.getArtifactId()).thenReturn(name.toLowerCase() + ".messages");

      when(packageService.getCucumberTestsPackageName(any(), eq(model))).thenReturn(pkg + "." + name.toLowerCase() + ".tests");
      
      @SuppressWarnings("rawtypes")
      EnumDto mockEnum = mock(EnumDto.class);
      when(generationService.getTransportTopicsDescription(any(), eq(model))).thenReturn(mockEnum);
      when(mockEnum.getFullyQualifiedName()).thenReturn(pkg + "." + name.toLowerCase() + ".transport.topics." + name + "TransportTopics");
      
      IFeatureInformation featureInfo0 = mock(IFeatureInformation.class);
      Path relPath = Paths.get("com", "ngc", "seaside", "testeval", "HamburgerService.removeTheCheese.feature");
      Path absPath = Paths.get("src", "test", "resources", "src", "test", "gherkin", "com", "ngc", "seaside", "testeval", "HamburgerService.removeTheCheese.feature").toAbsolutePath();
      when(featureInfo0.getRelativePath()).thenReturn(relPath);
      when(featureInfo0.getAbsolutePath()).thenReturn(absPath);
      IFeatureInformation featureInfo1 = mock(IFeatureInformation.class);
      relPath = Paths.get("com", "ngc", "seaside", "testeval", "HamburgerService.addBacon.feature");
      absPath = Paths.get("src", "test", "resources", "src", "test", "gherkin", "com", "ngc", "seaside", "testeval", "HamburgerService.addBacon.feature").toAbsolutePath();     
      when(featureInfo1.getRelativePath()).thenReturn(relPath);
      when(featureInfo1.getAbsolutePath()).thenReturn(absPath);
      TreeMap<String, IFeatureInformation> featureMap = new TreeMap<>();
      featureMap.put("a", featureInfo0);
      featureMap.put("b", featureInfo1);
      when(featureService.getFeatures(any(), eq(model))).thenReturn(featureMap);
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
         Paths.get("src",
                   "main",
                   "java",
                   model.getFullyQualifiedName().replace('.', File.separatorChar).toLowerCase(),
                   "tests"));
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
