package com.ngc.seaside.jellyfish.service.feature.impl.featureservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureInformation;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

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
      setupModel(sdPath, "com.ngc.seaside.testeval", "HamburgerService");

      TreeMap<String, IFeatureInformation> actualFeatures = featureService.getFeatures(sdPath, model);

      assertEquals(actualFeatures.size(), 2);

      for (Map.Entry<String, IFeatureInformation> entry : actualFeatures.entrySet()) {
         String key = entry.getKey();
         IFeatureInformation value = entry.getValue();
         assertTrue(key.contains("HamburgerService"));
      }
   }

   @Test
   public void testContentsofFeatureInformation() {
      Path sdPath = Paths.get("src", "test", "resources");
      setupModel(sdPath, "com.ngc.seaside.testeval", "MilkShakeService");
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
      TreeMap<String, IFeatureInformation> actualFeatures = featureService.getFeatures(sdPath, model);
      assertEquals(actualFeatures.size(), 1);

      for (Map.Entry<String, IFeatureInformation> entry : actualFeatures.entrySet()) {
         String key = entry.getKey();
         IFeatureInformation value = entry.getValue();
         assertTrue(key.contains("MilkShakeService"));
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
     
      TreeMap<String, IFeatureInformation> actualFeatures = featureService.getAllFeatures(sdPath, models);
      
   }

   @SuppressWarnings("unchecked")
   private void setupModel(Path sdPath, String pkg, String name) {
      when(model.getParent().getName()).thenReturn(pkg);
      when(model.getName()).thenReturn(name);
   }
   
   @SuppressWarnings("unchecked")
   private Collection<IModel> setupMultipleModels(Path sdPath, String pkg, String name) {
      when(model.getParent().getName()).thenReturn(pkg);
      when(model.getName()).thenReturn(name);
   }

}
