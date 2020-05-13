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
                        new LinkedHashSet<>(Collections.singleton(providerPlugin)), javaServiceGenerationService);

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
