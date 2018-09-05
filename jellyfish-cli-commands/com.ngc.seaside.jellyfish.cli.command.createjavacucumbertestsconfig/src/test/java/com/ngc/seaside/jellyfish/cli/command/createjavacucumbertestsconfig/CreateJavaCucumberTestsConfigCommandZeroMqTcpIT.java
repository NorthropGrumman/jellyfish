/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertestsconfig;

import com.ngc.seaside.jellyfish.api.CommonParameters;
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
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CreateJavaCucumberTestsConfigCommandZeroMqTcpIT extends CreateJavaCucumberTestsConfigCommandBaseIT {

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

      run(CreateJavaCucumberTestsConfigCommand.MODEL_PROPERTY,
               "com.ngc.seaside.threateval.EngagementTrackPriorityService",
               CreateJavaCucumberTestsConfigCommand.DEPLOYMENT_MODEL_PROPERTY,
               "com.ngc.seaside.threateval.EngagementTrackPriorityService",
               CreateJavaCucumberTestsConfigCommand.OUTPUT_DIRECTORY_PROPERTY,
               outputDirectory.getRoot().getAbsolutePath(),
               CommonParameters.PHASE.getName(),
               JellyfishCommandPhase.DEFERRED);

      Path projectDir = outputDirectory.getRoot().toPath().resolve(
               "com.ngc.seaside.threateval.engagementtrackpriorityservice.testsconfig");
      Path srcDir = projectDir.resolve(Paths.get("src",
               "main",
               "java",
               "com",
               "ngc",
               "seaside",
               "threateval",
               "engagementtrackpriorityservice",
               "testsconfig"));

      Path buildFile = projectDir.resolve("build.generated.gradle");
      Path configurationFile = srcDir.resolve("EngagementTrackPriorityServiceTestTransportConfiguration.java");
      Path zeroMqFile = srcDir.resolve("EngagementTrackPriorityServiceZeroMqConfiguration.java");

      assertTrue(Files.isRegularFile(buildFile));
      assertTrue(Files.isRegularFile(configurationFile));
      assertTrue(Files.isRegularFile(zeroMqFile));

      run(CreateJavaCucumberTestsConfigCommand.MODEL_PROPERTY,
               "com.ngc.seaside.threateval.TrackPriorityService",
               CreateJavaCucumberTestsConfigCommand.DEPLOYMENT_MODEL_PROPERTY,
               "com.ngc.seaside.threateval.TrackPriorityService",
               CreateJavaCucumberTestsConfigCommand.OUTPUT_DIRECTORY_PROPERTY,
               outputDirectory.getRoot().getAbsolutePath(),
               CommonParameters.PHASE.getName(),
               JellyfishCommandPhase.DEFERRED);

      projectDir = outputDirectory.getRoot().toPath()
               .resolve("com.ngc.seaside.threateval.trackpriorityservice.testsconfig");
      srcDir = projectDir.resolve(
               Paths.get("src", "main", "java", "com", "ngc", "seaside", "threateval", "trackpriorityservice",
                        "testsconfig"));

      buildFile = projectDir.resolve("build.generated.gradle");
      configurationFile = srcDir.resolve("TrackPriorityServiceTestTransportConfiguration.java");
      zeroMqFile = srcDir.resolve("TrackPriorityServiceZeroMqConfiguration.java");

      assertTrue(Files.isRegularFile(buildFile));
      assertTrue(Files.isRegularFile(configurationFile));
      assertTrue(Files.isRegularFile(zeroMqFile));
   }

}
