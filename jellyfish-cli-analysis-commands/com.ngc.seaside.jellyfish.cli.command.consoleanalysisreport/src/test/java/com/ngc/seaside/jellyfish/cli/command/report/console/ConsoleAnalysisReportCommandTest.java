package com.ngc.seaside.jellyfish.cli.command.report.console;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
   public void setup() {
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
                                                                                   String description) {
      ISourceLocation location = mock(ISourceLocation.class);
      when(location.getPath()).thenReturn(Paths.get("foo/bar/stuff.sd"));
      when(location.getLineNumber()).thenReturn(10);
      when(location.getColumn()).thenReturn(19);
      return type.createFinding(description, location, 1);
   }
}
