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
package com.ngc.seaside.jellyfish.cli.command.validate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.gherkin.api.IGherkinParsingResult;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

@RunWith(MockitoJUnitRunner.class)
public class ValidateFeaturesCommandTest {

   private ValidateFeaturesCommand command;

   @Mock
   private IJellyFishCommandOptions options;

   @Mock
   private IGherkinParsingResult parsingResult;

   @Mock
   private ILogService logService;

   @Before
   public void before() {
      when(options.getGherkinParsingResult()).thenReturn(parsingResult);

      command = new ValidateFeaturesCommand(logService);
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
      when(location.getLineNumber()).thenReturn(21);
      when(location.getColumn()).thenReturn(3);
      when(location.getPath()).thenReturn(Paths.get("src", "test", "resources", "invalidFile.txt"));

      when(parsingResult.isSuccessful()).thenReturn(false);
      when(parsingResult.getIssues()).thenReturn(Collections.singletonList(issue));

      command.run(options);
   }
}
