package com.ngc.seaside.jellyfish.cli.command.createjavadistribution;

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

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.regex.Pattern;

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
   private CreateJavaDistributionCommand fixture = new CreateJavaDistributionCommand();
   private IPromptUserService promptUserService = mock(IPromptUserService.class);
   private IJellyFishCommandOptions options = Mockito.mock(IJellyFishCommandOptions.class);
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

   @Before
   public void setup() throws IOException {
      // Setup test resources
      Properties props = System.getProperties();
      props.setProperty("NG_FW_HOME", Paths.get("src/main").toAbsolutePath().toString());
      //Path outputDirectory = Paths.get("C:\\Users\\J57467\\Downloads\\test");
      //outputDir = Files.createDirectories(outputDirectory);
      outputDir =  Files.createTempDirectory(null);
      outputDir.toFile().deleteOnExit();

      // Setup mock system descriptor
      when(options.getSystemDescriptor()).thenReturn(systemDescriptor);
      when(systemDescriptor.findModel("com.ngc.seaside.test.Model")).thenReturn(Optional.of(model));

      // Setup mock model
      when(model.getParent()).thenReturn(mock(IPackage.class));
      when(model.getParent().getName()).thenReturn("com.ngc.seaside");
      when(model.getName()).thenReturn("Model");

      fixture.setLogService(injector.getInstance(ILogService.class));
      fixture.setPromptService(promptUserService);
      fixture.setTemplateService(injector.getInstance(ITemplateService.class));

   }

   @Test
   public void testCommand() throws IOException {
      createSettings();

      runCommand(CreateJavaDistributionCommand.MODEL_PROPERTY, "com.ngc.seaside.test.Model",
                 CreateJavaDistributionCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString());

      Mockito.verify(options, Mockito.times(1)).getParameters();
      Mockito.verify(options, Mockito.times(1)).getSystemDescriptor();
   }

   private void runCommand(String... keyValues) {
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n + 1 < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter<String>(keyValues[n]).setValue(keyValues[n + 1]));
      }

      Mockito.when(options.getParameters()).thenReturn(collection);

      fixture.run(options);
   }

   private void checkLogContents(Path resourcesDir, String filename, int startData, int endData, int startField,
                                 int endField)
            throws IOException {
      Path file = resourcesDir.resolve(filename);
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

   private void checkGradleBuild(Path projectDir, String... exportPackages) throws IOException {
      Path buildFile = projectDir.resolve("build.gradle");
      Assert.assertTrue("build.gradle is missing", Files.isRegularFile(buildFile));
      String contents = new String(Files.readAllBytes(buildFile));
      //Assert.assertTrue(contents.contains(velocityPath.getFileName().toString()));
      for (String export : exportPackages) {
         Assert.assertTrue("Expected \"" + export + "\" in build.gradle", contents.contains(export));
      }
   }

   private void createSettings() throws IOException {
      try {
         Files.createFile(outputDir.resolve("settings.gradle"));
      } catch (FileAlreadyExistsException e) {
         // ignore
      }
   }
}
