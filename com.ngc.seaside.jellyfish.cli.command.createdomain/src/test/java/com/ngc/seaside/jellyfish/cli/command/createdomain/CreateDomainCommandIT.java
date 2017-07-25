package com.ngc.seaside.jellyfish.cli.command.createdomain;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.service.impl.templateservice.TemplateServiceGuiceModule;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.bootstrap.utilities.file.FileUtilitiesException;
import com.ngc.seaside.bootstrap.utilities.file.GradleSettingsUtilities;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.template.MockedTemplateService;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

import org.gradle.tooling.BuildException;
import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CreateDomainCommandIT {

   private IJellyFishCommand cmd = injector.getInstance(CreateDomainCommandGuiceWrapper.class);

   private IJellyFishCommandOptions options = Mockito.mock(IJellyFishCommandOptions.class);

   private Path outputDir;
   private Path velocityPath;

   @Before
   public void setup() throws IOException {
      outputDir = Files.createTempDirectory(null);
      outputDir.toFile().deleteOnExit();
      velocityPath = Paths.get("src", "test", "resources", "service-domain-source.vm").toAbsolutePath();

      Path sdDir = Paths.get("src", "test", "sd");
      PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.sd");
      Collection<Path> sdFiles = Files.walk(sdDir).filter(matcher::matches).collect(Collectors.toSet());
      ISystemDescriptorService sdService = injector.getInstance(ISystemDescriptorService.class);
      IParsingResult result = sdService.parseFiles(sdFiles);
      Assert.assertTrue(result.getIssues().toString(), result.isSuccessful());
      ISystemDescriptor sd = result.getSystemDescriptor();
      Mockito.when(options.getSystemDescriptor()).thenReturn(sd);
   }

   @Test
   public void testCommand() throws IOException, FileUtilitiesException {
      final String model = "com.ngc.seaside.test1.Model1";
      final String groupId = model.substring(0, model.lastIndexOf('.'));
      final String artifactId = model.substring(model.lastIndexOf('.') + 1).toLowerCase() + ".domain";
      runCommand(CreateDomainCommand.MODEL_PROPERTY, model, CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY,
         outputDir.toString(), CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString());

      Path projectDir = outputDir.resolve("com.ngc.seaside.test1.model1.domain");
      Assert.assertTrue("Cannot find project directory: " + projectDir, Files.isDirectory(projectDir));
      checkGradleBuild(projectDir, "com.ngc.seaside.test1.model1.domain");
      checkVelocity(projectDir);
      checkDomain(projectDir);
      checkBuild(groupId, artifactId, "Data1", "Data2", "Data3", "Data4");
   }

   @Test
   public void testWithExtensionCommand() throws IOException {
      final String extension = "asidgoajsdig";
      runCommand(CreateDomainCommand.MODEL_PROPERTY, "com.ngc.seaside.test1.Model1",
         CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString(),
         CreateDomainCommand.EXTENSION_PROPERTY, extension);

      Path projectDir = outputDir.resolve("com.ngc.seaside.test1.model1.domain");
      Assert.assertTrue("Cannot find project directory: " + projectDir, Files.isDirectory(projectDir));
      checkGradleBuild(projectDir, extension, "com.ngc.seaside.test1.model1.domain");
      checkVelocity(projectDir);
      checkDomain(projectDir);
   }

   @Test
   public void testCommandWithEmptyModel() throws IOException {
      runCommand(CreateDomainCommand.MODEL_PROPERTY, "com.ngc.seaside.test1.Model2",
         CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString());

      Path projectDir = outputDir.resolve("com.ngc.seaside.test1.model1.domain");
      Assert.assertFalse("Directory should not be created: " + projectDir, Files.isDirectory(projectDir));
   }

   @Test
   public void testCommandGroupId() throws IOException {
      final String groupId = "net.example.g1";
      runCommand(CreateDomainCommand.MODEL_PROPERTY, "com.ngc.seaside.test1.Model1",
         CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString(),
         CreateDomainCommand.GROUP_ID_PROPERTY, groupId);

      Path projectDir = outputDir.resolve(groupId + ".model1.domain");
      Assert.assertTrue("Cannot find model " + projectDir, Files.isDirectory(projectDir));
      checkGradleBuild(projectDir, groupId + ".model1.domain");
      checkVelocity(projectDir);
      checkDomain(projectDir);
   }

   @Test
   public void testCommandPackageSuffix() throws IOException {
      final String suffix = "example";
      runCommand(CreateDomainCommand.MODEL_PROPERTY, "com.ngc.seaside.test1.Model1",
         CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString(),
         CreateDomainCommand.PACKAGE_SUFFIX_PROPERTY, suffix);

      Path projectDir = outputDir.resolve("com.ngc.seaside.test1.model1." + suffix);
      Assert.assertTrue("Cannot find model " + projectDir, Files.isDirectory(projectDir));
      checkGradleBuild(projectDir, "com.ngc.seaside.test1.model1." + suffix);
      checkVelocity(projectDir);
      checkDomain(projectDir);
   }

   @Test
   public void testCommandArtifactId() throws IOException {
      final String artifact = "test.artifact.g1";
      runCommand(CreateDomainCommand.MODEL_PROPERTY, "com.ngc.seaside.test1.Model1",
         CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString(),
         CreateDomainCommand.ARTIFACT_ID_PROPERTY, artifact);

      Path projectDir = outputDir.resolve("com.ngc.seaside.test1." + artifact + ".domain");
      Assert.assertTrue("Cannot find model " + projectDir, Files.isDirectory(projectDir));
      checkGradleBuild(projectDir, "com.ngc.seaside.test1." + artifact + ".domain");
      checkVelocity(projectDir);
      checkDomain(projectDir);
   }

   @Test
   public void testCommandPackage() throws IOException {
      final String pkg = "com.test.e1.test.artifact.g1";
      runCommand(CreateDomainCommand.MODEL_PROPERTY, "com.ngc.seaside.test1.Model1",
         CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString(),
         CreateDomainCommand.PACKAGE_PROPERTY, pkg);

      Path projectDir = outputDir.resolve(pkg);
      Assert.assertTrue("Cannot find model " + projectDir, Files.isDirectory(projectDir));
      checkVelocity(projectDir);
      checkDomain(projectDir);
   }

   @Test
   public void testCommandUseModelStructure() throws IOException {
      runCommand(CreateDomainCommand.MODEL_PROPERTY, "com.ngc.seaside.test1.Model1",
         CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString(),
         CreateDomainCommand.USE_MODEL_STRUCTURE_PROPERTY, "true");

      Path projectDir = outputDir.resolve("com.ngc.seaside.test1.model1.domain");
      Assert.assertTrue("Cannot find project directory: " + projectDir, Files.isDirectory(projectDir));
      checkGradleBuild(projectDir, "com.ngc.seaside.test1", "com.ngc.seaside.test2", "com.ngc.seaside.test1.test3");
      checkVelocity(projectDir);
      checkDomain(projectDir);
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

   private void runCommand(String... keyValues) {
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n + 1 < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter<String>(keyValues[n]).setValue(keyValues[n + 1]));
      }

      Mockito.when(options.getParameters()).thenReturn(collection);

      cmd.run(options);
   }

   private void checkBuild(String groupId, String artifactId, String... names)
      throws IOException, FileUtilitiesException {
      String projectName = groupId + '.' + artifactId;
      Files.createFile(outputDir.resolve("settings.gradle"));
      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>(CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir));
      parameters.addParameter(new DefaultParameter<>(CreateDomainCommand.GROUP_ID_PROPERTY, groupId));
      parameters.addParameter(new DefaultParameter<>(CreateDomainCommand.ARTIFACT_ID_PROPERTY, artifactId));
      GradleSettingsUtilities.addProject(parameters);
      Files.copy(Paths.get("..", "build.gradle"), outputDir.resolve("build.gradle"));

      String gradleHome = System.getenv("GRADLE_HOME");
      Assert.assertNotNull("GRADLE_HOME not set", gradleHome);
      final ProjectConnection connection = GradleConnector.newConnector()
               .useInstallation(Paths.get(gradleHome).toFile()).forProjectDirectory(outputDir.toFile()).connect();
      try {
         BuildLauncher launcher = connection.newBuild().setColorOutput(false).setStandardError(System.err)
                  .setStandardOutput(System.out).forTasks("build");
         launcher.run();
      } catch (BuildException e) {
         throw new AssertionError("Gradle failed to build generated project (see standard error for details)", e);
      } finally {
         connection.close();
      }

      for (String name : names) {
         Assert.assertTrue("Could not find generated " + name + ".java",
            Files.walk(outputDir.resolve(projectName)).anyMatch(p -> p.endsWith(name + ".java")));
      }
   }

   private static final Module TEST_SERVICE_MODULE = new AbstractModule() {
      @Override
      protected void configure() {
         bind(ILogService.class).to(PrintStreamLogService.class);
         MockedTemplateService mockedTemplateService = new MockedTemplateService();
         mockedTemplateService = new MockedTemplateService().useRealPropertyService().useDefaultUserValues(true)
                  .setTemplateDirectory(CreateDomainCommand.class.getPackage().getName(),
                     Paths.get("src/main/template"));

         bind(ITemplateService.class).toInstance(mockedTemplateService);

         IResourceService mockResource = Mockito.mock(IResourceService.class);
         Mockito.when(mockResource.getResourceRootPath()).thenReturn(Paths.get("src", "main", "resources"));

         bind(IResourceService.class).toInstance(mockResource);
      }
   };

   private static Collection<Module> getModules() {
      Collection<Module> modules = new ArrayList<>();
      modules.add(TEST_SERVICE_MODULE);
      for (Module dynamicModule : ServiceLoader.load(Module.class)) {
         if (!(dynamicModule instanceof TemplateServiceGuiceModule)) {
            modules.add(dynamicModule);
         }
      }

      modules.removeIf(m -> m instanceof XTextSystemDescriptorServiceModule);
      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
      return modules;
   }

   private static final Injector injector = Guice.createInjector(getModules());

}
