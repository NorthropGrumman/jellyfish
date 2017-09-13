package com.ngc.seaside.jellyfish.service.codegen.javaservice.impl;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.MethodDto;
import com.ngc.seaside.jellyfish.service.codegen.testutils.FlowFactory;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.scenario.api.MessagingParadigm;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.EnumSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

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

   @Before
   public void setup() throws Throwable {
      when(model.getName()).thenReturn("EngagementTrackPriorityService");
      when(model.getScenarios()).thenReturn(new NamedChildCollection<>());

      when(packageNamingService.getServiceInterfacePackageName(options, model))
            .thenReturn("com.ngc.seaside.threateval.engagementtrackpriorityservice.api");

      service = new JavaServiceGenerationService();
      service.setLogService(logService);
      service.setScenarioService(scenarioService);
      service.setPackageNamingService(packageNamingService);
      service.activate();
   }

   @Test
   public void testDoesCreateInterfaceDescriptionForPubSubFlowPath() throws Throwable {
      IPublishSubscribeMessagingFlow flow = FlowFactory.newPubSubFlowPath("calculateTrackPriority");
      model.getScenarios().add(flow.getScenario());

      when(packageNamingService.getEventPackageName(options, flow.getInputs().iterator().next().getType()))
            .thenReturn("com.ngc.seaside.threateval.engagementtrackpriorityservice.event.input");
      when(packageNamingService.getEventPackageName(options, flow.getOutputs().iterator().next().getType()))
            .thenReturn("com.ngc.seaside.threateval.engagementtrackpriorityservice.event.output");

      when(scenarioService.getMessagingParadigms(options, flow.getScenario()))
            .thenReturn(EnumSet.of(MessagingParadigm.PUBLISH_SUBSCRIBE));
      when(scenarioService.getPubSubMessagingFlows(options, flow.getScenario()))
            .thenReturn(Collections.singletonList(flow));

      ClassDto dto = service.getServiceInterfaceDescription(options, model);
      assertNotNull("dto is null!",
                    dto);
      assertEquals("interface name not correct!",
                   "I" + model.getName(),
                   dto.getName());
      assertEquals("package name not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.api",
                   dto.getPackageName());
      assertEquals("incorrect number of methods!",
                   1,
                   dto.getMethods().size());

      MethodDto method = dto.getMethods().get(0);
      assertEquals("method name not correct!",
                   "calculateTrackPriority",
                   method.getName());
      assertFalse("method should not override!",
                  method.isOverride());
      assertTrue("method should return!",
                 method.isReturns());
      assertEquals("return not correct!",
                   "TestDataOutput",
                   method.getReturnSnippet());
      assertEquals("argument list not correct!",
                   "TestDataInput inputField",
                   method.getArgumentsListSnippet());

      assertTrue("missing import!",
                 dto.getImports().contains("com.ngc.seaside.threateval.engagementtrackpriorityservice.event.input"));
      assertTrue("missing import!",
                 dto.getImports().contains("com.ngc.seaside.threateval.engagementtrackpriorityservice.event.output"));
   }

   @Test
   public void testDoesCreateInterfaceDescriptionForPubSubFlowSink() throws Throwable {
      IPublishSubscribeMessagingFlow flow = FlowFactory.newPubSubFlowSink("calculateTrackPriority");
      model.getScenarios().add(flow.getScenario());

      when(packageNamingService.getEventPackageName(options, flow.getInputs().iterator().next().getType()))
            .thenReturn("com.ngc.seaside.threateval.engagementtrackpriorityservice.event.input");

      when(scenarioService.getMessagingParadigms(options, flow.getScenario()))
            .thenReturn(EnumSet.of(MessagingParadigm.PUBLISH_SUBSCRIBE));
      when(scenarioService.getPubSubMessagingFlows(options, flow.getScenario()))
            .thenReturn(Collections.singletonList(flow));

      ClassDto dto = service.getServiceInterfaceDescription(options, model);
      assertNotNull("dto is null!",
                    dto);
      assertEquals("interface name not correct!",
                   "I" + model.getName(),
                   dto.getName());
      assertEquals("package name not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.api",
                   dto.getPackageName());
      assertEquals("incorrect number of methods!",
                   1,
                   dto.getMethods().size());

      MethodDto method = dto.getMethods().get(0);
      assertEquals("method name not correct!",
                   "calculateTrackPriority",
                   method.getName());
      assertFalse("method should not override!",
                  method.isOverride());
      assertFalse("method should not return!",
                 method.isReturns());
      assertEquals("argument list not correct!",
                   "TestDataInput inputField",
                   method.getArgumentsListSnippet());

      assertTrue("missing import!",
                 dto.getImports().contains("com.ngc.seaside.threateval.engagementtrackpriorityservice.event.input"));
   }

   @Test
   public void testDoesCreateInterfaceDescriptionForPubSubFlowSource() throws Throwable {
      IPublishSubscribeMessagingFlow flow = FlowFactory.newPubSubFlowSource("calculateTrackPriority");
      model.getScenarios().add(flow.getScenario());

      when(packageNamingService.getEventPackageName(options, flow.getOutputs().iterator().next().getType()))
            .thenReturn("com.ngc.seaside.threateval.engagementtrackpriorityservice.event.output");

      when(scenarioService.getMessagingParadigms(options, flow.getScenario()))
            .thenReturn(EnumSet.of(MessagingParadigm.PUBLISH_SUBSCRIBE));
      when(scenarioService.getPubSubMessagingFlows(options, flow.getScenario()))
            .thenReturn(Collections.singletonList(flow));

      ClassDto dto = service.getServiceInterfaceDescription(options, model);
      assertNotNull("dto is null!",
                    dto);
      assertEquals("interface name not correct!",
                   "I" + model.getName(),
                   dto.getName());
      assertEquals("package name not correct!",
                   "com.ngc.seaside.threateval.engagementtrackpriorityservice.api",
                   dto.getPackageName());
      assertEquals("incorrect number of methods!",
                   1,
                   dto.getMethods().size());

      MethodDto method = dto.getMethods().get(0);
      assertEquals("method name not correct!",
                   "calculateTrackPriority",
                   method.getName());
      assertFalse("method should not override!",
                  method.isOverride());
      assertTrue("method should return!",
                 method.isReturns());
      assertEquals("return not correct!",
                   "TestDataOutput",
                   method.getReturnSnippet());
      assertEquals("argument list not correct!",
                   "",
                   method.getArgumentsListSnippet());

      assertTrue("missing import!",
                 dto.getImports().contains("com.ngc.seaside.threateval.engagementtrackpriorityservice.event.output"));
   }
}
