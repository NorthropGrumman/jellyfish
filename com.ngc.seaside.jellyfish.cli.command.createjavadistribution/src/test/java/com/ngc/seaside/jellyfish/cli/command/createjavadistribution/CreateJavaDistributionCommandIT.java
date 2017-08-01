package com.ngc.seaside.jellyfish.cli.command.createjavadistribution;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import com.ngc.blocs.guice.module.LogServiceModule;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.service.impl.templateservice.TemplateServiceGuiceWrapper;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.template.MockedTemplateService;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.basic.SystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreateJavaDistributionCommandIT {

   private static final Module TEST_SERVICE_MODULE = new AbstractModule() {
      @Override
      protected void configure() {
         bind(ILogService.class).to(PrintStreamLogService.class);
         bind(ITemplateService.class).to(TemplateServiceGuiceWrapper.class);
      }
   };
   private static final Injector injector = Guice.createInjector(getModules());
   private CreateJavaDistributionCommand cmd = new CreateJavaDistributionCommand();
   private IPromptUserService promptUserService = mock(IPromptUserService.class);
   private IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class);
   private ISystemDescriptor systemDescriptor = mock(SystemDescriptor.class);
   private IModel model = mock(Model.class);
   private Path outputDir;

   private static Collection<Module> getModules() {
      Collection<Module> modules = new ArrayList<>();
      modules.add(TEST_SERVICE_MODULE);
      for (Module dynamicModule : ServiceLoader.load(Module.class)) {
         if (!(dynamicModule instanceof LogServiceModule)) {
            modules.add(dynamicModule);
         }
      }

      modules.removeIf(m -> m instanceof XTextSystemDescriptorServiceModule);
      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
      return modules;
   }

   /**
    * @param folder must be a folder.
    */
   private static String printDirectoryTree(File folder) {
      if (!folder.isDirectory()) {
         throw new IllegalArgumentException("folder is not a Directory");
      }
      int indent = 0;
      StringBuilder sb = new StringBuilder();
      printDirectoryTree(folder, indent, sb);
      return sb.toString();
   }

   private static void printDirectoryTree(File folder, int indent,
                                          StringBuilder sb) {
      if (!folder.isDirectory()) {
         throw new IllegalArgumentException("folder is not a Directory");
      }
      sb.append(getIndentString(indent));
      sb.append("+--");
      sb.append(folder.getName());
      sb.append("/");
      sb.append("\n");

      Preconditions.checkNotNull(folder.listFiles());
      for (File file : folder.listFiles()) {
         if (file.isDirectory()) {
            printDirectoryTree(file, indent + 1, sb);
         } else {
            printFile(file, indent + 1, sb);
         }
      }

   }

   private static void printFile(File file, int indent, StringBuilder sb) {
      sb.append(getIndentString(indent));
      sb.append("+--");
      sb.append(file.getName());
      sb.append("\n");
   }

   private static String getIndentString(int indent) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < indent; i++) {
         sb.append("|  ");
      }
      return sb.toString();
   }

   @Before
   public void setup() throws IOException {
      // Setup test resources
      Properties props = System.getProperties();
      props.setProperty("NG_FW_HOME", Paths.get("src/main").toAbsolutePath().toString());

      Path outputDirectory = Paths.get("build/test-template");
      outputDir = Files.createDirectories(outputDirectory);

      // Use the testable template service.
      MockedTemplateService mockedTemplateService = new MockedTemplateService()
               .useRealPropertyService()
               .useDefaultUserValues(true)
               .setTemplateDirectory(CreateJavaDistributionCommand.class.getPackage().getName(),
                                     Paths.get("src/main/template"));

      // Setup mock system descriptor
      when(options.getSystemDescriptor()).thenReturn(systemDescriptor);
      when(systemDescriptor.findModel("com.ngc.seaside.test.Model")).thenReturn(Optional.of(model));

      // Setup mock model
      when(model.getParent()).thenReturn(mock(IPackage.class));
      when(model.getParent().getName()).thenReturn("com.ngc.seaside.test");
      when(model.getName()).thenReturn("Model");
      when(model.getFullyQualifiedName()).thenReturn("com.ngc.seaside.test.Model");

      cmd.setLogService(injector.getInstance(ILogService.class));
      cmd.setPromptService(promptUserService);
      cmd.setTemplateService(mockedTemplateService);

   }

   @Test
   public void testCommand() throws IOException {
      createSettings();

      runCommand(CreateJavaDistributionCommand.MODEL_PROPERTY, "com.ngc.seaside.test.Model",
                 CreateJavaDistributionCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString());

      Mockito.verify(options, Mockito.times(1)).getParameters();
      Mockito.verify(options, Mockito.times(1)).getSystemDescriptor();
      System.out.println(printDirectoryTree(outputDir.toFile()));
      checkGradleBuild(outputDir);
      checkLogContents(outputDir);
   }

   private void runCommand(String... keyValues) {
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n + 1 < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter<String>(keyValues[n]).setValue(keyValues[n + 1]));
      }

      Mockito.when(options.getParameters()).thenReturn(collection);

      cmd.run(options);
   }

   private void checkLogContents(Path projectDir) throws IOException {
      PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**log4j.xml");
      Collection<Path> gradleFiles = Files.walk(projectDir).filter(matcher::matches).collect(Collectors.toSet());

      // There should only be one log4j.xml file generated
      Assert.assertEquals(1, gradleFiles.size());
      Path generatedFile = Paths.get(gradleFiles.toArray()[0].toString());
      Assert.assertTrue("log4j.xml is missing", Files.isRegularFile(generatedFile));
      String actualContents = new String(Files.readAllBytes(generatedFile));

      Path expectedFile = Paths.get("src/test/resources/expectedfiles/log4j.xml.expected");
      String expectedContents = new String(Files.readAllBytes(expectedFile));

      Assert.assertEquals(expectedContents, actualContents);
   }

   private void checkGradleBuild(Path projectDir) throws IOException {
      PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**build.gradle");
      Collection<Path> gradleFiles = Files.walk(projectDir).filter(matcher::matches).collect(Collectors.toSet());

      // There should only be one build.gradle generated
      Assert.assertEquals(1,gradleFiles.size());
      Path generatedFile = Paths.get(gradleFiles.toArray()[0].toString());
      Assert.assertTrue("build.gradle is missing", Files.isRegularFile(generatedFile));
      String actualContents = new String(Files.readAllBytes(generatedFile));

      Path expectedFile = Paths.get("src/test/resources/expectedfiles/build.gradle.expected");
      String expectedContents = new String(Files.readAllBytes(expectedFile));

      Assert.assertEquals(expectedContents, actualContents);
   }

   private void createSettings() throws IOException {
      try {
         Files.createFile(outputDir.resolve("settings.gradle"));
      } catch (FileAlreadyExistsException e) {
         // ignore
      }
   }
}
