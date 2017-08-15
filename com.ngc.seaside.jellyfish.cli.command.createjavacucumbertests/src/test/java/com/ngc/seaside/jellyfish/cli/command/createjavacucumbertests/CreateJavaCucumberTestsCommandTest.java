package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests;

import com.ngc.seaside.bootstrap.service.impl.templateservice.TemplateService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.bootstrap.service.template.api.TemplateServiceException;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.basic.SystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class CreateJavaCucumberTestsCommandTest {

   private CreateJavaCucumberTestsCommand fixture;
   private IPromptUserService promptUserService = mock(IPromptUserService.class);
   private ISystemDescriptor systemDescriptor = mock(SystemDescriptor.class);
   private ITemplateService templateService = mock(TemplateService.class);
   private IModel model = mock(Model.class);

   @Before
   public void setup() {
      cmd.setLogService(logger);
   }

   @Test
   public void testCommand() {
      // TODO Auto-generated method stub
   }
   
}
