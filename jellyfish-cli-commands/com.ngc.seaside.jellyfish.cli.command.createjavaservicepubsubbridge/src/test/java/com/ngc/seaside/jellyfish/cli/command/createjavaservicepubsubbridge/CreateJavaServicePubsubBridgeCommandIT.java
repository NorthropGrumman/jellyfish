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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge;

import static com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge.CreateJavaServicePubsubBridgeCommand.OUTPUT_DIRECTORY_PROPERTY;
import static com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge.CreateJavaServicePubsubBridgeCommand.PUBSUB_BRIDGE_BUILD_TEMPLATE_SUFFIX;
import static com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge.CreateJavaServicePubsubBridgeCommand.PUBSUB_BRIDGE_GENERATED_BUILD_TEMPLATE_SUFFIX;
import static com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge.CreateJavaServicePubsubBridgeCommand.PUBSUB_BRIDGE_JAVA_TEMPLATE_SUFFIX;
import static com.ngc.seaside.jellyfish.cli.command.test.files.TestingFiles.assertFileContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.BaseServiceDtoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.IBaseServiceDtoFactory;
import com.ngc.seaside.jellyfish.cli.command.test.scenarios.FlowFactory;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedBuildManagementService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedPackageNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedProjectNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedTemplateService;
import com.ngc.seaside.jellyfish.service.codegen.api.IDataFieldGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.EnumDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.TypeDto;
import com.ngc.seaside.jellyfish.service.data.api.IDataService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IRequestResponseMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.utilities.command.JellyfishCommandPhase;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.test.systemdescriptor.ModelUtils;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CreateJavaServicePubsubBridgeCommandIT {

   private final CreateJavaServicePubsubBridgeCommand cmd = new CreateJavaServicePubsubBridgeCommand();

   private IJellyFishCommandOptions jellyFishCommandOptions = mock(IJellyFishCommandOptions.class);
   private DefaultParameterCollection parameters = new DefaultParameterCollection();

   private Path outputDirectory;

   private IBaseServiceDtoFactory templateDaoFactory;

   @Mock
   private IPackageNamingService packageService;

   @Mock
   private IProjectNamingService projectService;

   @Mock
   private IScenarioService scenarioService;

   @Mock
   private IJavaServiceGenerationService generatorService;

   @Mock
   private IDataService dataService;

   @Mock
   private IDataFieldGenerationService dataFieldGenerationService;

   @Mock
   private ILogService logService;

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

      templateDaoFactory = new BaseServiceDtoFactory(projectService,
                                                     packageService,
                                                     generatorService,
                                                     scenarioService,
                                                     dataService,
                                                     dataFieldGenerationService,
                                                     logService);

      cmd.setLogService(mock(ILogService.class));
      cmd.setPackageNamingService(new MockedPackageNamingService());
      cmd.setProjectNamingService(new MockedProjectNamingService());
      cmd.setBuildManagementService(new MockedBuildManagementService());
      cmd.setTemplateDaoFactory(templateDaoFactory);
      cmd.setTemplateService(templateService);
      cmd.setJavaServiceGenerationService(generatorService);

      outputDirectory = Files.createTempDirectory(null);
      parameters.addParameter(
            new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY, outputDirectory));
      parameters.addParameter(new DefaultParameter<>(CommonParameters.MODEL.getName(),
                                                     "com.ngc.seaside.threateval.EngagementTrackPriorityService"));
      // Turn off the file header with an empty file.
      parameters.addParameter(new DefaultParameter<>(
            CommonParameters.HEADER_FILE.getName(),
            Files.createTempFile(CreateJavaServicePubsubBridgeCommandIT.class.getSimpleName(), "tests")));

      ISystemDescriptor systemDescriptor = mock(ISystemDescriptor.class);
      when(systemDescriptor.findModel("com.ngc.seaside.threateval.EngagementTrackPriorityService")).thenReturn(
            Optional.of(model));

      when(jellyFishCommandOptions.getParameters()).thenReturn(parameters);
      when(jellyFishCommandOptions.getSystemDescriptor()).thenReturn(systemDescriptor);

      when(projectService.getPubSubBridgeProjectName(any(), any())).thenAnswer(args -> {
         IModel model = args.getArgument(1);
         IProjectInformation information = mock(IProjectInformation.class);
         String dirName = model.getFullyQualifiedName().toLowerCase() + ".pubsubbridge";
         when(information.getDirectoryName()).thenReturn(dirName);
         String artifactId = model.getName().toLowerCase() + ".pubsubbridge";
         when(information.getArtifactId()).thenReturn(artifactId);
         String groupId = model.getParent().getName();
         when(information.getGroupId()).thenReturn(groupId);
         return information;
      });

      when(packageService.getPubSubBridgePackageName(any(), any())).thenAnswer(args -> {
         IModel model = args.getArgument(1);
         return model.getFullyQualifiedName().toLowerCase() + ".bridge.pubsub";
      });

      setupMocksForBaseService();

      IScenario calculateTrackPriority0 = model.getScenarios()
            .getByName("calculateTrackPriority0")
            .get();
      IScenario getTrackPriority = model.getScenarios()
            .getByName("getTrackPriority")
            .get();
      IPublishSubscribeMessagingFlow pubSubFlow0 = FlowFactory.newPubSubFlowPath(calculateTrackPriority0);
      IRequestResponseMessagingFlow reqResFlow = FlowFactory.newRequestResponseServerFlow(getTrackPriority,
                                                                                          "trackPriorityRequest",
                                                                                          "trackPriorityResponse");
      when(scenarioService.getPubSubMessagingFlow(any(), eq(calculateTrackPriority0)))
            .thenReturn(Optional.of(pubSubFlow0));
      when(scenarioService.getRequestResponseMessagingFlow(any(), eq(calculateTrackPriority0)))
            .thenReturn(Optional.empty());

      when(scenarioService.getRequestResponseMessagingFlow(any(), eq(getTrackPriority)))
            .thenReturn(Optional.of(reqResFlow));
      when(scenarioService.getPubSubMessagingFlow(any(), eq(getTrackPriority)))
            .thenReturn(Optional.empty());
   }

   @Test
   public void testDoesRunDefaultPhase() throws Throwable {
      cmd.run(jellyFishCommandOptions);

      Path projectDirectory = outputDirectory
               .resolve("com.ngc.seaside.threateval.engagementtrackpriorityservice.pubsubbridge");
      Path gradleBuild = projectDirectory.resolve("build.gradle");
      assertTrue(Files.isRegularFile(gradleBuild));
   }

   @Test
   public void testDoesRunDeferredPhase() throws Throwable {
      parameters.addParameter(new DefaultParameter<>(CommonParameters.PHASE.getName(), JellyfishCommandPhase.DEFERRED));
      cmd.run(jellyFishCommandOptions);

      Path
            projectDirectory =
            outputDirectory.resolve("com.ngc.seaside.threateval.engagementtrackpriorityservice.pubsubbridge");
      assertTrue("Project directory incorrect", Files.isDirectory(projectDirectory));

      Path gradleBuild = projectDirectory.resolve("build.generated.gradle");
      assertTrue(Files.isRegularFile(gradleBuild));

      Path sourceDirectory = projectDirectory.resolve(Paths.get("src", "main", "java"));
      assertTrue(Files.isDirectory(sourceDirectory));

      List<Path> files = Files.walk(sourceDirectory)
            .filter(Files::isRegularFile)
            .sorted(Comparator.comparing(f -> f.getFileName().toString()))
            .collect(Collectors.toList());

      assertEquals(1, files.size());

      Path trackEngStatusSubPath = Paths.get(sourceDirectory.toAbsolutePath().toString(),
                                             "com/ngc/seaside/threateval/engagementtrackpriorityservice/bridge/pubsub",
                                             "/TrackEngagementStatus0Subscriber.java");

      assertTrue(Files.isRegularFile(trackEngStatusSubPath));

      //Check imports
      assertFileContains(trackEngStatusSubPath,
                         "\\bimport\\s+com.ngc.seaside.threateval.engagementtrackpriorityservice"
                               + ".api.IEngagementTrackPriorityService;");
      assertFileContains(trackEngStatusSubPath,
                         "\\bimport\\s+com.ngc.seaside.threateval.engagementtrackpriorityservice"
                               + ".events.TrackEngagementStatus0;");
      assertFileContains(trackEngStatusSubPath,
                         "\\bimport\\s+com.ngc.seaside.threateval.engagementtrackpriorityservice"
                               + ".events.TrackPriority0;");

      //Class declaration
      assertFileContains(trackEngStatusSubPath, "\\bpublic\\s+class\\s+TrackEngagementStatus0Subscriber\\b");
      assertFileContains(trackEngStatusSubPath, "\\bimplements\\s+IEventSubscriber<TrackEngagementStatus0>");

      //Event handling
      assertFileContains(trackEngStatusSubPath,
                         "\\bprivate\\s+IEngagementTrackPriorityService\\s+engagementTrackPriorityService\\b");
      assertFileContains(trackEngStatusSubPath, "\\bengagementTrackPriorityService.calculateTrackPriority0\\b");
      assertFileContains(trackEngStatusSubPath, "\\bTrackPriority0.TOPIC\\b");

      //Check OSGi setters/getters
      assertFileContains(trackEngStatusSubPath, "\\bpublic\\s+void\\s+setEngagementTrackPriorityService\\b");
      assertFileContains(trackEngStatusSubPath, "\\bpublic\\s+void\\s+removeEngagementTrackPriorityService\\b");
   }

   @After
   public void cleanup() {
      cmd.deactivate();
   }

   /**
    * @return Model used for testing
    */
   private static IModel newModelForTesting() {
      ModelUtils.PubSubModel model =
            new ModelUtils.PubSubModel("com.ngc.seaside.threateval.EngagementTrackPriorityService");

      IData trackEngagementStatus0 =
            ModelUtils.getMockNamedChild(IData.class, "com.ngc.seaside.threateval.TrackEngagementStatus0");
      IData trackPriority0 =
            ModelUtils.getMockNamedChild(IData.class, "com.ngc.seaside.threateval.TrackPriority0");
      model.addPubSub("calculateTrackPriority0",
                      "trackEngagementStatus0", trackEngagementStatus0,
                      "trackPriority0", trackPriority0);

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

   //Added for BaseServiceDto needs
   private void setupMocksForBaseService() {
      when(projectService.getBaseServiceProjectName(any(), any())).thenAnswer(args -> {
         IModel model = args.getArgument(1);
         IProjectInformation information = mock(IProjectInformation.class);
         String dirName = model.getFullyQualifiedName().toLowerCase() + ".base";
         when(information.getDirectoryName()).thenReturn(dirName);
         String artifactId = model.getName().toLowerCase() + ".base";
         when(information.getArtifactId()).thenReturn(artifactId);
         String groupId = model.getParent().getName();
         when(information.getGroupId()).thenReturn(groupId);
         return information;
      });
      when(projectService.getEventsProjectName(any(), any())).thenAnswer(args -> {
         IModel model = args.getArgument(1);
         IProjectInformation information = mock(IProjectInformation.class);
         when(information.getArtifactId()).thenReturn(model.getName().toLowerCase() + ".events");
         return information;
      });
      when(packageService.getServiceBaseImplementationPackageName(any(), any())).thenAnswer(args -> {
         IModel model = args.getArgument(1);
         return model.getFullyQualifiedName().toLowerCase() + ".base.impl";
      });
      when(packageService.getServiceInterfacePackageName(any(), any())).thenAnswer(args -> {
         IModel model = args.getArgument(1);
         return model.getFullyQualifiedName().toLowerCase() + ".api";
      });
      when(generatorService.getServiceInterfaceDescription(any(), any())).thenAnswer(args -> {
         IJellyFishCommandOptions options = args.getArgument(0);
         IModel model = args.getArgument(1);
         ClassDto interfaceDto = new ClassDto();
         interfaceDto.setName("I" + model.getName())
               .setPackageName(packageService.getServiceInterfacePackageName(options, model))
               .setImports(new LinkedHashSet<>(Arrays.asList(
                     "com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackEngagementStatus",
                     "com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackPriority")));
         return interfaceDto;
      });

      ClassDto abstractClassDto = new ClassDto();
      abstractClassDto.setName("Abstract" + model.getName())
            .setPackageName(packageService.getServiceBaseImplementationPackageName(jellyFishCommandOptions, model))
            .setImports(new HashSet<>(Arrays.asList("com.ngc.blocs.service.event.api.IEvent",
                                                    "com.ngc.seaside.threateval.engagementtrackpriorityservice.api"
                                                          + ".IEngagementTrackPriorityService",
                                                    "com.ngc.seaside.threateval.engagementtrackpriorityservice.events"
                                                          + ".TrackEngagementStatus",
                                                    "com.ngc.seaside.threateval.engagementtrackpriorityservice.events"
                                                          + ".TrackPriority")));

      when(generatorService.getBaseServiceDescription(any(), eq(model))).thenReturn(abstractClassDto);
      when(dataService.getEventClass(any(), any())).thenAnswer(args -> {
         INamedChild<IPackage> child = args.getArgument(1);
         TypeDto<?> typeDto = new ClassDto();
         typeDto.setPackageName(child.getParent().getName() + ".engagementtrackpriorityservice.events");
         typeDto.setTypeName(child.getName());
         return typeDto;
      });

      when(generatorService.getTransportTopicsDescription(any(), eq(model))).thenAnswer(args -> {
         EnumDto dto = new EnumDto();
         dto.setName("EngagementTrackPriorityServiceTransportTopics");
         dto.setPackageName("com.ngc.seaside.threateval.engagementtrackpriorityservice.transport.topic");
         dto.setImports(
               new LinkedHashSet<>(Collections.singleton("com.ngc.seaside.service.transport.api.ITransportTopic")));
         dto.setValues(new LinkedHashSet<>(Arrays.asList("TRACK_ENGAGEMENT_STATUS",
                                                         "TRACK_PRIORITY",
                                                         "GET_TRACK_PRIORITY")));
         return dto;
      });
   }
}
