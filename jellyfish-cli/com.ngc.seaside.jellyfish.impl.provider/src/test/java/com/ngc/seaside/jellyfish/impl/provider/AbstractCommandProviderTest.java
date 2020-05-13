/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.jellyfish.impl.provider;

import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.api.ParameterCategory;

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
      IUsage usage = new DefaultUsage("", new DefaultParameter<>("y").setParameterCategory(ParameterCategory.REQUIRED));
      when(command.getUsage()).thenReturn(usage);

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      provider.verifyRequiredParameters(command, parameters);
   }
}
