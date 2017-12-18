package com.ngc.seaside.jellyfish.cli.command.createjavaevents;

import static com.ngc.seaside.jellyfish.cli.command.test.files.TestingFiles.assertFileContains;
import static com.ngc.seaside.jellyfish.cli.command.test.files.TestingFiles.assertFileNotContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.ngc.seaside.systemdescriptor.ext.test.systemdescriptor.ModelUtils;
import com.ngc.seaside.systemdescriptor.ext.test.systemdescriptor.ModelUtils.PubSubModel;
import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CreateJavaEventsCommandIT {

   private final IJellyFishCommand command = injector.getInstance(CreateJavaEventsCommandGuiceWrapper.class);

   @Rule
   public final TemporaryFolder outputDirectory = new TemporaryFolder();

   @Before
   public void setup() {
   }

   @Test
   public void testCommand() throws Exception {
      IData[] data = new IData[9];
      for (int n = 0; n < data.length; n++) {
         data[n] = ModelUtils.getMockNamedChild(IData.class, "test" + n + ".Data" + n);
      }
      IEnumeration[] enums = new IEnumeration[5];
      for (int n = 0; n < enums.length; n++) {
         enums[n] = ModelUtils.getMockNamedChild(IEnumeration.class, "test" + n + ".Enum" + n);
      }
      ModelUtils.mockData(data[0],
         data[1],
         "intField",
         DataTypes.INT,
         "manyIntField",
         FieldCardinality.MANY,
         DataTypes.INT,
         "floatField",
         DataTypes.FLOAT,
         "manyFloatField",
         FieldCardinality.MANY,
         DataTypes.FLOAT,
         "booleanField",
         DataTypes.BOOLEAN,
         "manyBooleanField",
         FieldCardinality.MANY,
         DataTypes.BOOLEAN,
         "stringField",
         DataTypes.STRING,
         "manyStringField",
         FieldCardinality.MANY,
         DataTypes.STRING,
         "dataField",
         data[2],
         "manyDataField",
         FieldCardinality.MANY,
         data[2],
         "enumField",
         enums[0],
         "manyEnumField",
         FieldCardinality.MANY,
         enums[0]);

      ModelUtils.mockData(data[1],
         data[3],
         "superField",
         DataTypes.INT,
         "superDataField",
         data[4]);

      ModelUtils.mockData(data[2],
         data[5],
         "nestedField",
         DataTypes.INT,
         "nestedDataField",
         data[6]);

      ModelUtils.mockData(data[3],
         null,
         "superSuperField",
         DataTypes.INT,
         "superSuperDataField",
         data[7]);

      ModelUtils.mockData(data[4], null, "nestedSuperField", DataTypes.INT, "nstedSuperEnumField", enums[1]);

      ModelUtils.mockData(data[5], null, "superNestedField", DataTypes.INT, "superNestedEnumField", enums[2]);

      ModelUtils.mockData(data[6], null, "nestedNestedField", DataTypes.INT, "nestedNestedEnumField", enums[3]);

      ModelUtils.mockData(data[7], null, "superSuperNestedField", DataTypes.INT, "superSuperNestedEnumField", enums[4]);

      ModelUtils.mockData(data[8], null);

      PubSubModel model = new PubSubModel("com.Model");
      model.addInput("input1", data[0]);
      model.addOutput("output1", data[8]);

      runCommand(model);

      Path outputDirectory = this.outputDirectory.getRoot().toPath();
      Path projectDirectory = outputDirectory.resolve("generated-projects").resolve("com.model.events");

      assertTrue(
         "Project directory incorrect: "
            + Files.list(outputDirectory).map(Object::toString).collect(Collectors.joining(", ")),
         Files.isDirectory(projectDirectory));

      Path gradleBuild = projectDirectory.resolve("build.generated.gradle");
      assertTrue(Files.isRegularFile(gradleBuild));

      Path sourceDirectory = projectDirectory.resolve(Paths.get("src", "main", "java"));
      assertTrue(Files.isDirectory(sourceDirectory));

      Map<String, List<Path>> fileMap = Files.walk(sourceDirectory)
                                             .filter(Files::isRegularFile)
                                             .peek(
                                                file -> assertTrue(file.toString(), file.toString().endsWith(".java")))
                                             .collect(Collectors.groupingBy(file -> {
                                                String name = ((Path) file).getFileName().toString();
                                                name = name.substring(0, name.lastIndexOf('.'));
                                                return name;
                                             }));
      
      Map<String, Path> files = new HashMap<>();
      fileMap.forEach((name, list) -> {
         assertEquals("Unexpected duplicate files: " + list, 1, list.size());
         files.put(name, list.get(0));
      });

      assertEquals("Invalid number of files generated: ", data.length + enums.length, files.size());

      IntStream.range(0, data.length).forEach(i -> assertTrue(files.containsKey("Data" + i)));
      IntStream.range(0, enums.length).forEach(i -> assertTrue(files.containsKey("Enum" + i)));

      assertFileNotContains(files.get("Data0"), "\\babstract\\s+class\\b");
      assertFileContains(files.get("Data1"), "\\babstract\\s+class\\b");
      assertFileNotContains(files.get("Data2"), "\\babstract\\s+class\\b");
      assertFileContains(files.get("Data3"), "\\babstract\\s+class\\b");
      assertFileNotContains(files.get("Data4"), "\\babstract\\s+class\\b");
      assertFileContains(files.get("Data5"), "\\babstract\\s+class\\b");
      assertFileNotContains(files.get("Data6"), "\\babstract\\s+class\\b");
      assertFileNotContains(files.get("Data7"), "\\babstract\\s+class\\b");
   }

   private void runCommand(IModel model, String... keyValues) throws IOException {
      IJellyFishCommandOptions mockOptions = Mockito.mock(IJellyFishCommandOptions.class);
      DefaultParameterCollection collection = new DefaultParameterCollection();

      collection.addParameter(new DefaultParameter<>(
         CreateJavaEventsCommand.OUTPUT_DIRECTORY_PROPERTY,
         outputDirectory.getRoot().toPath()));
      collection.addParameter(new DefaultParameter<>(
         CreateJavaEventsCommand.MODEL_PROPERTY,
         model.getFullyQualifiedName()));

      for (int n = 0; n < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter<String>(keyValues[n]).setValue(keyValues[n + 1]));
      }

      when(mockOptions.getParameters()).thenReturn(collection);
      when(mockOptions.getSystemDescriptor()).thenReturn(mock(ISystemDescriptor.class));
      when(mockOptions.getSystemDescriptor().findModel(model.getFullyQualifiedName())).thenReturn(Optional.of(model));

      command.run(mockOptions);
   }

   private static final Module TEST_SERVICE_MODULE = new AbstractModule() {
      @Override
      protected void configure() {
         bind(ILogService.class).toInstance(mock(ILogService.class));
         MockedTemplateService mockedTemplateService = new MockedTemplateService().useRealPropertyService()
                                                                                  .setTemplateDirectory(
                                                                                     CreateJavaEventsCommand.class.getPackage()
                                                                                                                  .getName()
                                                                                        + CreateJavaEventsCommand.EVENTS_BUILD_TEMPLATE_SUFFIX,
                                                                                     Paths.get("src",
                                                                                        "main",
                                                                                        "templates",
                                                                                        CreateJavaEventsCommand.EVENTS_BUILD_TEMPLATE_SUFFIX.substring(
                                                                                           1)))
                                                                                  .setTemplateDirectory(
                                                                                     CreateJavaEventsCommand.class.getPackage()
                                                                                                                  .getName()
                                                                                        + CreateJavaEventsCommand.EVENTS_JAVA_TEMPLATE_SUFFIX,
                                                                                     Paths.get("src",
                                                                                        "main",
                                                                                        "templates",
                                                                                        CreateJavaEventsCommand.EVENTS_JAVA_TEMPLATE_SUFFIX.substring(
                                                                                           1)));
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
