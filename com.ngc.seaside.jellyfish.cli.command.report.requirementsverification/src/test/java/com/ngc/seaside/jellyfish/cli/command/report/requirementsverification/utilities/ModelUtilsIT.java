package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification.utilities;

import static org.junit.Assert.*;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class ModelUtilsIT {
   private static final PrintStreamLogService logger = new PrintStreamLogService();
   private static final Injector injector = Guice.createInjector(getModules());

   private DefaultParameterCollection parameters;

   @Mock
   private IJellyFishCommandOptions jellyFishCommandOptions;

   private static Collection<Module> getModules() {
      Collection<Module> modules = new ArrayList<>();
      modules.removeIf(m -> m instanceof XTextSystemDescriptorServiceModule);
      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
      modules.add(new AbstractModule() {
         @Override
         protected void configure() {
            bind(ILogService.class).toInstance(logger);
         }
      });
      return modules;
   }

   @Before
   public void setup() throws IOException {
      parameters = new DefaultParameterCollection();
      Mockito.when(jellyFishCommandOptions.getParameters()).thenReturn(parameters);
      Mockito.when(jellyFishCommandOptions.getSystemDescriptorProjectPath()).thenReturn(Paths.get("src/test/resources"));

      // Setup mock system descriptor
      Path sdDir = Paths.get("src", "test", "resources");
      PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.sd");
      Collection<Path> sdFiles = Files.walk(sdDir).filter(matcher::matches).collect(Collectors.toSet());
      ISystemDescriptorService sdService = injector.getInstance(ISystemDescriptorService.class);
      IParsingResult result = sdService.parseFiles(sdFiles);
      Assert.assertTrue(result.getIssues().toString(), result.isSuccessful());
      ISystemDescriptor sd = result.getSystemDescriptor();
      Mockito.when(jellyFishCommandOptions.getSystemDescriptor()).thenReturn(sd);
   }

   @Test
   public void givenFeatureFolderWithNonFeatureFiles_whenGetAllFeatures_thenNoExceptionThrown() {
      IPackage mockPackage = Mockito.mock(IPackage.class);         
      IModel mockModel = Mockito.mock(IModel.class);         

      Mockito.when(mockPackage.getName()).thenReturn("com.ngc.seaside.testeval4");
      Mockito.when(mockModel.getParent()).thenReturn(mockPackage);
      
      Collection<IModel> models = new ArrayList<>();
      models.add(mockModel);
      
      ModelUtils.getAllFeatures(jellyFishCommandOptions, models, "src/test/gherkin");
   }
}
