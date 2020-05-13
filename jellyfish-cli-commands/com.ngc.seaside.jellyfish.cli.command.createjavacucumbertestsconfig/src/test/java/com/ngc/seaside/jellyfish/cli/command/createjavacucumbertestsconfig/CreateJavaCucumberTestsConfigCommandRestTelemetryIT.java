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
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.telemetry.RestTelemetryConfigurationPlugin;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.telemetry.RestTelemetryTopicPlugin;
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
public class CreateJavaCucumberTestsConfigCommandRestTelemetryIT
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

      RestTelemetryTopicPlugin topicPlugin = new RestTelemetryTopicPlugin(telemetryService);
      RestTelemetryConfigurationPlugin configurationPlugin = new RestTelemetryConfigurationPlugin(telemetryService);
      SparkTransportProviderPlugin sparkProviderPlugin =
               new SparkTransportProviderPlugin(Collections.singleton(topicPlugin));
      HttpClientTransportProviderPlugin httpProviderPlugin =
               new HttpClientTransportProviderPlugin(Collections.singleton(topicPlugin));
      TransportServiceConfigurationPlugin transportPlugin =
               new TransportServiceConfigurationPlugin(
                        new LinkedHashSet<>(Arrays.asList(sparkProviderPlugin, httpProviderPlugin)),
                        javaServiceGenerationService);

      command.addConfigurationPlugin(sparkProviderPlugin);
      command.addConfigurationPlugin(httpProviderPlugin);
      command.addConfigurationPlugin(configurationPlugin);
      command.addConfigurationPlugin(transportPlugin);

   }

   @Test
   public void restTelemetry() throws Throwable {

      telemetryService.addRestTelemetryConfiguration(model, "localhost", "0.0.0.0", 52412,
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
      Path configurationFile = srcDir.resolve("TrackPriorityServiceTestTransportConfiguration.java");
      Path sparkFile = srcDir.resolve("TrackPriorityServiceSparkConfiguration.java");
      Path httpFile = srcDir.resolve("TrackPriorityServiceHttpClientConfiguration.java");
      Path telemetryFile = srcDir.resolve("TrackPriorityServiceTelemetryConfiguration.java");

      assertTrue(Files.isRegularFile(buildFile));
      assertTrue(Files.isRegularFile(configurationFile));
      assertFalse(Files.isRegularFile(telemetryFile));
      assertTrue(Files.isRegularFile(httpFile));
      assertFalse(Files.isRegularFile(sparkFile));
   }

}
