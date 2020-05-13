/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.jellyfish.service.feature.impl.featureservice;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureInformation;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class FeatureServiceTest {

   private FeatureService featureService;

   @Mock(answer = Answers.RETURNS_DEEP_STUBS)
   private IJellyFishCommandOptions options;

   @Mock
   private ISystemDescriptor systemDescriptor;

   @Before
   public void setup() throws IOException {
      featureService = new FeatureService();
      when(options.getParsingResult().getTestSourcesRoot())
               .thenReturn(Paths.get("src", "test", "resources", "gherkin"));
      when(options.getSystemDescriptor()).thenReturn(systemDescriptor);
      setupModel("com.ngc.seaside.testeval", "HamburgerService", "removeTheCheese", "removeBeef", "removeTomatoes",
               "addBacon", "addTomatoes", "addBeef");
      setupModel("com.ngc.seaside.testeval", "HotdogService", "addKechup", "addMustard");
      setupModel("com.ngc.seaside.testeval", "McDonalService", "provideAverageService");
      setupModel("com.ngc.seaside.testeval", "McDonalService2", "provideAverageService");
      setupModel("com.ngc.seaside.testeval", "MilkShakeService", "melt");
      setupModel("com.ngc.seaside.testeval2", "BooService", "handleTooMuchBoo");
      setupModel("com.ngc.seaside.testeval2", "FooBarService", "handleFooBar");
      setupModel("com.ngc.seaside.testeval2", "FooService", "calculateFooness");
      setupModel("com.ngc.seaside.testeval3", "BotaService");
   }

   @Test
   public void testDoesObtainFeatureFilesForOneModel() {
      IModel model = systemDescriptor.findModel("com.ngc.seaside.testeval", "HamburgerService").get();

      Collection<IFeatureInformation> actualFeatures = featureService.getFeatures(options, model);

      assertEquals(actualFeatures.toString(), 2, actualFeatures.size());

      for (IFeatureInformation feature : actualFeatures) {
         Path key = feature.getPath();
         assertTrue(key.toString().contains("HamburgerService"));
         assertTrue(feature.getModel().isPresent());
         assertTrue(feature.getScenario().isPresent());
      }
   }

   @Test
   public void testDoesObtainFeatureFilesForScenario() {
      IModel model = systemDescriptor.findModel("com.ngc.seaside.testeval", "HamburgerService").get();
      IScenario scenario = model.getScenarios().getByName("addBacon").get();

      when(scenario.getName()).thenReturn("addBacon");
      when(scenario.getParent()).thenReturn(model);
      Collection<IFeatureInformation> actualFeatures = featureService.getFeatures(options, scenario);

      assertEquals("should return only 1 feature file!", 1, actualFeatures.size());

      IFeatureInformation feature = actualFeatures.iterator().next();
      assertEquals("feature is not correct!",
               "HamburgerService.addBacon.feature",
               feature.getPath().getFileName().toString());
      assertEquals(model, feature.getModel().get());
      assertEquals(scenario, feature.getScenario().get());
   }

   @Test
   public void testContentsofFeatureInformation() {
      IModel model = systemDescriptor.findModel("com.ngc.seaside.testeval", "MilkShakeService").get();
      Path expectedAbsolutePath = Paths.get("com.ngc.seaside.jellyfish.service.feature.impl.featureservice",
               "src",
               "test",
               "resources",
               "gherkin",
               "com",
               "ngc",
               "seaside",
               "testeval",
               "MilkShakeService.melt.feature");

      Collection<IFeatureInformation> actualFeatures = featureService.getFeatures(options, model);
      assertEquals(actualFeatures.size(), 1);

      for (IFeatureInformation feature : actualFeatures) {
         Path key = feature.getPath();
         IFeatureInformation value = feature;
         assertTrue(key.toString().contains("MilkShakeService"));
         assertTrue(value.getPath().toAbsolutePath().toString().contains(expectedAbsolutePath.toString()));
         assertTrue(feature.getModel().isPresent());
         assertTrue(feature.getScenario().isPresent());
         assertEquals("MilkShakeService", value.getModel().get().getName());
         assertEquals("melt", value.getScenario().get().getName());
      }
   }

   @Test
   public void testDoesObtainFeatureFilesForMultipleModels() {
      Collection<IFeatureInformation> actualFeatures = featureService.getAllFeatures(options);
      assertEquals(actualFeatures.size(), 10);
      long models = actualFeatures.stream().filter(feature -> feature.getModel().isPresent()).count();
      assertEquals(9, models);
      long scenarios = actualFeatures.stream().filter(feature -> feature.getScenario().isPresent()).count();
      assertEquals(8, scenarios);
   }

   private void setupModel(String pkg, String name, String... scenarios) {
      IModel model = mock(IModel.class, Mockito.RETURNS_DEEP_STUBS);
      when(model.getParent().getName()).thenReturn(pkg);
      when(model.getName()).thenReturn(name);
      when(systemDescriptor.findModel(pkg, name)).thenReturn(Optional.of(model));
      when(systemDescriptor.findModel(pkg + "." + name)).thenReturn(Optional.of(model));
      for (String scenario : scenarios) {
         IScenario s = mock(IScenario.class);
         when(s.getName()).thenReturn(scenario);
         when(s.getParent()).thenReturn(model);
         when(model.getScenarios().getByName(scenario)).thenReturn(Optional.of(s));
      }
   }

}
