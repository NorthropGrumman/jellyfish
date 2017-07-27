package com.ngc.seaside.jellyfish.cli.command.createjavaevents;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.resource.MockedResourceService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateJavaEventsCommandTest {

   private CreateJavaEventsCommand command;

   private DefaultParameterCollection parameters;

   private MockedResourceService resourceService;

   @Mock
   private IJellyFishCommandOptions options;

   @Mock
   private ILogService logService;

   @Mock
   private IJellyFishCommandProvider commandProvider;

   @Before
   public void setup() {
      IParsingResult result = mock(IParsingResult.class);

      parameters = new DefaultParameterCollection();
      when(options.getParameters()).thenReturn(parameters);
      when(options.getParsingResult()).thenReturn(result);

      resourceService = new MockedResourceService();

      command = new CreateJavaEventsCommand();
      command.setLogService(logService);
      command.setResourceService(resourceService);
      command.setJellyFishCommandProvider(commandProvider);
      command.activate();

   }

   @Test
   public void testDoesCommandInvokeDomainCommandWithDefaults() {
      resourceService.onNextReadDrain(
            CreateJavaEventsCommandTest.class
                  .getClassLoader()
                  .getResourceAsStream(CreateJavaEventsCommand.EVENT_SOURCE_VELOCITY_TEMPLATE));

      parameters.addParameter(new DefaultParameter<>("model", "my.Model"));
      command.run(options);

      ArgumentCaptor<IJellyFishCommandOptions> optionsCapture = ArgumentCaptor.forClass(IJellyFishCommandOptions.class);
      verify(commandProvider).run(eq(CreateJavaEventsCommand.CREATE_DOMAIN_COMMAND_NAME),
                                  optionsCapture.capture());

      IJellyFishCommandOptions delegateOptions = optionsCapture.getValue();
      assertTrue("does not include original parameters!",
                 delegateOptions.getParameters().containsParameter("model"));

      assertTrue("does not contain domain template file property!",
                 delegateOptions.getParameters().containsParameter(
                       CreateJavaEventsCommand.DOMAIN_TEMPLATE_FILE_PROPERTY));
      assertTrue("domain template file default is a null value!",
                 delegateOptions.getParameters()
                       .getParameter(CreateJavaEventsCommand.DOMAIN_TEMPLATE_FILE_PROPERTY)
                       .getStringValue() != null);

      assertTrue("does not contain package suffix property!",
                 delegateOptions.getParameters()
                       .containsParameter(CreateJavaEventsCommand.PACKAGE_SUFFIX_PROPERTY));
      assertEquals("package suffix default not correct!",
                   CreateJavaEventsCommand.DEFAULT_PACKAGE_SUFFIX,
                   delegateOptions.getParameters()
                         .getParameter(CreateJavaEventsCommand.PACKAGE_SUFFIX_PROPERTY)
                         .getStringValue());
   }

   @Test
   public void testDoesCommandAllowForCustomPackageSuffix() throws Throwable {
      resourceService.onNextReadDrain(
            CreateJavaEventsCommandTest.class
                  .getClassLoader()
                  .getResourceAsStream(CreateJavaEventsCommand.EVENT_SOURCE_VELOCITY_TEMPLATE));

      parameters.addParameter(new DefaultParameter<>(CreateJavaEventsCommand.PACKAGE_SUFFIX_PROPERTY,
                                                     "my.suffix"));
      parameters.addParameter(new DefaultParameter<>("model", "my.Model"));
      command.run(options);

      ArgumentCaptor<IJellyFishCommandOptions> optionsCapture = ArgumentCaptor.forClass(IJellyFishCommandOptions.class);
      verify(commandProvider).run(eq(CreateJavaEventsCommand.CREATE_DOMAIN_COMMAND_NAME),
                                  optionsCapture.capture());

      IJellyFishCommandOptions delegateOptions = optionsCapture.getValue();
      assertTrue("does not contain package suffix property!",
                 delegateOptions.getParameters()
                       .containsParameter(CreateJavaEventsCommand.PACKAGE_SUFFIX_PROPERTY));
      assertEquals("package suffix default not correct!",
                   "my.suffix",
                   delegateOptions.getParameters()
                         .getParameter(CreateJavaEventsCommand.PACKAGE_SUFFIX_PROPERTY)
                         .getStringValue());
   }

   @Test
   public void testDoesCommandAllowForCustomVelocityTemplateFile() throws Throwable {
      parameters.addParameter(new DefaultParameter<>(CreateJavaEventsCommand.EVENT_TEMPLATE_FILE_PROPERTY,
                                                     "my/template/file"));
      parameters.addParameter(new DefaultParameter<>("model", "my.Model"));
      command.run(options);

      ArgumentCaptor<IJellyFishCommandOptions> optionsCapture = ArgumentCaptor.forClass(IJellyFishCommandOptions.class);
      verify(commandProvider).run(eq(CreateJavaEventsCommand.CREATE_DOMAIN_COMMAND_NAME),
                                  optionsCapture.capture());

      IJellyFishCommandOptions delegateOptions = optionsCapture.getValue();
      assertTrue("does not contain domain template file property!",
                 delegateOptions.getParameters().containsParameter(
                       CreateJavaEventsCommand.DOMAIN_TEMPLATE_FILE_PROPERTY));
      assertEquals("domain template file default incorrect!",
                   "my/template/file",
                   delegateOptions.getParameters()
                         .getParameter(CreateJavaEventsCommand.DOMAIN_TEMPLATE_FILE_PROPERTY).getStringValue());
   }
}
