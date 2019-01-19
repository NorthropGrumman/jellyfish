/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.cli.command.analyze.feature;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureInformation;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureService;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocatorService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import static com.ngc.seaside.jellyfish.cli.command.analyze.feature.FeatureFindingTypes.FEATURE_WITHOUT_SCENARIO;
import static com.ngc.seaside.jellyfish.cli.command.analyze.feature.FeatureFindingTypes.SCENARIO_WITHOUT_FEATURE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AnalyzeFeaturesCommandTest {

   private AnalyzeFeaturesCommand command;

   @Mock(answer = Answers.RETURNS_DEEP_STUBS)
   private IAnalysisService analysisService;

   @Mock
   private ISourceLocatorService sourceLocatorService;

   @Mock
   private IFeatureService featureService;

   @Mock(answer = Answers.RETURNS_DEEP_STUBS)
   private IJellyFishCommandOptions options;

   @Before
   public void setup() {
      command = new AnalyzeFeaturesCommand();
      command.setLogService(mock(ILogService.class));
      command.setAnalysisService(analysisService);
      command.setSourceLocatorService(sourceLocatorService);
      command.setFeatureService(featureService);
      command.activate();
   }

   @Test
   public void testAnalysis() {
      Path featurePath = Paths.get("src", "test", "resources", "gherkin", "com", "ngc", "test");
      IFeatureInformation info1 = mock(IFeatureInformation.class, RETURNS_DEEP_STUBS);
      IFeatureInformation info2 = mock(IFeatureInformation.class, RETURNS_DEEP_STUBS);
      IModel model = mock(IModel.class, RETURNS_DEEP_STUBS);
      IScenario scenario1 = mock(IScenario.class, RETURNS_DEEP_STUBS);
      IScenario scenario2 = mock(IScenario.class, RETURNS_DEEP_STUBS);
      when(model.getFullyQualifiedName()).thenReturn("com.ngc.test.ExampleModel");
      INamedChildCollection<IModel, IScenario> scenarioCollection = new NamedChildCollection<>();
      scenarioCollection.add(scenario1);
      scenarioCollection.add(scenario2);
      when(model.getScenarios()).thenReturn(scenarioCollection);
      when(scenario1.getName()).thenReturn("scenario1");
      when(scenario1.getParent()).thenReturn(model);
      when(scenario2.getName()).thenReturn("missingScenario2");
      when(scenario2.getParent()).thenReturn(model);
      when(info1.getModel()).thenReturn(Optional.of(model));
      when(info2.getModel()).thenReturn(Optional.empty());
      when(info1.getScenario()).thenReturn(Optional.of(scenario1));
      when(info2.getScenario()).thenReturn(Optional.empty());
      when(info1.getPath()).thenReturn(featurePath.resolve("ExampleModel.scenario1.feature"));
      when(info2.getPath()).thenReturn(featurePath.resolve("UnknownModel.scenario1.feature"));
      when(featureService.getAllFeatures(any())).thenReturn(Arrays.asList(info1, info2));
      IPackage pkg = mock(IPackage.class, RETURNS_DEEP_STUBS);
      INamedChildCollection<ISystemDescriptor, IPackage> pkgCollection = new NamedChildCollection<>();
      pkgCollection.add(pkg);
      when(options.getSystemDescriptor().getPackages()).thenReturn(pkgCollection);
      INamedChildCollection<IPackage, IModel> modelCollection = new NamedChildCollection<>();
      modelCollection.add(model);
      when(pkg.getModels()).thenReturn(modelCollection);

      ISourceLocation location = mock(ISourceLocation.class);
      when(sourceLocatorService.getLocation(scenario2, false)).thenReturn(location);

      command.run(options);

      ArgumentCaptor<SystemDescriptorFinding<?>> captor = ArgumentCaptor.forClass(SystemDescriptorFinding.class);
      verify(analysisService, times(2)).addFinding(captor.capture());
      SystemDescriptorFinding<?> finding1 = captor.getAllValues().get(0);
      SystemDescriptorFinding<?> finding2 = captor.getAllValues().get(1);
      assertTrue(finding1.getType() == FEATURE_WITHOUT_SCENARIO || finding2.getType() == FEATURE_WITHOUT_SCENARIO);
      assertTrue(finding1.getType() == SCENARIO_WITHOUT_FEATURE || finding2.getType() == SCENARIO_WITHOUT_FEATURE);
      SystemDescriptorFinding<?> featureWithoutScenario =
               finding1.getType() == FEATURE_WITHOUT_SCENARIO ? finding1 : finding2;
      SystemDescriptorFinding<?> scenarioWithoutFeature =
               finding1.getType() == SCENARIO_WITHOUT_FEATURE ? finding1 : finding2;
      assertEquals(Optional.empty(), featureWithoutScenario.getLocation());
      assertEquals(location, scenarioWithoutFeature.getLocation().get());
   }

}
