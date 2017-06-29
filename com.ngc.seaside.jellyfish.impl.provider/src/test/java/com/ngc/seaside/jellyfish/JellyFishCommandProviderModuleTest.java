package com.ngc.seaside.jellyfish;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.impl.provider.JellyFishCommandProviderModule;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(MockitoJUnitRunner.class)
public class JellyFishCommandProviderModuleTest {

   private JellyFishCommandProviderModule module;

   private final IJellyFishCommand mockCommand = Mockito.mock(IJellyFishCommand.class);

   private String previousNgFwHome;

   private IJellyFishCommandOptions options;
   
   @Before
   public void before() throws Throwable {
      previousNgFwHome = System.getProperty("NG_FW_HOME");
      String ngFwHome = Paths.get(System.getProperty("user.dir"), "build", "resources", "test").toAbsolutePath().toString();
      System.setProperty("NG_FW_HOME", ngFwHome);
      Injector injector = getInjector();
      
      Mockito.doAnswer(invocation -> {
         options = (IJellyFishCommandOptions) invocation.getArguments()[0];
         return null;
      }).when(mockCommand).run(Mockito.any(IJellyFishCommandOptions.class));
      Mockito.when(mockCommand.getName()).thenReturn("mock");
      
      module = injector.getInstance(JellyFishCommandProviderModule.class);
      module.addCommand(mockCommand);
      
   }

   @Test
   public void systemDescriptorLoadedIntoMemoryTest() throws URISyntaxException {
      Path root = Paths.get(getClass().getClassLoader().getResource("valid-project").toURI());

      module.run(new String[] { "mock", "-DinputDir=" + root });
      Assert.assertNotNull(options);
      Assert.assertNotNull(options.getSystemDescriptor());
   }

   public void invalidSdProjectStructureParsed() throws URISyntaxException {
      Path root = Paths.get(getClass().getClassLoader().getResource("invalid-grammar-project").toURI());

      module.run(new String[] { "mock", "-DinputDir=" + root });
      Assert.assertNotNull(options);
      Assert.assertNull(options.getSystemDescriptor());
   }

   @Test
   public void invalidDirProjectStructureParsed() throws URISyntaxException {
      Path root = Paths.get(System.getProperty("user.dir"), "invalid", "path");

      module.run(new String[] { "mock", "-DinputDir=" + root });
      Assert.assertNotNull(options);
      Assert.assertNull(options.getSystemDescriptor());
   }

   @After
   public void after() throws Throwable {
      if (previousNgFwHome == null) {
         System.clearProperty("NG_FW_HOME");
      } else {
         System.setProperty("NG_FW_HOME", previousNgFwHome);
      }
   }

   private static Injector getInjector() {
      Collection<Module> modules = new ArrayList<>();
      modules.add(new JellyFishCommandProviderModule());
      for (Module dynamicModule : ServiceLoader.load(Module.class)) {
         modules.add(dynamicModule);
      }
      // TODO TH: put a comment here explaining this
      modules.removeIf(m -> m instanceof XTextSystemDescriptorServiceModule);
      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
      return Guice.createInjector(modules);
   }
}
