package com.ngc.seaside.bootstrap.impl.provider;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.IBootstrapCommandOptions;
import com.ngc.seaside.bootstrap.command.impl.createjavabundle.CreateJavaBundle;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.template.api.DefaultTemplateOutput;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateOutput;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.ICommandOptions;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

      DefaultParameterCollection collection = new DefaultParameterCollection();
      collection.addParameter(new DefaultParameter("outputDir").setValue("//does//not//matter//"));
      when(parameterService.parseParameters(Arrays.asList("-DoutputDir=//does//not//matter//")))
               .thenReturn(collection);
      when(templateService.templateExists(CreateJavaBundle.class.getPackage().getName()))
               .thenReturn(true);
      when(parameterService.parseParameters(anyMap())).thenReturn(new DefaultParameterCollection());

      ITemplateOutput output = new DefaultTemplateOutput().setOutputPath(Paths.get("."))
               .setProperties(new HashMap<>());
      when(templateService.unpack(CreateJavaBundle.class.getPackage().getName(), collection, Paths.get("//does//not//matter//"), false))
               .thenReturn(output);

      fixture.addCommand(command);
      fixture.run(new String[]{"create-java-bundle", "-DoutputDir=//does//not//matter//"});

      IBootstrapCommandOptions options = command.getCommandOptions();
      assertNotNull("The options must not be null", options);

      assertTrue(options.getParameters().containsParameter("outputDir"));
      assertTrue(options.getParameters().containsParameter("templateFinalOutputDir"));

      assertEquals("//does//not//matter//", options.getParameters().getParameter("outputDir").getValue());
      assertEquals(".", options.getParameters().getParameter("templateFinalOutputDir").getValue());
   }



}
