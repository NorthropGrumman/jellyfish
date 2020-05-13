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
package com.ngc.seaside.systemdescriptor.service.impl.gherkin;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;
import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IFeature;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinScenario;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinTag;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GherkinParsingResultTest {

   private GherkinParsingResult result;

   @Before
   public void setup() {
      result = new GherkinParsingResult();
   }

   @Test
   public void testDoesDetermineIfParsingWasSuccessful() {
      assertTrue("parsing should be successful!",
                 result.isSuccessful());

      IParsingIssue issue = mock(IParsingIssue.class);
      result.addIssue(issue);
      assertFalse("parsing should not be successful!",
                  result.isSuccessful());
   }

   @Test
   public void testDoesGetFeaturesAndIssues() {
      IParsingIssue issue = mock(IParsingIssue.class);
      IFeature feature = mock(IFeature.class);

      result.addIssue(issue).addFeature(feature);
      assertTrue("missing issue!",
                 result.getIssues().contains(issue));
      assertTrue("missing feature!",
                 result.getFeatures().contains(feature));
   }

   @Test
   public void testDoesFindFeatureByNameAndPackage() {
      IFeature feature1 = mock(IFeature.class);
      IFeature feature2 = mock(IFeature.class);
      when(feature1.getFullyQualifiedName()).thenReturn("a.b.MyFeature.myScenario");
      when(feature2.getFullyQualifiedName()).thenReturn("a.b.MyFeature2.myScenario");

      result.addFeature(feature1).addFeature(feature2);

      assertEquals("feature not found by name and package!",
                   feature1,
                   result.findFeature("a.b", "MyFeature.myScenario").orElse(null));
      assertFalse("should not have found feature!",
                  result.findFeature("does.not.Exists").isPresent());
   }

   @Test
   public void testDoesFindFeatureByModel() {
      IPackage package1 = mock(IPackage.class);
      when(package1.getName()).thenReturn("a.b");
      IModel model1 = mock(IModel.class);
      IModel model2 = mock(IModel.class);
      when(model1.getParent()).thenReturn(package1);
      when(model2.getParent()).thenReturn(package1);
      when(model1.getName()).thenReturn("MyModel1");
      when(model2.getName()).thenReturn("MyModel2");

      IFeature feature1 = mock(IFeature.class);
      IFeature feature2 = mock(IFeature.class);
      IFeature feature3 = mock(IFeature.class);
      when(feature1.getPackage()).thenReturn("a.b");
      when(feature2.getPackage()).thenReturn("a.b");
      when(feature3.getPackage()).thenReturn("c.c");
      when(feature1.getName()).thenReturn("MyModel1.sayHello");
      when(feature2.getName()).thenReturn("MyModel1.sayHi");
      when(feature3.getName()).thenReturn("MyModel2.sayGreetings");

      result.addFeatures(Arrays.asList(feature1, feature2, feature3));

      assertTrue("did not find feature by model!",
                 result.findFeatures(model1).contains(feature1));
      assertTrue("did not find feature by model!",
                 result.findFeatures(model1).contains(feature2));
      assertEquals("did not find feature by model!",
                   2,
                   result.findFeatures(model1).size());
      assertTrue("did not expect to find features!",
                 result.findFeatures(model2).isEmpty());
   }

   @Test
   public void testDoesFindFeatureByScenario() {
      IScenario scenario1 = mock(IScenario.class);
      IScenario scenario2 = mock(IScenario.class);
      IScenario scenario3 = mock(IScenario.class);
      IFeature feature1 = mock(IFeature.class);
      IFeature feature2 = mock(IFeature.class);
      IFeature feature3 = mock(IFeature.class);
      when(feature1.getModelScenario()).thenReturn(Optional.of(scenario1));
      when(feature2.getModelScenario()).thenReturn(Optional.of(scenario2));
      when(feature3.getModelScenario()).thenReturn(Optional.empty());

      result.addFeatures(Arrays.asList(feature1, feature2, feature3));

      assertEquals("feature not found by scenario!",
                   feature1,
                   result.findFeature(scenario1).orElse(null));
      assertFalse("should not have found feature!",
                  result.findFeature(scenario3).isPresent());
   }

   @Test
   public void testDoesFindFeatureByTags() {
      IGherkinTag tag1 = tag("a");
      IGherkinTag tag2 = tag("b");
      IGherkinTag tag3 = tag("c");
      IFeature feature1 = mock(IFeature.class);
      IFeature feature2 = mock(IFeature.class);
      IFeature feature3 = mock(IFeature.class);
      when(feature1.getTags()).thenReturn(Collections.singleton(tag1));
      when(feature2.getTags()).thenReturn(Arrays.asList(tag1, tag2));
      when(feature3.getTags()).thenReturn(Collections.singleton(tag3));

      result.addFeatures(Arrays.asList(feature1, feature2, feature3));

      Collection<IFeature> features = result.findFeatures(tag1.getName());
      assertTrue("did not find feature by tag!",
                 features.contains(feature1));
      assertTrue("did not find feature by tag!",
                 features.contains(feature2));
      assertEquals("found to many features!",
                   2,
                   features.size());
   }

   @Test
   public void testDoesFindScenariosByTags() {
      String tag1 = "a";
      String tag2 = "b";
      IGherkinScenario scenario1 = mock(IGherkinScenario.class);
      IGherkinScenario scenario2 = mock(IGherkinScenario.class);
      IGherkinScenario scenario3 = mock(IGherkinScenario.class);
      IFeature feature1 = mock(IFeature.class);
      IFeature feature2 = mock(IFeature.class);
      IFeature feature3 = mock(IFeature.class);
      NamedChildCollection<IFeature, IGherkinScenario> collection1 = new NamedChildCollection<>();
      NamedChildCollection<IFeature, IGherkinScenario> collection2 = new NamedChildCollection<>();
      NamedChildCollection<IFeature, IGherkinScenario> collection3 = new NamedChildCollection<>();

      when(scenario1.hasTag(tag1)).thenReturn(true);
      when(scenario2.hasTag(tag2)).thenReturn(true);
      collection1.add(scenario1);
      collection2.add(scenario2);
      collection3.add(scenario3);
      when(feature1.getScenarios()).thenReturn(collection1);
      when(feature2.getScenarios()).thenReturn(collection2);
      when(feature3.getScenarios()).thenReturn(collection3);

      result.addFeatures(Arrays.asList(feature1, feature2, feature3));

      Collection<IGherkinScenario> scenarios = result.findScenarios(tag1, tag2);
      assertTrue("did not find scenarios by tag!",
                 scenarios.contains(scenario1));
      assertTrue("did not find scenarios by tag!",
                 scenarios.contains(scenario1));
      assertEquals("found too many scenarios!",
                   2,
                   scenarios.size());
   }

   private static IGherkinTag tag(String name) {
      IGherkinTag tag = mock(IGherkinTag.class);
      when(tag.getName()).thenReturn(name);
      return tag;
   }
}
