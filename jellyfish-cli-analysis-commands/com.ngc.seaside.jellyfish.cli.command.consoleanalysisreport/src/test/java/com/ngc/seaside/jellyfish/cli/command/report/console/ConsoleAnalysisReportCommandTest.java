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
package com.ngc.seaside.jellyfish.cli.command.report.console;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.service.log.api.PrintStreamLogService;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;

@RunWith(MockitoJUnitRunner.class)
public class ConsoleAnalysisReportCommandTest {

   private ConsoleAnalysisReportCommand command;

   private ILogService logService;

   @Mock
   private IAnalysisService analysisService;

   @Mock
   private ICommandOptions options;

   @Mock
   private IParameterCollection parameters;

   @Before
   public void setup() throws Throwable {
      logService = new PrintStreamLogService();

      command = new ConsoleAnalysisReportCommand();
      command.setLogService(logService);
      command.setAnalysisService(analysisService);
      command.activate();

      when(parameters.getAllParameters()).thenReturn(Arrays.asList(
            new DefaultParameter<>("a").setValue("b"),
            new DefaultParameter<>("c").setValue("d")));
      when(options.getParameters()).thenReturn(parameters);

      List<SystemDescriptorFinding<ISystemDescriptorFindingType>> findings = Arrays.asList(
            newFinding(FOO_TYPE, "Red foo alert!  Foos gone crazy."),
            newFinding(FOO_TYPE, "Yep, them's foos alright."),
            newFinding(BAR_TYPE, "You got some bars here.  You probably didn't know that, did you?"),
            newFinding(COO_TYPE, "These aren't the coos you're looking for.  Move along."),
            newFinding(COO_TYPE, "Nothing to see here.")
      );
      when(analysisService.getFindings()).thenReturn(findings);
   }

   @Test
   public void testDoesPrintFindings() {
      command.run(options);
   }

   private static final ISystemDescriptorFindingType FOO_TYPE = ISystemDescriptorFindingType.createFindingType(
         "foo",
         "## Foo\n"
         + "Foos are bad and cause issues with stuff.  Too many foos are even worse.",
         ISystemDescriptorFindingType.Severity.ERROR);

   private static final ISystemDescriptorFindingType BAR_TYPE = ISystemDescriptorFindingType.createFindingType(
         "bar",
         "## Bar\n"
         + "Bars aren't too bad but they can be annoying.  You should avoid these where possible.",
         ISystemDescriptorFindingType.Severity.WARNING);

   private static final ISystemDescriptorFindingType COO_TYPE = ISystemDescriptorFindingType.createFindingType(
         "coo",
         "# Coo\n"
         + "Coos are okay.  Trust me.",
         ISystemDescriptorFindingType.Severity.INFO);

   private static SystemDescriptorFinding<ISystemDescriptorFindingType> newFinding(ISystemDescriptorFindingType type,
                                                                                   String description)
         throws URISyntaxException {
      Path path = Paths.get(ConsoleAnalysisReportCommandTest.class.getClassLoader().getResource("example.sd").toURI());

      ISourceLocation location = mock(ISourceLocation.class);
      when(location.getPath()).thenReturn(path);
      when(location.getLineNumber()).thenReturn(3);
      when(location.getColumn()).thenReturn(7);
      when(location.getLength()).thenReturn(7);
      return type.createFinding(description, location, 1);
   }
}
