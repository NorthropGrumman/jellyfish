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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocatorService;

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
