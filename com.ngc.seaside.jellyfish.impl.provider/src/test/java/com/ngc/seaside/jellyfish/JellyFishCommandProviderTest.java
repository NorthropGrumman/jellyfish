package com.ngc.seaside.jellyfish;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.template.api.DefaultTemplateOutput;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateOutput;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.JellyFishCommandConfiguration;
import com.ngc.seaside.jellyfish.impl.provider.JellyFishCommandProvider;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 */
public class JellyFishCommandProviderTest {

   private final static String TEMPLATE_PACKAGE_NAME = "com.ngc.seaside.jellyfish.command.impl.createjavabundle";

   private ILogService logService;
   private ITemplateService templateService;
   private IParameterService parameterService;
   private ISystemDescriptorService systemDescriptorService;

   private JellyFishCommandProvider fixture;

   @Before
   public void setup() {
      logService = new PrintStreamLogService();
      templateService = mock(ITemplateService.class);
      parameterService = mock(IParameterService.class);
      systemDescriptorService = mock(ISystemDescriptorService.class);

      fixture = new JellyFishCommandProvider() {
         /**
          * The Bootstrap command provider uses the package name from the class in order to look up the templateContent.
          * Therefore, it is necessary to return our own value since we can't mock the final method getClass
          */
         @Override
         protected String getCommandTemplatePrefix(IJellyFishCommand command) {
            return TEMPLATE_PACKAGE_NAME;
         }
      };

      fixture.setLogService(logService);
      fixture.setTemplateService(templateService);
      fixture.setParameterService(parameterService);
      fixture.setSystemDescriptorService(systemDescriptorService);

      fixture.activate();
   }

   @After
   public void after() {
      fixture.deactivate();
      fixture.removeLogService(logService);
      fixture.removeParameterService(parameterService);
      fixture.removeSystemDescriptorService(systemDescriptorService);
      fixture.removeTemplateService(templateService);
   }

   @Test
   public void testGetCommand() {
      IJellyFishCommand command = mock(IJellyFishCommand.class);
      final String name = "create-java-bundle";
      when(command.getName()).thenReturn(name);

      fixture.addCommand(command);

      IJellyFishCommand createJavaBundleCommand = fixture.getCommand(name);

      assertNotNull(createJavaBundleCommand);
      assertEquals(name, createJavaBundleCommand.getName());

      fixture.removeCommand(command);

      assertNull(fixture.getCommand(name));
   }

   @Test
   public void testRun() {
      IJellyFishCommand command = mock(IJellyFishCommand.class);
      when(command.getName()).thenReturn("create-java-bundle");

      DefaultParameterCollection collection = new DefaultParameterCollection();
      collection.addParameter(new DefaultParameter<>("outputDir", "//does//not//matter//"));
      when(parameterService.parseParameters(Collections.singletonList("-DoutputDir=//does//not//matter//")))
            .thenReturn(collection);
      when(parameterService.parseParameters(anyMap())).thenReturn(new DefaultParameterCollection());

      when(templateService.templateExists(TEMPLATE_PACKAGE_NAME))
            .thenReturn(true);
      ITemplateOutput output = new DefaultTemplateOutput().setOutputPath(Paths.get("."))
            .setProperties(new HashMap<>());
      when(templateService.unpack(TEMPLATE_PACKAGE_NAME,
                                  collection,
                                  Paths.get("//does//not//matter//"), false)).thenReturn(output);

      //we aren't testing the system descriptor service, just that it actually gets called
      IParsingResult result = mock(IParsingResult.class);
      when(result.isSuccessful()).thenReturn(true);
      when(result.getSystemDescriptor()).thenReturn(null);
      when(systemDescriptorService.parseProject(any())).thenReturn(result);

      fixture.addCommand(command);
      fixture.run(new String[]{"create-java-bundle", "-DoutputDir=//does//not//matter//"});

      ArgumentCaptor<IJellyFishCommandOptions> optionsCapture = ArgumentCaptor.forClass(IJellyFishCommandOptions.class);
      verify(command).run(optionsCapture.capture());

      IJellyFishCommandOptions options = optionsCapture.getValue();

      assertNotNull("The options must not be null", options);

      //we set it to null above, ensure it really is null
      assertEquals(null, options.getSystemDescriptor());
      assertTrue(options.getParameters().containsParameter("outputDirectory"));
      assertTrue(options.getParameters().containsParameter("templateFinalOutputDirectory"));
   }

   @Test
   public void testDoesRunWithoutInvokingTemplateService() throws Throwable {
      IJellyFishCommand command = mock(NoTemplateJfCommand.class);
      when(command.getName()).thenReturn("create-java-bundle");

      DefaultParameterCollection collection = new DefaultParameterCollection();
      collection.addParameter(new DefaultParameter<>("outputDir", "//does//not//matter//"));
      when(parameterService.parseParameters(Collections.singletonList("-DoutputDir=//does//not//matter//")))
            .thenReturn(collection);
      when(parameterService.parseParameters(anyMap())).thenReturn(new DefaultParameterCollection());

      //we aren't testing the system descriptor service, just that it actually gets called
      IParsingResult result = mock(IParsingResult.class);
      when(result.isSuccessful()).thenReturn(true);
      when(result.getSystemDescriptor()).thenReturn(null);
      when(systemDescriptorService.parseProject(any())).thenReturn(result);

      fixture.addCommand(command);
      fixture.run(new String[]{"create-java-bundle", "-DoutputDir=//does//not//matter//"});

      ArgumentCaptor<IJellyFishCommandOptions> optionsCapture = ArgumentCaptor.forClass(IJellyFishCommandOptions.class);
      verify(command).run(optionsCapture.capture());

      IJellyFishCommandOptions options = optionsCapture.getValue();

      assertNotNull("The options must not be null", options);

      //we set it to null above, ensure it really is null
      assertEquals(null, options.getSystemDescriptor());
      assertTrue(options.getParameters().containsParameter("outputDir"));

      verify(templateService, never()).templateExists(anyString());
      verify(templateService, never()).unpack(anyString(),
                                              any(IParameterCollection.class),
                                              any(Path.class),
                                              anyBoolean());
   }

   @JellyFishCommandConfiguration(autoTemplateProcessing = false)
   public static interface NoTemplateJfCommand extends IJellyFishCommand {

   }
}
