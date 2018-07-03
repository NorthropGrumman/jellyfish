package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.BaseServiceDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.io.ZeroMqIOTopicPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.zeromq.ZeroMqTcpTransportProviderPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportservice.TransportServiceConfigurationPlugin;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ConnectionType;
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
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CreateJavaServiceGeneratedConfigCommandZeroMqTcpIT extends CreateJavaServiceGeneratedConfigCommandBaseIT {

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

      ZeroMqIOTopicPlugin topicPlugin =
               new ZeroMqIOTopicPlugin(transportService, generateService, scenarioService);
      ZeroMqTcpTransportProviderPlugin providerPlugin =
               new ZeroMqTcpTransportProviderPlugin(Collections.singleton(topicPlugin));
      TransportServiceConfigurationPlugin transportPlugin =
               new TransportServiceConfigurationPlugin(
                        new LinkedHashSet<>(Collections.singleton(providerPlugin)));

      command.addConfigurationPlugin(providerPlugin);
      command.addConfigurationPlugin(transportPlugin);

   }

   @Test
   public void zeroMqTcp() throws Throwable {

      transportService.addZeroMqTcpConfiguration("trackEngagementStatus",
               ConnectionType.SOURCE_BINDS_TARGET_CONNECTS,
               "*",
               "localhost",
               1000);

      transportService.addZeroMqTcpConfiguration("trackPriorityRequest",
               ConnectionType.SOURCE_BINDS_TARGET_CONNECTS,
               "*",
               "localhost",
               1001);

      run(
               CreateJavaServiceGeneratedConfigCommand.MODEL_PROPERTY,
               "com.ngc.seaside.threateval.EngagementTrackPriorityService",
               CreateJavaServiceGeneratedConfigCommand.DEPLOYMENT_MODEL_PROPERTY,
               "com.ngc.seaside.threateval.EngagementTrackPriorityService",
               CreateJavaServiceGeneratedConfigCommand.OUTPUT_DIRECTORY_PROPERTY,
               outputDirectory.getRoot().getAbsolutePath(),
               CommonParameters.PHASE.getName(),
               JellyfishCommandPhase.DEFERRED);

      Path projectDir = outputDirectory.getRoot().toPath().resolve(
               "com.ngc.seaside.threateval.engagementtrackpriorityservice.config");
      Path srcDir = projectDir.resolve(Paths.get("src",
               "main",
               "java",
               "com",
               "ngc",
               "seaside",
               "threateval",
               "engagementtrackpriorityservice",
               "config"));

      Path buildFile = projectDir.resolve("build.generated.gradle");
      Path configurationFile = srcDir.resolve("EngagementTrackPriorityServiceTransportConfiguration.java");
      Path zeroMqFile = srcDir.resolve("EngagementTrackPriorityServiceZeroMqConfiguration.java");

      assertTrue(Files.isRegularFile(buildFile));
      assertTrue(Files.isRegularFile(configurationFile));
      assertTrue(Files.walk(projectDir.getParent()).map(Object::toString).collect(Collectors.joining(",")),
               Files.isRegularFile(zeroMqFile));

      run(
               CreateJavaServiceGeneratedConfigCommand.MODEL_PROPERTY,
               "com.ngc.seaside.threateval.TrackPriorityService",
               CreateJavaServiceGeneratedConfigCommand.DEPLOYMENT_MODEL_PROPERTY,
               "com.ngc.seaside.threateval.TrackPriorityService",
               CreateJavaServiceGeneratedConfigCommand.OUTPUT_DIRECTORY_PROPERTY,
               outputDirectory.getRoot().getAbsolutePath(),
               CommonParameters.PHASE.getName(),
               JellyfishCommandPhase.DEFERRED);

      projectDir = outputDirectory.getRoot().toPath().resolve("com.ngc.seaside.threateval.trackpriorityservice.config");
      srcDir = projectDir.resolve(
               Paths.get("src", "main", "java", "com", "ngc", "seaside", "threateval", "trackpriorityservice",
                        "config"));

      buildFile = projectDir.resolve("build.generated.gradle");
      configurationFile = srcDir.resolve("TrackPriorityServiceTransportConfiguration.java");
      zeroMqFile = srcDir.resolve("TrackPriorityServiceZeroMqConfiguration.java");

      assertTrue(Files.isRegularFile(buildFile));
      assertTrue(Files.isRegularFile(configurationFile));
      assertTrue(Files.isRegularFile(zeroMqFile));
   }

}
