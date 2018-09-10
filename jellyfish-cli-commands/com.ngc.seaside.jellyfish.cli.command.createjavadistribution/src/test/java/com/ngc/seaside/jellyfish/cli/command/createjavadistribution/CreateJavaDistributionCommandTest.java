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
package com.ngc.seaside.jellyfish.cli.command.createjavadistribution;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedBuildManagementService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedPackageNamingService;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedProjectNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateOutput;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
      IProjectNamingService projectNamingService = new MockedProjectNamingService();

      //set up mock package naming service
      IPackageNamingService packageNamingService = new MockedPackageNamingService();

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
      when(templateService.unpack(CreateJavaDistributionCommand.class.getPackage().getName(),
                                  collection,
                                  Paths.get("/just/a/mock/path"),
                                  false)).thenReturn(new ITemplateOutput() {
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
