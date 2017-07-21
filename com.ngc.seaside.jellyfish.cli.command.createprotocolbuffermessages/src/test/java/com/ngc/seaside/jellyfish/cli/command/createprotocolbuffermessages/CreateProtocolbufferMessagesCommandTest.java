package com.ngc.seaside.jellyfish.cli.command.createprotocolbuffermessages;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.ngc.blocs.guice.module.LogServiceModule;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.cli.command.createdomain.CreateDomainCommandGuiceWrapper;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;

public class CreateProtocolbufferMessagesCommandTest {
   
   private IJellyFishCommand cmd = injector.getInstance(CreateProtocolbufferMessagesCommandGuiceWrapper.class);

   
   private IJellyFishCommandOptions options = Mockito.mock(IJellyFishCommandOptions.class);
   private IParameter<String> parameter = Mockito.mock(IParameter.class);

   private PrintStreamLogService logger = new PrintStreamLogService();

   @Before
   public void setup() throws IOException{
//      Path sdDir = Paths.get("src", "test", "sd");
//      PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.sd");
//      Collection<Path> sdFiles = Files.walk(sdDir).filter(matcher::matches).collect(Collectors.toSet());
//      ISystemDescriptorService sdService = injector.getInstance(ISystemDescriptorService.class);
//      IParsingResult result = sdService.parseFiles(sdFiles);
//      Assert.assertTrue(result.getIssues().toString(), result.isSuccessful());
//      ISystemDescriptor sd = result.getSystemDescriptor();
//      Mockito.when(options.getSystemDescriptor()).thenReturn(sd);
   }

   @Test
   public void testCommand() {
      
      //TODO Should we even inject the IJellyFishCommandProvider class or just mock it?
      
      cmd.run(options);
   }
   
   private static final Module LOG_SERVICE_MODULE = new AbstractModule() {
      @Override
      protected void configure() {
         bind(ILogService.class).to(PrintStreamLogService.class);
      }
   };
   
   private static final Module JELLYFISH_CMD_PROVIDER_MODULE = new AbstractModule() {
      @Override
      protected void configure() {
        // bind(IJellyFishCommandProvider.class).to(JellyFishCommandProvider.class);
      }
   };
   
   private static Collection<Module> getModules() {
      Collection<Module> modules = new ArrayList<>();
      modules.add(LOG_SERVICE_MODULE);
      modules.add(JELLYFISH_CMD_PROVIDER_MODULE);
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
