package com.ngc.seaside.jellyfish.service.codegen.javaservice.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow.FlowType;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.scenario.api.MessagingParadigm;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;

@RunWith(MockitoJUnitRunner.class)
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
   public void testDoesCreateInterfaceDescriptionForPubSubFlowPath() throws Throwable {
      IPublishSubscribeMessagingFlow flow = FlowFactory.newPubSubFlowPath("calculateTrackPriority");
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
      assertEquals("incorrect number of methods!", 1, dto.getMethods().size());

      MethodDto method = dto.getMethods().get(0);
      assertEquals("method name not correct!", "calculateTrackPriority", method.getName());
      assertFalse("method should not override!", method.isOverride());
      assertTrue("method should return!", method.isReturns());
      assertEquals("return not correct!", "TestDataOutput", method.getReturnSnippet());
      assertEquals("argument list not correct!", "TestDataInput inputField", method.getArgumentsListSnippet());
      System.out.println(dto.getImports());
      assertTrue("missing import!",
         dto.getImports()
            .contains("com.ngc.seaside.threateval.engagementtrackpriorityservice.event.input.TestDataInput"));
      assertTrue("missing import!",
         dto.getImports()
            .contains("com.ngc.seaside.threateval.engagementtrackpriorityservice.event.output.TestDataOutput"));

      ClassDto<PubSubMethodDto> baseDto = service.getBaseServiceDescription(options, model);
      assertNotNull("dto is null!", baseDto);
      assertEquals("base name not correct!", "Abstract" + model.getName(), baseDto.getName());
      assertEquals("package name not correct!",
         "com.ngc.seaside.threateval.engagementtrackpriorityservice.base.impl",
         baseDto.getPackageName());
      assertEquals("incorrect number of methods!", 2, baseDto.getMethods().size());

      PubSubMethodDto subscriber = baseDto.getMethods().get(0);
      PubSubMethodDto publisher = baseDto.getMethods().get(1);
      if (subscriber.isPublisher()) {
         PubSubMethodDto temp = subscriber;
         subscriber = publisher;
         publisher = temp;
      }
      assertEquals("method name not correct!", "receiveTestDataInput", subscriber.getName());
      assertFalse("method should not override!", subscriber.isOverride());
      assertFalse("method should not return!", subscriber.isReturns());
      assertEquals("argument list not correct!", "IEvent<TestDataInput> event", subscriber.getArgumentsListSnippet());
      assertFalse("method should be a subscriber!", subscriber.isPublisher());
      assertEquals("method should have a path flow type!", FlowType.PATH, subscriber.getFlow().getFlowType());
      assertEquals("subscriber's publishers not correct!", 1, subscriber.getPublishMethods().size());
      assertEquals("subscriber's publishers not correct!",
         "calculateTrackPriority",
         subscriber.getPublishMethods().entrySet().iterator().next().getKey());
      assertEquals("subscriber's publishers not correct!",
         publisher.getMethodSignature(),
         subscriber.getPublishMethods().entrySet().iterator().next().getValue().getMethodSignature());

      assertEquals("method name not correct!", "publishTestDataOutput", publisher.getName());
      assertFalse("method should not override!", publisher.isOverride());
      assertFalse("method should not return!", publisher.isReturns());
      assertEquals("argument list not correct!", "TestDataOutput outputField", publisher.getArgumentsListSnippet());
      assertTrue("method should be a publisher!", publisher.isPublisher());
      assertEquals("method should have a path flow type!", FlowType.PATH, publisher.getFlow().getFlowType());
      assertTrue("method should not have publishing methods!", publisher.getPublishMethods().isEmpty());
      assertEquals("publishing topic not correct!", "TestDataOutput.TOPIC", publisher.getPublishingTopic());

      assertTrue("missing import!",
         baseDto.getImports()
                .contains("com.ngc.seaside.threateval.engagementtrackpriorityservice.event.input.TestDataInput"));
      assertTrue("missing import!",
         baseDto.getImports()
                .contains("com.ngc.seaside.threateval.engagementtrackpriorityservice.event.output.TestDataOutput"));
   }

   @Test
   public void testDoesCreateInterfaceDescriptionForPubSubFlowSink() throws Throwable {
      IPublishSubscribeMessagingFlow flow = FlowFactory.newPubSubFlowSink("calculateTrackPriority");
      model.getScenarios().add(flow.getScenario());

      when(packageNamingService.getEventPackageName(options, flow.getInputs().iterator().next().getType()))
                                                                                                           .thenReturn(
                                                                                                              "com.ngc.seaside.threateval.engagementtrackpriorityservice.event.input");

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
      assertEquals("incorrect number of methods!", 1, dto.getMethods().size());

      MethodDto method = dto.getMethods().get(0);
      assertEquals("method name not correct!", "calculateTrackPriority", method.getName());
      assertFalse("method should not override!", method.isOverride());
      assertFalse("method should not return!", method.isReturns());
      assertEquals("argument list not correct!", "TestDataInput inputField", method.getArgumentsListSnippet());

      assertTrue("missing import!",
         dto.getImports()
            .contains("com.ngc.seaside.threateval.engagementtrackpriorityservice.event.input.TestDataInput"));

      ClassDto<PubSubMethodDto> baseDto = service.getBaseServiceDescription(options, model);
      assertNotNull("dto is null!", baseDto);
      assertEquals("base name not correct!", "Abstract" + model.getName(), baseDto.getName());
      assertEquals("package name not correct!",
         "com.ngc.seaside.threateval.engagementtrackpriorityservice.base.impl",
         baseDto.getPackageName());
      assertEquals("incorrect number of methods!", 1, baseDto.getMethods().size());

      PubSubMethodDto subscriber = baseDto.getMethods().get(0);
      assertEquals("method name not correct!", "receiveTestDataInput", subscriber.getName());
      assertFalse("method should not override!", subscriber.isOverride());
      assertFalse("method should not return!", subscriber.isReturns());
      assertEquals("argument list not correct!", "IEvent<TestDataInput> event", subscriber.getArgumentsListSnippet());
      assertFalse("method should be a subscriber!", subscriber.isPublisher());
      assertEquals("method should have a sink flow type!", FlowType.SINK, subscriber.getFlow().getFlowType());
      assertEquals("subscriber's publishers not correct!", 1, subscriber.getPublishMethods().size());
      assertEquals("subscriber's publishers not correct!",
         "calculateTrackPriority",
         subscriber.getPublishMethods().entrySet().iterator().next().getKey());
      assertNull("subscriber has not publisher!",
         subscriber.getPublishMethods().entrySet().iterator().next().getValue());

      assertTrue("missing import!",
         baseDto.getImports()
                .contains("com.ngc.seaside.threateval.engagementtrackpriorityservice.event.input.TestDataInput"));
   }

   @Test
   public void testDoesCreateInterfaceDescriptionForPubSubFlowSource() throws Throwable {
      IPublishSubscribeMessagingFlow flow = FlowFactory.newPubSubFlowSource("calculateTrackPriority");
      model.getScenarios().add(flow.getScenario());

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
      assertEquals("incorrect number of methods!", 1, dto.getMethods().size());

      MethodDto method = dto.getMethods().get(0);
      assertEquals("method name not correct!", "calculateTrackPriority", method.getName());
      assertFalse("method should not override!", method.isOverride());
      assertFalse("method should not return!", method.isReturns());
      assertEquals("argument list not correct!", "Consumer<TestDataOutput> consumer", method.getArgumentsListSnippet());

      assertTrue("missing import!",
         dto.getImports()
            .contains("com.ngc.seaside.threateval.engagementtrackpriorityservice.event.output.TestDataOutput"));

      ClassDto<PubSubMethodDto> baseDto = service.getBaseServiceDescription(options, model);
      assertNotNull(baseDto);
      assertEquals("base name not correct!", "Abstract" + model.getName(), baseDto.getName());
      assertEquals("package name not correct!",
         "com.ngc.seaside.threateval.engagementtrackpriorityservice.base.impl",
         baseDto.getPackageName());
      assertEquals("incorrect number of methods!", 1, baseDto.getMethods().size());
      PubSubMethodDto publisher = baseDto.getMethods().get(0);
      assertEquals("method name not correct!", "publishTestDataOutput", publisher.getName());
      assertFalse("method should not override", publisher.isOverride());
      assertFalse("method should not return!", publisher.isReturns());
      assertEquals("argument list not correct!", "TestDataOutput outputField", publisher.getArgumentsListSnippet());
      assertTrue("method should be a publisher!", publisher.isPublisher());
      assertEquals("method should have a source flow type!", FlowType.SOURCE, publisher.getFlow().getFlowType());
      assertEquals("source's publishing method not correct!", 1, publisher.getPublishMethods().size());
      assertEquals("source's publishing method not correct!",
         "calculateTrackPriority",
         publisher.getPublishMethods().entrySet().iterator().next().getKey());
      assertNull("source's publishing method not correct!",
         publisher.getPublishMethods().entrySet().iterator().next().getValue());
      assertEquals("publishing topic not correct!", "TestDataOutput.TOPIC", publisher.getPublishingTopic());

      assertTrue("missing import!",
         baseDto.getImports()
                .contains("com.ngc.seaside.threateval.engagementtrackpriorityservice.event.output.TestDataOutput"));
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
