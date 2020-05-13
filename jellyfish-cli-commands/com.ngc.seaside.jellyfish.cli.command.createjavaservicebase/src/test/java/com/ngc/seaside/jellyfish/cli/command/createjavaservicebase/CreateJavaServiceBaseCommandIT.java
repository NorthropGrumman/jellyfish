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
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedDataFieldGenerationService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedDataService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedPackageNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedProjectNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedTemplateService;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IRequestResponseMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.user.api.IJellyfishUserService;
import com.ngc.seaside.jellyfish.utilities.command.JellyfishCommandPhase;
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
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.CreateJavaServiceBaseCommand.SERVICE_BASE_BUILD_TEMPLATE_SUFFIX;
import static com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.CreateJavaServiceBaseCommand.SERVICE_BASE_GENERATED_BUILD_TEMPLATE_SUFFIX;
import static com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.CreateJavaServiceBaseCommand.SERVICE_BASE_TEMPLATE_SUFFIX;
import static com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.CreateJavaServiceBaseCommand.TOPICS_TEMPLATE_SUFFIX;
import static org.junit.Assert.assertTrue;
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

   private final MockedProjectNamingService projectService = new MockedProjectNamingService();

   private final MockedPackageNamingService packageService = new MockedPackageNamingService();

   private final MockedJavaServiceGenerationService generatorService =
         new MockedJavaServiceGenerationService(packageService);

   @Mock
   private IScenarioService scenarioService;

   private final MockedDataService dataService = new MockedDataService();

   private final MockedDataFieldGenerationService dataFieldGenerationService = new MockedDataFieldGenerationService();

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
                  Paths.get("src", "main", "templates", SERVICE_BASE_GENERATED_BUILD_TEMPLATE_SUFFIX))
            .setTemplateDirectory(
                  CreateJavaServiceBaseCommand.class.getPackage().getName() + "-"
                  + SERVICE_BASE_BUILD_TEMPLATE_SUFFIX,
                  Paths.get("src", "main", "templates", SERVICE_BASE_BUILD_TEMPLATE_SUFFIX))
            .setTemplateDirectory(
               CreateJavaServiceBaseCommand.class.getPackage().getName() + "-"
               + SERVICE_BASE_TEMPLATE_SUFFIX,
               Paths.get("src", "main", "templates", SERVICE_BASE_TEMPLATE_SUFFIX))
            .setTemplateDirectory(
                     CreateJavaServiceBaseCommand.class.getPackage().getName() + "-"
                     + TOPICS_TEMPLATE_SUFFIX,
                     Paths.get("src", "main", "templates", TOPICS_TEMPLATE_SUFFIX));

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
      command.setJellyfishUserService(mock(IJellyfishUserService.class, Mockito.RETURNS_DEEP_STUBS));

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
      // Turn off the file header with an empty file.
      parameters.addParameter(new DefaultParameter<>(
            CommonParameters.HEADER_FILE.getName(),
            Files.createTempFile(CreateJavaServiceBaseCommandIT.class.getSimpleName(), "tests")));
   }

   @Test
   public void testDoesRunDeferredPhase() {
      parameters.addParameter(new DefaultParameter<>(CommonParameters.PHASE.getName(), JellyfishCommandPhase.DEFERRED));
      command.run(jellyFishCommandOptions);
      Path baseDir = outputDirectory.toPath().resolve("com.ngc.seaside.threateval.engagementtrackpriorityservice.base");
      assertTrue(Files.isDirectory(baseDir));
      assertTrue(Files.isRegularFile(baseDir.resolve("build.generated.gradle")));
      Path srcDir = baseDir.resolve(Paths.get("src", "main", "java", "com", "ngc", "seaside", "threateval",
            "engagementtrackpriorityservice"));
      Path ifc = srcDir.resolve("api").resolve("IEngagementTrackPriorityService.java");
      assertTrue(Files.isRegularFile(ifc));
      Path base = srcDir.resolve("base").resolve("AbstractEngagementTrackPriorityService.java");
      assertTrue(Files.isRegularFile(base));
      Path topics = srcDir.resolve("topics").resolve("EngagementTrackPriorityServiceTransportTopics.java");
      assertTrue(Files.isRegularFile(topics));
   }

   @Test
   public void testDoesRunDeferredPhaseWithCorrelation() throws Throwable {
      IScenario calculateTrackPriority = model.getScenarios()
            .getByName("calculateTrackPriority")
            .get();
      IPublishSubscribeMessagingFlow pubSubFlow = FlowFactory.newCorrelatingPubSubFlowPath(calculateTrackPriority);
      when(scenarioService.getPubSubMessagingFlow(any(), eq(calculateTrackPriority)))
            .thenReturn(Optional.of(pubSubFlow));

      parameters.addParameter(new DefaultParameter<>(CommonParameters.PHASE.getName(), JellyfishCommandPhase.DEFERRED));
      command.run(jellyFishCommandOptions);

      Path baseDir = outputDirectory.toPath().resolve("com.ngc.seaside.threateval.engagementtrackpriorityservice.base");
      assertTrue(Files.isDirectory(baseDir));
      assertTrue(Files.isRegularFile(baseDir.resolve("build.generated.gradle")));
      Path srcDir = baseDir.resolve(Paths.get("src", "main", "java", "com", "ngc", "seaside", "threateval", 
            "engagementtrackpriorityservice"));
      Path ifc = srcDir.resolve("api").resolve("IEngagementTrackPriorityService.java");
      assertTrue(Files.isRegularFile(ifc));
      Path base = srcDir.resolve("base").resolve("AbstractEngagementTrackPriorityService.java");
      assertTrue(Files.isRegularFile(base));
      Path topics = srcDir.resolve("topics").resolve("EngagementTrackPriorityServiceTransportTopics.java");
      assertTrue(Files.isRegularFile(topics));
   }

   @Test
   public void testDoesRunDefaultPhase() throws Throwable {
      command.run(jellyFishCommandOptions);

      Path projectDirectory = outputDirectory
               .toPath()
               .resolve("com.ngc.seaside.threateval.engagementtrackpriorityservice.base");
      Path gradleBuild = projectDirectory.resolve("build.gradle");
      assertTrue(Files.isRegularFile(gradleBuild));
   }

   /**
    *
    * @return Model used for testing
    */
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
