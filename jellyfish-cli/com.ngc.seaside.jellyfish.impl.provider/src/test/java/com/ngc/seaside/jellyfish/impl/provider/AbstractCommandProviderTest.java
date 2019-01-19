/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
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
