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
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;
import com.ngc.seaside.systemdescriptor.service.gherkin.api.IGherkinParsingResult;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IFeature;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinScenario;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.NamedChildCollectionWithDuplicateNames;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocatorService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AnalyzeFeaturesCommandTest {

   private AnalyzeFeatureScenariosCommand command;

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

   @Before
   public void setup() {
      when(options.getGherkinParsingResult()).thenReturn(gherkinParsingResult);
      when(gherkinParsingResult.isSuccessful()).thenReturn(true);
      when(options.getParameters()).thenReturn(new DefaultParameterCollection());
      ISystemDescriptor sd = mock(ISystemDescriptor.class);
      when(options.getSystemDescriptor()).thenReturn(sd);
      when(sd.getPackages()).thenReturn(new NamedChildCollection<>());

      command = new AnalyzeFeatureScenariosCommand(logService,
                                           analysisService,
                                           sourceLocatorService);
      command.activate();
   }

   @Test
   public void testDoesNotReportUniqueScenariosInFeature() {
      IFeature feature = mock(IFeature.class);
      when(gherkinParsingResult.getFeatures()).thenReturn(Collections.singleton(feature));
      INamedChildCollection<IFeature, IGherkinScenario> scenarios = new NamedChildCollectionWithDuplicateNames<>();
      IGherkinScenario scenario1 = getMockedScenario("Unique name1");
      IGherkinScenario scenario2 = getMockedScenario("Unique name2");
      scenarios.addAll(Arrays.asList(scenario1, scenario2));
      when(feature.getScenarios()).thenReturn(scenarios);

      command.run(options);

      verify(analysisService, never()).addFinding(any());
   }

   @Test
   public void testDoesReportDuplicateScenariosInFeature() {
      IFeature feature = mock(IFeature.class);
      when(gherkinParsingResult.getFeatures()).thenReturn(Collections.singleton(feature));
      INamedChildCollection<IFeature, IGherkinScenario> scenarios = new NamedChildCollectionWithDuplicateNames<>();
      IGherkinScenario scenario1 = getMockedScenario("Duplicate name");
      IGherkinScenario scenario2 = getMockedScenario("Unique name");
      IGherkinScenario scenario3 = getMockedScenario("Duplicate name");
      scenarios.addAll(Arrays.asList(scenario1, scenario2, scenario3));
      when(feature.getScenarios()).thenReturn(scenarios);

      command.run(options);

      ArgumentCaptor<SystemDescriptorFinding<?>> captor = ArgumentCaptor.forClass(SystemDescriptorFinding.class);
      verify(analysisService, times(1)).addFinding(captor.capture());
      assertEquals(FeatureScenariosFindingTypes.FEATURE_WITH_DUPLICATE_SCENARIOS, captor.getValue().getType());
   }

   private static IGherkinScenario getMockedScenario(String name) {
      IGherkinScenario scenario = mock(IGherkinScenario.class);
      when(scenario.getName()).thenReturn(name);
      return scenario;
   }

}
