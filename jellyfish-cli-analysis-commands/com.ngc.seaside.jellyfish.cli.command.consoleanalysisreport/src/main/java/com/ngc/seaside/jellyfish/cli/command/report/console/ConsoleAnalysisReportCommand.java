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
import com.sun.org.apache.bcel.internal.generic.NEW;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.Collection;
import java.util.Iterator;

public class ConsoleAnalysisReportCommand implements ICommand<ICommandOptions> {

   private static final String NEWLINE = System.lineSeparator();

   public static final String NAME = "console-report";

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
      // Sort by severity first.
      Multimap<ISystemDescriptorFindingType.Severity, SystemDescriptorFinding<?>> findings = ArrayListMultimap.create();
      for (SystemDescriptorFinding<?> finding : analysisService.getFindings()) {
         findings.put(finding.getType().getSeverity(), finding);
      }

      StringBuilder sb = new StringBuilder(NEWLINE);
      logSummary(findings, sb);
      sb.append(NEWLINE).append("# Errors").append(NEWLINE);
      logFindings(findings.get(ISystemDescriptorFindingType.Severity.ERROR),
                  sb);
      sb.append(NEWLINE).append("# Warnings").append(NEWLINE);
      logFindings(findings.get(ISystemDescriptorFindingType.Severity.WARNING),
                  sb);
      sb.append(NEWLINE);
      logFindings(findings.get(ISystemDescriptorFindingType.Severity.INFO),
                  sb);
      sb.append(NEWLINE);
      logRuntimeInformation(sb, commandOptions);

      logService.info(getClass(), sb);
   }

   private static void logSummary(Multimap<ISystemDescriptorFindingType.Severity, SystemDescriptorFinding<?>> findings,
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

   private static void logFindings(Collection<SystemDescriptorFinding<?>> findings,
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

   private static String getLocationString(ISourceLocation location) {
      String s = "<location unknown>";
      if (location != null) {
         s = String.format("%s, line: %s, col: %s", location.getPath(), location.getLineNumber(), location.getColumn());
      }
      return s;
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


   private void logRuntimeInformation(StringBuilder sb, ICommandOptions commandOptions) {
      sb.append("# Runtime Information").append(NEWLINE);
      sb.append("Jellyfish executed with the following parameters:").append(NEWLINE);
      commandOptions.getParameters().getAllParameters()
            .stream()
            .map(p -> p.getName() + "=" + p.getValue())
            .forEach(v -> sb.append(v).append(NEWLINE));
   }
}
