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
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;
import com.ngc.seaside.systemdescriptor.service.gherkin.api.IGherkinParsingResult;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IFeature;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocatorService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AnalyzeFeaturesCommandTest {

   private AnalyzeFeaturesCommand command;

   @Mock(answer = Answers.RETURNS_DEEP_STUBS)
   private IAnalysisService analysisService;

   @Mock
   private ILogService logService;

   @Mock
   private ISourceLocatorService sourceLocatorService;

   @Mock
   private IJellyFishCommandOptions options;

   @Mock
   private IGherkinParsingResult gherkinParsingResult;

   @Mock
   private ISystemDescriptor systemDescriptor;

   @Before
   public void setup() {
      when(options.getGherkinParsingResult()).thenReturn(gherkinParsingResult);
      when(options.getSystemDescriptor()).thenReturn(systemDescriptor);
      when(gherkinParsingResult.isSuccessful()).thenReturn(true);

      command = new AnalyzeFeaturesCommand(logService,
                                           analysisService,
                                           sourceLocatorService);
      command.activate();
   }

   @Test
   public void testToesReportFindingWithMissingFeatureFile() {
      IModel model1 = mock(IModel.class);
      IModel model2 = mock(IModel.class);
      IScenario scenario1 = mock(IScenario.class);
      IScenario scenario2 = mock(IScenario.class);
      IFeature feature = mock(IFeature.class);
      NamedChildCollection<IModel, IScenario> scenarios1 = new NamedChildCollection<>();
      NamedChildCollection<IModel, IScenario> scenarios2 = new NamedChildCollection<>();
      when(scenario1.getParent()).thenReturn(model1);
      when(scenario2.getParent()).thenReturn(model2);
      scenarios1.add(scenario1);
      scenarios2.add(scenario2);
      when(model1.getFullyQualifiedName()).thenReturn("a.b.C");
      when(model2.getFullyQualifiedName()).thenReturn("a.b.D");
      when(model1.getScenarios()).thenReturn(scenarios1);
      when(model2.getScenarios()).thenReturn(scenarios2);
      when(gherkinParsingResult.findFeature(scenario1)).thenReturn(Optional.empty());
      when(gherkinParsingResult.findFeature(scenario2)).thenReturn(Optional.of(feature));

      setupForOptionsForSingleModel(model1);
      command.run(options);
      setupForOptionsForSingleModel(model2);
      command.run(options);

      @SuppressWarnings({"unchecked"})
      ArgumentCaptor<SystemDescriptorFinding<?>> captor = ArgumentCaptor.forClass(SystemDescriptorFinding.class);
      verify(analysisService).addFinding(captor.capture());
      SystemDescriptorFinding<?> finding = captor.getValue();
      assertEquals("did not report finding correctly!",
                   FeatureFindingTypes.SCENARIO_WITHOUT_FEATURE,
                   finding.getType());
   }

   @Test
   public void testDoesReportFindingWithMissingSdScenario() {
      IFeature feature1 = mock(IFeature.class);
      IFeature feature2 = mock(IFeature.class);
      IScenario scenario = mock(IScenario.class);
      when(feature1.getModelScenario()).thenReturn(Optional.empty());
      when(feature2.getModelScenario()).thenReturn(Optional.of(scenario));
      when(gherkinParsingResult.getFeatures()).thenReturn(Arrays.asList(feature1, feature2));

      setupForNoModels();
      command.run(options);

      @SuppressWarnings({"unchecked"})
      ArgumentCaptor<SystemDescriptorFinding<?>> captor = ArgumentCaptor.forClass(SystemDescriptorFinding.class);
      verify(analysisService).addFinding(captor.capture());
      SystemDescriptorFinding<?> finding = captor.getValue();
      assertEquals("did not report finding correctly!",
                   FeatureFindingTypes.FEATURE_WITHOUT_SCENARIO,
                   finding.getType());
   }

   private void setupForOptionsForSingleModel(IModel model) {
      DefaultParameterCollection params = new DefaultParameterCollection();
      params.addParameter(new DefaultParameter<>(CommonParameters.MODEL.getName(), model.getFullyQualifiedName()));
      when(options.getParameters()).thenReturn(params);
      when(systemDescriptor.findModel(model.getFullyQualifiedName())).thenReturn(Optional.of(model));
   }

   private void setupForNoModels() {
      when(options.getParameters()).thenReturn(new DefaultParameterCollection());
      when(systemDescriptor.getPackages()).thenReturn(new NamedChildCollection<>());
   }
}
