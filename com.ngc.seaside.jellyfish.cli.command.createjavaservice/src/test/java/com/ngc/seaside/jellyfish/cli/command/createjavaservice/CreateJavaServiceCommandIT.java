package com.ngc.seaside.jellyfish.cli.command.createjavaservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dao.ITemplateDaoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dao.TemplateDaoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dao.TemplateDaoFactoryTest;
import com.ngc.seaside.jellyfish.cli.command.test.template.MockedTemplateService;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateJavaServiceCommandIT {

   private CreateJavaServiceCommand command;

   private MockedTemplateService templateService;

   private ITemplateDaoFactory templateDaoFactory;

   private DefaultParameterCollection parameters;

   private String outputDirectoryName;

   @Mock
   private IJellyFishCommandOptions jellyFishCommandOptions;

   @Mock
   private ILogService logService;

   @Mock
   private IPromptUserService promptUserService;

   @Before
   public void setup() throws Throwable {
      templateService = new MockedTemplateService()
            .useRealPropertyService()
            .setTemplateDirectory(CreateJavaServiceCommand.class.getPackage().getName(),
                                  Paths.get("src", "main", "template"));

      templateDaoFactory = new TemplateDaoFactory();

      ISystemDescriptor systemDescriptor = mock(ISystemDescriptor.class);
      when(systemDescriptor.findModel("com.ngc.seaside.threateval.EngagementTrackPriorityService"))
            .thenReturn(Optional.of(TemplateDaoFactoryTest.newModelForTesting()));

      parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>(CreateJavaServiceCommand.MODEL_PROPERTY,
                                                     "com.ngc.seaside.threateval.EngagementTrackPriorityService"));
      parameters.addParameter(new DefaultParameter<>(CreateJavaServiceCommand.OUTPUT_DIRECTORY_PROPERTY,
                                                     "build/tests/CreateJavaServiceCommandIT"));

      when(jellyFishCommandOptions.getParameters()).thenReturn(parameters);
      when(jellyFishCommandOptions.getSystemDescriptor()).thenReturn(systemDescriptor);

      command = new CreateJavaServiceCommand();
      command.setLogService(logService);
      command.setPromptService(promptUserService);
      command.setTemplateDaoFactory(templateDaoFactory);
      command.setTemplateService(templateService);
   }

   @Test
   public void testDoesGenerateServiceWithSuppliedCommands() throws Throwable {
      command.run(jellyFishCommandOptions);
   }

   @Test
   public void testDoesGenerateServiceWithNoCommands() throws Throwable {
   }
}
