package com.ngc.seaside.jellyfish.cli.command.createprotocolbuffermessages;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import com.ngc.blocs.guice.module.LogServiceModule;
import com.ngc.blocs.guice.module.ResourceServiceModule;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.blocs.test.impl.common.resource.MockedResourceService;
import com.ngc.seaside.bootstrap.service.impl.templateservice.TemplateServiceGuiceModule;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createdomain.CreateDomainCommand;
import com.ngc.seaside.jellyfish.cli.command.test.template.MockedTemplateService;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class CreateProtocolbufferMessagesCommandIT {

   private IJellyFishCommand cmd;

   private MockedResourceService resourceService;
   private Path outputDir;
   private Path velocityPath;

   @Mock
   private IJellyFishCommandOptions options;

   @Before
   public void setup() throws IOException {
      outputDir = Files.createTempDirectory(null);
      outputDir.toFile().deleteOnExit();
      velocityPath = Paths.get("src", "test", "resources", "proto-messages.vm").toAbsolutePath();

      resourceService = new MockedResourceService()
            .onNextReadDrain(
                  CreateProtocolbufferMessagesCommandIT.class
                        .getClassLoader()
                        .getResourceAsStream(CreateProtocolbufferMessagesCommand.TEMPLATE_FILE));

      Injector injector = Guice.createInjector(getModules());
      cmd = injector.getInstance(CreateProtocolbufferMessagesCommandGuiceWrapper.class);

      Path sdDir = Paths.get("src", "test", "sd");
      PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.sd");
      Collection<Path> sdFiles = Files.walk(sdDir).filter(matcher::matches).collect(Collectors.toSet());
      ISystemDescriptorService sdService = injector.getInstance(ISystemDescriptorService.class);
      IParsingResult result = sdService.parseFiles(sdFiles);
      Assert.assertTrue(result.getIssues().toString(), result.isSuccessful());
      ISystemDescriptor sd = result.getSystemDescriptor();
      Mockito.when(options.getParsingResult()).thenReturn(result);
      Mockito.when(options.getSystemDescriptor()).thenReturn(sd);
   }

   @Test
   public void testCommand() throws IOException {
      runCommand(CreateDomainCommand.MODEL_PROPERTY, "com.ngc.seaside.test1.Model1",
                 CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
                 CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString());

      Path projectDir = outputDir.resolve("generated-projects/com.ngc.seaside.test1.model1.messages");
      Assert.assertTrue("Cannot find project directory: " + projectDir, Files.isDirectory(projectDir));
      checkGradleBuild(projectDir, "com.ngc.blocs.gradle.plugin.domain");
      checkVelocity(projectDir);
      checkDomain(projectDir);
   }

   @Test
   public void testExtensionOverride() throws IOException {
      runCommand(CreateDomainCommand.MODEL_PROPERTY, "com.ngc.seaside.test1.Model1",
                 CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
                 CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString());

      Path projectDir = outputDir.resolve("generated-projects/com.ngc.seaside.test1.model1.messages");
      Assert.assertTrue("Cannot find project directory: " + projectDir, Files.isDirectory(projectDir));
   }
   
   @Test
   public void testCommandArtifactId() throws IOException {
      final String artifact = "test.artifact.g1";
      runCommand(CreateDomainCommand.MODEL_PROPERTY, "com.ngc.seaside.test1.Model1",
         CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString(),
         CreateDomainCommand.ARTIFACT_ID_PROPERTY, artifact);
      
      Path projectDir = outputDir.resolve("generated-projects/com.ngc.seaside.test1." + artifact);
      Assert.assertTrue("Cannot find project directory: " + projectDir, Files.isDirectory(projectDir));
   }
   
   @Test
   public void testCommandProjectGenerator() throws IOException {
      Function<IData, String> packageGenerator = (d) -> "foo";
      
      runCommand(CreateDomainCommand.MODEL_PROPERTY, "com.ngc.seaside.test1.Model1",
         CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString(),
         CreateDomainCommand.PACKAGE_GENERATOR_PROPERTY, packageGenerator);
      
      Path projectDir = outputDir.resolve("generated-projects/com.ngc.seaside.test1.model1.messages");
      Assert.assertTrue("Cannot find project directory: " + projectDir, Files.isDirectory(projectDir));
      checkGradleBuild(projectDir, "com.ngc.seaside.test1.model1");
      checkGradleBuildDoesntContain(projectDir, "foo");
   }

   @Test
   public void testGradleBuildForProtoBufferItems() throws IOException {
      runCommand(CreateDomainCommand.MODEL_PROPERTY, "com.ngc.seaside.test1.Model1",
                 CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
                 CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString());

      Path projectDir = outputDir.resolve("generated-projects/com.ngc.seaside.test1.model1.messages");
      Assert.assertTrue("Cannot find project directory: " + projectDir, Files.isDirectory(projectDir));
      checkGradleBuild(projectDir, "com.google.protobuf", "src/main/resources/velocity/proto-messages.vm");
   }

   private void runCommand(Object... keyValues) {
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n + 1 < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter<>((String) keyValues[n]).setValue(keyValues[n + 1]));
      }
      Mockito.when(options.getParameters()).thenReturn(collection);
      cmd.run(options);
   }

   private void checkGradleBuild(Path projectDir, String... fileContents) throws IOException {
      Path buildFile = projectDir.resolve("build.gradle");
      Assert.assertTrue("build.gradle is missing", Files.isRegularFile(buildFile));
      String contents = new String(Files.readAllBytes(buildFile));
      Assert.assertTrue(contents.contains(velocityPath.getFileName().toString()));
      for (String content : fileContents) {
         Assert.assertTrue("Expected \"" + content + "\" in build.gradle", contents.contains(content));
      }
   }
   
   private void checkGradleBuildDoesntContain(Path projectDir, String... fileContents) throws IOException {
      Path buildFile = projectDir.resolve("build.gradle");
      Assert.assertTrue("build.gradle is missing", Files.isRegularFile(buildFile));
      String contents = new String(Files.readAllBytes(buildFile));
      Assert.assertTrue(contents.contains(velocityPath.getFileName().toString()));
      for (String content : fileContents) {
         Assert.assertFalse("Didn't expect \"" + content + "\" in build.gradle", contents.contains(content));
      }
   }

   private void checkVelocity(Path projectDir) {
      Path velocityFolder = projectDir.resolve(Paths.get("src", "main", "resources", "velocity"));
      Assert.assertTrue("Could not find velocity folder", Files.isDirectory(velocityFolder));
      Assert.assertTrue("Could not find velocity file: " + velocityPath.getFileName(),
                        Files.isRegularFile(velocityFolder.resolve(velocityPath.getFileName())));
   }

   private void checkDomain(Path projectDir) throws IOException {
      Path domainDir = projectDir.resolve(Paths.get("src", "main", "resources", "domain"));
      checkDomainFiles(domainDir, "com.ngc.seaside.test1", "com.ngc.seaside.test2", "com.ngc.seaside.test1.test3");
      checkDomainContents(domainDir, "com.ngc.seaside.test1", 1, 2, 0, 5);
      checkDomainContents(domainDir, "com.ngc.seaside.test2", 3, 4, 6, 7);
      checkDomainContents(domainDir, "com.ngc.seaside.test1.test3", 5, 5, 8, 8);
   }

   private void checkDomainFiles(Path domainDir, String... filenames) throws IOException {
      try {
         Assert.assertEquals(filenames.length, Files.list(domainDir).count());
      } catch (NoSuchFileException e) {
         Assert.assertEquals(0, filenames.length);
      }
      for (String pkg : filenames) {
         Assert.assertTrue("Missing file: " + pkg + ".xml", Files.isRegularFile(domainDir.resolve(pkg + ".xml")));
      }
   }

   private void checkDomainContents(Path domainDir, String filename, int startData, int endData, int startField,
                                    int endField)
         throws IOException {
      Path file = domainDir.resolve(filename + ".xml");
      String text = new String(Files.readAllBytes(file));
      for (int n = startData; n <= endData; n++) {
         Assert.assertTrue("Couldn't find Data" + n + " in " + file, text.contains("Data" + n));
      }
      Assert.assertFalse(Pattern.compile("Data[^" + startData + "-" + endData + "]").matcher(text).find());

      for (int n = startField; n <= endField; n++) {
         Assert.assertTrue("Couldn't find field" + n + " in " + file, text.contains("field" + n));
      }
      Assert.assertFalse(Pattern.compile("field[^" + startField + "-" + endField + "]").matcher(text).find());
   }

   private final Module testServiceModule = new AbstractModule() {
      @Override
      protected void configure() {
         bind(ILogService.class).to(PrintStreamLogService.class);
         MockedTemplateService mockedTemplateService = new MockedTemplateService()
               .useRealPropertyService()
               .useDefaultUserValues(true)
               .setTemplateDirectory(
                     CreateProtocolbufferMessagesCommand.class.getPackage().getName(),
                     Paths.get("src/main/template"));

         bind(ITemplateService.class).toInstance(mockedTemplateService);
         bind(IResourceService.class).toInstance(resourceService);
      }
   };

   private Collection<Module> getModules() {
      Collection<Module> modules = new ArrayList<>();
      modules.add(testServiceModule);
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

}
