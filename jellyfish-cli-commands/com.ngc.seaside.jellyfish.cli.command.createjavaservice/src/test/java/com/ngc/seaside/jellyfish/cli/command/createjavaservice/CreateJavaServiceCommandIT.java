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
package com.ngc.seaside.jellyfish.cli.command.createjavaservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.IServiceDtoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.ServiceDtoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.BaseServiceDtoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.IBaseServiceDtoFactory;
import com.ngc.seaside.jellyfish.cli.command.test.scenarios.FlowFactory;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedBuildManagementService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedTemplateService;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.codegen.api.IDataFieldGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.TypeDto;
import com.ngc.seaside.jellyfish.service.codegen.api.java.IGeneratedJavaField;
import com.ngc.seaside.jellyfish.service.data.api.IDataService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.jellyfish.service.scenario.api.IPublishSubscribeMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IRequestResponseMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.user.api.IJellyfishUserService;
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
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;

import static com.ngc.seaside.jellyfish.cli.command.test.files.TestingFiles.assertFileContains;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CreateJavaServiceCommandIT {

   private CreateJavaServiceCommand command;

   private MockedTemplateService templateService;

   private IServiceDtoFactory serviceTemplateDaoFactory;

   private IBaseServiceDtoFactory baseServiceTemplateDaoFactory;

   private DefaultParameterCollection parameters;

   private File directory;

   @Rule
   public final TemporaryFolder outputDirectory = new TemporaryFolder();

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

   private IModel testModel;

   @Before
   public void setup() throws Throwable {
      directory = outputDirectory.getRoot();
      Files.createFile(directory.toPath().resolve("settings.gradle"));

      buildManagementService = new MockedBuildManagementService();

      templateService = new MockedTemplateService()
            .useRealPropertyService()
            .setTemplateDirectory(
                  CreateJavaServiceCommand.class.getPackage().getName(),
                  Paths.get("src", "main", "template"));

      serviceTemplateDaoFactory = new ServiceDtoFactory(projectService, packageService, buildManagementService);

      baseServiceTemplateDaoFactory = new BaseServiceDtoFactory(projectService,
                                                                packageService,
                                                                generatorService,
                                                                scenarioService,
                                                                dataService,
                                                                dataFieldGenerationService,
                                                                logService);

      ISystemDescriptor systemDescriptor = mock(ISystemDescriptor.class);
      testModel = newModelForTesting();

      when(systemDescriptor.findModel("com.ngc.seaside.threateval.EngagementTrackPriorityService")).thenReturn(
            Optional.of(testModel));

      parameters = new DefaultParameterCollection();
      when(jellyFishCommandOptions.getParameters()).thenReturn(parameters);
      when(jellyFishCommandOptions.getSystemDescriptor()).thenReturn(systemDescriptor);

      command = new CreateJavaServiceCommand();
      command.setLogService(logService);
      command.setServiceTemplateDaoFactory(serviceTemplateDaoFactory);
      command.setBaseServiceTemplateDaoFactory(baseServiceTemplateDaoFactory);
      command.setTemplateService(templateService);
      command.setProjectNamingService(projectService);
      command.setBuildManagementService(buildManagementService);
      command.setJellyfishUserService(mock(IJellyfishUserService.class, Mockito.RETURNS_DEEP_STUBS));

      when(projectService.getServiceProjectName(any(), any())).thenAnswer(args -> {
         IModel model = args.getArgument(1);
         IProjectInformation information = mock(IProjectInformation.class);
         String qualName = model.getFullyQualifiedName().toLowerCase();
         when(information.getDirectoryName()).thenReturn(qualName);
         return information;
      });
      when(projectService.getBaseServiceProjectName(any(), any())).thenAnswer(args -> {
         IModel model = args.getArgument(1);
         IProjectInformation information = mock(IProjectInformation.class);
         String qualName = model.getFullyQualifiedName().toLowerCase();
         String name = model.getName().toLowerCase();
         when(information.getDirectoryName()).thenReturn(qualName + ".base");
         when(information.getArtifactId()).thenReturn(name + ".base");
         return information;
      });
      when(projectService.getEventsProjectName(any(), any())).thenAnswer(args -> {
         IModel model = args.getArgument(1);
         IProjectInformation information = mock(IProjectInformation.class);
         String name = model.getName().toLowerCase();
         when(information.getArtifactId()).thenReturn(name + ".events");
         return information;
      });
      when(packageService.getServiceBaseImplementationPackageName(any(), any())).thenAnswer(args -> {
         IModel model = args.getArgument(1);
         return model.getFullyQualifiedName().toLowerCase() + ".base.impl";
      });
      when(packageService.getServiceImplementationPackageName(any(), any())).thenAnswer(args -> {
         IModel model = args.getArgument(1);
         return model.getFullyQualifiedName().toLowerCase() + ".impl";
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

      abstractClassDto.setName("Abstract" + testModel.getName())
            .setPackageName(packageService.getServiceBaseImplementationPackageName(jellyFishCommandOptions, testModel))
            .setImports(new HashSet<>(Arrays.asList("com.ngc.blocs.service.event.api.IEvent",
                                                    "com.ngc.seaside.threateval.engagementtrackpriorityservice"
                                                    + ".api.IEngagementTrackPriorityService",
                                                    "com.ngc.seaside.threateval.engagementtrackpriorityservice"
                                                    + ".events.TrackEngagementStatus",
                                                    "com.ngc.seaside.threateval.engagementtrackpriorityservice"
                                                    + ".events.TrackPriority")));

      when(generatorService.getBaseServiceDescription(any(), any())).thenReturn(abstractClassDto);
      when(dataService.getEventClass(any(), any())).thenAnswer(args -> {
         INamedChild<IPackage> child = args.getArgument(1);
         TypeDto<?> typeDto = new ClassDto();
         typeDto.setPackageName(child.getParent().getName() + ".engagementtrackpriorityservice.events");
         typeDto.setTypeName(child.getName());
         return typeDto;
      });

      IGeneratedJavaField javaField = mock(IGeneratedJavaField.class);
      when(javaField.getJavaGetterName()).thenReturn("getFoo");
      when(dataFieldGenerationService.getEventsField(any(), any())).thenReturn(javaField);

      IScenario calculateTrackPriority = testModel.getScenarios()
            .getByName("calculateTrackPriority")
            .get();
      IScenario getTrackPriority = testModel.getScenarios()
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
   }

   @Test
   public void testDoesGenerateService() throws Throwable {
      parameters.addParameter(new DefaultParameter<>(CreateJavaServiceCommand.MODEL_PROPERTY,
                                                     "com.ngc.seaside.threateval.EngagementTrackPriorityService"));
      parameters.addParameter(new DefaultParameter<>(CreateJavaServiceCommand.OUTPUT_DIRECTORY_PROPERTY,
                                                     directory.getAbsolutePath()));

      command.run(jellyFishCommandOptions);

      Path gradleBuildPath = directory.toPath().resolve(Paths.get(
            "com.ngc.seaside.threateval.engagementtrackpriorityservice",
            "build.gradle"));

      assertFileContains(gradleBuildPath, "\\bproject\\(['\"]:engagementtrackpriorityservice"
                                          + ".events['\"]\\)");
      assertFileContains(gradleBuildPath, "\\bproject\\(['\"]:engagementtrackpriorityservice."
                                          + "base['\"]\\)");

      Path servicePath = directory.toPath().resolve(Paths.get(
            "com.ngc.seaside.threateval.engagementtrackpriorityservice",
            "src/main/java/com/ngc/seaside/threateval/engagementtrackpriorityservice/impl/"
            + "EngagementTrackPriorityService.java"));

      assertFileContains(servicePath, "\\bclass\\s+EngagementTrackPriorityService\\b");
      assertFileContains(servicePath, "extends\\s+\\S*?AbstractEngagementTrackPriorityService");

      assertFileContains(servicePath, "\\bTrackPriority\\s+doCalculateTrackPriority\\s*\\(");

      Path testPath = directory.toPath().resolve(Paths.get("com.ngc.seaside.threateval"
                                                           + ".engagementtrackpriorityservice",
                                                           "src/test/java/com/ngc/seaside/threateval"
                                                           + "/engagementtrackpriorityservice/impl"
                                                           + "/EngagementTrackPriorityServiceTest.java"));

      assertFileContains(testPath, "\\bpackage\\s+com.ngc.seaside.threateval."
                                   + "engagementtrackpriorityservice.impl\\s*;");
      assertFileContains(testPath, "\\bclass\\s+EngagementTrackPriorityServiceTest\\b");

   }

   @Test
   public void testDoesGenerateServiceInvolvingCorrelation() throws Throwable {
      IScenario calculateTrackPriority = testModel.getScenarios()
            .getByName("calculateTrackPriority")
            .get();
      IPublishSubscribeMessagingFlow pubSubFlow = FlowFactory.newCorrelatingPubSubFlowPath(calculateTrackPriority);
      when(scenarioService.getPubSubMessagingFlow(any(), eq(calculateTrackPriority)))
            .thenReturn(Optional.of(pubSubFlow));

      parameters.addParameter(new DefaultParameter<>(CreateJavaServiceCommand.MODEL_PROPERTY,
                                                     "com.ngc.seaside.threateval.EngagementTrackPriorityService"));
      parameters.addParameter(new DefaultParameter<>(CreateJavaServiceCommand.OUTPUT_DIRECTORY_PROPERTY,
                                                     directory.getAbsolutePath()));

      command.run(jellyFishCommandOptions);

      Path gradleBuildPath = directory.toPath().resolve(Paths.get(
            "com.ngc.seaside.threateval.engagementtrackpriorityservice",
            "build.gradle"));

      assertFileContains(gradleBuildPath, "\\bproject\\(['\"]:engagementtrackpriorityservice"
                                          + ".events['\"]\\)");
      assertFileContains(gradleBuildPath, "\\bproject\\(['\"]:engagementtrackpriorityservice."
                                          + "base['\"]\\)");

      Path servicePath = directory.toPath().resolve(Paths.get(
            "com.ngc.seaside.threateval.engagementtrackpriorityservice",
            "src/main/java/com/ngc/seaside/threateval/engagementtrackpriorityservice/impl/"
            + "EngagementTrackPriorityService.java"));

      assertFileContains(servicePath, "\\bclass\\s+EngagementTrackPriorityService\\b");
      assertFileContains(servicePath, "extends\\s+\\S*?AbstractEngagementTrackPriorityService");

      assertFileContains(servicePath, "\\bTrackPriority\\s+doCalculateTrackPriority\\s*\\(");

      Path testPath = directory.toPath().resolve(Paths.get("com.ngc.seaside.threateval"
                                                           + ".engagementtrackpriorityservice",
                                                           "src/test/java/com/ngc/seaside/threateval"
                                                           + "/engagementtrackpriorityservice/impl"
                                                           + "/EngagementTrackPriorityServiceTest.java"));

      assertFileContains(testPath, "\\bpackage\\s+com.ngc.seaside.threateval."
                                   + "engagementtrackpriorityservice.impl\\s*;");
      assertFileContains(testPath, "\\bclass\\s+EngagementTrackPriorityServiceTest\\b");

   }

   /**
    * @return Model used for testing
    */
   public static IModel newModelForTesting() {
      ModelUtils.PubSubModel model =
            new ModelUtils.PubSubModel("com.ngc.seaside.threateval.EngagementTrackPriorityService");
      IData trackEngagementStatus =
            ModelUtils.getMockNamedChild(IData.class,
                                         "com.ngc.seaside.threateval.TrackEngagementStatus");
      IData trackPriority =
            ModelUtils.getMockNamedChild(IData.class, "com.ngc.seaside.threateval.TrackPriority");
      model.addPubSub("calculateTrackPriority",
                      "trackEngagementStatus", trackEngagementStatus,
                      "trackPriority", trackPriority);

      IData trackPriorityRequest =
            ModelUtils.getMockNamedChild(IData.class, "com.ngc.seaside.threateval.TrackPriorityRequest");
      IData trackPriorityResponse =
            ModelUtils.getMockNamedChild(IData.class,
                                         "com.ngc.seaside.threateval.TrackPriorityResponse");
      ModelUtils.addReqRes(model,
                           "getTrackPriority",
                           "trackPriorityRequest", trackPriorityRequest,
                           "trackPriorityResponse", trackPriorityResponse);

      return model;
   }

}
