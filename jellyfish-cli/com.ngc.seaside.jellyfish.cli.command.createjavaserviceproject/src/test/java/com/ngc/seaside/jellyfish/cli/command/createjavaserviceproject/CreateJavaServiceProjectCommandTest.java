package com.ngc.seaside.jellyfish.cli.command.createjavaserviceproject;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IParameter;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateJavaServiceProjectCommandTest {

   private CreateJavaServiceProjectCommand command;

   @Mock
   private IJellyFishCommandOptions options;

   @Mock
   private ILogService logService;

   @Mock
   private ITemplateService templateService;

   @Mock
   private IProjectNamingService projectNamingService;

   @Mock
   private IJellyFishCommandProvider commandProvider;

   @Mock
   private ISystemDescriptor systemDescriptor;

   @Mock
   private IModel model;

   @Mock
   private IProjectInformation projectInformation;

   @Rule
   public TemporaryFolder outputDirectory = new TemporaryFolder();

   private String outputDirectoryName;

   private DefaultParameterCollection parameters;

   @Before
   public void setup() throws Throwable {
      when(projectInformation.getDirectoryName()).thenReturn("projectDirectory");
      when(projectNamingService.getBaseServiceProjectName(any(IJellyFishCommandOptions.class), eq(model)))
            .thenReturn(projectInformation);
      when(projectNamingService.getEventsProjectName(any(IJellyFishCommandOptions.class), eq(model)))
            .thenReturn(projectInformation);
      when(projectNamingService.getConnectorProjectName(any(IJellyFishCommandOptions.class), eq(model)))
            .thenReturn(projectInformation);
      when(projectNamingService.getMessageProjectName(any(IJellyFishCommandOptions.class), eq(model)))
            .thenReturn(projectInformation);

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

      command = new CreateJavaServiceProjectCommand();
      command.setLogService(logService);
      command.setJellyFishCommandProvider(commandProvider);
      command.setTemplateService(templateService);
      command.setProjectNamingService(projectNamingService);
      command.activate();
   }

   @Test
   public void testDoesCreateProjectWithSuppliedParameters() throws Throwable {

      parameters.addParameter(new DefaultParameter<>(CreateJavaServiceProjectCommand.MODEL_PROPERTY,
                                                     model.getFullyQualifiedName()));
      parameters.addParameter(new DefaultParameter<>(CreateJavaServiceProjectCommand.OUTPUT_DIRECTORY_PROPERTY,
                                                     outputDirectoryName));
      parameters.addParameter(new DefaultParameter<>(CreateJavaServiceProjectCommand.GROUP_ID_PROPERTY,
                                                     "my.group"));
      parameters.addParameter(new DefaultParameter<>(CreateJavaServiceProjectCommand.PROJECT_NAME_PROPERTY,
                                                     "my-project"));
      //TODO remove or refactor
//      parameters.addParameter(new DefaultParameter<>(CreateJavaServiceProjectCommand.REPOSITORY_URL, 
//    		  "http://10.207.42.137/nexus/repository/maven-public/"));
//      parameters.addParameter(new DefaultParameter<>(CreateJavaServiceProjectCommand.GROUP_ARTIFACT_VERSION_EXTENSION, 
//    		  "com.ngc.seaside. threateval:threatevaluation.descriptor:2.0.0"));

      command.run(options);

      ArgumentCaptor<IJellyFishCommandOptions> capture = ArgumentCaptor.forClass(IJellyFishCommandOptions.class);
      verify(commandProvider).run(eq(CreateJavaServiceProjectCommand.CREATE_JELLYFISH_GRADLE_PROJECT_COMMAND_NAME),
                                  capture.capture());
      verifyParametersForCreateJellyFishGradleProjectCommand(capture.getValue(), "my.group", "my-project");

      verify(commandProvider).run(eq(CreateJavaServiceProjectCommand.CREATE_DOMAIN_COMMAND_NAME),
                                  capture.capture());
      verifyParametersForCreateDomainCommand(capture.getValue(), "my-project");

      verify(commandProvider).run(eq(CreateJavaServiceProjectCommand.CREATE_JAVA_CUCUMBER_TESTS_COMMAND_NAME),
                                  capture.capture());
      verifyParametersForCreateCucumberTestsCommand(capture.getValue(), "my-project");

      verify(commandProvider).run(eq(CreateJavaServiceProjectCommand.CREATE_JAVA_DISTRIBUTION_COMMAND_NAME),
                                  capture.capture());
      verifyParametersForCreateDistributionCommand(capture.getValue(), "my-project");

      verify(commandProvider).run(eq(CreateJavaServiceProjectCommand.CREATE_JAVA_SERVICE_COMMAND_NAME),
                                  capture.capture());
      verifyParametersForCreateServiceCommand(capture.getValue(), "my-project");

      verify(commandProvider).run(eq(CreateJavaServiceProjectCommand.CREATE_JAVA_SERVICE_CONFIG_COMMAND_NAME),
                                  capture.capture());
      verifyParametersForCreateServiceConfigCommand(capture.getValue(), "my-project");

      verify(templateService).unpack(
            eq(CreateJavaServiceProjectCommand.class.getPackage().getName()),
            withParameter(CreateJavaServiceProjectCommand.GRADLE_JELLYFISH_COMMAND_PARAMETER_NAME,
                          CreateJavaServiceProjectCommand.CREATE_JAVA_SERVICE_BASE_COMMAND_NAME),
            any(Path.class),
            eq(false));

      verify(templateService).unpack(
            eq(CreateJavaServiceProjectCommand.class.getPackage().getName()),
            withParameter(CreateJavaServiceProjectCommand.GRADLE_JELLYFISH_COMMAND_PARAMETER_NAME,
                          CreateJavaServiceProjectCommand.CREATE_JAVA_EVENTS_COMMAND_NAME),
            any(Path.class),
            eq(false));

      verify(templateService).unpack(
            eq(CreateJavaServiceProjectCommand.class.getPackage().getName()),
            withParameter(CreateJavaServiceProjectCommand.GRADLE_JELLYFISH_COMMAND_PARAMETER_NAME,
                          CreateJavaServiceProjectCommand.CREATE_PROTOCOLBUFFER_MESSAGES_COMMAND_NAME),
            any(Path.class),
            eq(false));

      verify(templateService).unpack(
            eq(CreateJavaServiceProjectCommand.class.getPackage().getName()),
            withParameter(CreateJavaServiceProjectCommand.GRADLE_JELLYFISH_COMMAND_PARAMETER_NAME,
                          CreateJavaServiceProjectCommand.CREATE_JAVA_PUBSUB_CONNECTOR_COMMAND_NAME),
            any(Path.class),
            eq(false));
   }


   @Test
   public void testWithoutDomain() throws Throwable {
      String modelName = model.getFullyQualifiedName();

      parameters.addParameter(
            new DefaultParameter<>(CreateJavaServiceProjectCommand.OUTPUT_DIRECTORY_PROPERTY, outputDirectoryName));
      parameters.addParameter(new DefaultParameter<>(CreateJavaServiceProjectCommand.MODEL_PROPERTY, modelName));
      parameters.addParameter(new DefaultParameter<>(CreateJavaServiceProjectCommand.CREATE_SERVICE_DOMAIN_PROPERTY,
                                                     "false"));

      command.run(options);

      ArgumentCaptor<IJellyFishCommandOptions> capture = ArgumentCaptor.forClass(IJellyFishCommandOptions.class);

      verify(commandProvider, never()).run(eq(CreateJavaServiceProjectCommand.CREATE_DOMAIN_COMMAND_NAME),
                                           capture.capture());
   }

   private void verifyParametersForCreateJellyFishGradleProjectCommand(IJellyFishCommandOptions options,
                                                                       String groupId,
                                                                       String projectName) {
      requireParameter(options, CreateJavaServiceProjectCommand.GROUP_ID_PROPERTY, groupId);
      requireParameter(options, CreateJavaServiceProjectCommand.PROJECT_NAME_PROPERTY, projectName);
      requireParameter(options, CreateJavaServiceProjectCommand.OUTPUT_DIRECTORY_PROPERTY, outputDirectoryName);
      requireParameter(options, CreateJavaServiceProjectCommand.MODEL_PROPERTY, model.getFullyQualifiedName());
   }

   private void verifyParametersForCreateDomainCommand(IJellyFishCommandOptions options,
                                                       String projectName) {
      requireParameter(options,
                       CreateJavaServiceProjectCommand.OUTPUT_DIRECTORY_PROPERTY,
                       Paths.get(outputDirectoryName, projectName).toAbsolutePath().toString());
   }

   private void verifyParametersForCreateCucumberTestsCommand(IJellyFishCommandOptions options,
                                                              String projectName) {
      requireParameter(options,
                       CreateJavaServiceProjectCommand.OUTPUT_DIRECTORY_PROPERTY,
                       Paths.get(outputDirectoryName, projectName).toAbsolutePath().toString());
   }

   private void verifyParametersForCreateDistributionCommand(IJellyFishCommandOptions options,
                                                             String projectName) {
      requireParameter(options,
                       CreateJavaServiceProjectCommand.OUTPUT_DIRECTORY_PROPERTY,
                       Paths.get(outputDirectoryName, projectName).toAbsolutePath().toString());
   }

   private void verifyParametersForCreateServiceCommand(IJellyFishCommandOptions options,
                                                        String projectName) {
      requireParameter(options,
                       CreateJavaServiceProjectCommand.OUTPUT_DIRECTORY_PROPERTY,
                       Paths.get(outputDirectoryName, projectName).toAbsolutePath().toString());
   }

   private void verifyParametersForCreateServiceConfigCommand(IJellyFishCommandOptions options,
                                                              String projectName) {
      requireParameter(options,
                       CreateJavaServiceProjectCommand.OUTPUT_DIRECTORY_PROPERTY,
                       Paths.get(outputDirectoryName, projectName).toAbsolutePath().toString());
   }

   private static void requireParameter(IJellyFishCommandOptions options, String parameter, Object value) {
      assertTrue("missing parameter " + parameter,
                 options.getParameters().containsParameter(parameter));
      assertEquals("value of parameter " + parameter + " incorrect!",
                   value,
                   options.getParameters().getParameter(parameter).getValue());
   }

   private static IParameterCollection withParameter(final String name, final Object value) {
      return argThat(argument -> {
         IParameter<?> param = argument.getParameter(name);
         return param != null & Objects.equals(value, param.getValue());
      });
   }
}