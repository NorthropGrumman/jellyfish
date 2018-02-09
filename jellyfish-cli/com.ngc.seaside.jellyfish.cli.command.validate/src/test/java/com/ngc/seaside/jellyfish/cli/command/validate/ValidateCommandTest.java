package com.ngc.seaside.jellyfish.cli.command.validate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Paths;
import java.util.Collections;

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
      IParsingIssue issue = mock(IParsingIssue.class);
      when(issue.getSeverity()).thenReturn(Severity.ERROR);
      when(issue.getMessage()).thenReturn("some error message");
      when(issue.getLineNumber()).thenReturn(4);
      when(issue.getColumn()).thenReturn(3);
      when(issue.getOffendingFile()).thenReturn(Paths.get("src", "test", "resources", "invalidFile.txt"));

      when(parsingResult.isSuccessful()).thenReturn(false);
      when(parsingResult.getIssues()).thenReturn(Collections.singletonList(issue));

      command.run(options);
   }

}
