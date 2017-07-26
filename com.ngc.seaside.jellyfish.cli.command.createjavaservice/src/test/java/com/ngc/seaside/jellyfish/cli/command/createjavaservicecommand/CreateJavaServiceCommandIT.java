package com.ngc.seaside.jellyfish.cli.command.createjavaservicecommand;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import com.ngc.blocs.guice.module.LogServiceModule;
import com.ngc.blocs.guice.module.ResourceServiceModule;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.service.impl.templateservice.TemplateServiceGuiceModule;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.template.MockedTemplateService;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
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
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;

public class CreateJavaServiceCommandIT {

   private static final Module TEST_SERVICE_MODULE = new AbstractModule() {
      @Override
      protected void configure() {
         bind(ILogService.class).to(PrintStreamLogService.class);
         MockedTemplateService mockedTemplateService = new MockedTemplateService();
         mockedTemplateService = new MockedTemplateService().useRealPropertyService().useDefaultUserValues(true)
                  .setTemplateDirectory(CreateJavaServiceCommand.class.getPackage().getName(),
                                        Paths.get("src/main/template"));

         bind(ITemplateService.class).toInstance(mockedTemplateService);

         IResourceService mockResource = Mockito.mock(IResourceService.class);
         Mockito.when(mockResource.getResourceRootPath()).thenReturn(Paths.get("src", "main", "resources"));

         bind(IResourceService.class).toInstance(mockResource);
      }
   };
   private static final Injector injector = Guice.createInjector(getModules());
   private IJellyFishCommand cmd = injector.getInstance(CreateJavaServiceCommandGuiceWrapper.class);
   private IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class);
   private IModel model;
   private Path outputDir;

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

   private static Collection<Module> getModules() {
      Collection<Module> modules = new ArrayList<>();
      modules.add(TEST_SERVICE_MODULE);
      for (Module dynamicModule : ServiceLoader.load(Module.class)) {
         if (!(dynamicModule instanceof LogServiceModule) && !(dynamicModule instanceof ResourceServiceModule)
             && !(dynamicModule instanceof TemplateServiceGuiceModule)) {
            modules.add(dynamicModule);
         }
      }

      modules.removeIf(m -> m instanceof XTextSystemDescriptorServiceModule);
      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
      return modules;
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
      Path sdDir = Paths.get("src", "test", "sd");
      PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.sd");
      Collection<Path> sdFiles = Files.walk(sdDir).filter(matcher::matches).collect(Collectors.toSet());
      ISystemDescriptorService sdService = injector.getInstance(ISystemDescriptorService.class);
      IParsingResult result = sdService.parseFiles(sdFiles);
      Assert.assertTrue(result.getIssues().toString(), result.isSuccessful());
      ISystemDescriptor sd = result.getSystemDescriptor();
      String modelId = "com.ngc.seaside.test1.Model1";
      model = sd.findModel(modelId).orElseThrow(() -> new CommandException("Unknown model:" + modelId));
      Mockito.when(options.getSystemDescriptor()).thenReturn(sd);

   }

   @Test
   public void testCommand() throws IOException {
      createSettings();

      runCommand(CreateJavaServiceCommand.MODEL_PROPERTY, "com.ngc.seaside.test1.Model1",
                 CreateJavaServiceCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString());

      Mockito.verify(options, Mockito.times(1)).getParameters();
      Mockito.verify(options, Mockito.times(1)).getSystemDescriptor();

      printOutputFolderStructure(outputDir);

      model.getScenarios().forEach(iScenario -> {
         System.out.println("SCENARIO:" + iScenario.getWhens().toString());
          iScenario.getWhens().forEach( iScenarioStep -> iScenarioStep.getParameters().forEach(s -> {
             System.out.println(s);
             System.out.println( model.getInputs().getByName(s).get().getType().getName());
             //System.out.println(iScenarioStep.);
          }));

      });
      model.getScenarios().forEach(iScenario -> System.out.println("SCENARIO:" + iScenario.getThens().toString()));

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
      Assert.assertTrue(ouputFolderTree.contains(model.getName() + ".java"));
      Assert.assertTrue(ouputFolderTree.contains(model.getName() + "Test.java"));
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
