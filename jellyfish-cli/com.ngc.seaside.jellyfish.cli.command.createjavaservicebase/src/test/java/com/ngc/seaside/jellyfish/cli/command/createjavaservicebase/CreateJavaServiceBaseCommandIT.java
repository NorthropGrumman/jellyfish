package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.BaseServiceDtoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.IBaseServiceDtoFactory;
import com.ngc.seaside.jellyfish.cli.command.test.scenarios.FlowFactory;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedBuildManagementService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedTemplateService;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
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
import com.ngc.seaside.jellyfish.utilities.command.JellyfishCommandPhase;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.test.systemdescriptor.ModelUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;

import static com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.CreateJavaServiceBaseCommand.SERVICE_BASE_BUILD_TEMPLATE_SUFFIX;
import static com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.CreateJavaServiceBaseCommand.SERVICE_BASE_GENERATED_BUILD_TEMPLATE_SUFFIX;
import static com.ngc.seaside.jellyfish.cli.command.test.files.TestingFiles.assertFileContains;
import static com.ngc.seaside.jellyfish.cli.command.test.files.TestingFiles.assertFileLinesEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CreateJavaServiceBaseCommandIT {

   private CreateJavaServiceBaseCommand command;

   private MockedTemplateService templateService;

   private IBaseServiceDtoFactory templateDaoFactory;

   private DefaultParameterCollection parameters;

   private File outputDirectory;

   @Rule
   public final TemporaryFolder tempFolder = new TemporaryFolder();

   @Mock
   private IJellyFishCommandOptions jellyFishCommandOptions;

   @Mock
   private ILogService logService;

   @Mock
   private IProjectNamingService projectService;

   @Mock
   private IPackageNamingService packageService;

   @Mock
   private IJavaServiceGenerationService generatorService;

   @Mock
   private IScenarioService scenarioService;

   @Mock
   private IDataService dataService;

   @Mock
   private IDataFieldGenerationService dataFieldGenerationService;

   private IBuildManagementService buildManagementService;

   private IModel model = newModelForTesting();

   @Before
   public void setup() throws Throwable {
      tempFolder.newFile("settings.gradle");
      outputDirectory = tempFolder.getRoot();

      buildManagementService = new MockedBuildManagementService();

      templateService = new MockedTemplateService()
            .useRealPropertyService()
            .setTemplateDirectory(
                  CreateJavaServiceBaseCommand.class.getPackage().getName() + "-"
                  + SERVICE_BASE_GENERATED_BUILD_TEMPLATE_SUFFIX,
                  Paths.get("src", "main", "templates", "genbuild"))
            .setTemplateDirectory(
                  CreateJavaServiceBaseCommand.class.getPackage().getName() + "-"
                  + SERVICE_BASE_BUILD_TEMPLATE_SUFFIX,
                  Paths.get("src", "main", "templates", "build"));

      templateDaoFactory = new BaseServiceDtoFactory(projectService,
                                                     packageService,
                                                     generatorService,
                                                     scenarioService,
                                                     dataService,
                                                     dataFieldGenerationService,
                                                     logService);

      ISystemDescriptor systemDescriptor = mock(ISystemDescriptor.class);
      when(systemDescriptor.findModel("com.ngc.seaside.threateval.EngagementTrackPriorityService")).thenReturn(
            Optional.of(model));

      parameters = new DefaultParameterCollection();
      when(jellyFishCommandOptions.getParameters()).thenReturn(parameters);
      when(jellyFishCommandOptions.getSystemDescriptor()).thenReturn(systemDescriptor);

      command = new CreateJavaServiceBaseCommand();
      command.setLogService(logService);
      command.setTemplateDaoFactory(templateDaoFactory);
      command.setTemplateService(templateService);
      command.setProjectNamingService(projectService);
      command.setBuildManagementService(buildManagementService);

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
                                                    "com.ngc.seaside.threateval.engagementtrackpriorityservice.api.IEngagementTrackPriorityService",
                                                    "com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackEngagementStatus",
                                                    "com.ngc.seaside.threateval.engagementtrackpriorityservice.events.TrackPriority")));

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

      IScenario calculateTrackPriority = model.getScenarios()
            .getByName("calculateTrackPriority")
            .get();
      IScenario getTrackPriority = model.getScenarios()
            .getByName("getTrackPriority")
            .get();
      IPublishSubscribeMessagingFlow pubSubFlow = FlowFactory.newPubSubFlowPath(calculateTrackPriority);
      IRequestResponseMessagingFlow reqResFlow = FlowFactory.newRequestResponseServerFlow(getTrackPriority,
                                                                                          "trackPriorityRequest",
                                                                                          "trackPriorityResponse");
      when(scenarioService.getPubSubMessagingFlow(any(), eq(calculateTrackPriority)))
            .thenReturn(Optional.of(pubSubFlow));
      when(scenarioService.getRequestResponseMessagingFlow(any(), eq(calculateTrackPriority)))
            .thenReturn(Optional.empty());
      when(scenarioService.getRequestResponseMessagingFlow(any(), eq(getTrackPriority)))
            .thenReturn(Optional.of(reqResFlow));
      when(scenarioService.getPubSubMessagingFlow(any(), eq(getTrackPriority)))
            .thenReturn(Optional.empty());

      parameters.addParameter(new DefaultParameter<>(CommonParameters.MODEL.getName(),
                                                     "com.ngc.seaside.threateval.EngagementTrackPriorityService"));
      parameters.addParameter(new DefaultParameter<>(CommonParameters.OUTPUT_DIRECTORY.getName(),
                                                     outputDirectory.getAbsolutePath()));
   }

   @Test
   public void testDoesRunDeferredPhase() throws Throwable {
      parameters.addParameter(new DefaultParameter<>(CommonParameters.PHASE.getName(), JellyfishCommandPhase.DEFERRED));
      command.run(jellyFishCommandOptions);

      Path gradleBuildPath = Paths.get(outputDirectory.getAbsolutePath(),
                                       "com.ngc.seaside.threateval.engagementtrackpriorityservice.base",
                                       "build.generated.gradle");

      assertFileContains(gradleBuildPath, "project\\s*\\(\\s*['\"]:engagementtrackpriorityservice.events['\"]\\s*\\)");

      Path abstractPath = Paths.get(outputDirectory.getAbsolutePath(),
                                    "com.ngc.seaside.threateval.engagementtrackpriorityservice.base",
                                    "src/main/java/com/ngc/seaside/threateval/engagementtrackpriorityservice/base/impl/AbstractEngagementTrackPriorityService.java");

      assertFileContains(abstractPath, "\\babstract\\s+class\\s+AbstractEngagementTrackPriorityService\\b");
      assertFileContains(abstractPath, "\\bimplements\\s+.*?\\bIEngagementTrackPriorityService\\b");
      assertFileContains(abstractPath, "\"service:com.ngc.seaside.threateval.EngagementTrackPriorityService\"");
      assertFileContains(abstractPath,
                         "\\bvoid\\s+receiveTrackEngagementStatus\\s*?\\(\\s*?\\S*?IEvent\\s*<\\s*?\\S*?TrackEngagementStatus\\s*?>\\s*?event\\s*?\\)");
      assertFileContains(abstractPath, "\\bpublishTrackPriority\\s*\\(");

      Path interfacePath = Paths.get(outputDirectory.getAbsolutePath(),
                                     "com.ngc.seaside.threateval.engagementtrackpriorityservice.base",
                                     "src/main/java/com/ngc/seaside/threateval/engagementtrackpriorityservice/api/IEngagementTrackPriorityService.java");

      assertFileContains(interfacePath, "\\binterface\\s+IEngagementTrackPriorityService\\b");
      assertFileContains(interfacePath,
                         "\\bTrackPriority\\s+calculateTrackPriority\\s*\\(\\s*?\\S*?\\bTrackEngagementStatus\\s+trackEngagementStatus\\s*\\)");

      Path topicsPath = Paths.get(outputDirectory.getAbsolutePath(),
                                  "com.ngc.seaside.threateval.engagementtrackpriorityservice.base",
                                  "src/main/java/com/ngc/seaside/threateval/engagementtrackpriorityservice/transport/topic/EngagementTrackPriorityServiceTransportTopics.java");

      assertFileContains(topicsPath, "\\benum\\s+EngagementTrackPriorityServiceTransportTopics");
      assertFileContains(topicsPath, "\\bimplements\\s+\\S*\\bITransportTopic\\b");
      assertFileContains(topicsPath, "TRACK_ENGAGEMENT_STATUS");
      assertFileContains(topicsPath, "TRACK_PRIORITY");
   }

   @Test
   public void testDoesRunDefaultPhase() throws Throwable {
      command.run(jellyFishCommandOptions);

      Path projectDirectory = outputDirectory
            .toPath()
            .resolve("com.ngc.seaside.threateval.engagementtrackpriorityservice.base");
      assertFileLinesEquals(
            "build.gradle not correct!",
            Paths.get("src", "test", "resources", "build.gradle.expected"),
            projectDirectory.resolve("build.gradle"));
   }

   public static IModel newModelForTesting() {
      ModelUtils.PubSubModel model =
            new ModelUtils.PubSubModel("com.ngc.seaside.threateval.EngagementTrackPriorityService");

      IData trackEngagementStatus =
            ModelUtils.getMockNamedChild(IData.class, "com.ngc.seaside.threateval.TrackEngagementStatus");
      IData trackPriority =
            ModelUtils.getMockNamedChild(IData.class, "com.ngc.seaside.threateval.TrackPriority");
      model.addPubSub("calculateTrackPriority",
                      "trackEngagementStatus", trackEngagementStatus,
                      "trackPriority", trackPriority);

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
