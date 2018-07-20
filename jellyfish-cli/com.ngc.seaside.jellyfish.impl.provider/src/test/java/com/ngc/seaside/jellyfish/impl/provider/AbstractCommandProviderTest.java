package com.ngc.seaside.jellyfish.impl.provider;

import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AbstractCommandProviderTest {

   private AbstractCommandProvider<ICommandOptions, ICommand<ICommandOptions>, Void> provider;

   @Mock
   private ICommand<ICommandOptions> command;

   @Before
   public void setup() {
      when(command.getName()).thenReturn("test-command");

      provider = new AbstractCommandProvider<ICommandOptions, ICommand<ICommandOptions>, Void>() {
         @Override
         public IUsage getUsage() {
            throw new UnsupportedOperationException("not implemented");
         }

         @Override
         public Void run(String[] arguments) {
            throw new UnsupportedOperationException("not implemented");
         }

         @Override
         public void run(String command, ICommandOptions commandOptions) {
            throw new UnsupportedOperationException("not implemented");
         }
      };
      provider.activate();
   }

   @Test
   public void testDoesGetCommands() {
      provider.addCommand(command);
      assertEquals("command not correct!",
                   command,
                   provider.getCommand(command.getName()));
      assertNull("should return null if command not found!",
                 provider.getCommand("command-does-not-exists"));
   }

   @Test(expected = CommandException.class)
   public void testDoesVerifyRequiredParameters() {
      IUsage usage = new DefaultUsage("", new DefaultParameter<>("y").setRequired(true));
      when(command.getUsage()).thenReturn(usage);

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      provider.verifyRequiredParameters(command, parameters);
   }
}
