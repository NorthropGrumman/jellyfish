package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertestsconfig;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.telemetryreporting.RestTelemetryReportingConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.telemetryreporting.RestTelemetryReportingTopicPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.httpclient.HttpClientTransportProviderPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.spark.SparkTransportProviderPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportservice.TransportServiceConfigurationPlugin;
import com.ngc.seaside.jellyfish.service.config.api.dto.HttpMethod;
import com.ngc.seaside.jellyfish.utilities.command.JellyfishCommandPhase;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CreateJavaCucumberTestsConfigCommandRestTelemetryReportingIT
         extends CreateJavaCucumberTestsConfigCommandBaseIT {

   @Rule
   public final TemporaryFolder outputDirectory = new TemporaryFolder();

   private IModel model;

   @Before
   public void setup() throws Exception {
      outputDirectory.newFile("settings.gradle");

      super.setup();

      model = newRequestResponseModelForTesting();
      ISystemDescriptor systemDescriptor = mock(ISystemDescriptor.class);
      when(systemDescriptor.findModel("com.ngc.seaside.threateval.TrackPriorityService"))
               .thenReturn(Optional.of(model));

      when(jellyFishCommandOptions.getSystemDescriptor()).thenReturn(systemDescriptor);

      RestTelemetryReportingTopicPlugin topicPlugin =
               new RestTelemetryReportingTopicPlugin(telemetryReportingService, generateService);
      RestTelemetryReportingConfigurationPlugin configurationPlugin =
               new RestTelemetryReportingConfigurationPlugin(logService, telemetryReportingService, generateService);
      SparkTransportProviderPlugin sparkProviderPlugin =
               new SparkTransportProviderPlugin(Collections.singleton(topicPlugin));
      HttpClientTransportProviderPlugin httpProviderPlugin =
               new HttpClientTransportProviderPlugin(Collections.singleton(topicPlugin));
      TransportServiceConfigurationPlugin transportPlugin =
               new TransportServiceConfigurationPlugin(
                        new LinkedHashSet<>(Arrays.asList(sparkProviderPlugin, httpProviderPlugin)));

      command.addConfigurationPlugin(sparkProviderPlugin);
      command.addConfigurationPlugin(httpProviderPlugin);
      command.addConfigurationPlugin(configurationPlugin);
      command.addConfigurationPlugin(transportPlugin);

   }

   @Test
   public void restTelemetryReporting() throws Throwable {

      telemetryReportingService.addRestTelemetryReportingConfiguration(model, 1, "localhost", "0.0.0.0", 52412,
               "/trackPriorityRequest", "application/x-protobuf",
               HttpMethod.POST);

      run(CreateJavaCucumberTestsConfigCommand.MODEL_PROPERTY,
               "com.ngc.seaside.threateval.TrackPriorityService",
               CreateJavaCucumberTestsConfigCommand.DEPLOYMENT_MODEL_PROPERTY,
               "com.ngc.seaside.threateval.TrackPriorityService",
               CreateJavaCucumberTestsConfigCommand.OUTPUT_DIRECTORY_PROPERTY,
               outputDirectory.getRoot().getAbsolutePath(),
               CommonParameters.PHASE.getName(), JellyfishCommandPhase.DEFERRED);

      Path projectDir =
               outputDirectory.getRoot()
                        .toPath()
                        .resolve("com.ngc.seaside.threateval.trackpriorityservice.testsconfig");
      Path srcDir =
               projectDir.resolve(Paths.get("src", "main", "java", "com", "ngc", "seaside", "threateval",
                        "trackpriorityservice", "testsconfig"));

      Path buildFile = projectDir.resolve("build.generated.gradle");
      Path configurationFile = srcDir.resolve("TrackPriorityServiceTransportConfiguration.java");
      Path sparkFile = srcDir.resolve("TrackPriorityServiceSparkConfiguration.java");
      Path httpFile = srcDir.resolve("TrackPriorityServiceHttpClientConfiguration.java");
      Path telemetryReportingFile = srcDir.resolve("TrackPriorityServiceTelemetryReportingConfiguration.java");

      assertTrue(Files.isRegularFile(buildFile));
      assertTrue(Files.isRegularFile(configurationFile));
      assertFalse(Files.isRegularFile(telemetryReportingFile));
      assertFalse(Files.isRegularFile(sparkFile));
      assertFalse(Files.isRegularFile(httpFile));
   }

}
