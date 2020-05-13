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
package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertestsconfig;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.CreateJavaServiceGeneratedConfigCommand;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedBuildManagementService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedPackageNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedProjectNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedScenarioService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedTemplateService;
import com.ngc.seaside.jellyfish.cli.command.test.service.config.MockedAdminstrationConfigurationService;
import com.ngc.seaside.jellyfish.cli.command.test.service.config.MockedTelemetryConfigurationService;
import com.ngc.seaside.jellyfish.cli.command.test.service.config.MockedTelemetryReportingConfigurationService;
import com.ngc.seaside.jellyfish.cli.command.test.service.config.MockedTransportConfigurationService;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.user.api.IJellyfishUserService;
import com.ngc.seaside.systemdescriptor.model.impl.basic.Package;
import com.ngc.seaside.systemdescriptor.model.impl.basic.data.Data;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.DataReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.Scenario;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.ScenarioStep;

import org.mockito.Mockito;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreateJavaCucumberTestsConfigCommandBaseIT {

   protected final CreateJavaCucumberTestsConfigCommand command = new CreateJavaCucumberTestsConfigCommand();

   protected final IJellyFishCommandOptions jellyFishCommandOptions = mock(IJellyFishCommandOptions.class);

   protected final DefaultParameterCollection parameters = new DefaultParameterCollection();

   protected final ILogService logService = mock(ILogService.class);

   protected final MockedTemplateService templateService = new MockedTemplateService();

   protected final MockedProjectNamingService projectService = new MockedProjectNamingService();

   protected final MockedBuildManagementService buildManagementService = new MockedBuildManagementService();

   protected final IPackageNamingService packageService = new MockedPackageNamingService();

   protected final MockedTransportConfigurationService transportService = new MockedTransportConfigurationService();

   protected final MockedJavaServiceGenerationService generateService = new MockedJavaServiceGenerationService();

   protected final MockedScenarioService scenarioService = new MockedScenarioService();

   protected final MockedTelemetryConfigurationService telemetryService = new MockedTelemetryConfigurationService();

   protected final MockedTelemetryReportingConfigurationService telemetryReportingService =
            new MockedTelemetryReportingConfigurationService();

   protected final MockedAdminstrationConfigurationService adminService = new MockedAdminstrationConfigurationService();

   protected final IJavaServiceGenerationService javaServiceGenerationService =
         mock(IJavaServiceGenerationService.class);

   protected void setup() throws Exception {
      templateService.useRealPropertyService();

      when(javaServiceGenerationService.getServiceInterfaceDescription(any(), any())).thenReturn(
            new ClassDto().setTypeName("IFooService").setPackageName("a.b.c").setName("fooService")
      );

      Files.list(Paths.get("..", "com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig", "src",
               "main", "templates"))
               .filter(Files::isDirectory)
               .filter(path -> !Files.isDirectory(Paths.get("src", "main", "templates", path.getFileName().toString())))
               .forEach(path -> {
                  try {
                     templateService.setTemplateDirectory(
                              CreateJavaServiceGeneratedConfigCommand.class.getPackage().getName() + "-"
                                       + path.getFileName(),
                              path.toRealPath());
                  } catch (IOException e) {
                     throw new UncheckedIOException(e);
                  }
               });
      Files.list(Paths.get("src", "main", "templates"))
               .filter(Files::isDirectory)
               .forEach(path -> {
                  templateService.setTemplateDirectory(
                           CreateJavaCucumberTestsConfigCommand.class.getPackage().getName() + "-"
                                    + path.getFileName(),
                           path);
               });

      when(jellyFishCommandOptions.getParameters()).thenReturn(parameters);
      command.setLogService(logService);
      command.setProjectNamingService(projectService);
      command.setPackageNamingService(packageService);
      command.setTemplateService(templateService);
      command.setBuildManagementService(buildManagementService);
      command.setJellyfishUserService(mock(IJellyfishUserService.class, Mockito.RETURNS_DEEP_STUBS));
   }

   protected void run(Object... args) {
      for (int i = 0; i < args.length; i += 2) {
         String parameterName = args[i].toString();
         Object parameterValue = args[i + 1];
         parameters.addParameter(new DefaultParameter<>(parameterName, parameterValue));
      }

      command.run(jellyFishCommandOptions);
   }

   /**
    * @return Pub Sub Model used for testing
    */
   public static Model newPubSubModelForTesting() {
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
      model.addInput(new DataReferenceField("trackEngagementStatus").setParent(model).setType(trackEngagementStatus));
      model.addOutput(new DataReferenceField("trackPriority").setParent(model).setType(trackPriority));
      model.addScenario(calculateTrackPriority);
      calculateTrackPriority.setParent(model);

      Package p = new Package("com.ngc.seaside.threateval");
      p.addModel(model);

      return model;
   }

   /**
    * @return Response Model used for testing
    */
   public static Model newRequestResponseModelForTesting() {
      Data trackPriorityRequest = new Data("TrackPriorityRequest");
      Data trackPriorityResponse = new Data("TrackPriorityResponse");

      Scenario getTrackPriorities = new Scenario("getTrackPriorities");

      ScenarioStep step = new ScenarioStep();
      step.setKeyword("receivingRequest");
      step.getParameters().add("trackPriorityRequest");
      step.setParent(getTrackPriorities);
      getTrackPriorities.setWhens(Collections.singletonList(step));

      step = new ScenarioStep();
      step.setKeyword("willRespond");
      step.getParameters().add("with");
      step.getParameters().add("trackPriorityResponse");
      step.setParent(getTrackPriorities);
      getTrackPriorities.setThens(Collections.singletonList(step));

      Model model = new Model("TrackPriorityService");
      model.addInput(new DataReferenceField("trackPriorityRequest").setParent(model).setType(trackPriorityRequest));
      model.addOutput(new DataReferenceField("trackPriorityResponse").setParent(model).setType(trackPriorityResponse));
      model.addScenario(getTrackPriorities);
      getTrackPriorities.setParent(model);

      Package p = new Package("com.ngc.seaside.threateval");
      p.addModel(model);

      return model;
   }

}
