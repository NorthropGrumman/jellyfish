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
package com.ngc.seaside.jellyfish.cli.command.report.html;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.cli.command.test.service.MockedTemplateService;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.jellyfish.service.analysis.api.IReportingOutputService;
import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;

@RunWith(MockitoJUnitRunner.class)
public class HtmlAnalysisReportCommandIT {

   private HtmlAnalysisReportCommand command;

   private Path outputDirectory;

   private DefaultParameterCollection parameters;

   @Rule
   public TemporaryFolder temporaryFolder = new TemporaryFolder();

   private MockedTemplateService templateService;

   @Mock
   private ILogService logService;

   @Mock
   private IAnalysisService analysisService;

   @Mock
   private IReportingOutputService reportingOutputService;

   @Mock
   private ICommandOptions options;

   @Before
   public void setup() throws Throwable {
      outputDirectory = temporaryFolder.newFolder().toPath();

      templateService = new MockedTemplateService()
            .useRealPropertyService()
            .setTemplateDirectory(
                  HtmlAnalysisReportCommand.HTML_REPORT_TEMPLATE_PREFIX + "-"
                  + HtmlAnalysisReportCommand.HTML_REPORT_TEMPLATE_SUFFIX,
                  Paths.get("src", "main", "templates", HtmlAnalysisReportCommand.HTML_REPORT_TEMPLATE_SUFFIX));

      // Just return whatever is given.
      when(reportingOutputService.convert(anyString()))
            .then(invocation -> invocation.getArgument(0));

      parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>(CommonParameters.OUTPUT_DIRECTORY.getName())
                                    .setValue(outputDirectory.toAbsolutePath()));
      parameters.addParameter(new DefaultParameter<>(HtmlAnalysisReportCommand.REPORT_NAME_PARAMETER_NAME)
                                    .setValue("test"));
      when(options.getParameters()).thenReturn(parameters);

      List<SystemDescriptorFinding<ISystemDescriptorFindingType>> findings = Arrays.asList(
            newFinding(FOO_TYPE, "Red foo alert!  Foos gone crazy."),
            newFinding(FOO_TYPE, "Yep, them's foos alright."),
            newFinding(BAR_TYPE, "You got some bars here.  You probably didn't know that, did you?"),
            newFinding(COO_TYPE, "These aren't the coos you're looking for.  Move along."),
            newFinding(COO_TYPE, "Nothing to see here.")
      );
      when(analysisService.getFindings()).thenReturn(findings);

      command = new HtmlAnalysisReportCommand();
      command.setLogService(logService);
      command.setTemplateService(templateService);
      command.setAnalysisService(analysisService);
      command.setReportingOutputService(reportingOutputService);
      command.activate();
   }

   @Test
   public void testDoesGenerateHtmlReport() {
      command.run(options);
      assertTrue("report not created!",
                 Files.isRegularFile(outputDirectory.resolve("test.html")));
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
      Path path = Paths.get(HtmlAnalysisReportCommandIT.class.getClassLoader().getResource("example.sd").toURI());

      ISourceLocation location = mock(ISourceLocation.class);
      when(location.getPath()).thenReturn(path);
      when(location.getLineNumber()).thenReturn(3);
      when(location.getColumn()).thenReturn(7);
      when(location.getLength()).thenReturn(7);
      return type.createFinding(description, location, 1);
   }
}
