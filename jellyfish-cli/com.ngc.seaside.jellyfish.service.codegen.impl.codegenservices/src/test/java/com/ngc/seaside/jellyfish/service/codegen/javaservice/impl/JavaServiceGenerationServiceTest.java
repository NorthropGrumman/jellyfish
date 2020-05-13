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
package com.ngc.seaside.jellyfish.service.codegen.javaservice.impl;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.scenarios.FlowFactory;
import com.ngc.seaside.jellyfish.cli.command.test.service.config.MockedTelemetryConfigurationService;
import com.ngc.seaside.jellyfish.cli.command.test.service.config.MockedTelemetryReportingConfigurationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.EnumDto;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IRequestResponseMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.scenario.api.MessagingParadigm;
import com.ngc.seaside.jellyfish.service.scenario.correlation.api.ICorrelationDescription;
import com.ngc.seaside.jellyfish.service.scenario.correlation.api.ICorrelationExpression;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class JavaServiceGenerationServiceTest {

   private JavaServiceGenerationService service;

   @Mock
   private IModel model;

   @Mock
   private IJellyFishCommandOptions options;

   @Mock
   private IScenarioService scenarioService;

   @Mock
   private IPackageNamingService packageNamingService;

   @Mock
   private ILogService logService;

   @Mock
   private ITransportConfigurationService transportConfService;

   @Mock
   private Collection<ICorrelationExpression> inputInputCorrelations;

   @Mock
   private Collection<ICorrelationExpression> inputOutputCorrelations;

   @Mock
   private ICorrelationDescription correlationService;

   @Mock
   private ICorrelationDescription test;
   
   @Before
   public void setup() {
      when(model.getName()).thenReturn("EngagementTrackPriorityService");
      when(model.getScenarios()).thenReturn(new NamedChildCollection<>());
      when(model.getParts()).thenReturn(new NamedChildCollection<>());

      when(packageNamingService.getServiceInterfacePackageName(options, model)).thenReturn(
            "com.ngc.seaside.threateval.engagementtrackpriorityservice.api");
      when(packageNamingService.getServiceBaseImplementationPackageName(options, model)).thenReturn(
            "com.ngc.seaside.threateval.engagementtrackpriorityservice.base.impl");

      service = new JavaServiceGenerationService();
      service.setLogService(logService);
      service.setScenarioService(scenarioService);
      service.setPackageNamingService(packageNamingService);
      service.setTransportConfigurationService(transportConfService);
      service.setTelemetryConfigurationService(new MockedTelemetryConfigurationService());
      service.setTelemetryReportingConfigurationService(new MockedTelemetryReportingConfigurationService());
      service.activate();
   }

   @Test
   public void testDoesCreateInterfaceDescriptionForPubSubFlowPathWithCorrelation() {
      IPublishSubscribeMessagingFlow flow = FlowFactory.newPubSubFlowPath("calculateTrackPriority");
      when(flow.getCorrelationDescription()).thenReturn(Optional.of(test));
      when(test.getCompletenessExpressions()).thenReturn(inputInputCorrelations);
      when(test.getCorrelationExpressions()).thenReturn(inputOutputCorrelations);

      model.getScenarios().add(flow.getScenario());

      when(packageNamingService.getEventPackageName(options, flow.getInputs().iterator().next().getType()))
            .thenReturn("com.ngc.seaside.threateval.engagementtrackpriorityservice.event.input");
      when(packageNamingService.getEventPackageName(options, flow.getOutputs().iterator().next().getType()))
            .thenReturn("com.ngc.seaside.threateval.engagementtrackpriorityservice.event.output");

      when(scenarioService.getMessagingParadigms(options, flow.getScenario())).thenReturn(EnumSet.of(
            MessagingParadigm.PUBLISH_SUBSCRIBE));
      when(scenarioService.getPubSubMessagingFlow(options, flow.getScenario())).thenReturn(
            Optional.of(flow));

      ClassDto dto = service.getServiceInterfaceDescription(options, model);
      assertNotNull("dto is null!", dto);
      assertEquals("interface name not correct!", "I" + model.getName(), dto.getName());
      assertEquals("package name not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.api",
                   dto.getPackageName());

      ClassDto baseDto = service.getBaseServiceDescription(options, model);
      assertNotNull("dto is null!", baseDto);
      assertEquals("base name not correct!", "Abstract" + model.getName(), baseDto.getName());
      assertEquals("package name not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.base.impl",
                   baseDto.getPackageName());
   }

   @Test
   public void testTransportTopics() {
      IPublishSubscribeMessagingFlow pubSubFlow = FlowFactory.newPubSubFlowPath("calculateTrackPriority");
      model.getScenarios().add(pubSubFlow.getScenario());
      when(scenarioService.getPubSubMessagingFlow(options, pubSubFlow.getScenario()))
            .thenReturn(Optional.of(pubSubFlow));

      IRequestResponseMessagingFlow reqResFlow = FlowFactory.newRequestResponseServerFlow("getTrackPriority");
      model.getScenarios().add(reqResFlow.getScenario());
      when(scenarioService.getRequestResponseMessagingFlow(options, reqResFlow.getScenario()))
            .thenReturn(Optional.of(reqResFlow));

      when(transportConfService.getTransportTopicName(any(), any())).thenAnswer(args -> {
         IDataReferenceField field = args.getArgument(1);
         return field.getName();
      });

      EnumDto topicsEnum = service.getTransportTopicsDescription(options, model);
      assertEquals("EngagementTrackPriorityServiceTransportTopics", topicsEnum.getName());
      assertEquals("missing transport topics!",
                   new HashSet<>(Arrays.asList("inputField", "outputField", "requestField")),
                   topicsEnum.getValues());
   }
}
