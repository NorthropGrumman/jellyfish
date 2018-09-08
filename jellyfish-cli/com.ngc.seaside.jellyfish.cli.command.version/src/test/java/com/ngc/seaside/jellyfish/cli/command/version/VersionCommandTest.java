/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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

package com.ngc.seaside.jellyfish.cli.command.version;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.jellyfish.api.ICommandOptions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

      text = output.toString().replaceAll("\n", " ");
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
