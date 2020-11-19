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
package com.ngc.seaside.jellyfish.cli.command.analyze.feature;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocatorService;

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
