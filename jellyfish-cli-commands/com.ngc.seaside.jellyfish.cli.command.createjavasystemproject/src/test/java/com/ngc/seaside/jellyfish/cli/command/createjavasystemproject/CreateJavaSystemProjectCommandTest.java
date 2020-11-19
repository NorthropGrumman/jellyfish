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
package com.ngc.seaside.jellyfish.cli.command.createjavasystemproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;
import java.util.Optional;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class CreateJavaSystemProjectCommandTest {

   private CreateJavaSystemProjectCommand command;

   @Mock
   private IJellyFishCommandOptions options;

   @Mock
   private ILogService logService;

   @Mock
   private IJellyFishCommandProvider commandProvider;

   @Mock
   private ISystemDescriptor systemDescriptor;

   @Mock
   private IModel model;

   @Rule
   public TemporaryFolder outputDirectory = new TemporaryFolder();

   private String outputDirectoryName;

   private DefaultParameterCollection parameters;

   @Before
   public void setup() {
      when(options.getSystemDescriptor()).thenReturn(systemDescriptor);

      IPackage packagez = mock(IPackage.class);
      when(packagez.getName()).thenReturn("example.of");

      when(model.getName()).thenReturn("MyModel");
      when(model.getFullyQualifiedName()).thenReturn("example.of.MyModel");
      when(model.getParent()).thenReturn(packagez);
      when(systemDescriptor.findModel("example.of.MyModel")).thenReturn(Optional.of(model));

      parameters = new DefaultParameterCollection();
      when(options.getParameters()).thenReturn(parameters);

      outputDirectoryName = outputDirectory.getRoot().getAbsolutePath();

      command = new CreateJavaSystemProjectCommand();
      command.setLogService(logService);
      command.setJellyFishCommandProvider(commandProvider);
      command.activate();
   }

   @Test
   public void testDoesCreateProjectWithSuppliedParameters() {

      parameters.addParameter(new DefaultParameter<>(CreateJavaSystemProjectCommand.MODEL_PROPERTY,
                                                     model.getFullyQualifiedName()));
      parameters.addParameter(new DefaultParameter<>(CreateJavaSystemProjectCommand.OUTPUT_DIRECTORY_PROPERTY,
                                                     outputDirectoryName));
      parameters.addParameter(new DefaultParameter<>(CreateJavaSystemProjectCommand.GROUP_ID_PROPERTY,
                                                     "my.group"));
      parameters.addParameter(new DefaultParameter<>(CreateJavaSystemProjectCommand.PROJECT_NAME_PROPERTY,
                                                     "my-project"));

      command.run(options);

      ArgumentCaptor<IJellyFishCommandOptions> capture = ArgumentCaptor.forClass(IJellyFishCommandOptions.class);
      verify(commandProvider).run(eq(CreateJavaSystemProjectCommand.CREATE_JELLYFISH_GRADLE_PROJECT_COMMAND_NAME),
                                  capture.capture());
      verifyParametersForCreateJellyFishGradleProjectCommand(capture.getValue(), "my.group", "my-project");

      verify(commandProvider).run(eq(CreateJavaSystemProjectCommand.CREATE_JAVA_CUCUMBER_TESTS_COMMAND_NAME),
                                  capture.capture());
      verifyParametersForCreateCucumberTestsCommand(capture.getValue(), "my-project");

      verify(commandProvider).run(eq(CreateJavaSystemProjectCommand.CREATE_JAVA_DISTRIBUTION_COMMAND_NAME),
                                  capture.capture());
      verifyParametersForCreateDistributionCommand(capture.getValue(), "my-project");

      verify(commandProvider).run(eq(CreateJavaSystemProjectCommand.CREATE_JAVA_SERVICE_BASE_COMMAND_NAME),
                                  capture.capture());
      verifyParametersForCreateServiceBaseCommand(capture.getValue(), "my-project");

      verify(commandProvider).run(eq(CreateJavaSystemProjectCommand.CREATE_PROTOCOLBUFFER_MESSAGES_COMMAND_NAME),
                                  capture.capture());
      verifyParametersForCreatePbMessagesCommand(capture.getValue(), "my-project");

   }


   private void verifyParametersForCreateJellyFishGradleProjectCommand(IJellyFishCommandOptions options,
                                                                       String groupId,
                                                                       String projectName) {
      requireParameter(options, CreateJavaSystemProjectCommand.GROUP_ID_PROPERTY, groupId);
      requireParameter(options, CreateJavaSystemProjectCommand.PROJECT_NAME_PROPERTY, projectName);
      requireParameter(options, CreateJavaSystemProjectCommand.OUTPUT_DIRECTORY_PROPERTY, outputDirectoryName);
      requireParameter(options, CreateJavaSystemProjectCommand.MODEL_PROPERTY, model.getFullyQualifiedName());
   }

   private void verifyParametersForCreateCucumberTestsCommand(IJellyFishCommandOptions options,
                                                              String projectName) {
      requireParameter(options,
                       CreateJavaSystemProjectCommand.OUTPUT_DIRECTORY_PROPERTY,
                       Paths.get(outputDirectoryName, projectName).toAbsolutePath().toString());
   }

   private void verifyParametersForCreateDistributionCommand(IJellyFishCommandOptions options,
                                                             String projectName) {
      requireParameter(options,
                       CreateJavaSystemProjectCommand.OUTPUT_DIRECTORY_PROPERTY,
                       Paths.get(outputDirectoryName, projectName).toAbsolutePath().toString());
   }

   private void verifyParametersForCreateServiceBaseCommand(IJellyFishCommandOptions options,
                                                            String projectName) {
      requireParameter(options,
                       CreateJavaSystemProjectCommand.OUTPUT_DIRECTORY_PROPERTY,
                       Paths.get(outputDirectoryName, projectName).toAbsolutePath().toString());
   }

   private void verifyParametersForCreatePbMessagesCommand(IJellyFishCommandOptions options,
                                                           String projectName) {
      requireParameter(options,
                       CreateJavaSystemProjectCommand.OUTPUT_DIRECTORY_PROPERTY,
                       Paths.get(outputDirectoryName, projectName).toAbsolutePath().toString());
   }

   private static void requireParameter(IJellyFishCommandOptions options, String parameter, Object value) {
      assertTrue("missing parameter " + parameter,
                 options.getParameters().containsParameter(parameter));
      assertEquals("value of parameter " + parameter + " incorrect!",
                   value,
                   options.getParameters().getParameter(parameter).getValue());
   }
}
