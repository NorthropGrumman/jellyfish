package com.ngc.seaside.jellyfish.cli.command.createjavaserviceconfig;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.template.MockedTemplateService;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static com.ngc.seaside.jellyfish.cli.command.test.files.TestingFiles.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

   @Before
   public void setup() throws Throwable {
      outputDirectory.newFile("settings.gradle");

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

      command = new CreateJavaServiceConfigCommand();
      command.setLogService(logService);
      command.setTemplateService(templateService);
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
            "settings.gradle not correct!",
            Paths.get("src", "test", "resources", "expectedfiles", "service-config-settings.gradle.expected"),
            Paths.get(outputDirectory.getRoot().getAbsolutePath(), "settings.gradle"));

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
      calculateTrackPriority.setWhens(listOf(step));

      step = new ScenarioStep();
      step.setKeyword("willPublish");
      step.getParameters().add("trackPriority");
      calculateTrackPriority.setThens(listOf(step));

      Model model = new Model("EngagementTrackPriorityService");
      model.addInput(new DataReferenceField("trackEngagementStatus").setType(trackEngagementStatus));
      model.addOutput(new DataReferenceField("trackPriority").setType(trackPriority));
      model.addScenario(calculateTrackPriority);
      calculateTrackPriority.setParent(model);

      Package p = new Package("com.ngc.seaside.threateval");
      p.addModel(model);

      return model;
   }

   private static <T> ArrayList<T> listOf(T... things) {
      // TODO TH:
      // Fix the basic model impl.  ArrayList SHOULD NOT be in the signature.
      return new ArrayList<>(Arrays.asList(things));
   }
}
