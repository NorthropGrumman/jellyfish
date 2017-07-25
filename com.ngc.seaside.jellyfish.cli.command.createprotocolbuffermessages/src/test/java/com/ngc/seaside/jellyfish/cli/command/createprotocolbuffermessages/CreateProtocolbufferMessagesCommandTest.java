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
import com.ngc.seaside.bootstrap.service.impl.templateservice.TemplateServiceGuiceModule;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.template.MockedTemplateService;
import com.ngc.seaside.jellyfish.cli.command.createdomain.CreateDomainCommand;
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
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class CreateProtocolbufferMessagesCommandTest {

   private IJellyFishCommand cmd = injector.getInstance(CreateProtocolbufferMessagesCommandGuiceWrapper.class);

   private IJellyFishCommandOptions options = Mockito.mock(IJellyFishCommandOptions.class);
   private IParameter<String> parameter = Mockito.mock(IParameter.class);

   private PrintStreamLogService logger = new PrintStreamLogService();

   private Path outputDir;
   private Path velocityPath;

   @Before
   public void setup() throws IOException {
      outputDir = Files.createTempDirectory(null);
      outputDir.toFile().deleteOnExit();
      velocityPath = Paths.get("src", "test", "resources", "proto-messages.vm").toAbsolutePath();

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
   public void testCommand() {

      //
      // //Testing that I can call hardcoded help command
      // DefaultParameterCollection parameters = new DefaultParameterCollection();
      // DefaultParameter parameter = new DefaultParameter("verbose").setRequired(false);
      // parameter.setValue("true");
      // parameters.addParameter(parameter);
      // Mockito.when(options.getParameters()).thenReturn(parameters);
      //
      // cmd.run(options);

      runCommand(CreateDomainCommand.MODEL_PROPERTY, "com.ngc.seaside.test1.Model1",
         CreateDomainCommand.OUTPUT_DIRECTORY_PROPERTY, outputDir.toString(),
         CreateDomainCommand.DOMAIN_TEMPLATE_FILE_PROPERTY, velocityPath.toString());
   }

   private void runCommand(String... keyValues) {
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n + 1 < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter<String>(keyValues[n]).setValue(keyValues[n + 1]));
      }

      Mockito.when(options.getParameters()).thenReturn(collection);

      cmd.run(options);
   }

   private static final Module TEST_SERVICE_MODULE = new AbstractModule() {
      @Override
      protected void configure() {
         bind(ILogService.class).to(PrintStreamLogService.class);
         MockedTemplateService mockedTemplateService = new MockedTemplateService();
         mockedTemplateService = new MockedTemplateService().useRealPropertyService().useDefaultUserValues(true)
                  .setTemplateDirectory(CreateProtocolbufferMessagesCommand.class.getPackage().getName(),
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
         if (!(dynamicModule instanceof LogServiceModule) && !(dynamicModule instanceof ResourceServiceModule)
            && !(dynamicModule instanceof TemplateServiceGuiceModule)) {
            modules.add(dynamicModule);
         }
      }

      modules.removeIf(m -> m instanceof XTextSystemDescriptorServiceModule);
      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
      return modules;
   }

   private static final Injector injector = Guice.createInjector(getModules());

}
