package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig;

import static com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.CreateJavaServiceGeneratedConfigCommand.CONFIG_BUILD_TEMPLATE_SUFFIX;
import static com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.CreateJavaServiceGeneratedConfigCommand.CONFIG_GENERATED_BUILD_TEMPLATE_SUFFIX;
import static com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.multicast.MulticastTransportProviderConfigDto.MULTICAST_TEMPLATE_SUFFIX;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedBuildManagementService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedPackageNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedProjectNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedScenarioService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedTemplateService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedTransportConfigurationService;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.utilities.command.JellyfishCommandPhase;
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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
@RunWith(MockitoJUnitRunner.class)
public class CreateJavaServiceGeneratedConfigCommandIT {

   private CreateJavaServiceGeneratedConfigCommand command;

   @Rule
   public final TemporaryFolder outputDirectory = new TemporaryFolder();

   @Mock
   private IJellyFishCommandOptions jellyFishCommandOptions;

   private DefaultParameterCollection parameters;

   @Mock
   private ILogService logService;

   private MockedTemplateService templateService;

   private IProjectNamingService projectService = new MockedProjectNamingService();

   private IPackageNamingService packageService = new MockedPackageNamingService();

   private IBuildManagementService buildManagementService = new MockedBuildManagementService();

   private MockedTransportConfigurationService transportConfigService = new MockedTransportConfigurationService();

   private IJavaServiceGenerationService generateService = new MockedJavaServiceGenerationService(packageService);
   
   private IScenarioService scenarioService = new MockedScenarioService();

   @Before
   public void setup() throws Throwable {
      outputDirectory.newFile("settings.gradle");

      templateService = new MockedTemplateService()
            .useRealPropertyService()
            .setTemplateDirectory(
                  CreateJavaServiceGeneratedConfigCommand.class.getPackage().getName() + "-"
                           + CONFIG_GENERATED_BUILD_TEMPLATE_SUFFIX,
                  Paths.get("src", "main", "templates", CONFIG_GENERATED_BUILD_TEMPLATE_SUFFIX))
            .setTemplateDirectory(
                  CreateJavaServiceGeneratedConfigCommand.class.getPackage().getName() + "-"
                           + CONFIG_BUILD_TEMPLATE_SUFFIX,
                  Paths.get("src", "main", "templates", CONFIG_BUILD_TEMPLATE_SUFFIX));
      
      ISystemDescriptor systemDescriptor = mock(ISystemDescriptor.class);
      when(systemDescriptor.findModel("com.ngc.seaside.threateval.EngagementTrackPriorityService"))
            .thenReturn(Optional.of(newModelForTesting()));

      parameters = new DefaultParameterCollection();
      when(jellyFishCommandOptions.getParameters()).thenReturn(parameters);
      when(jellyFishCommandOptions.getSystemDescriptor()).thenReturn(systemDescriptor);

      command = new CreateJavaServiceGeneratedConfigCommand();
      command.setLogService(logService);
      command.setProjectNamingService(projectService);
      command.setPackageNamingService(packageService);
      command.setTemplateService(templateService);
      command.setBuildManagementService(buildManagementService);
      command.setJavaServiceGenerationService(generateService);
      command.setTransportConfigurationService(transportConfigService);
      command.setScenarioService(scenarioService);
   }

   @Test
   public void multicastFunctionalTest() throws Throwable {
      templateService.setTemplateDirectory(
         CreateJavaServiceGeneratedConfigCommand.class.getPackage().getName() + "-"
                  + MULTICAST_TEMPLATE_SUFFIX,
         Paths.get("src", "main", "templates", MULTICAST_TEMPLATE_SUFFIX));
      
      transportConfigService.addMulticastConfiguration("trackEngagementStatus", "224.5.6.7",
                                                       61000, "127.0.0.1", "127.0.0.1");
      transportConfigService.addMulticastConfiguration("trackEngagementStatus", "224.5.6.7",
                                                       61001, "127.0.0.1", "127.0.0.1");
      
      run(
         CreateJavaServiceGeneratedConfigCommand.MODEL_PROPERTY, "com.ngc.seaside.threateval.EngagementTrackPriorityService",
         CreateJavaServiceGeneratedConfigCommand.DEPLOYMENT_MODEL_PROPERTY, "",
         CreateJavaServiceGeneratedConfigCommand.OUTPUT_DIRECTORY_PROPERTY, outputDirectory.getRoot().getAbsolutePath(),
         CommonParameters.PHASE.getName(), JellyfishCommandPhase.DEFERRED);
      
      Path projectDir = outputDirectory.getRoot().toPath().resolve("com.ngc.seaside.threateval.engagementtrackpriorityservice.config");
      Path srcDir = projectDir.resolve(Paths.get("src", "main", "java", "com", "ngc", "seaside", "threateval", "engagementtrackpriorityservice", "config"));

      Path buildFile = projectDir.resolve("build.generated.gradle");
      Path configurationFile = srcDir.resolve("EngagementTrackPriorityServiceTransportConfiguration.java");
      Path multicastFile = srcDir.resolve("EngagementTrackPriorityServiceMulticastConfiguration.java");
      
      assertTrue(Files.isRegularFile(buildFile));
      assertTrue(Files.isRegularFile(configurationFile));
      assertTrue(Files.isRegularFile(multicastFile));
   }
   
   private void run(Object... args) {
      
      for (int i = 0; i < args.length; i += 2) {
         String parameterName = args[i].toString();
         Object parameterValue = args[i + 1];
         parameters.addParameter(new DefaultParameter<>(parameterName, parameterValue));
      }
      
      command.run(jellyFishCommandOptions);
   }

   public static Model newModelForTesting() {
      Data trackEngagementStatus = new Data("TrackEngagementStatus");
      Data trackPriority = new Data("TrackPriority");

      Scenario calculateTrackPriority = new Scenario("calculateTrackPriority");

      ScenarioStep step = new ScenarioStep();
      step.setKeyword("receiving");
      step.getParameters().add("trackEngagementStatus");
      step.setParent(calculateTrackPriority);
      calculateTrackPriority.setWhens(Collections.singletonList(step));

      step = new ScenarioStep();
      step.setKeyword("willPublish");
      step.getParameters().add("trackPriority");
      step.setParent(calculateTrackPriority);
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
