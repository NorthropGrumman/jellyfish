package com.ngc.seaside.bootstrap.impl.provider;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.api.IBootstrapCommand;
import com.ngc.seaside.bootstrap.api.IBootstrapCommandOptions;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.template.api.DefaultTemplateOutput;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateOutput;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 */
public class BootstrapCommandProviderTest {

   private final static String TEMPLATE_PACKAGE_NAME = "com.ngc.seaside.bootstrap.command.impl.createjavabundle";

   private ILogService logService;
   private ITemplateService templateService;
   private IParameterService parameterService;

   private BootstrapCommandProvider fixture;

   @Before
   public void setup() {
      logService = new PrintStreamLogService();
      templateService = mock(ITemplateService.class);
      parameterService = mock(IParameterService.class);

      fixture = new BootstrapCommandProvider() {
         /**
          * The Bootstrap command provider uses the package name from the class in order to look up the templateContent.
          * Therefore, it is necessary to return our own value since we can't mock the final method getClass
          */
         @Override
         protected String getCommandTemplatePrefix(IBootstrapCommand command) {
            return TEMPLATE_PACKAGE_NAME;
         }
      };
      fixture.setLogService(logService);
      fixture.setTemplateService(templateService);
      fixture.setParameterService(parameterService);
      fixture.activate();
   }

   @Test
   public void testRun() {

      IBootstrapCommand command = mock(IBootstrapCommand.class);
      when(command.getName()).thenReturn("create-java-bundle");

      DefaultParameterCollection collection = new DefaultParameterCollection();
      collection.addParameter(new DefaultParameter("outputDir").setValue("//does//not//matter//"));
      when(parameterService.parseParameters(Arrays.asList("-DoutputDir=//does//not//matter//")))
               .thenReturn(collection);
      when(parameterService.parseParameters(anyMap())).thenReturn(new DefaultParameterCollection());

      when(templateService.templateExists(TEMPLATE_PACKAGE_NAME))
               .thenReturn(true);
      ITemplateOutput output = new DefaultTemplateOutput().setOutputPath(Paths.get("."))
               .setProperties(new HashMap<>());
      when(templateService.unpack(TEMPLATE_PACKAGE_NAME,
                                  collection,
                                  Paths.get("//does//not//matter//"), false)).thenReturn(output);

      fixture.addCommand(command);
      fixture.run(new String[] { "create-java-bundle", "-DoutputDir=//does//not//matter//" });

      ArgumentCaptor<IBootstrapCommandOptions> optionsCapture = ArgumentCaptor.forClass(IBootstrapCommandOptions.class);
      verify(command).run(optionsCapture.capture());

      IBootstrapCommandOptions options = optionsCapture.getValue();

      assertNotNull("The options must not be null", options);

      assertTrue(options.getParameters().containsParameter("outputDirectory"));
      assertTrue(options.getParameters().containsParameter("templateFinalOutputDirectory"));
   }

}
