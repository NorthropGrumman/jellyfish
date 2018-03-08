package com.ngc.seaside.jellyfish.cli.command.createjavadistribution;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedBuildManagementService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateOutput;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

public class CreateJavaDistributionCommandTest {

   private CreateJavaDistributionCommand fixture;
   private IJellyFishCommandOptions options = mock(IJellyFishCommandOptions.class);
   private ISystemDescriptor systemDescriptor = mock(ISystemDescriptor.class);
   private ITemplateService templateService = mock(ITemplateService.class);
   private IModel model = mock(IModel.class);
   private Path createDirectoriesPath;

   @Before
   public void setup() throws IOException {
      

      // Setup mock system descriptor
      when(options.getSystemDescriptor()).thenReturn(systemDescriptor);
      when(options.getParameters()).thenReturn(mock(IParameterCollection.class));
      when(systemDescriptor.findModel("com.ngc.seaside.test.Model")).thenReturn(Optional.of(model));
      
      //Setup mock project naming service
      IProjectNamingService projectNamingService = mock(IProjectNamingService.class);
      IProjectInformation distributionProjName = mock(IProjectInformation.class);
      IProjectInformation eventsProjName = mock(IProjectInformation.class);
      IProjectInformation domainProjName = mock(IProjectInformation.class);
      IProjectInformation connectorProjName = mock(IProjectInformation.class);
      IProjectInformation configProjName = mock(IProjectInformation.class);
      IProjectInformation baseServiceProjName = mock(IProjectInformation.class);
      IProjectInformation serviceProjName = mock(IProjectInformation.class);
      IProjectInformation messagesProjName = mock(IProjectInformation.class);
      when (projectNamingService.getDistributionProjectName(any(), any())).thenReturn(distributionProjName);
      when (projectNamingService.getEventsProjectName(any(), any())).thenReturn(eventsProjName);
      when (projectNamingService.getDomainProjectName(any(), any())).thenReturn(domainProjName);
      when (projectNamingService.getConnectorProjectName(any(), any())).thenReturn(connectorProjName);
      when (projectNamingService.getConfigProjectName(any(), any())).thenReturn(configProjName);
      when (projectNamingService.getBaseServiceProjectName(any(), any())).thenReturn(baseServiceProjName);
      when (projectNamingService.getServiceProjectName(any(), any())).thenReturn(serviceProjName);
      when (projectNamingService.getMessageProjectName(any(), any())).thenReturn(messagesProjName);
      
      when (distributionProjName.getArtifactId()).thenReturn("model.distribution");
      when (distributionProjName.getGroupId()).thenReturn("com.ngc.seaside.test");
      
      when (eventsProjName.getArtifactId()).thenReturn("model.events");
      when (domainProjName.getArtifactId()).thenReturn("model.domain");
      when (connectorProjName.getArtifactId()).thenReturn("model.connector");
      when (configProjName.getArtifactId()).thenReturn("model.config");
      when (baseServiceProjName.getArtifactId()).thenReturn("model.base");
      when (serviceProjName.getArtifactId()).thenReturn("model.impl");
      when (messagesProjName.getArtifactId()).thenReturn("model.messages");
      
      
      //set up mock package naming service
      IPackageNamingService packageNamingService = mock(IPackageNamingService.class);
      when (packageNamingService.getDistributionPackageName(any(), any())).thenReturn("com.ngc.seaside.test");
      

      // Setup mock model
      when(model.getParent()).thenReturn(mock(IPackage.class));
      when(model.getParent().getName()).thenReturn("com.ngc.seaside.test");
      when(model.getName()).thenReturn("Model");

      // Setup class under test
      fixture = new CreateJavaDistributionCommand() {
         @Override
         protected void doCreateDirectories(Path outputDirectory) {
            createDirectoriesPath = outputDirectory;
         }
      };

      fixture.setLogService(mock(ILogService.class));
      fixture.setTemplateService(templateService);
      fixture.setProjectNamingService(projectNamingService);
      fixture.setPackageNamingService(packageNamingService);
      fixture.setBuildManagementService(new MockedBuildManagementService());
   }

   @Test
   public void testCommandWithoutOptionalParams() {
      runCommand(CreateJavaDistributionCommand.MODEL_PROPERTY, "com.ngc.seaside.test.Model",
                 CreateJavaDistributionCommand.OUTPUT_DIRECTORY_PROPERTY, "/just/a/mock/path");

      // Verify mocked behaviors
      verify(options, times(1)).getSystemDescriptor();
      verify(model, times(1)).getName();
      verify(model, times(1)).getParent();

      // Verify passed values
      Assert.assertEquals(Paths.get("/just/a/mock/path").toAbsolutePath().toString(),
                          createDirectoriesPath.toAbsolutePath().toString());
   }

   @Test
   public void testCommandWithOptionalParams() {
      runCommand(CreateJavaDistributionCommand.MODEL_PROPERTY, "com.ngc.seaside.test.Model",
                 CreateJavaDistributionCommand.OUTPUT_DIRECTORY_PROPERTY, "/just/a/mock/path",
                 CreateJavaDistributionCommand.ARTIFACT_ID_PROPERTY, "model",
                 CreateJavaDistributionCommand.GROUP_ID_PROPERTY, "com.ngc.seaside.test");

      // Verify mocked behaviors
      verify(options, times(1)).getSystemDescriptor();
      verify(model, times(1)).getName();
      verify(model, times(1)).getParent();

      // Verify passed values
      Assert.assertEquals(Paths.get("/just/a/mock/path").toAbsolutePath().toString(),
                          createDirectoriesPath.toAbsolutePath().toString());
   }

   private void runCommand(String... keyValues) {
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n + 1 < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter<String>(keyValues[n]).setValue(keyValues[n + 1]));
      }

      when(options.getParameters()).thenReturn(collection);

      // Setup mock template service
      when(templateService.unpack(CreateJavaDistributionCommand.class.getPackage().getName(), collection,
                                  Paths.get("/just/a/mock/path"), false)).thenReturn(new ITemplateOutput() {
         @Override
         public Map<String, ?> getProperties() {
            return collection.getParameterMap();
         }

         @Override
         public Path getOutputPath() {
            return Paths.get("/just/a/mock/path");
         }
      });
      fixture.run(options);
   }
}
