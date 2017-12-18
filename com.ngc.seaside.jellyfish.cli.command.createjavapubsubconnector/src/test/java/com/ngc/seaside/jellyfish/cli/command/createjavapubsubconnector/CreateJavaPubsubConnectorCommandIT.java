package com.ngc.seaside.jellyfish.cli.command.createjavapubsubconnector;

import static com.ngc.seaside.jellyfish.cli.command.test.files.TestingFiles.assertFileContains;
import static com.ngc.seaside.jellyfish.cli.command.test.files.TestingFiles.assertFileNotContains;
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
      final String model = "com.ngc.Model1";

      runCommand(CreateJavaPubsubConnectorCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(), CreateJavaPubsubConnectorCommand.MODEL_PROPERTY, model);
      
      Path srcFolder = outputDir.resolve(Paths.get("generated-projects", "com.ngc.model1.connector", "src", "main", "java", "com", "ngc", "model1", "connector"));
      Path connectorFile = srcFolder.resolve("Model1Connector.java");
      Path conversionFile = srcFolder.resolve("Model1DataConversion.java");
      
      assertTrue(Files.isRegularFile(connectorFile));
      
      Files.lines(conversionFile).forEach(System.out::println);
      // Check for nested data type conversions
      assertFileContains(conversionFile, "\\bconvert\\s*\\(\\s*\\S*Child1\\b");
      assertFileContains(conversionFile, "\\bconvert\\s*\\(\\s*\\S*Child2\\b");
      assertFileContains(conversionFile, "\\bconvert\\s*\\(\\s*\\S*Data1\\b");
      assertFileContains(conversionFile, "\\bconvert\\s*\\(\\s*\\S*Enum1\\b");
      
      // Check for base and child data fields
      assertFileContains(conversionFile, "\\bsetIntFieldBase1\\b");
      assertFileContains(conversionFile, "\\bsetManyIntFieldBase1\\b");
      assertFileContains(conversionFile, "\\bsetFloatFieldBase1\\b");
      assertFileContains(conversionFile, "\\bsetManyFloatFieldBase1\\b");
      assertFileContains(conversionFile, "\\bsetStringFieldBase1\\b");
      assertFileContains(conversionFile, "\\bsetManyStringFieldBase1\\b");
      assertFileContains(conversionFile, "\\bsetDataFieldBase1\\b");
      assertFileContains(conversionFile, "\\bsetManyDataFieldBase1\\b");
      assertFileContains(conversionFile, "\\bsetEnumFieldBase1\\b");
      assertFileContains(conversionFile, "\\bsetManyEnumFieldBase1\\b");
      assertFileContains(conversionFile, "\\bsetInheritedDataFieldBase1\\b");
      assertFileContains(conversionFile, "\\bsetManyInheritedDataFieldBase1\\b");
      
      assertFileContains(conversionFile, "\\bsetIntFieldBase2\\b");
      assertFileContains(conversionFile, "\\bsetManyIntFieldBase2\\b");
      assertFileContains(conversionFile, "\\bsetFloatFieldBase2\\b");
      assertFileContains(conversionFile, "\\bsetManyFloatFieldBase2\\b");
      assertFileContains(conversionFile, "\\bsetStringFieldBase2\\b");
      assertFileContains(conversionFile, "\\bsetManyStringFieldBase2\\b");
      assertFileContains(conversionFile, "\\bsetDataFieldBase2\\b");
      assertFileContains(conversionFile, "\\bsetManyDataFieldBase2\\b");
      assertFileContains(conversionFile, "\\bsetEnumFieldBase2\\b");
      assertFileContains(conversionFile, "\\bsetManyEnumFieldBase2\\b");
      
      assertFileContains(conversionFile, "\\bsetIntFieldChild1\\b");
      assertFileContains(conversionFile, "\\bsetManyIntFieldChild1\\b");
      assertFileContains(conversionFile, "\\bsetFloatFieldChild1\\b");
      assertFileContains(conversionFile, "\\bsetManyFloatFieldChild1\\b");
      assertFileContains(conversionFile, "\\bsetStringFieldChild1\\b");
      assertFileContains(conversionFile, "\\bsetManyStringFieldChild1\\b");
      assertFileContains(conversionFile, "\\bsetDataFieldChild1\\b");
      assertFileContains(conversionFile, "\\bsetManyDataFieldChild1\\b");
      assertFileContains(conversionFile, "\\bsetEnumFieldChild1\\b");
      assertFileContains(conversionFile, "\\bsetManyEnumFieldChild1\\b");
      assertFileContains(conversionFile, "\\bsetInheritedDataFieldChild1\\b");
      assertFileContains(conversionFile, "\\bsetManyInheritedDataFieldChild1\\b");
      assertFileContains(conversionFile, "\\bsetRecursiveDataFieldChild1\\b");
      assertFileContains(conversionFile, "\\bsetManyRecursiveDataFieldChild1\\b");
      
      assertFileContains(conversionFile, "\\bsetIntFieldChild2\\b");
      assertFileContains(conversionFile, "\\bsetManyIntFieldChild2\\b");
      assertFileContains(conversionFile, "\\bsetFloatFieldChild2\\b");
      assertFileContains(conversionFile, "\\bsetManyFloatFieldChild2\\b");
      assertFileContains(conversionFile, "\\bsetStringFieldChild2\\b");
      assertFileContains(conversionFile, "\\bsetManyStringFieldChild2\\b");
      assertFileContains(conversionFile, "\\bsetDataFieldChild2\\b");
      assertFileContains(conversionFile, "\\bsetManyDataFieldChild2\\b");
      assertFileContains(conversionFile, "\\bsetEnumFieldChild2\\b");
      assertFileContains(conversionFile, "\\bsetManyEnumFieldChild2\\b");
      assertFileContains(conversionFile, "\\bsetRecursiveDataFieldChild2\\b");
      assertFileContains(conversionFile, "\\bsetManyRecursiveDataFieldChild2\\b");

      // Check that many primitives are handled correctly
      assertFileNotContains(conversionFile, "<\\s*(?:boolean|int|long|float)\\s*>");

      
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
         bind(ILogService.class).toInstance(mock(ILogService.class));
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
