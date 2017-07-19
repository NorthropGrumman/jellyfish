package com.ngc.seaside.jellyfish.cli.command.createdomain;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.ngc.blocs.guice.module.LogServiceModule;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

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
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CreateDomainCommandTest {

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

   @Test(expected = CommandException.class)
   public void testNoModelsWithData() throws IOException {
      Path sdDir = Paths.get("src", "test", "sd");
      PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.sd");
      Collection<Path> sdFiles = Files.walk(sdDir).filter(matcher::matches).filter(sd -> {
         try {
            String contents = new String(Files.readAllBytes(sd));
            return !contents.contains("model");
         } catch (IOException e) {
            return false;
         }
      }).collect(Collectors.toSet());
      sdFiles = new HashSet<>(sdFiles);
      sdFiles.add(sdDir.resolve(Paths.get("com", "ngc", "seaside", "test1", "Model2.sd")));
      ISystemDescriptorService sdService = injector.getInstance(ISystemDescriptorService.class);
      IParsingResult result = sdService.parseFiles(sdFiles);
      Assert.assertTrue(result.getIssues().toString(), result.isSuccessful());
      ISystemDescriptor sd = result.getSystemDescriptor();
      Mockito.when(options.getSystemDescriptor()).thenReturn(sd);

      runCommand(CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString());
   }

   @Test(expected = CommandException.class)
   public void testNoModels() throws IOException {
      Path sdDir = Paths.get("src", "test", "sd");
      PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.sd");
      Collection<Path> sdFiles = Files.walk(sdDir).filter(matcher::matches).filter(sd -> {
         try {
            String contents = new String(Files.readAllBytes(sd));
            return !contents.contains("model");
         } catch (IOException e) {
            return false;
         }
      }).collect(Collectors.toSet());
      ISystemDescriptorService sdService = injector.getInstance(ISystemDescriptorService.class);
      IParsingResult result = sdService.parseFiles(sdFiles);
      Assert.assertTrue(result.getIssues().toString(), result.isSuccessful());
      ISystemDescriptor sd = result.getSystemDescriptor();
      Mockito.when(options.getSystemDescriptor()).thenReturn(sd);

      runCommand(CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString());
   }

   @Test
   public void testCommand() throws IOException {
      runCommand(CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString());

      Path model1 = outputDir.resolve("com.ngc.seaside.test1.model1.domainmodel");
      Assert.assertTrue("Cannot find model " + model1, Files.isDirectory(model1));
      checkGradleBuild(model1, "com.ngc.seaside.test1.model1.domainmodel");
      checkVelocity(model1);
      checkDomainFiles(model1, "com.ngc.seaside.test1", "com.ngc.seaside.test2", "com.ngc.seaside.test1.test3",
         "com.ngc.seaside.test2.test4");
      checkDomainContents(model1, "com.ngc.seaside.test1", 1, 2, 0, 5);
      checkDomainContents(model1, "com.ngc.seaside.test2", 3, 4, 6, 7);
      checkDomainContents(model1, "com.ngc.seaside.test1.test3", 5, 5, 8, 8);
      checkDomainContents(model1, "com.ngc.seaside.test2.test4", 6, 6, 9, 9);

      Path model3 = outputDir.resolve("com.ngc.seaside.test2.model3.domainmodel");
      Assert.assertTrue("Cannot find model " + model3, Files.isDirectory(model3));
      checkGradleBuild(model3, "com.ngc.seaside.test2.model3.domainmodel");
      checkVelocity(model3);
      checkDomainFiles(model3, "com.ngc.seaside.test1", "com.ngc.seaside.test2");
      checkDomainContents(model3, "com.ngc.seaside.test1", 1, 2, 0, 5);
      checkDomainContents(model3, "com.ngc.seaside.test2", 3, 4, 6, 7);

      Path model4 = outputDir.resolve("com.ngc.seaside.test1.test3.model4.domainmodel");
      Assert.assertTrue("Cannot find model " + model4, Files.isDirectory(model4));
      checkGradleBuild(model4, "com.ngc.seaside.test1.test3.model4.domainmodel");
      checkVelocity(model4);
      checkDomainFiles(model4, "com.ngc.seaside.test1", "com.ngc.seaside.test2", "com.ngc.seaside.test1.test3",
         "com.ngc.seaside.test2.test4");
      checkDomainContents(model4, "com.ngc.seaside.test1", 1, 2, 0, 5);
      checkDomainContents(model4, "com.ngc.seaside.test2", 3, 4, 6, 7);
      checkDomainContents(model4, "com.ngc.seaside.test1.test3", 5, 5, 8, 8);
      checkDomainContents(model4, "com.ngc.seaside.test2.test4", 6, 6, 9, 9);

      Path model5 = outputDir.resolve("com.ngc.seaside.test2.test4.model5.domainmodel");
      Assert.assertTrue("Cannot find model " + model5, Files.isDirectory(model5));
      checkGradleBuild(model5, "com.ngc.seaside.test2.test4.model5.domainmodel");
      checkVelocity(model5);
      checkDomainFiles(model5, "com.ngc.seaside.test2", "com.ngc.seaside.test1.test3", "com.ngc.seaside.test2.test4");
      checkDomainContents(model5, "com.ngc.seaside.test2", 3, 4, 6, 7);
      checkDomainContents(model5, "com.ngc.seaside.test1.test3", 5, 5, 8, 8);
      checkDomainContents(model5, "com.ngc.seaside.test2.test4", 6, 6, 9, 9);

      Assert.assertEquals(4, Files.list(outputDir).count());
   }

   @Test
   public void testCommandGroupId() throws IOException {
      final String groupId = "net.example.g1";
      runCommand(CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString(),
         CreateDomainCommand.GROUP_ID_PROPERTY, groupId);

      Path model1 = outputDir.resolve(groupId + ".model1.domainmodel");
      Assert.assertTrue("Cannot find model " + model1, Files.isDirectory(model1));
      checkGradleBuild(model1, groupId + ".model1.domainmodel");
      checkVelocity(model1);
      checkDomainFiles(model1, "com.ngc.seaside.test1", "com.ngc.seaside.test2", "com.ngc.seaside.test1.test3",
         "com.ngc.seaside.test2.test4");
      checkDomainContents(model1, "com.ngc.seaside.test1", 1, 2, 0, 5);
      checkDomainContents(model1, "com.ngc.seaside.test2", 3, 4, 6, 7);
      checkDomainContents(model1, "com.ngc.seaside.test1.test3", 5, 5, 8, 8);
      checkDomainContents(model1, "com.ngc.seaside.test2.test4", 6, 6, 9, 9);

      Path model3 = outputDir.resolve(groupId + ".model3.domainmodel");
      Assert.assertTrue("Cannot find model " + model3, Files.isDirectory(model3));
      checkGradleBuild(model3, groupId + ".model3.domainmodel");
      checkVelocity(model3);
      checkDomainFiles(model3, "com.ngc.seaside.test1", "com.ngc.seaside.test2");
      checkDomainContents(model3, "com.ngc.seaside.test1", 1, 2, 0, 5);
      checkDomainContents(model3, "com.ngc.seaside.test2", 3, 4, 6, 7);

      Path model4 = outputDir.resolve(groupId + ".model4.domainmodel");
      Assert.assertTrue("Cannot find model " + model4, Files.isDirectory(model4));
      checkGradleBuild(model4, groupId + ".model4.domainmodel");
      checkVelocity(model4);
      checkDomainFiles(model4, "com.ngc.seaside.test1", "com.ngc.seaside.test2", "com.ngc.seaside.test1.test3",
         "com.ngc.seaside.test2.test4");
      checkDomainContents(model4, "com.ngc.seaside.test1", 1, 2, 0, 5);
      checkDomainContents(model4, "com.ngc.seaside.test2", 3, 4, 6, 7);
      checkDomainContents(model4, "com.ngc.seaside.test1.test3", 5, 5, 8, 8);
      checkDomainContents(model4, "com.ngc.seaside.test2.test4", 6, 6, 9, 9);

      Path model5 = outputDir.resolve(groupId + ".model5.domainmodel");
      Assert.assertTrue("Cannot find model " + model5, Files.isDirectory(model5));
      checkGradleBuild(model5, groupId + ".model5.domainmodel");
      checkVelocity(model5);
      checkDomainFiles(model5, "com.ngc.seaside.test2", "com.ngc.seaside.test1.test3", "com.ngc.seaside.test2.test4");
      checkDomainContents(model5, "com.ngc.seaside.test2", 3, 4, 6, 7);
      checkDomainContents(model5, "com.ngc.seaside.test1.test3", 5, 5, 8, 8);
      checkDomainContents(model5, "com.ngc.seaside.test2.test4", 6, 6, 9, 9);

      Assert.assertEquals(4, Files.list(outputDir).count());
   }

   @Test
   public void testCommandPackageSuffix() throws IOException {
      final String suffix = "example";
      runCommand(CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString(),
         CreateDomainCommand.PACKAGE_SUFFIX_PROPERTY, suffix);

      Path model1 = outputDir.resolve("com.ngc.seaside.test1.model1." + suffix);
      Assert.assertTrue("Cannot find model " + model1, Files.isDirectory(model1));
      checkGradleBuild(model1, "com.ngc.seaside.test1.model1." + suffix);
      checkVelocity(model1);
      checkDomainFiles(model1, "com.ngc.seaside.test1", "com.ngc.seaside.test2", "com.ngc.seaside.test1.test3",
         "com.ngc.seaside.test2.test4");
      checkDomainContents(model1, "com.ngc.seaside.test1", 1, 2, 0, 5);
      checkDomainContents(model1, "com.ngc.seaside.test2", 3, 4, 6, 7);
      checkDomainContents(model1, "com.ngc.seaside.test1.test3", 5, 5, 8, 8);
      checkDomainContents(model1, "com.ngc.seaside.test2.test4", 6, 6, 9, 9);

      Path model3 = outputDir.resolve("com.ngc.seaside.test2.model3." + suffix);
      Assert.assertTrue("Cannot find model " + model3, Files.isDirectory(model3));
      checkGradleBuild(model3, "com.ngc.seaside.test2.model3." + suffix);
      checkVelocity(model3);
      checkDomainFiles(model3, "com.ngc.seaside.test1", "com.ngc.seaside.test2");
      checkDomainContents(model3, "com.ngc.seaside.test1", 1, 2, 0, 5);
      checkDomainContents(model3, "com.ngc.seaside.test2", 3, 4, 6, 7);

      Path model4 = outputDir.resolve("com.ngc.seaside.test1.test3.model4." + suffix);
      Assert.assertTrue("Cannot find model " + model4, Files.isDirectory(model4));
      checkGradleBuild(model4, "com.ngc.seaside.test1.test3.model4." + suffix);
      checkVelocity(model4);
      checkDomainFiles(model4, "com.ngc.seaside.test1", "com.ngc.seaside.test2", "com.ngc.seaside.test1.test3",
         "com.ngc.seaside.test2.test4");
      checkDomainContents(model4, "com.ngc.seaside.test1", 1, 2, 0, 5);
      checkDomainContents(model4, "com.ngc.seaside.test2", 3, 4, 6, 7);
      checkDomainContents(model4, "com.ngc.seaside.test1.test3", 5, 5, 8, 8);
      checkDomainContents(model4, "com.ngc.seaside.test2.test4", 6, 6, 9, 9);

      Path model5 = outputDir.resolve("com.ngc.seaside.test2.test4.model5." + suffix);
      Assert.assertTrue("Cannot find model " + model5, Files.isDirectory(model5));
      checkGradleBuild(model5, "com.ngc.seaside.test2.test4.model5." + suffix);
      checkVelocity(model5);
      checkDomainFiles(model5, "com.ngc.seaside.test2", "com.ngc.seaside.test1.test3", "com.ngc.seaside.test2.test4");
      checkDomainContents(model5, "com.ngc.seaside.test2", 3, 4, 6, 7);
      checkDomainContents(model5, "com.ngc.seaside.test1.test3", 5, 5, 8, 8);
      checkDomainContents(model5, "com.ngc.seaside.test2.test4", 6, 6, 9, 9);

      Assert.assertEquals(4, Files.list(outputDir).count());
   }

   @Test
   public void testModel() throws IOException {
      final String model = "com.ngc.seaside.test1.Model1";
      runCommand(CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString(), CreateDomainCommand.MODEL_PROPERTY,
         model);

      Path model1 = outputDir.resolve("com.ngc.seaside.test1.model1.domainmodel");
      Assert.assertTrue("Cannot find model " + model1, Files.isDirectory(model1));
      checkGradleBuild(model1, "com.ngc.seaside.test1.model1.domainmodel");
      checkVelocity(model1);
      checkDomainFiles(model1, "com.ngc.seaside.test1", "com.ngc.seaside.test2", "com.ngc.seaside.test1.test3",
         "com.ngc.seaside.test2.test4");
      checkDomainContents(model1, "com.ngc.seaside.test1", 1, 2, 0, 5);
      checkDomainContents(model1, "com.ngc.seaside.test2", 3, 4, 6, 7);
      checkDomainContents(model1, "com.ngc.seaside.test1.test3", 5, 5, 8, 8);
      checkDomainContents(model1, "com.ngc.seaside.test2.test4", 6, 6, 9, 9);

      Assert.assertEquals(1, Files.list(outputDir).count());
   }

   @Test
   public void testModelArtifactId() throws IOException {
      final String model = "com.ngc.seaside.test1.Model1";
      final String artifact = "test.artifact.g1";
      runCommand(CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString(), CreateDomainCommand.MODEL_PROPERTY,
         model, CreateDomainCommand.ARTIFACT_ID_PROPERTY, artifact);

      Path model1 = outputDir.resolve("com.ngc.seaside.test1." + artifact + ".domainmodel");
      Assert.assertTrue("Cannot find model " + model1, Files.isDirectory(model1));
      checkGradleBuild(model1, "com.ngc.seaside.test1." + artifact + ".domainmodel");
      checkVelocity(model1);
      checkDomainFiles(model1, "com.ngc.seaside.test1", "com.ngc.seaside.test2", "com.ngc.seaside.test1.test3",
         "com.ngc.seaside.test2.test4");
      checkDomainContents(model1, "com.ngc.seaside.test1", 1, 2, 0, 5);
      checkDomainContents(model1, "com.ngc.seaside.test2", 3, 4, 6, 7);
      checkDomainContents(model1, "com.ngc.seaside.test1.test3", 5, 5, 8, 8);
      checkDomainContents(model1, "com.ngc.seaside.test2.test4", 6, 6, 9, 9);

      Assert.assertEquals(1, Files.list(outputDir).count());
   }

   @Test
   public void testModelPackage() throws IOException {
      final String model = "com.ngc.seaside.test1.Model1";
      final String pkg = "com.test.e1.test.artifact.g1";
      runCommand(CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString(), CreateDomainCommand.MODEL_PROPERTY,
         model, CreateDomainCommand.PACKAGE_PROPERTY, pkg);

      Path model1 = outputDir.resolve(pkg);
      Assert.assertTrue("Cannot find model " + model1, Files.isDirectory(model1));
      checkVelocity(model1);
      checkDomainFiles(model1, "com.ngc.seaside.test1", "com.ngc.seaside.test2", "com.ngc.seaside.test1.test3",
         "com.ngc.seaside.test2.test4");
      checkDomainContents(model1, "com.ngc.seaside.test1", 1, 2, 0, 5);
      checkDomainContents(model1, "com.ngc.seaside.test2", 3, 4, 6, 7);
      checkDomainContents(model1, "com.ngc.seaside.test1.test3", 5, 5, 8, 8);
      checkDomainContents(model1, "com.ngc.seaside.test2.test4", 6, 6, 9, 9);

      Assert.assertEquals(1, Files.list(outputDir).count());
   }

   @Test
   public void testStereoTypes() throws IOException {
      final String stereotypes = "a1,a2,a9";
      runCommand(CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString(),
         CreateDomainCommand.STEREOTYPES_PROPERTY, stereotypes);

      Path model1 = outputDir.resolve("com.ngc.seaside.test1.model1.domainmodel");
      Assert.assertTrue("Cannot find model " + model1, Files.isDirectory(model1));
      checkVelocity(model1);
      checkDomainFiles(model1, "com.ngc.seaside.test1", "com.ngc.seaside.test2", "com.ngc.seaside.test1.test3",
         "com.ngc.seaside.test2.test4");
      checkDomainContents(model1, "com.ngc.seaside.test1", 1, 2, 0, 5);
      checkDomainContents(model1, "com.ngc.seaside.test2", 3, 4, 6, 7);
      checkDomainContents(model1, "com.ngc.seaside.test1.test3", 5, 5, 8, 8);
      checkDomainContents(model1, "com.ngc.seaside.test2.test4", 6, 6, 9, 9);

      Path model5 = outputDir.resolve("com.ngc.seaside.test2.test4.model5.domainmodel");
      Assert.assertTrue("Cannot find model " + model5, Files.isDirectory(model5));
      checkVelocity(model5);
      checkDomainFiles(model5, "com.ngc.seaside.test2", "com.ngc.seaside.test1.test3", "com.ngc.seaside.test2.test4");
      checkDomainContents(model5, "com.ngc.seaside.test2", 3, 4, 6, 7);
      checkDomainContents(model5, "com.ngc.seaside.test1.test3", 5, 5, 8, 8);
      checkDomainContents(model5, "com.ngc.seaside.test2.test4", 6, 6, 9, 9);

      Assert.assertEquals(2, Files.list(outputDir).count());
   }

   @Test
   public void testIgnoreStereoTypes() throws IOException {
      final String stereotypes = "a1, a2, a9";
      runCommand(CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString(),
         CreateDomainCommand.IGNORE_STEREOTYPES_PROPERTY, stereotypes);

      Path model3 = outputDir.resolve("com.ngc.seaside.test2.model3.domainmodel");
      Assert.assertTrue("Cannot find model " + model3, Files.isDirectory(model3));
      checkGradleBuild(model3, "com.ngc.seaside.test2.model3.domainmodel");
      checkVelocity(model3);
      checkDomainFiles(model3, "com.ngc.seaside.test1", "com.ngc.seaside.test2");
      checkDomainContents(model3, "com.ngc.seaside.test1", 1, 2, 0, 5);
      checkDomainContents(model3, "com.ngc.seaside.test2", 3, 4, 6, 7);

      Path model4 = outputDir.resolve("com.ngc.seaside.test1.test3.model4.domainmodel");
      Assert.assertTrue("Cannot find model " + model4, Files.isDirectory(model4));
      checkGradleBuild(model4, "com.ngc.seaside.test1.test3.model4.domainmodel");
      checkVelocity(model4);
      checkDomainFiles(model4, "com.ngc.seaside.test1", "com.ngc.seaside.test2", "com.ngc.seaside.test1.test3",
         "com.ngc.seaside.test2.test4");
      checkDomainContents(model4, "com.ngc.seaside.test1", 1, 2, 0, 5);
      checkDomainContents(model4, "com.ngc.seaside.test2", 3, 4, 6, 7);
      checkDomainContents(model4, "com.ngc.seaside.test1.test3", 5, 5, 8, 8);
      checkDomainContents(model4, "com.ngc.seaside.test2.test4", 6, 6, 9, 9);

      Assert.assertEquals(2, Files.list(outputDir).count());
   }

   private Path getDomain(Path projectDir) {
      return projectDir.resolve(Paths.get("src", "main", "resources", "domain"));
   }

   private void checkGradleBuild(Path projectDir, String... exportPackages) throws IOException {
      Path buildFile = projectDir.resolve("build.gradle");
      Assert.assertTrue("build.gradle is missing", Files.isRegularFile(buildFile));
      String contents = new String(Files.readAllBytes(buildFile));
      Assert.assertTrue(contents.contains(velocityPath.getFileName().toString()));
      for (String export : exportPackages) {
         Assert.assertTrue("Expected \"" + export + "\" in build.gradle", contents.contains(export));
      }
   }

   private void checkVelocity(Path projectDir) {
      Path velocityFolder = projectDir.resolve(Paths.get("src", "main", "resources", "velocity"));
      Assert.assertTrue("Could not find velocity folder", Files.isDirectory(velocityFolder));
      Assert.assertTrue("Could not find velocity file: " + velocityPath.getFileName(),
         Files.isRegularFile(velocityFolder.resolve(velocityPath.getFileName())));
   }

   private void checkDomainFiles(Path projectDir, String... filenames) throws IOException {
      Path domainDir = getDomain(projectDir);
      try {
         Assert.assertEquals(filenames.length, Files.list(domainDir).count());
      } catch (NoSuchFileException e) {
         Assert.assertEquals(0, filenames.length);
      }
      for (String pkg : filenames) {
         Assert.assertTrue("Missing file: " + pkg + ".xml", Files.isRegularFile(domainDir.resolve(pkg + ".xml")));
      }
   }

   private void checkDomainContents(Path projectDir, String filename, int startData, int endData, int startField,
            int endField)
      throws IOException {
      Path file = getDomain(projectDir).resolve(filename + ".xml");
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
         collection.addParameter(new DefaultParameter(keyValues[n]).setValue(keyValues[n + 1]));
      }

      Mockito.when(options.getParameters()).thenReturn(collection);

      cmd.run(options);
   }

   private static final Module LOG_SERVICE_MODULE = new AbstractModule() {
      @Override
      protected void configure() {
         bind(ILogService.class).to(PrintStreamLogService.class);
      }
   };

   private static Collection<Module> getModules() {
      Collection<Module> modules = new ArrayList<>();
      modules.add(LOG_SERVICE_MODULE);
      for (Module dynamicModule : ServiceLoader.load(Module.class)) {
         if (!(dynamicModule instanceof LogServiceModule)) {
            modules.add(dynamicModule);
         }
      }

      modules.removeIf(m -> m instanceof XTextSystemDescriptorServiceModule);
      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
      return modules;
   }

   private static final Injector injector = Guice.createInjector(getModules());

}
