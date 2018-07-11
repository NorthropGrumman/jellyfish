package com.ngc.seaside.jellyfish.cli.command.report.console;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A report command that outputs all findings added to the {@link IAnalysisService} to the console via the log service.
 */
public class ConsoleAnalysisReportCommand implements ICommand<ICommandOptions> {

   private static final String NEWLINE = System.lineSeparator();

   /**
    * The name of the command.
    */
   public static final String NAME = "console-report";

   /**
    * The number of lines to show that precede a finding.  IE, the last 3 lines before a finding in a file occurred will
    * be shown when displaying the details for the finding.
    */
   private static final int PRECEDING_LINES_TO_SHOW = 3;

   /**
    * The number of lines to show that succeed a finding.  IE, the next 3 lines after a finding in a file occurred will
    * be shown when displaying the details for the finding.
    */
   private static final int SUCCEEDING_LINES_TO_SHOW = 3;

   private ILogService logService;

   private IAnalysisService analysisService;

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IUsage getUsage() {
      return new DefaultUsage("Outputs the results of analysis to the console.  This command is rarely ran directly;"
                              + " instead it is run with the 'analyze' command.");
   }

   @Override
   public void run(ICommandOptions commandOptions) {
      Collection<SystemDescriptorFinding<ISystemDescriptorFindingType>> all = analysisService.getFindings();
      if (all.isEmpty()) {
         logService.info(getClass(), "No findings to report.");
      } else {
         // Sort by severity first.
         Multimap<ISystemDescriptorFindingType.Severity, SystemDescriptorFinding<?>> findings =
               ArrayListMultimap.create();
         for (SystemDescriptorFinding<?> finding : all) {
            findings.put(finding.getType().getSeverity(), finding);
         }

         StringBuilder sb = new StringBuilder(NEWLINE);
         logSummary(findings, sb);
         sb.append(NEWLINE).append("# Errors").append(NEWLINE).append(NEWLINE);
         logFindings(findings.get(ISystemDescriptorFindingType.Severity.ERROR),
                     sb);
         sb.append(NEWLINE).append("# Warnings").append(NEWLINE).append(NEWLINE);
         logFindings(findings.get(ISystemDescriptorFindingType.Severity.WARNING),
                     sb);
         sb.append(NEWLINE);
         logFindings(findings.get(ISystemDescriptorFindingType.Severity.INFO),
                     sb);
         sb.append(NEWLINE);
         logRuntimeInformation(sb, commandOptions);

         logService.info(getClass(), sb);
      }
   }

   @Activate
   public void activate() {
      logService.debug(ConsoleAnalysisReportCommand.class, "Activated.");
   }

   @Deactivate
   public void deactivate() {
      logService.debug(ConsoleAnalysisReportCommand.class, "Deactivated.");
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeAnalysisService")
   public void setAnalysisService(IAnalysisService ref) {
      this.analysisService = ref;
   }

   public void removeAnalysisService(IAnalysisService ref) {
      setAnalysisService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   private void logSummary(Multimap<ISystemDescriptorFindingType.Severity, SystemDescriptorFinding<?>> findings,
                           StringBuilder sb) {
      sb.append("# Summary").append(NEWLINE);
      sb.append(findings.get(ISystemDescriptorFindingType.Severity.ERROR).size())
            .append(" ")
            .append(ISystemDescriptorFindingType.Severity.ERROR.toString().toLowerCase())
            .append("s")
            .append(NEWLINE);
      sb.append(findings.get(ISystemDescriptorFindingType.Severity.WARNING).size())
            .append(" ")
            .append(ISystemDescriptorFindingType.Severity.WARNING.toString().toLowerCase())
            .append("s")
            .append(NEWLINE);
   }

   private void logFindings(Collection<SystemDescriptorFinding<?>> findings,
                            StringBuilder sb) {
      // Group into types.
      Multimap<ISystemDescriptorFindingType, SystemDescriptorFinding<?>> sorted = LinkedListMultimap.create();
      findings.forEach(f -> sorted.put(f.getType(), f));

      for (ISystemDescriptorFindingType type : sorted.keySet()) {
         sb.append(type.getDescription())
               .append(NEWLINE)
               .append(NEWLINE);
         for (Iterator<SystemDescriptorFinding<?>> i = sorted.get(type).iterator(); i.hasNext(); ) {
            SystemDescriptorFinding<?> finding = i.next();
            sb.append(getLocationString(finding.getLocation().orElse(null))).append(NEWLINE);
            sb.append(finding.getMessage()).append(NEWLINE);
            if (i.hasNext()) {
               sb.append(NEWLINE);
            }
         }
      }
   }

   private void logRuntimeInformation(StringBuilder sb, ICommandOptions commandOptions) {
      sb.append("# Runtime Information").append(NEWLINE);
      sb.append("Jellyfish executed with the following parameters:").append(NEWLINE);
      commandOptions.getParameters().getAllParameters()
            .stream()
            .map(p -> "* " + p.getName() + " = " + p.getValue())
            .forEach(v -> sb.append(v).append(NEWLINE));
   }

   private String getLocationString(ISourceLocation location) {
      String s = "<location unknown>";
      if (location != null) {
         s = String.format("%s, line: %s, col: %s%n%s",
                           location.getPath(),
                           location.getLineNumber(),
                           location.getColumn(),
                           getLocationContents(location));
      }
      return s;
   }

   private String getLocationContents(ISourceLocation location) {
      StringBuilder sb = new StringBuilder();
      try {
         List<String> lines = Files.readAllLines(location.getPath());
         int line = location.getLineNumber() - 1;

         for (int i = Math.max(0, line - PRECEDING_LINES_TO_SHOW);
              i < Math.min(line + 1 + SUCCEEDING_LINES_TO_SHOW, lines.size());
              i++) {
            if (i == line) {
               sb.append(lines.get(i))
                     .append(NEWLINE)
                     .append(getOffendingLineHighlight(location))
                     .append(NEWLINE);
            } else {
               sb.append(lines.get(i)).append(NEWLINE);
            }
         }
      } catch (IOException e) {
         logService.error(getClass(), e, "Unable to read source from %s for finding location.", location.getPath());
         sb = new StringBuilder("source not available");
      }

      return sb.toString();
   }

   private static String getOffendingLineHighlight(ISourceLocation location) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < location.getColumn() + location.getLength() - 1; i++) {
         if (i + 1 >= location.getColumn()) {
            sb.append("^");
         } else {
            sb.append(" ");
         }
      }
      return sb.toString();
   }
}
