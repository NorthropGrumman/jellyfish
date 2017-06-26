package com.ngc.seaside.bootstrap.impl.provider;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.command.impl.createjavabundle.CreateJavaBundle;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * @author justan.provence@ngc.com
 */
public class BootstrapCommandProviderTest {

   private ILogService logService;
   private ITemplateService templateService;
   private IParameterService parameterService;

   private BootstrapCommandProvider fixture;

   @Before
   public void setup() {
      logService = new PrintStreamLogService();
      templateService = mock(ITemplateService.class);
      parameterService = mock(IParameterService.class);

      fixture = new BootstrapCommandProvider();
      fixture.setLogService(logService);
      fixture.setTemplateService(templateService);
      fixture.setParameterService(parameterService);
      fixture.activate();
   }

   @Test
   public void testRun() {
      CreateJavaBundle command = new CreateJavaBundle();

      fixture.addCommand(command);
      fixture.run(new String[]{"create-java-bundle"});


   }



}
