package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertestsconfig;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.io.MulticastIOTopicPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider.multicast.MulticastTransportProviderPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportservice.TransportServiceConfigurationPlugin;
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
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CreateJavaCucumberTestsConfigCommandMulticastIT extends CreateJavaCucumberTestsConfigCommandBaseIT {

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

      MulticastIOTopicPlugin topicPlugin =
               new MulticastIOTopicPlugin(transportService, generateService, scenarioService);
      MulticastTransportProviderPlugin providerPlugin =
               new MulticastTransportProviderPlugin(Collections.singleton(topicPlugin));
      TransportServiceConfigurationPlugin transportPlugin =
               new TransportServiceConfigurationPlugin(Collections.singleton(providerPlugin));

      command.addConfigurationPlugin(providerPlugin);
      command.addConfigurationPlugin(transportPlugin);

   }

   @Test
   public void multicast() throws Throwable {

      transportService.addMulticastConfiguration("trackEngagementStatus", "224.5.6.7",
               61000, "127.0.0.1", "127.0.0.1");
      transportService.addMulticastConfiguration("trackEngagementStatus", "224.5.6.7",
               61001, "127.0.0.1", "127.0.0.1");

      run(CreateJavaCucumberTestsConfigCommand.MODEL_PROPERTY,
               "com.ngc.seaside.threateval.EngagementTrackPriorityService",
               CreateJavaCucumberTestsConfigCommand.DEPLOYMENT_MODEL_PROPERTY,
               "com.ngc.seaside.threateval.EngagementTrackPriorityService",
               CreateJavaCucumberTestsConfigCommand.OUTPUT_DIRECTORY_PROPERTY,
               outputDirectory.getRoot().getAbsolutePath(),
               CommonParameters.PHASE.getName(), JellyfishCommandPhase.DEFERRED);

      Path projectDir =
               outputDirectory.getRoot().toPath()
                        .resolve("com.ngc.seaside.threateval.engagementtrackpriorityservice.testsconfig");
      Path srcDir =
               projectDir.resolve(Paths.get("src", "main", "java", "com", "ngc", "seaside", "threateval",
                        "engagementtrackpriorityservice", "testsconfig"));

      Path buildFile = projectDir.resolve("build.generated.gradle");
      Path configurationFile = srcDir.resolve("EngagementTrackPriorityServiceTestTransportConfiguration.java");
      Path multicastFile = srcDir.resolve("EngagementTrackPriorityServiceMulticastConfiguration.java");

      assertTrue(Files.isRegularFile(buildFile));
      assertTrue(Files.isRegularFile(configurationFile));
      assertTrue(Files.isRegularFile(multicastFile));
   }

}
