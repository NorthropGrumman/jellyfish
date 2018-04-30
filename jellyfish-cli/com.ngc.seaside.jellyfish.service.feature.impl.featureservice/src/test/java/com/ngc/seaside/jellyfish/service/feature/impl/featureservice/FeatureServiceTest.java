package com.ngc.seaside.jellyfish.service.feature.impl.featureservice;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureInformation;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.NavigableMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeatureServiceTest {

   private FeatureService featureService;

   @Mock
   private IJellyFishCommandOptions options;

   @Mock
   private ISystemDescriptor systemDescriptor;

   @Mock
   private IModel model;

   @Before
   public void setup() throws IOException {
      featureService = new FeatureService();
      featureService.setLogService(new PrintStreamLogService());

      // Setup mock model
      when(model.getParent()).thenReturn(mock(IPackage.class));
   }

   @Test
   public void testDoesObtainFeatureFilesForOneModel() {
      Path sdPath = Paths.get("src", "test", "resources");
      setupModel("com.ngc.seaside.testeval", "HamburgerService");

      NavigableMap<Path, IFeatureInformation> actualFeatures = featureService.getFeatures(sdPath, model);

      assertEquals(actualFeatures.size(), 2);

      for (Entry<Path, IFeatureInformation> entry : actualFeatures.entrySet()) {
         Path key = entry.getKey();
         assertTrue(key.toString().contains("HamburgerService"));
      }
   }

   @Test
   public void testDoesObtainFeatureFilesForScenario() {
      Path sdPath = Paths.get("src", "test", "resources");
      setupModel("com.ngc.seaside.testeval", "HamburgerService");

      IScenario scenario = mock(IScenario.class);
      when(scenario.getName()).thenReturn("addBacon");
      when(scenario.getParent()).thenReturn(model);
      NavigableMap<Path, IFeatureInformation> actualFeatures = featureService.getFeatures(sdPath, scenario);

      assertEquals("should return only 1 feature file!",
                   actualFeatures.size(),
                   1);

      assertEquals("feature is not correct!",
                   "HamburgerService.addBacon",
                   actualFeatures.values().iterator().next().getFullyQualifiedName());
   }

   @Test
   public void testContentsofFeatureInformation() {
      Path sdPath = Paths.get("src", "test", "resources");
      setupModel("com.ngc.seaside.testeval", "MilkShakeService");
      Path expectedAbsolutePath = Paths.get("com.ngc.seaside.jellyfish.service.feature.impl.featureservice",
                                            "src",
                                            "test",
                                            "resources",
                                            "src",
                                            "test",
                                            "gherkin",
                                            "com",
                                            "ngc",
                                            "seaside",
                                            "testeval",
                                            "MilkShakeService.melt.feature");

      Path expectedRelativePath = Paths.get("com", "ngc", "seaside", "testeval", "MilkShakeService.melt.feature");
      NavigableMap<Path, IFeatureInformation> actualFeatures = featureService.getFeatures(sdPath, model);
      assertEquals(actualFeatures.size(), 1);

      for (Entry<Path, IFeatureInformation> entry : actualFeatures.entrySet()) {
         Path key = entry.getKey();
         IFeatureInformation value = entry.getValue();
         assertTrue(key.toString().contains("MilkShakeService"));
         assertTrue(value.getAbsolutePath().toString().contains(expectedAbsolutePath.toString()));
         assertEquals("MilkShakeService.melt.feature", value.getFileName());
         assertEquals("MilkShakeService.melt", value.getFullyQualifiedName());
         assertEquals("melt", value.getName());
         assertEquals(expectedRelativePath, value.getRelativePath());
      }
   }

   @Test
   public void testDoesObtainFeatureFilesForMultipleModels() {
      Path sdPath = Paths.get("src", "test", "resources");
      Collection<IModel> models = setupMultipleModels();
      NavigableMap<Path, IFeatureInformation> actualFeatures = featureService.getAllFeatures(sdPath, models);
      assertEquals(actualFeatures.size(), 4);
   }

   private void setupModel(String pkg, String name) {
      when(model.getParent().getName()).thenReturn(pkg);
      when(model.getName()).thenReturn(name);
   }

   private Collection<IModel> setupMultipleModels() {
      Collection<IModel> mockModelCollection = new ArrayList<>();

      IModel model0 = mock(IModel.class);
      IModel model1 = mock(IModel.class);
      IModel model2 = mock(IModel.class);

      when(model0.getParent()).thenReturn(mock(IPackage.class));
      when(model1.getParent()).thenReturn(mock(IPackage.class));
      when(model2.getParent()).thenReturn(mock(IPackage.class));

      when(model0.getParent().getName()).thenReturn("com.ngc.seaside.testeval");
      when(model1.getParent().getName()).thenReturn("com.ngc.seaside.testeval2");
      when(model2.getParent().getName()).thenReturn("com.ngc.seaside.testeval3");

      when(model0.getName()).thenReturn("HamburgerService");
      when(model1.getName()).thenReturn("BooService");
      when(model2.getName()).thenReturn("BotaService");

      mockModelCollection.add(model0);
      mockModelCollection.add(model1);
      mockModelCollection.add(model2);

      return mockModelCollection;
   }
}
