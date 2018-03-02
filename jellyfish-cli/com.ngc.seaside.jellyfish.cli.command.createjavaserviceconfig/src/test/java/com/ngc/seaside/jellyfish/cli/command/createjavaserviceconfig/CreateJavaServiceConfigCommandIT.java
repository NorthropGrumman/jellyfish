package com.ngc.seaside.jellyfish.cli.command.createjavaserviceconfig;

import static com.ngc.seaside.jellyfish.cli.command.test.files.TestingFiles.assertFileLinesEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedBuildManagementService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedTemplateService;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.basic.Package;
import com.ngc.seaside.systemdescriptor.model.impl.basic.data.Data;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.DataReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.Scenario;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.ScenarioStep;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class CreateJavaServiceConfigCommandIT {

   private CreateJavaServiceConfigCommand command;

   private MockedTemplateService templateService;

   private DefaultParameterCollection parameters;

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

   private IBuildManagementService buildManagementService;

   @Before
   public void setup() throws Throwable {
      outputDirectory.newFile("settings.gradle");

      buildManagementService = new MockedBuildManagementService();

      templateService = new MockedTemplateService()
            .useRealPropertyService()
            .setTemplateDirectory(CreateJavaServiceConfigCommand.class.getPackage().getName(),
                                  Paths.get("src", "main", "template"));

      ISystemDescriptor systemDescriptor = mock(ISystemDescriptor.class);
      when(systemDescriptor.findModel("com.ngc.seaside.threateval.EngagementTrackPriorityService"))
            .thenReturn(Optional.of(newModelForTesting()));

      parameters = new DefaultParameterCollection();
      when(jellyFishCommandOptions.getParameters()).thenReturn(parameters);
      when(jellyFishCommandOptions.getSystemDescriptor()).thenReturn(systemDescriptor);

      when(projectService.getConfigProjectName(any(), any())).thenAnswer(args -> {
         IModel model = args.getArgument(1);
         IProjectInformation info = mock(IProjectInformation.class);
         when(info.getDirectoryName()).thenReturn(model.getFullyQualifiedName().toLowerCase() + ".config");
         return info;
      });
      
      when(projectService.getBaseServiceProjectName(any(), any())).thenAnswer(args -> {
         IModel model = args.getArgument(1);
         IProjectInformation info = mock(IProjectInformation.class);
         when(info.getArtifactId()).thenReturn(model.getName().toLowerCase() + ".base");
         return info;
      });
      
      
      when(packageService.getConfigPackageName(any(), any())).thenAnswer(args -> {
         IModel model = args.getArgument(1);
         return model.getFullyQualifiedName().toLowerCase() + ".transport.config";
      });
      
      command = new CreateJavaServiceConfigCommand();
      command.setLogService(logService);
      command.setProjectNamingService(projectService);
      command.setPackageNamingService(packageService);
      command.setTemplateService(templateService);
      command.setBuildManagementService(buildManagementService);
   }

   @Test
   public void testDoesGenerateServiceConfigWithSuppliedCommands() throws Throwable {
      parameters.addParameter(new DefaultParameter<>(CreateJavaServiceConfigCommand.MODEL_PROPERTY,
                                                     "com.ngc.seaside.threateval.EngagementTrackPriorityService"));
      parameters.addParameter(new DefaultParameter<>(CreateJavaServiceConfigCommand.OUTPUT_DIRECTORY_PROPERTY,
                                                     outputDirectory.getRoot().getAbsolutePath()));

      command.run(jellyFishCommandOptions);

      assertFileLinesEquals(
            "build.gradle not correct!",
            Paths.get("src", "test", "resources", "expectedfiles", "service-config-build.gradle.expected"),
            Paths.get(outputDirectory.getRoot().getAbsolutePath(),
                      "com.ngc.seaside.threateval.engagementtrackpriorityservice.config", "build.gradle"));

      assertFileLinesEquals(
            "transport config not correct!",
            Paths.get("src", "test", "resources", "expectedfiles", "service-transport-config.java.expected"),
            Paths.get(outputDirectory.getRoot().getAbsolutePath(),
                      "com.ngc.seaside.threateval.engagementtrackpriorityservice.config",
                      "src/main/java/com/ngc/seaside/threateval/engagementtrackpriorityservice/transport/config/EngagementTrackPriorityServiceTransportConfiguration.java"));
   }

   public static Model newModelForTesting() {
      Data trackEngagementStatus = new Data("TrackEngagementStatus");
      Data trackPriority = new Data("TrackPriority");

      Scenario calculateTrackPriority = new Scenario("calculateTrackPriority");

      ScenarioStep step = new ScenarioStep();
      step.setKeyword("receiving");
      step.getParameters().add("trackEngagementStatus");
      calculateTrackPriority.setWhens(Collections.singletonList(step));

      step = new ScenarioStep();
      step.setKeyword("willPublish");
      step.getParameters().add("trackPriority");
      calculateTrackPriority.setThens(Collections.singletonList(step));

      Model model = new Model("EngagementTrackPriorityService");
      model.addInput(new DataReferenceField("trackEngagementStatus").setType(trackEngagementStatus));
      model.addOutput(new DataReferenceField("trackPriority").setType(trackPriority));
      model.addScenario(calculateTrackPriority);
      calculateTrackPriority.setParent(model);

      Package p = new Package("com.ngc.seaside.threateval");
      p.addModel(model);

      return model;
   }

}
