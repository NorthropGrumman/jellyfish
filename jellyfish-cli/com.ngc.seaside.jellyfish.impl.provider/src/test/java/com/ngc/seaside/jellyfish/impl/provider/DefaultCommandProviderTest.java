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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.parameter.api.IParameterService;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;

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
