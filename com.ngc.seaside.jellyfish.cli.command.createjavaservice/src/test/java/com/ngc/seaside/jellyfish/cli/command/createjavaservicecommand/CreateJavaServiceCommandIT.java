package com.ngc.seaside.jellyfish.cli.command.createjavaservicecommand;

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

public class CreateJavaServiceCommandIT {

   private static final Module TEST_SERVICE_MODULE = new AbstractModule() {
      @Override
      protected void configure() {
         bind(ILogService.class).to(PrintStreamLogService.class);
         bind(ITemplateService.class).to(TemplateServiceGuiceWrapper.class);
      }
   };
   private static final Injector injector = Guice.createInjector(getModules());
   private CreateJavaServiceCommand cmd = new CreateJavaServiceCommand();
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

      // Comment the two lines below if you wish to use a known output directory.
      //outputDir = Files.createTempDirectory(null);
      //outputDir.toFile().deleteOnExit();

      // Uncomment the lines below if you wish to view the output directory
      Path outputDirectory = Paths.get("build/test-template");
      outputDir = Files.createDirectories(outputDirectory);

      // Use the testable template service.
      MockedTemplateService mockedTemplateService = new MockedTemplateService()
               .useRealPropertyService()
               .useDefaultUserValues(true)
               .setTemplateDirectory(CreateJavaServiceCommand.class.getPackage().getName(),
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

      runCommand(CreateJavaServiceCommand.MODEL_PROPERTY, "com.ngc.seaside.test.Model",
                 CreateJavaServiceCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString());

      Mockito.verify(options, Mockito.times(1)).getParameters();
      Mockito.verify(options, Mockito.times(1)).getSystemDescriptor();

      printOutputFolderStructure(outputDir);

      //checkGradleBuild(outputDir);
      //checkLogContents(outputDir);
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
      Assert.assertTrue(gradleFiles.size() == 1);
      Path buildFile = Paths.get(gradleFiles.toArray()[0].toString());
      Assert.assertTrue("log4j.xml is missing", Files.isRegularFile(buildFile));
      String contents = new String(Files.readAllBytes(buildFile));

      // Verify that model is injected
      Assert.assertTrue(
               contents.contains("value=\"%d{yyyy-MM-dd HH:mm:ss} [model:" + model.getFullyQualifiedName() + "]"));
      int startLength = contents.indexOf("%d{yyyy-MM-dd HH:mm:ss} [model:" + model.getFullyQualifiedName() + "]");
      int endLength = contents.length();
      String contents2 = contents.substring(startLength, endLength);
      Assert.assertTrue(
               contents2.contains("value=\"%d{yyyy-MM-dd HH:mm:ss} [model:" + model.getFullyQualifiedName() + "]"));
      Assert.assertTrue(contents2.contains("value=\"${NG_FW_HOME}/logs/" + model.getFullyQualifiedName() + ".log\""));
   }

   private void printOutputFolderStructure(Path outputDir) {
      String ouputFolderTree = printDirectoryTree(outputDir.toFile());
      Assert.assertTrue(ouputFolderTree.contains(model.getName()+ ".java"));
      Assert.assertTrue(ouputFolderTree.contains(model.getName()+ "Test.java"));
      System.out.println(ouputFolderTree);
   }

   private void checkGradleBuild(Path projectDir) throws IOException {
      PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**build.gradle");
      Collection<Path> gradleFiles = Files.walk(projectDir).filter(matcher::matches).collect(Collectors.toSet());

      // There should only be one build.gradle generated
      Assert.assertTrue(gradleFiles.size() == 1);
      Path buildFile = Paths.get(gradleFiles.toArray()[0].toString());
      Assert.assertTrue("build.gradle is missing", Files.isRegularFile(buildFile));
      String contents = new String(Files.readAllBytes(buildFile));

      // Verify apply block
      Assert.assertTrue(contents.contains("apply plugin: 'com.ngc.seaside.distribution'"));

      //Verify seaside distribution block
      Assert.assertTrue(contents.contains("seasideDistribution {"));
      Assert.assertTrue(contents.contains("buildDir = 'build'"));
      Assert.assertTrue(contents.contains("distributionName = \"${groupId}.${project.name}-${version}\""));
      Assert.assertTrue(
               contents.contains("distributionDir = \"build/distribution/${group}.${project.name}-${version}\""));
      Assert.assertTrue(contents.contains("distributionDestDir = 'build/distribution/'"));

      // Verify dependencies block
      Assert.assertTrue(contents.contains("dependencies {"));
      Assert.assertTrue(contents.contains("bundles project(\":" + model.getName().toLowerCase() + ".events\")"));
      Assert.assertTrue(contents.contains("bundles project(\":" + model.getName().toLowerCase() + ".domain\")"));
      Assert.assertTrue(contents.contains("bundles project(\":" + model.getName().toLowerCase() + ".connector\")"));
      Assert.assertTrue(contents.contains("bundles project(\":" + model.getName().toLowerCase() + ".base\")"));
      Assert.assertTrue(contents.contains("bundles project(\":" + model.getName().toLowerCase() + "\")"));
   }

   private void createSettings() throws IOException {
      try {
         Files.createFile(outputDir.resolve("settings.gradle"));
      } catch (FileAlreadyExistsException e) {
         // ignore
      }
   }
}
