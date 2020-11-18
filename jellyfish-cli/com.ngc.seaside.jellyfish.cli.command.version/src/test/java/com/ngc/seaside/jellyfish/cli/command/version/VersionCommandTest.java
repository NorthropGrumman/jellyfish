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
package com.ngc.seaside.jellyfish.cli.command.version;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.systemdescriptor.service.log.api.PrintStreamLogService;

@RunWith(MockitoJUnitRunner.class)
public class VersionCommandTest {

   @Mock
   private ICommandOptions options;

   private String text;

   private VersionCommand cmd;

   @Before
   public void before() {
      ByteArrayOutputStream output = new ByteArrayOutputStream();

      cmd = new VersionCommand();
      cmd.setLogService(new PrintStreamLogService(new PrintStream(output)));

      cmd.run(options);

      text = output.toString().replaceAll(System.lineSeparator(), " ");
   }

   @Test
   public void testCommandSetup() {
      assertEquals("version command name is incorrect!", VersionCommand.COMMAND_NAME, cmd.getName());
      assertEquals("version command usage is incorrect!", VersionCommand.COMMAND_USAGE, cmd.getUsage());
   }

   @Test
   public void testCommandDisplaysVersions() {
      assertTrue("Jellyfish version not present", text.matches(".*Jellyfish\\h+\\d+\\.\\d+\\.\\d+(-SNAPSHOT)?.*"));
      assertTrue("Java version not present", text.matches(".*Java\\h+\\d+\\.\\d+\\.\\d+.*"));
   }

   @Test
   public void testCommandDisplaysEnvironmentVariables() {
      VersionCommand.ENVIRONMENT_VARIABLE_NAMES_AND_DEFAULT_VALUES.keySet()
            .forEach(name -> assertTrue(String.format("%s is not present!", name),
                                        text.matches(".*" + name + "\\h+[\\w/]+( \\(default\\))?.*")));
   }
}
