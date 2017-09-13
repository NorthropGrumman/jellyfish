package com.ngc.seaside.jellyfish.cli.command.createjavapubsubconnector;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.service.impl.templateservice.TemplateServiceGuiceModule;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavapubsubconnector.CreateJavaPubsubConnectorCommand;
import com.ngc.seaside.jellyfish.cli.command.createjavapubsubconnector.CreateJavaPubsubConnectorCommandGuiceWrapper;
import com.ngc.seaside.jellyfish.cli.command.test.template.MockedTemplateService;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class CreateJavaPubsubConnectorCommandIT {

   private IJellyFishCommand cmd = injector.getInstance(CreateJavaPubsubConnectorCommandGuiceWrapper.class);
   private IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class);

   private Path outputDir;

   @Before
   public void setup() throws IOException {
      outputDir = Files.createTempDirectory(null);

      Path sdDir = Paths.get("src", "test", "sd");
      PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.sd");
      Collection<Path> sdFiles = Files.walk(sdDir).filter(matcher::matches).collect(Collectors.toSet());
      ISystemDescriptorService sdService = injector.getInstance(ISystemDescriptorService.class);
      IParsingResult result = sdService.parseFiles(sdFiles);
      assertTrue(result.getIssues().toString(), result.isSuccessful());
      ISystemDescriptor sd = result.getSystemDescriptor();
      Mockito.when(options.getSystemDescriptor()).thenReturn(sd);
   }

   @Test
   public void testCommand() throws IOException {
      final String model = "com.Model";

      runCommand(CreateJavaPubsubConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(), "model", model);
      
      Path connectorFile = outputDir.resolve(Paths.get("com.model.connector", "src", "main", "java", "com", "model", "connector", "ModelConnector.java"));
      Path conversionFile = outputDir.resolve(Paths.get("com.model.connector", "src", "main", "java", "com", "model", "connector", "ModelDataConversion.java"));
      
      assertTrue(Files.isRegularFile(connectorFile));
      assertTrue(Files.isRegularFile(conversionFile));
      
      List<String> conversionLines = Files.readAllLines(conversionFile);
      
      // Check for nested data type conversions
      assertTrue(conversionLines.stream().anyMatch(line -> line.matches(".*convert\\s*\\(\\s*com.model.events.Data2.*")));
      assertTrue(conversionLines.stream().anyMatch(line -> line.matches(".*convert\\s*\\(\\s*com.model.events.Enum1.*")));

      // Check that many primitives are handled correctly
      assertTrue(conversionLines.stream().noneMatch(line -> line.contains("List<int>")));
      assertTrue(conversionLines.stream().noneMatch(line -> line.contains("List<float>")));
      assertTrue(conversionLines.stream().noneMatch(line -> line.contains("List<boolean>")));

      
   }

   private void runCommand(String... keyValues) throws IOException {
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter<>(keyValues[n]).setValue(keyValues[n + 1]));
      }

      Mockito.when(options.getParameters()).thenReturn(collection);

      cmd.run(options);

   }

   private static final Module TEST_SERVICE_MODULE = new AbstractModule() {
      @Override
      protected void configure() {
         bind(ILogService.class).to(PrintStreamLogService.class);
         MockedTemplateService mockedTemplateService = new MockedTemplateService().useRealPropertyService().useDefaultUserValues(true)
                  .setTemplateDirectory(CreateJavaPubsubConnectorCommand.class.getPackage().getName(), Paths.get("src", "main", "template"));
         bind(ITemplateService.class).toInstance(mockedTemplateService);
      }
   };

   private static Collection<Module> getModules() {
      Collection<Module> modules = new ArrayList<>();
      modules.add(TEST_SERVICE_MODULE);
      for (Module dynamicModule : ServiceLoader.load(Module.class)) {
         if (!(dynamicModule instanceof TemplateServiceGuiceModule || dynamicModule instanceof PrintStreamLogService)) {
            modules.add(dynamicModule);
         }
      }

      modules.removeIf(m -> m instanceof XTextSystemDescriptorServiceModule);
      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
      return modules;
   }

   private static final Injector injector = Guice.createInjector(getModules());

}
