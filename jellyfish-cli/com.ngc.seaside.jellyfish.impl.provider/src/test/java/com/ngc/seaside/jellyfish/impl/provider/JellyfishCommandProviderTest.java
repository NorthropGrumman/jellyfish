package com.ngc.seaside.jellyfish.impl.provider;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.parameter.api.IParameterService;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JellyfishCommandProviderTest {

   private JellyfishCommandProvider provider;

   @Mock
   private ILogService logService;

   @Mock
   private IParameterService parameterService;

   @Mock
   private ISystemDescriptorService systemDescriptorService;

   @Mock
   private IParsingResult parsingResult;

   @Mock
   private ISystemDescriptor systemDescriptor;

   @Before
   public void setup() {
      provider = new JellyfishCommandProvider();
      provider.setLogService(logService);
      provider.setParameterService(parameterService);
      provider.setSystemDescriptorService(systemDescriptorService);
      provider.activate();
   }

   @Test
   public void testDoesGetUsage() {
      IUsage usage = provider.getUsage();
      assertNotNull("usage is null!",
                    usage);
      assertNotNull("description not set!",
                    usage.getDescription());
      assertTrue("missing parameter input directory!",
                 usage.getAllParameters().contains(CommonParameters.INPUT_DIRECTORY));
      assertTrue("missing parameter GAV!",
                 usage.getAllParameters().contains(CommonParameters.GROUP_ARTIFACT_VERSION));
   }

   @Test
   public void testDoesGetCommands() {
      IJellyFishCommand command = mockedCommand("foo-command", true);
      provider.addCommand(command);
      assertEquals("command not correct!",
                   command,
                   provider.getCommand(command.getName()));
      assertNull("should return null if command not found!",
                 provider.getCommand("command-does-not-exists"));
   }

   @Test
   public void testDoesRunCommandWhichDoesNotRequireValidProject() {
      IJellyFishCommand command = mockedCommand("foo-command", false);
      when(parameterService.parseParameters(anyList())).thenReturn(mockedParams());
      when(systemDescriptorService.parseProject(any(Path.class))).thenReturn(parsingResult);

      provider.addCommand(command);
      provider.run(new String[]{command.getName()});

      verify(command).run(any(IJellyFishCommandOptions.class));
   }

   @Test
   public void testDoesRunCommandWithValidProject() {
      IJellyFishCommand command = mockedCommand("foo-command", true);
      when(parameterService.parseParameters(anyList())).thenReturn(mockedParams());
      when(systemDescriptorService.parseProject(any(Path.class))).thenReturn(parsingResult);
      when(parsingResult.getSystemDescriptor()).thenReturn(systemDescriptor);

      provider.addCommand(command);
      provider.run(new String[]{command.getName()});

      verify(command).run(any(IJellyFishCommandOptions.class));
   }

   @Test(expected = CommandException.class)
   public void testDoesRunNotCommandWithInvalidProjectIfValidProjectRequired() {
      IJellyFishCommand command = mockedCommand("foo-command", true);
      when(parameterService.parseParameters(anyList())).thenReturn(mockedParams());
      when(systemDescriptorService.parseProject(any(Path.class))).thenReturn(parsingResult);
      when(parsingResult.getSystemDescriptor()).thenReturn(null);

      provider.addCommand(command);
      provider.run(new String[]{command.getName()});
   }

   @Test(expected = CommandException.class)
   public void testDoesCheckRequiredParametersBeforeRunningCommand() {
      IUsage usage = new DefaultUsage("", new DefaultParameter<>("y").setRequired(true));

      IJellyFishCommand command = mockedCommand("foo-command", false);
      when(command.getUsage()).thenReturn(usage);
      when(parameterService.parseParameters(anyList())).thenReturn(mockedParams("x"));

      provider.addCommand(command);
      provider.run(new String[]{command.getName()});
   }

   @Test(expected = IllegalArgumentException.class)
   public void testDoesFailIfCommandNotFound() {
      when(parameterService.parseParameters(anyList())).thenReturn(mockedParams());
      provider.run(new String[]{"command-does-not-exist"});
   }

   @After
   public void after() {
      provider.removeLogService(logService);
      provider.removeParameterService(parameterService);
      provider.removeSystemDescriptorService(systemDescriptorService);
      provider.deactivate();
   }

   private static IJellyFishCommand mockedCommand(String name, boolean requireValidProject) {
      IJellyFishCommand command = mock(IJellyFishCommand.class);
      when(command.getName()).thenReturn(name);
      when(command.requiresValidSystemDescriptorProject()).thenReturn(requireValidProject);
      when(command.getUsage()).thenReturn(new DefaultUsage("", Collections.emptyList()));
      return command;
   }

   private static IParameterCollection mockedParams(String... paramNames) {
      DefaultParameterCollection collection = new DefaultParameterCollection();
      for (String name : paramNames) {
         collection.addParameter(new DefaultParameter<>(name, name));
      }
      return collection;
   }
}
