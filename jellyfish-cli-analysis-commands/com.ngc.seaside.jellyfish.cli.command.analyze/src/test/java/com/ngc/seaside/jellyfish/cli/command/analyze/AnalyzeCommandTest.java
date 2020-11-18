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
package com.ngc.seaside.jellyfish.cli.command.analyze;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.ICommandProvider;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandProvider;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;

@RunWith(MockitoJUnitRunner.class)
public class AnalyzeCommandTest {

   private AnalyzeCommand command;

   @Mock
   private ILogService logService;

   @Mock
   private IJellyFishCommandProvider jellyFishCommandProvider;

   @Mock
   private ICommandProvider<ICommandOptions, ICommand<ICommandOptions>, ICommandOptions> commandProvider;

   @Mock
   private IJellyFishCommandOptions commandOptions;

   private DefaultParameterCollection parameters;

   @Before
   public void setup() {
      parameters = new DefaultParameterCollection();
      when(commandOptions.getParameters()).thenReturn(parameters);

      command = new AnalyzeCommand();
      command.setLogService(logService);
      command.setJellyFishCommandProvider(jellyFishCommandProvider);
      command.setCommandProvider(commandProvider);
      command.activate();
   }

   @Test
   public void testDoesRequireParameters() {
      assertTrue("failed to register required parameter '" + AnalyzeCommand.ANALYSES_PARAMETER_NAME + "'",
                 command.getUsage()
                       .getRequiredParameters()
                       .stream()
                       .anyMatch(p -> p.getName().equals(AnalyzeCommand.ANALYSES_PARAMETER_NAME)));
      assertTrue("failed to register parameter '" + AnalyzeCommand.REPORTS_PARAMETER_NAME + "'",
                 command.getUsage()
                       .getAllParameters()
                       .stream()
                       .anyMatch(p -> p.getName().equals(AnalyzeCommand.REPORTS_PARAMETER_NAME)));
   }

   @Test
   public void testDoesRunAnalysesAndReports() {
      String[] analyses = {"analysis1", "analysis2"};
      String[] reports = {"report1", "report2"};
      parameters.addParameter(new DefaultParameter<>(AnalyzeCommand.ANALYSES_PARAMETER_NAME,
                                                     Arrays.stream(analyses).collect(Collectors.joining(","))));
      parameters.addParameter(new DefaultParameter<>(AnalyzeCommand.REPORTS_PARAMETER_NAME,
                                                     Arrays.stream(reports).collect(Collectors.joining(","))));

      IJellyFishCommand jellyFishCommand = mock(IJellyFishCommand.class);
      for (String analysis : analyses) {
         when(jellyFishCommandProvider.getCommand(analysis)).thenReturn(jellyFishCommand);
      }
      @SuppressWarnings({"unchecked"})
      ICommand<ICommandOptions> basicCommand = mock(ICommand.class);
      for (String report : reports) {
         when(commandProvider.getCommand(report)).thenReturn(basicCommand);
      }

      command.run(commandOptions);

      for (String analysis : analyses) {
         verify(jellyFishCommandProvider).run(analysis, commandOptions);
      }
      for (String report : reports) {
         verify(commandProvider).run(report, commandOptions);
      }
   }

   @Test
   public void testDoesRunSingleAnalysisAndReport() {
      String analysis = "analysis";
      String report = "report";
      parameters.addParameter(new DefaultParameter<>(AnalyzeCommand.ANALYSES_PARAMETER_NAME, analysis));
      parameters.addParameter(new DefaultParameter<>(AnalyzeCommand.REPORTS_PARAMETER_NAME, report));

      IJellyFishCommand jellyFishCommand = mock(IJellyFishCommand.class);
      when(jellyFishCommandProvider.getCommand(analysis)).thenReturn(jellyFishCommand);
      when(jellyFishCommandProvider.getCommand(report)).thenReturn(jellyFishCommand);

      command.run(commandOptions);

      verify(jellyFishCommandProvider).run(analysis, commandOptions);
      verify(jellyFishCommandProvider).run(report, commandOptions);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testDoesHandleUnknownAnalysisOrReport() {
      String analysis = "analysis";
      String report = "report";
      parameters.addParameter(new DefaultParameter<>(AnalyzeCommand.ANALYSES_PARAMETER_NAME, analysis));
      parameters.addParameter(new DefaultParameter<>(AnalyzeCommand.REPORTS_PARAMETER_NAME, report));
      command.run(commandOptions);
   }
}
