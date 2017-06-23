package com.ngc.seaside.jellyfish;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.impl.provider.JellyFishCommandProviderModule;
import com.ngc.seaside.systemdescriptor.service.api.ParsingException;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

@RunWith(MockitoJUnitRunner.class)
public class JellyFishCommandProviderTests
{

   private JellyFishCommandProviderModule module;

   private final MockCommand mockCommand = new MockCommand();

   private String previousNgFwHome;
   
   @Before
   public void before() throws Throwable
   {
      previousNgFwHome = System.getProperty("NG_FW_HOME");
      String ngFwHome = Paths.get(System.getProperty("user.dir"), "build", "resources", "test").toAbsolutePath().toString();
      System.setProperty("NG_FW_HOME", ngFwHome);
      Injector injector = getInjector();
      module = injector.getInstance(JellyFishCommandProviderModule.class);
      module.addCommand(mockCommand);
   }

   @Test
   public void systemDescriptorLoadedIntoMemoryTest() throws URISyntaxException
   {
      Path root = Paths.get(getClass().getClassLoader().getResource("valid-project").toURI());
      module.run(new String[] { "mock", "-DinputDir=" + root });
      IJellyFishCommandOptions options = mockCommand.options;
      Assert.assertNotNull(options);
      Assert.assertNotNull(options.getSystemDescriptor());
   }

   @Test(expected = ParsingException.class)
   public void invalidSdProjectStructureParsed() throws URISyntaxException
   {
      Path root = Paths.get(getClass().getClassLoader().getResource("invalid-grammar-project").toURI());
      module.run(new String[] { "mock", "-DinputDir=" + root });
   }

   @Test(expected = IllegalArgumentException.class)
   public void invalidDirProjectStructureParsed() throws URISyntaxException
   {
      Path root = Paths.get(System.getProperty("user.dir"), "invalid", "path");
      module.run(new String[] { "mock", "-DinputDir=" + root });
   }

   @After
   public void after() throws Throwable
   {
      if (previousNgFwHome == null) {
         System.clearProperty("NG_FW_HOME");
      } else {
         System.setProperty("NG_FW_HOME", previousNgFwHome);
      }
   }

   private static class MockCommand implements IJellyFishCommand
   {

      private IJellyFishCommandOptions options;

      @Override
      public String getName()
      {
         return "mock";
      }

      @Override
      public IUsage getUsage()
      {
         return new DefaultUsage("", new DefaultParameter("inputDir", false));
      }

      @Override
      public void run(IJellyFishCommandOptions commandOptions)
      {
         Assert.assertNull("MockCommand should be only called once", options);
         this.options = commandOptions;
      }

   }

   private static Injector getInjector()
   {
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
