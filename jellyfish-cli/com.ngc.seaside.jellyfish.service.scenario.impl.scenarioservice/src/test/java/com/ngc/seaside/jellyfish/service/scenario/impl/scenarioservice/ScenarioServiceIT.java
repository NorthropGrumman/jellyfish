package com.ngc.seaside.jellyfish.service.scenario.impl.scenarioservice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IRequestResponseMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.MessagingParadigm;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.scenario.impl.module.StepsSystemDescriptorServiceModule;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.CorrelateStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.PublishStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.ReceiveStepHandler;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;
import com.ngc.seaside.systemdescriptor.service.repository.api.IRepositoryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ScenarioServiceIT {

   private ScenarioService service;

   private ISystemDescriptor systemDescriptor;

   @Mock
   private IJellyFishCommandOptions options;

   @Mock
   private ILogService logService;

   private final Module testModule = new AbstractModule() {
      @Override
      protected void configure() {
         bind(ILogService.class).toInstance(logService);
         bind(IRepositoryService.class).toInstance(mock(IRepositoryService.class));
      }
   };

   @Before
   public void setup() throws Throwable {
      systemDescriptor = getSystemDescriptor();

      service = new ScenarioService();
      service.setLogService(logService);
      service.setPublishStepHandler(new PublishStepHandler());
      service.setReceiveStepHandler(new ReceiveStepHandler());
      service.setCorrelationStepHandler(new CorrelateStepHandler());
      service.activate();
   }

   @Test
   public void testDoesDeterminePubSubMessageParadigms() {
      IScenario scenario = systemDescriptor.findModel("com.ngc.seaside.threateval.EngagementTrackPriorityService")
            .get()
            .getScenarios()
            .getByName("calculateTrackPriority")
            .get();

      Collection<MessagingParadigm> paradigms = service.getMessagingParadigms(options, scenario);
      assertTrue("does not contain correct paradigms!",
                 paradigms.contains(MessagingParadigm.PUBLISH_SUBSCRIBE));
      assertEquals("contains extra paradigms!",
                   1,
                   paradigms.size());
   }

   @Test
   public void testDoesDetermineRequestResponseMessageParadigms() {
      IScenario scenario = systemDescriptor.findModel("com.ngc.seaside.threateval.TrackPriorityService")
            .get()
            .getScenarios()
            .getByName("getPriority")
            .get();

      Collection<MessagingParadigm> paradigms = service.getMessagingParadigms(options, scenario);
      assertTrue("does not contain correct paradigms!",
                 paradigms.contains(MessagingParadigm.REQUEST_RESPONSE));
      assertEquals("contains extra paradigms!",
                   1,
                   paradigms.size());
   }

   @Test
   public void testDoesGetSimplePubSubFlow() {
      IModel model = systemDescriptor.findModel("com.ngc.seaside.threateval.EngagementTrackPriorityService").get();
      IScenario scenario = model.getScenarios().getByName("calculateTrackPriority").get();

      Optional<IPublishSubscribeMessagingFlow> optionalFlow = service.getPubSubMessagingFlow(options, scenario);
      assertTrue("contains an incorrect number of flows!", optionalFlow.isPresent());

      IPublishSubscribeMessagingFlow flow = optionalFlow.get();
      assertEquals("flow scenario not correct!",
                   scenario,
                   flow.getScenario());
      assertEquals("flow type not not correct!",
                   IPublishSubscribeMessagingFlow.FlowType.PATH,
                   flow.getFlowType());
      assertEquals("paradigm not correct!",
                   MessagingParadigm.PUBLISH_SUBSCRIBE,
                   flow.getMessagingParadigm());
      assertEquals("wrong number of inputs!",
                   1,
                   flow.getInputs().size());
      assertEquals("wrong number of outputs!",
                   1,
                   flow.getOutputs().size());
      assertEquals("input not correct!",
                   model.getInputs().getByName("trackEngagementStatus").get(),
                   flow.getInputs().iterator().next());
      assertEquals("output not correct!",
                   model.getOutputs().getByName("trackPriority").get(),
                   flow.getOutputs().iterator().next());
   }

   @Test
   public void testDoesGetServerSideRequestResponseFlow() {
      IModel model = systemDescriptor.findModel("com.ngc.seaside.threateval.TrackPriorityService").get();
      IScenario scenario = model.getScenarios().getByName("getPriority").get();

      Optional<IRequestResponseMessagingFlow> optionalFlow = service.getRequestResponseMessagingFlows(options,
                                                                                                      scenario);
      assertTrue("contains an incorrect number of flows!", optionalFlow.isPresent());

      IRequestResponseMessagingFlow flow = optionalFlow.get();
      assertEquals("flow scenario not correct!",
                   scenario,
                   flow.getScenario());
      assertEquals("flow type not not correct!",
                   IRequestResponseMessagingFlow.FlowType.SERVER,
                   flow.getFlowType());
      assertEquals("paradigm not correct!",
                   MessagingParadigm.REQUEST_RESPONSE,
                   flow.getMessagingParadigm());
      assertEquals("input not correct!",
                   model.getInputs().getByName("droppedSystemTrack").get(),
                   flow.getInput());
      assertEquals("output not correct!",
                   model.getOutputs().getByName("prioritizedSystemTracks").get(),
                   flow.getOutput());
   }

   private ISystemDescriptor getSystemDescriptor() {
      Collection<Module> modules = new ArrayList<>();
      modules.add(testModule);
      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
      modules.add(new StepsSystemDescriptorServiceModule());

      return Guice.createInjector(modules)
            .getInstance(ISystemDescriptorService.class)
            .parseProject(Paths.get("src/test/resources/"))
            .getSystemDescriptor();
   }
}
