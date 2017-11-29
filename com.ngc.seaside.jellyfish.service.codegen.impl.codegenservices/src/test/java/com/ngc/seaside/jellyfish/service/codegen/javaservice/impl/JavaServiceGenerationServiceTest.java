package com.ngc.seaside.jellyfish.service.codegen.javaservice.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.EnumDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.MethodDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.PubSubMethodDto;
import com.ngc.seaside.jellyfish.service.codegen.testutils.FlowFactory;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
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
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Optional;

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
   public void setup() throws Throwable {
      when(model.getName()).thenReturn("EngagementTrackPriorityService");
      when(model.getScenarios()).thenReturn(new NamedChildCollection<>());

      when(packageNamingService.getServiceInterfacePackageName(options, model)).thenReturn(
         "com.ngc.seaside.threateval.engagementtrackpriorityservice.api");
      when(packageNamingService.getServiceBaseImplementationPackageName(options, model)).thenReturn(
         "com.ngc.seaside.threateval.engagementtrackpriorityservice.base.impl");

      service = new JavaServiceGenerationService();
      service.setLogService(logService);
      service.setScenarioService(scenarioService);
      service.setPackageNamingService(packageNamingService);
      service.setTransportConfigurationService(transportConfService);
      service.activate();
   }

   @Test
   public void testDoesCreateInterfaceDescriptionForPubSubFlowPathWithCorrelation() throws Throwable {
      IPublishSubscribeMessagingFlow flow = FlowFactory.newPubSubFlowPath("calculateTrackPriority");
      when(flow.getCorrelationDescription()).thenReturn(Optional.of(test));
      when(test.getCompletenessExpressions()).thenReturn(inputInputCorrelations);
      when(test.getCorrelationExpressions()).thenReturn(inputOutputCorrelations);
      
      model.getScenarios().add(flow.getScenario());

      when(packageNamingService.getEventPackageName(options, flow.getInputs().iterator().next().getType()))
                                                                                                           .thenReturn(
                                                                                                              "com.ngc.seaside.threateval.engagementtrackpriorityservice.event.input");
      when(packageNamingService.getEventPackageName(options, flow.getOutputs().iterator().next().getType()))
                                                                                                            .thenReturn(
                                                                                                               "com.ngc.seaside.threateval.engagementtrackpriorityservice.event.output");

      when(scenarioService.getMessagingParadigms(options, flow.getScenario()))
                                                                              .thenReturn(EnumSet.of(
                                                                                 MessagingParadigm.PUBLISH_SUBSCRIBE));
      when(scenarioService.getPubSubMessagingFlows(options, flow.getScenario()))
                                                                                .thenReturn(
                                                                                   Collections.singletonList(flow));

      ClassDto<MethodDto> dto = service.getServiceInterfaceDescription(options, model);
      assertNotNull("dto is null!", dto);
      assertEquals("interface name not correct!", "I" + model.getName(), dto.getName());
      assertEquals("package name not correct!",
         "com.ngc.seaside.threateval.engagementtrackpriorityservice.api",
         dto.getPackageName());
      assertEquals("incorrect number of methods!", 0, dto.getMethods().size());
     

      ClassDto<PubSubMethodDto> baseDto = service.getBaseServiceDescription(options, model);
      assertNotNull("dto is null!", baseDto);
      assertEquals("base name not correct!", "Abstract" + model.getName(), baseDto.getName());
      assertEquals("package name not correct!",
         "com.ngc.seaside.threateval.engagementtrackpriorityservice.base.impl",
         baseDto.getPackageName());
      assertEquals("incorrect number of methods!", 0, baseDto.getMethods().size());

   }

   @Test
   public void testTranportTopics() throws Throwable {
      IPublishSubscribeMessagingFlow flow = FlowFactory.newPubSubFlowPath("calculateTrackPriority");
      model.getScenarios().add(flow.getScenario());

      when(scenarioService.getPubSubMessagingFlows(options, flow.getScenario())).thenReturn(
         Collections.singletonList(flow));

      when(transportConfService.getTransportTopicName(any(), any())).thenAnswer(args -> {
         IDataReferenceField field = args.getArgument(1);
         return field.getName();
      });

      EnumDto<?> topicsEnum = service.getTransportTopicsDescription(options, model);
      assertEquals("EngagementTrackPriorityServiceTransportTopics", topicsEnum.getName());
      assertEquals(new HashSet<>(Arrays.asList("inputField", "outputField")), topicsEnum.getValues());
   }
}
