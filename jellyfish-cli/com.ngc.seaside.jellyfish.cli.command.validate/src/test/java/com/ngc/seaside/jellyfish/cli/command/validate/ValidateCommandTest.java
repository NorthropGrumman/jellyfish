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
package com.ngc.seaside.jellyfish.cli.command.validate;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Paths;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValidateCommandTest {

   private ValidateCommand command = new ValidateCommand();

   @Mock
   private IJellyFishCommandOptions options;

   @Mock
   private IParsingResult parsingResult;

   @Before
   public void before() {
      when(options.getParsingResult()).thenReturn(parsingResult);
      command.setLogService(mock(ILogService.class));
   }

   @Test
   public void testValidateValidResult() {
      when(parsingResult.isSuccessful()).thenReturn(true);
      command.run(options);
   }

   @Test(expected = CommandException.class)
   public void testValidateInvalidResult() {
      IParsingIssue issue = mock(IParsingIssue.class, Mockito.RETURNS_DEEP_STUBS);
      ISourceLocation location = issue.getLocation();
      when(issue.getSeverity()).thenReturn(Severity.ERROR);
      when(issue.getMessage()).thenReturn("some error message");
      when(location.getLineNumber()).thenReturn(4);
      when(location.getColumn()).thenReturn(3);
      when(location.getPath()).thenReturn(Paths.get("src", "test", "resources", "invalidFile.txt"));

      when(parsingResult.isSuccessful()).thenReturn(false);
      when(parsingResult.getIssues()).thenReturn(Collections.singletonList(issue));

      command.run(options);
   }

}
