package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.BaseServiceDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.io.RestIOTopicPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.httpclient.HttpClientTransportProviderPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.spark.SparkTransportProviderPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportservice.TransportServiceConfigurationPlugin;
import com.ngc.seaside.jellyfish.service.config.api.dto.HttpMethod;
import com.ngc.seaside.jellyfish.utilities.command.JellyfishCommandPhase;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CreateJavaServiceGeneratedConfigCommandRestIT extends CreateJavaServiceGeneratedConfigCommandBaseIT {

   @Rule
   public final TemporaryFolder outputDirectory = new TemporaryFolder();

   @Before
   public void setup() throws Exception {
      outputDirectory.newFile("settings.gradle");

      super.setup();

      ISystemDescriptor systemDescriptor = mock(ISystemDescriptor.class);
      when(systemDescriptor.findModel("com.ngc.seaside.threateval.EngagementTrackPriorityService"))
               .thenReturn(Optional.of(newPubSubModelForTesting()));
      when(systemDescriptor.findModel("com.ngc.seaside.threateval.TrackPriorityService"))
               .thenReturn(Optional.of(newRequestResponseModelForTesting()));

      when(jellyFishCommandOptions.getSystemDescriptor()).thenReturn(systemDescriptor);

      BaseServiceDto baseServiceDto = mock(BaseServiceDto.class);
      when(baseServiceDto.getCorrelationMethods()).thenReturn(new ArrayList<>());
      when(baseServiceDto.getBasicPubSubMethods()).thenReturn(new ArrayList<>());
      when(baseServiceDtoFactory.newDto(any(), any())).thenReturn(baseServiceDto);

      RestIOTopicPlugin topicPlugin =
               new RestIOTopicPlugin(transportService, generateService, scenarioService);
      SparkTransportProviderPlugin sparkProviderPlugin =
               new SparkTransportProviderPlugin(Collections.singleton(topicPlugin));
      HttpClientTransportProviderPlugin httpProviderPlugin =
               new HttpClientTransportProviderPlugin(Collections.singleton(topicPlugin));
      TransportServiceConfigurationPlugin transportPlugin =
               new TransportServiceConfigurationPlugin(
                        new LinkedHashSet<>(Arrays.asList(sparkProviderPlugin, httpProviderPlugin)));

      command.addConfigurationPlugin(sparkProviderPlugin);
      command.addConfigurationPlugin(httpProviderPlugin);
      command.addConfigurationPlugin(transportPlugin);

   }

   @Test
   public void rest() throws Throwable {

      transportService.addRestConfiguration("trackPriorityRequest", "localhost", "0.0.0.0", 52412,
               "/trackPriorityRequest", "application/x-protobuf", HttpMethod.POST);

      run(CreateJavaServiceGeneratedConfigCommand.MODEL_PROPERTY,
               "com.ngc.seaside.threateval.TrackPriorityService",
               CreateJavaServiceGeneratedConfigCommand.DEPLOYMENT_MODEL_PROPERTY,
               "com.ngc.seaside.threateval.TrackPriorityService",
               CreateJavaServiceGeneratedConfigCommand.OUTPUT_DIRECTORY_PROPERTY,
               outputDirectory.getRoot().getAbsolutePath(),
               CommonParameters.PHASE.getName(), JellyfishCommandPhase.DEFERRED);

      Path projectDir =
               outputDirectory.getRoot()
                        .toPath()
                        .resolve("com.ngc.seaside.threateval.trackpriorityservice.config");
      Path srcDir =
               projectDir.resolve(Paths.get("src", "main", "java", "com", "ngc", "seaside", "threateval",
                        "trackpriorityservice", "config"));

      Path buildFile = projectDir.resolve("build.generated.gradle");
      Path configurationFile = srcDir.resolve("TrackPriorityServiceTransportConfiguration.java");
      Path httpConfigurationFile = srcDir.resolve("TrackPriorityServiceHttpClientConfiguration.java");
      Path restFile = srcDir.resolve("TrackPriorityServiceSparkConfiguration.java");

      assertTrue(Files.isRegularFile(buildFile));
      assertTrue(Files.isRegularFile(configurationFile));
      assertTrue(Files.isRegularFile(restFile));
      assertFalse(Files.isRegularFile(httpConfigurationFile));
   }

}
