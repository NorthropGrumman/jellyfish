package com.ngc.seaside.jellyfish.cli.command.createdomain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.ngc.blocs.domain.impl.common.generated.Tdomain;
import com.ngc.blocs.domain.impl.common.generated.Tobject;
import com.ngc.blocs.domain.impl.common.generated.Tproperty;
import com.ngc.blocs.jaxb.impl.common.JAXBUtilities;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.blocs.test.impl.common.resource.MockedResourceService;
import com.ngc.seaside.bootstrap.service.impl.templateservice.TemplateServiceGuiceModule;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.files.TestingFiles;
import com.ngc.seaside.jellyfish.cli.command.test.template.MockedTemplateService;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CreateDomainCommandIT {

   private IJellyFishCommand cmd = injector.getInstance(CreateDomainCommandGuiceWrapper.class);

   private IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class);

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
      assertTrue(result.getIssues().toString(), result.isSuccessful());
      ISystemDescriptor sd = result.getSystemDescriptor();
      when(options.getSystemDescriptor()).thenReturn(sd);
   }

   @Test
   public void testCommand() throws Exception {
      runCommand(CreateDomainCommand.MODEL_PROPERTY,
         "com.ngc.Model1",
         CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY,
         outputDir,
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY,
         velocityPath);

      Path projectDir = outputDir.resolve("com.ngc.model1.domain");
      assertTrue("Cannot find project directory: " + projectDir, Files.isDirectory(projectDir));
      checkGradleBuild(projectDir, "com.ngc.model1.domain");
      checkVelocity(projectDir);
      List<Tdomain> domains = getDomain(projectDir);
      domains.forEach(domain -> assertFalse(domain.getConfig() != null && domain.getConfig().isUseVerboseImports()));
      Map<String, Tobject> objects = getDomainObjects(projectDir);
      assertEquals(6, objects.size());

      assertDomainObject(objects.get("com.ngc.model1.domain.b1.Base1"),
         null,
         true,
         "com.ngc.model1.domain.b1.Base1",
         "int intFieldBase1",
         "many int manyIntFieldBase1",
         "float floatFieldBase1",
         "many float manyFloatFieldBase1",
         "String stringFieldBase1",
         "many String manyStringFieldBase1",
         "com.ngc.model1.domain.d1.Data1 dataFieldBase1",
         "many com.ngc.model1.domain.d1.Data1 manyDataFieldBase1",
         "com.ngc.model1.domain.e1.Enum1 enumFieldBase1",
         "many com.ngc.model1.domain.e1.Enum1 manyEnumFieldBase1",
         "com.ngc.model1.domain.c2.Child2 inheritedDataFieldBase1",
         "many com.ngc.model1.domain.c2.Child2 manyInheritedDataFieldBase1");

      assertDomainObject(objects.get("com.ngc.model1.domain.b2.Base2"),
         null,
         true,
         "com.ngc.model1.domain.b2.Base2",
         "int intFieldBase2",
         "many int manyIntFieldBase2",
         "float floatFieldBase2",
         "many float manyFloatFieldBase2",
         "String stringFieldBase2",
         "many String manyStringFieldBase2",
         "com.ngc.model1.domain.d1.Data1 dataFieldBase2",
         "many com.ngc.model1.domain.d1.Data1 manyDataFieldBase2",
         "com.ngc.model1.domain.e1.Enum1 enumFieldBase2",
         "many com.ngc.model1.domain.e1.Enum1 manyEnumFieldBase2");

      assertDomainObject(objects.get("com.ngc.model1.domain.c1.Child1"),
         "com.ngc.model1.domain.b1.Base1",
         false,
         "com.ngc.model1.domain.c1.Child1",
         "int intFieldChild1",
         "many int manyIntFieldChild1",
         "float floatFieldChild1",
         "many float manyFloatFieldChild1",
         "String stringFieldChild1",
         "many String manyStringFieldChild1",
         "com.ngc.model1.domain.d1.Data1 dataFieldChild1",
         "many com.ngc.model1.domain.d1.Data1 manyDataFieldChild1",
         "com.ngc.model1.domain.e1.Enum1 enumFieldChild1",
         "many com.ngc.model1.domain.e1.Enum1 manyEnumFieldChild1",
         "com.ngc.model1.domain.c2.Child2 inheritedDataFieldChild1",
         "many com.ngc.model1.domain.c2.Child2 manyInheritedDataFieldChild1",
         "com.ngc.model1.domain.c1.Child1 recursiveDataFieldChild1",
         "many com.ngc.model1.domain.c1.Child1 manyRecursiveDataFieldChild1");

      assertDomainObject(objects.get("com.ngc.model1.domain.c2.Child2"),
         "com.ngc.model1.domain.b2.Base2",
         false,
         "com.ngc.model1.domain.c2.Child2",
         "int intFieldChild2",
         "many int manyIntFieldChild2",
         "float floatFieldChild2",
         "many float manyFloatFieldChild2",
         "String stringFieldChild2",
         "many String manyStringFieldChild2",
         "com.ngc.model1.domain.d1.Data1 dataFieldChild2",
         "many com.ngc.model1.domain.d1.Data1 manyDataFieldChild2",
         "com.ngc.model1.domain.e1.Enum1 enumFieldChild2",
         "many com.ngc.model1.domain.e1.Enum1 manyEnumFieldChild2",
         "com.ngc.model1.domain.c2.Child2 recursiveDataFieldChild2",
         "many com.ngc.model1.domain.c2.Child2 manyRecursiveDataFieldChild2");

      assertDomainObject(objects.get("com.ngc.model1.domain.d1.Data1"), null, false, "com.ngc.model1.domain.d1.Data1");

   }

   @Test
   public void testWithExtensionCommand() throws Exception {
      final String extension = "asidgoajsdig";
      runCommand(CreateDomainCommand.MODEL_PROPERTY,
         "com.ngc.Model1",
         CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY,
         outputDir,
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY,
         velocityPath,
         CreateDomainCommand.EXTENSION_PROPERTY,
         extension);

      Path projectDir = outputDir.resolve("com.ngc.model1.domain");
      assertTrue("Cannot find project directory: " + projectDir, Files.isDirectory(projectDir));
      checkGradleBuild(projectDir, extension, "com.ngc.model1.domain");
   }

   @Test
   public void testCommandWithPackageGenerator() throws Exception {
      Function<INamedChild<IPackage>, String> packageGenerator = (d) -> "foo";

      runCommand(CreateDomainCommand.MODEL_PROPERTY,
         "com.ngc.Model1",
         CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY,
         outputDir,
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY,
         velocityPath,
         CreateDomainCommand.PACKAGE_GENERATOR_PROPERTY,
         packageGenerator);
      Path projectDir = outputDir.resolve("com.ngc.model1.domain");
      assertTrue("Cannot find project directory: " + projectDir, Files.isDirectory(projectDir));
      checkGradleBuild(projectDir, "foo");
      checkVelocity(projectDir);
      Map<String, Map<String, Tproperty>> domains = getDomainProperties(projectDir);
      domains.forEach((object, properties) -> {
         assertFalse(object.contains("com"));
         properties.forEach((name, property) -> {
            assertFalse(name.contains("com"));
            assertFalse(property.getType().contains("com"));
         });
      });
   }

   private void checkGradleBuild(Path projectDir, String... fileContents) throws IOException {
      Path buildFile = projectDir.resolve("build.gradle");
      assertTrue("build.gradle is missing", Files.isRegularFile(buildFile));
      TestingFiles.assertFileContains(buildFile, velocityPath.getFileName().toString());
      for (String content : fileContents) {
         TestingFiles.assertFileContains(buildFile, content);
      }
   }

   private void checkVelocity(Path projectDir) {
      Path velocityFolder = projectDir.resolve(Paths.get("src", "main", "resources", "velocity"));
      assertTrue("Could not find velocity folder", Files.isDirectory(velocityFolder));
      assertTrue("Could not find velocity file: " + velocityPath.getFileName(),
         Files.isRegularFile(velocityFolder.resolve(velocityPath.getFileName())));
   }

   private List<Tdomain> getDomain(Path projectDir) throws IOException {
      Path domainDir = projectDir.resolve(Paths.get("src", "main", "resources", "domain"));
      assertTrue(Files.isDirectory(domainDir));
      List<Tdomain> domains = Files.list(domainDir)
                                   .filter(Files::isRegularFile)
                                   .filter(f -> f.toAbsolutePath().toString().endsWith(".xml"))
                                   .map(f -> {
                                      try {
                                         return JAXBUtilities.load(f.toFile(), Tdomain.class);
                                      } catch (IOException e) {
                                         throw new AssertionError(e);
                                      }
                                   })
                                   .collect(Collectors.toList());
      return domains;
   }

   private Map<String, Tobject> getDomainObjects(Path projectDir) throws IOException {
      List<Tdomain> domains = getDomain(projectDir);
      Map<String, Tobject> objects = new HashMap<>();
      domains.forEach(domain -> {
         domain.getObject().forEach(object -> objects.put(object.getClazz(), object));
      });
      return objects;
   }

   private Map<String, Map<String, Tproperty>> getDomainProperties(Path projectDir) throws IOException {
      List<Tdomain> domains = getDomain(projectDir);
      Map<String, Map<String, Tproperty>> objects = new HashMap<>();
      for (Tdomain domain : domains) {
         for (Tobject object : domain.getObject()) {
            Map<String, Tproperty> properties = new HashMap<>();
            for (Tproperty property : object.getProperty()) {
               properties.put(property.getName(), property);
            }
            objects.put(object.getClazz(), properties);
         }
      }
      return objects;
   }

   private void assertDomainObject(Tobject object, String superObject, boolean isObjectAbstract, String name,
            String... properties) {
      assertNotNull(name + " wasn't a domain object", object);
      assertEquals(name, object.getClazz());
      if (superObject == null) {
         assertTrue("object " + name + " does not extend another object",
            object.getExtends() == null || object.getExtends().isEmpty());
      } else {
         assertEquals("Incorrect value for object " + name, superObject, object.getExtends());
      }
      assertEquals("Incorrect value for object " + name,
         isObjectAbstract,
         object.isAbstract() != null && object.isAbstract());
      assertEquals("Incorrect value for object " + name, properties.length, object.getProperty().size());
      for (String property : properties) {
         final boolean isMultiple;
         final boolean isAbstract;
         if (property.startsWith("abstract ")) {
            isAbstract = true;
            property = property.substring(9);
         } else {
            isAbstract = false;
         }
         if (property.startsWith("many ")) {
            isMultiple = true;
            property = property.substring(5);
         } else {
            isMultiple = false;
         }
         String type = property.substring(0, property.indexOf(' '));
         String fieldName = property.substring(property.indexOf(' ') + 1);
         Tproperty actualProperty = object.getProperty()
                                          .stream()
                                          .filter(prop -> prop.getName().equals(fieldName))
                                          .findAny()
                                          .orElseThrow(
                                             () -> new AssertionError(fieldName + " not found in object " + name));
         assertNotNull("property " + fieldName + " is missing from object " + name, actualProperty);
         assertEquals("Invalid value for property " + fieldName + " from object " + name,
            isMultiple,
            actualProperty.isMultiple() != null && actualProperty.isMultiple());
         assertEquals("Invalid value for property " + fieldName + " from object " + name,
            isAbstract,
            actualProperty.isAbstract() != null && actualProperty.isAbstract());
         assertEquals("Invalid type for property " + fieldName + " from object " + name,
            type,
            actualProperty.getType());
      }
   }

   private void runCommand(Object... keyValues) {
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n + 1 < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter<>((String) keyValues[n], keyValues[n + 1]));
      }

      when(options.getParameters()).thenReturn(collection);

      cmd.run(options);
   }

   private static final Module TEST_SERVICE_MODULE = new AbstractModule() {
      @Override
      protected void configure() {
         bind(ILogService.class).toInstance(mock(ILogService.class));
         MockedTemplateService mockedTemplateService = new MockedTemplateService()
                                                                                  .useRealPropertyService()
                                                                                  .useDefaultUserValues(true)
                                                                                  .setTemplateDirectory(
                                                                                     CreateDomainCommand.class.getPackage()
                                                                                                              .getName(),
                                                                                     Paths.get("src",
                                                                                        "main",
                                                                                        "template"));

         MockedResourceService resourceService = new MockedResourceService()
                                                                            .onNextReadDrain(CreateDomainCommandIT.class
                                                                                                                        .getClassLoader()
                                                                                                                        .getResourceAsStream(
                                                                                                                           CreateDomainCommand.DEFAULT_DOMAIN_TEMPLATE_FILE));

         bind(ITemplateService.class).toInstance(mockedTemplateService);
         bind(IResourceService.class).toInstance(resourceService);
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
