package com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.scenarios.FlowFactory;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedBuildManagementService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedDataFieldGenerationService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedDataService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedPackageNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedProjectNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedTemplateService;
import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IRequestResponseMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.test.systemdescriptor.ModelUtils;

import static com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge.CreateJavaServicePubsubBridgeCommand.OUTPUT_DIRECTORY_PROPERTY;
import static com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge.CreateJavaServicePubsubBridgeCommand.PUBSUB_BRIDGE_BUILD_TEMPLATE_SUFFIX;
import static com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge.CreateJavaServicePubsubBridgeCommand.PUBSUB_BRIDGE_GENERATED_BUILD_TEMPLATE_SUFFIX;
import static com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge.CreateJavaServicePubsubBridgeCommand.PUBSUB_BRIDGE_JAVA_TEMPLATE_SUFFIX;
@RunWith(MockitoJUnitRunner.class)
public class CreateJavaServicePubsubBridgeCommandIT {

   private final CreateJavaServicePubsubBridgeCommand cmd = new CreateJavaServicePubsubBridgeCommand();

   private IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class);
   private DefaultParameterCollection parameters = new DefaultParameterCollection();
   
   private Path outputDirectory;
   
   @Mock
   private IScenarioService scenarioService;
   
   private IModel model = newModelForTesting();

   @Before
   public void setup() throws IOException {
      ITemplateService templateService = new MockedTemplateService()
               .useRealPropertyService()
               .setTemplateDirectory(
                     CreateJavaServicePubsubBridgeCommand.class.getPackage().getName() + "-"
                     + PUBSUB_BRIDGE_GENERATED_BUILD_TEMPLATE_SUFFIX,
                     Paths.get("src", "main", "templates", "genbuild"))
               .setTemplateDirectory(
                  CreateJavaServicePubsubBridgeCommand.class.getPackage().getName() + "-"
                     + PUBSUB_BRIDGE_BUILD_TEMPLATE_SUFFIX,
                     Paths.get("src", "main", "templates", "build"))
               .setTemplateDirectory(
                  CreateJavaServicePubsubBridgeCommand.class.getPackage().getName() + "-"
                     + PUBSUB_BRIDGE_JAVA_TEMPLATE_SUFFIX,
                     Paths.get("src", "main", "templates", "java"));

         cmd.setLogService(mock(ILogService.class));
         cmd.setPackageNamingService(new MockedPackageNamingService());
         cmd.setProjectNamingService(new MockedProjectNamingService());
         cmd.setBuildManagementService(new MockedBuildManagementService());
         cmd.setTemplateService(templateService);
         
         outputDirectory = Files.createTempDirectory(null);
         parameters.addParameter(
               new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, outputDirectory));
         
         
         
         
         
         IScenario calculateTrackPriority0 = model.getScenarios()
                  .getByName("calculateTrackPriority0")
                  .get();
         IScenario calculateTrackPriority1 = model.getScenarios()
                  .getByName("calculateTrackPriority1")
                  .get();
         IScenario getTrackPriority = model.getScenarios()
                  .getByName("getTrackPriority")
                  .get();
         IPublishSubscribeMessagingFlow pubSubFlow0 = FlowFactory.newPubSubFlowPath(calculateTrackPriority0);
         IPublishSubscribeMessagingFlow pubSubFlow1 = FlowFactory.newPubSubFlowPath(calculateTrackPriority1);
         IRequestResponseMessagingFlow reqResFlow = FlowFactory.newRequestResponseServerFlow(getTrackPriority,
                                                                                             "trackPriorityRequest",
                                                                                             "trackPriorityResponse");
         when(scenarioService.getPubSubMessagingFlow(any(), eq(calculateTrackPriority0)))
               .thenReturn(Optional.of(pubSubFlow0));
         when(scenarioService.getPubSubMessagingFlow(any(), eq(calculateTrackPriority1)))
         .thenReturn(Optional.of(pubSubFlow1));
         when(scenarioService.getRequestResponseMessagingFlow(any(), eq(calculateTrackPriority0)))
               .thenReturn(Optional.empty());
         when(scenarioService.getRequestResponseMessagingFlow(any(), eq(calculateTrackPriority1)))
         .thenReturn(Optional.empty());
         when(scenarioService.getRequestResponseMessagingFlow(any(), eq(getTrackPriority)))
               .thenReturn(Optional.of(reqResFlow));
         when(scenarioService.getPubSubMessagingFlow(any(), eq(getTrackPriority)))
               .thenReturn(Optional.empty());

         parameters.addParameter(new DefaultParameter<>(CommonParameters.MODEL.getName(),
                                                        "com.ngc.seaside.threateval.EngagementTrackPriorityService"));
         parameters.addParameter(new DefaultParameter<>(CommonParameters.OUTPUT_DIRECTORY.getName(),
                                                        outputDirectory));

   }

   @Test
   public void testCommand() {
      // TODO Auto-generated method stub
   }

   @After
   public void cleanup() {
      cmd.deactivate();
   }
   
   /**
   *
   * @return Model used for testing
   */
  public static IModel newModelForTesting() {
     ModelUtils.PubSubModel model =
           new ModelUtils.PubSubModel("com.ngc.seaside.threateval.EngagementTrackPriorityService");

     IData trackEngagementStatus0 =
           ModelUtils.getMockNamedChild(IData.class, "com.ngc.seaside.threateval.TrackEngagementStatus0");
     IData trackPriority0 =
           ModelUtils.getMockNamedChild(IData.class, "com.ngc.seaside.threateval.TrackPriority0");
     model.addPubSub("calculateTrackPriority0",
                     "trackEngagementStatus0", trackEngagementStatus0,
                     "trackPriority0", trackPriority0);
     
     IData trackEngagementStatus1 =
              ModelUtils.getMockNamedChild(IData.class, "com.ngc.seaside.threateval.TrackEngagementStatus1");
     IData trackPriority1 =
              ModelUtils.getMockNamedChild(IData.class, "com.ngc.seaside.threateval.TrackPriority1");
     model.addPubSub("calculateTrackPriority1",
                        "trackEngagementStatus1", trackEngagementStatus1,
                        "trackPriority1", trackPriority1);

     IData trackPriorityRequest =
           ModelUtils.getMockNamedChild(IData.class, "com.ngc.seaside.threateval.TrackPriorityRequest");
     IData trackPriorityResponse =
           ModelUtils.getMockNamedChild(IData.class, "com.ngc.seaside.threateval.TrackPriorityResponse");
     ModelUtils.addReqRes(model,
                          "getTrackPriority",
                          "trackPriorityRequest", trackPriorityRequest,
                          "trackPriorityResponse", trackPriorityResponse);

     return model;
  }
}
