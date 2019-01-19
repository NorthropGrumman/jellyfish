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

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.parameter.api.IParameterService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCommandProviderTest {

   private DefaultCommandProvider provider;

   @Mock
   private ICommand<ICommandOptions> command;

   @Mock
   private ILogService logService;

   @Mock
   private IParameterService parameterService;

   @Before
   public void setup() {
      when(command.getName()).thenReturn("test-command");
      when(command.getUsage()).thenReturn(new DefaultUsage("", Collections.emptyList()));

      provider = new DefaultCommandProvider();
      provider.setLogService(logService);
      provider.setParameterService(parameterService);
      provider.activate();
   }

   @Test
   public void testDoesGetUsage() {
      IUsage usage = provider.getUsage();
      assertNotNull("usage is null!",
                    usage);
      assertNotNull("description not set!",
                    usage.getDescription());
      assertTrue("no default parameters should be required!",
                 usage.getRequiredParameters().isEmpty());
   }

   @Test
   public void testDoesRunCommand() {
      DefaultParameterCollection parameters = new DefaultParameterCollection();

      when(parameterService.parseParameters(anyList())).thenReturn(parameters);
      provider.addCommand(command);
      provider.run(new String[]{command.getName()});
      verify(command).run(any(ICommandOptions.class));
   }
}
