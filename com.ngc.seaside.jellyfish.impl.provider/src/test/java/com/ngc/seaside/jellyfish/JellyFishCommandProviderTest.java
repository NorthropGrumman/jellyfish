package com.ngc.seaside.jellyfish;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.DefaultTemplateOutput;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateOutput;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;
import com.ngc.seaside.command.api.*;
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
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.*;
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

   private JellyFishCommandProvider provider;

   @Before
   public void setup() {
      logService = new PrintStreamLogService();
      templateService = mock(ITemplateService.class);
      parameterService = mock(IParameterService.class);
      systemDescriptorService = mock(ISystemDescriptorService.class);

      provider = new JellyFishCommandProvider() {
         /**
          * The Bootstrap command provider uses the package name from the class in order to look up the templateContent.
          * Therefore, it is necessary to return our own value since we can't mock the final method getClass
          */
         @Override
         protected String getCommandTemplatePrefix(IJellyFishCommand command) {
            return TEMPLATE_PACKAGE_NAME;
         }
      };

      provider.setLogService(logService);
      provider.setTemplateService(templateService);
      provider.setParameterService(parameterService);
      provider.setSystemDescriptorService(systemDescriptorService);

      provider.activate();
   }

   @After
   public void after() {
      provider.deactivate();
      provider.removeLogService(logService);
      provider.removeParameterService(parameterService);
      provider.removeSystemDescriptorService(systemDescriptorService);
      provider.removeTemplateService(templateService);
   }

   @Test
   public void testGetCommand() {
      IJellyFishCommand command = mock(IJellyFishCommand.class);
      final String name = "create-java-bundle";
      when(command.getName()).thenReturn(name);

      provider.addCommand(command);

      IJellyFishCommand createJavaBundleCommand = provider.getCommand(name);

      assertNotNull(createJavaBundleCommand);
      assertEquals(name, createJavaBundleCommand.getName());

      provider.removeCommand(command);

      assertNull(provider.getCommand(name));
   }

   @Test
   public void testRun() {
      IJellyFishCommand command = mock(IJellyFishCommand.class);
      when(command.getName()).thenReturn("create-java-bundle");
      when(command.getUsage()).thenReturn(mock(IUsage.class));
      when(command.getUsage().getRequiredParameters()).thenReturn(Collections.emptyList());
      Path outputDir = Paths.get(".");
      DefaultParameterCollection collection = new DefaultParameterCollection();
      collection.addParameter(new DefaultParameter<>("outputDir", outputDir));

      when(parameterService.parseParameters(Collections.singletonList("-DoutputDir=" + outputDir)))
            .thenReturn(collection);
      when(parameterService.parseParameters(anyMap())).thenReturn(new DefaultParameterCollection());

      when(templateService.templateExists(TEMPLATE_PACKAGE_NAME))
            .thenReturn(true);
      ITemplateOutput output = new DefaultTemplateOutput()
            .setOutputPath(outputDir)
            .setProperties(new HashMap<>());
      when(templateService.unpack(TEMPLATE_PACKAGE_NAME,
                                  collection,
                                  outputDir,
                                  false)).thenReturn(output);

      //we aren't testing the system descriptor service, just that it actually gets called
      IParsingResult result = mock(IParsingResult.class);
      when(result.isSuccessful()).thenReturn(true);
      when(result.getSystemDescriptor()).thenReturn(null);
      when(systemDescriptorService.parseProject(any())).thenReturn(result);

      provider.addCommand(command);
      provider.run(new String[]{"create-java-bundle", "-DoutputDir=" + outputDir});

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
      when(command.getUsage()).thenReturn(mock(IUsage.class));
      when(command.getUsage().getRequiredParameters()).thenReturn(Collections.emptyList());

      Path outputDir = Paths.get(".");
      DefaultParameterCollection collection = new DefaultParameterCollection();
      collection.addParameter(new DefaultParameter<>("outputDir", outputDir));
      when(parameterService.parseParameters(Collections.singletonList("-DoutputDir=" + outputDir)))
            .thenReturn(collection);
      when(parameterService.parseParameters(anyMap())).thenReturn(new DefaultParameterCollection());

      //we aren't testing the system descriptor service, just that it actually gets called
      IParsingResult result = mock(IParsingResult.class);
      when(result.isSuccessful()).thenReturn(true);
      when(result.getSystemDescriptor()).thenReturn(null);
      when(systemDescriptorService.parseProject(any())).thenReturn(result);

      provider.addCommand(command);
      provider.run(new String[]{"create-java-bundle", "-DoutputDir=" + outputDir});

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

   @Test
   public void testDoesNotRunCommandIfSystemDescriptorIsInvalid() {
      IJellyFishCommand command = mock(IJellyFishCommand.class);
      when(command.getName()).thenReturn("create-java-bundle");
      when(command.getUsage()).thenReturn(mock(IUsage.class));
      when(command.getUsage().getRequiredParameters()).thenReturn(Collections.emptyList());

      Path outputDir = Paths.get(".");
      DefaultParameterCollection collection = new DefaultParameterCollection();
      collection.addParameter(new DefaultParameter<>("outputDir", outputDir));

      when(parameterService.parseParameters(Collections.singletonList("-DoutputDir=" + outputDir)))
            .thenReturn(collection);
      when(parameterService.parseParameters(anyMap())).thenReturn(new DefaultParameterCollection());

      when(templateService.templateExists(TEMPLATE_PACKAGE_NAME))
            .thenReturn(true);
      ITemplateOutput output = new DefaultTemplateOutput()
            .setOutputPath(outputDir)
            .setProperties(new HashMap<>());
      when(templateService.unpack(TEMPLATE_PACKAGE_NAME,
                                  collection,
                                  outputDir,
                                  false)).thenReturn(output);

      //we aren't testing the system descriptor service, just that it actually gets called
      IParsingResult result = mock(IParsingResult.class);
      when(result.isSuccessful()).thenReturn(false);
      when(systemDescriptorService.parseProject(any())).thenReturn(result);

      provider.addCommand(command);
      try {
         provider.run(new String[]{"create-java-bundle", "-DoutputDir=" + outputDir});
         fail("did not throw CommandException if command requires valid SystemDescriptor");
      } catch (CommandException e) {
         // Expected.
      }
      verify(command, never()).run(any());
   }

   @Test
   public void testDoesRunCommandIfCommandAllowsForInvalidSystemDescriptor() {
      IJellyFishCommand command = mock(ToleratingInvalidSdJfCommand.class);
      when(command.getName()).thenReturn("create-java-bundle");
      when(command.getUsage()).thenReturn(mock(IUsage.class));
      when(command.getUsage().getRequiredParameters()).thenReturn(Collections.emptyList());

      Path outputDir = Paths.get(".");
      DefaultParameterCollection collection = new DefaultParameterCollection();
      collection.addParameter(new DefaultParameter<>("outputDir", outputDir));

      when(parameterService.parseParameters(Collections.singletonList("-DoutputDir=" + outputDir)))
            .thenReturn(collection);
      when(parameterService.parseParameters(anyMap())).thenReturn(new DefaultParameterCollection());

      when(templateService.templateExists(TEMPLATE_PACKAGE_NAME))
            .thenReturn(true);
      ITemplateOutput output = new DefaultTemplateOutput()
            .setOutputPath(outputDir)
            .setProperties(new HashMap<>());
      when(templateService.unpack(TEMPLATE_PACKAGE_NAME,
                                  collection,
                                  outputDir,
                                  false)).thenReturn(output);

      //we aren't testing the system descriptor service, just that it actually gets called
      IParsingResult result = mock(IParsingResult.class);
      when(result.isSuccessful()).thenReturn(false);
      when(result.getSystemDescriptor()).thenReturn(null);
      when(systemDescriptorService.parseProject(any())).thenReturn(result);

      provider.addCommand(command);
      provider.run(new String[]{"create-java-bundle", "-DoutputDir=" + outputDir});

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
   public void doesRunWithoutRequiredParams() {
      IJellyFishCommand command = mock(IJellyFishCommand.class);
      IPromptUserService promptService = mock(IPromptUserService.class);
      provider.setPromptService(promptService);

      final String TEST_PARAM_NAME = "outputDirectory";
      final String TEST_PARAM_VALUE = "testDir";

      when(promptService.prompt(eq(TEST_PARAM_NAME), any(), any())).thenReturn(TEST_PARAM_VALUE);

      when(command.getName()).thenReturn("create-java-bundle");
      when(command.getUsage()).thenReturn(mock(IUsage.class));
      when(command.getUsage().getRequiredParameters()).thenReturn(Collections.singletonList(new DefaultParameter<>(TEST_PARAM_NAME)));
      when(command.getUsage().getAllParameters()).thenReturn(Collections.singletonList(new DefaultParameter<>(TEST_PARAM_NAME)));

      when(parameterService.parseParameters(anyList())).thenReturn(new DefaultParameterCollection());
      when(parameterService.parseParameters(anyMap())).thenReturn(new DefaultParameterCollection());

      //we aren't testing the system descriptor service, just that it actually gets called
      IParsingResult result = mock(IParsingResult.class);
      when(result.isSuccessful()).thenReturn(true);
      when(result.getSystemDescriptor()).thenReturn(null);
      when(systemDescriptorService.parseProject(any())).thenReturn(result);

      provider.addCommand(command);
      provider.run(new String[]{"create-java-bundle"});

      ArgumentCaptor<IJellyFishCommandOptions> optionsCapture = ArgumentCaptor.forClass(IJellyFishCommandOptions.class);
      verify(command).run(optionsCapture.capture());

      IJellyFishCommandOptions options = optionsCapture.getValue();
      assertNotNull("The options must not be null", options);

      //we set it to null above, ensure it really is null
      assertTrue(options.getParameters().containsParameter(TEST_PARAM_NAME));
      assertNotNull(options.getParameters().getParameter(TEST_PARAM_NAME).getValue());
      assertEquals(TEST_PARAM_VALUE, options.getParameters().getParameter(TEST_PARAM_NAME).getValue());
   }

   @JellyFishCommandConfiguration(autoTemplateProcessing = false)
   public interface NoTemplateJfCommand extends IJellyFishCommand {

   }

   @JellyFishCommandConfiguration(requireValidSystemDescriptor = false)
   public interface ToleratingInvalidSdJfCommand extends IJellyFishCommand {

   }
}
