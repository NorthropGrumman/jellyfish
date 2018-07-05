package com.ngc.seaside.jellyfish.cli.command.report.html;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.jellyfish.service.analysis.api.IReportingOutputService;
import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * A report command that outputs all findings added to the {@link IAnalysisService} to an HTML report.
 */
public class HtmlAnalysisReportCommand implements ICommand<ICommandOptions> {

   private static final String NEWLINE = System.lineSeparator();

   /**
    * The name of the command.
    */
   public static final String NAME = "html-report";

   static final String REPORT_FILE_NAME_PARAMETER_NAME = "reportFileName";
   static final String HTML_REPORT_TEMPLATE_SUFFIX = "report";

   private ILogService logService;

   private ITemplateService templateService;

   private IAnalysisService analysisService;

   private IReportingOutputService reportingOutputService;

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IUsage getUsage() {
      return new DefaultUsage(
            "Outputs the results of analysis to an HTML report.  This command is rarely ran directly;"
            + " instead it is run with the 'analyze' command.",
            new DefaultParameter<String>(REPORT_FILE_NAME_PARAMETER_NAME)
                  .setDescription("The file name of the report to generate.  This file will be located within the"
                                  + " directory given by the outputDirectory parameter.")
                  .setRequired(true),
            CommonParameters.OUTPUT_DIRECTORY.required());
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

         HtmlReportDto dto = new HtmlReportDto()
               .setContent(reportingOutputService.convert(sb.toString()))
               .setReportName(commandOptions.getParameters()
                                    .getParameter(REPORT_FILE_NAME_PARAMETER_NAME)
                                    .getStringValue());
         outputReport(commandOptions, dto);
      }
   }

   @Activate
   public void activate() {
      logService.debug(HtmlAnalysisReportCommand.class, "Activated.");
   }

   @Deactivate
   public void deactivate() {
      logService.debug(HtmlAnalysisReportCommand.class, "Deactivated.");
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

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeTemplateService")
   public void setTemplateService(ITemplateService ref) {
      this.templateService = ref;
   }

   public void removeTemplateService(ITemplateService ref) {
      setTemplateService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeReportingOutputService")
   public void setReportingOutputService(IReportingOutputService ref) {
      this.reportingOutputService = ref;
   }

   public void removeReportingOutputService(IReportingOutputService ref) {
      setReportingOutputService(null);
   }

   private void outputReport(ICommandOptions commandOptions, String report) {
      String html = reportingOutputService.convert(report);
      Path outputDirectory = Paths.get(commandOptions.getParameters()
                                             .getParameter(CommonParameters.OUTPUT_DIRECTORY.getName())
                                             .getStringValue());
      Path outputFile = outputDirectory.resolve(commandOptions.getParameters()
                                                      .getParameter(REPORT_FILE_NAME_PARAMETER_NAME)
                                                      .getStringValue())
            .toAbsolutePath();
      // If the parent directory does not exists, create it.  This method will not throw an exception if the directory
      // already exists.
      try {
         Files.createDirectories(outputDirectory);
         // Overwrite any existing file.
         Files.write(outputFile,
                     Collections.singleton(html),
                     StandardOpenOption.CREATE,
                     StandardOpenOption.TRUNCATE_EXISTING);
      } catch (IOException e) {
         throw new RuntimeException("error while writing HTML report to " + outputFile, e);
      }

      logService.debug(getClass(), "Successfully created HTML report at %s.", outputDirectory);
   }

   private void outputReport(ICommandOptions options, HtmlReportDto dto) {
      Path outputDirectory = Paths.get(options.getParameters()
                                             .getParameter(CommonParameters.OUTPUT_DIRECTORY.getName())
                                             .getStringValue());
      templateService.unpack(getClass().getPackage().getName() + "-" + HTML_REPORT_TEMPLATE_SUFFIX,
                             options.getParameters(),
                             outputDirectory,
                             true);
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

   private static void logRuntimeInformation(StringBuilder sb, ICommandOptions commandOptions) {
      sb.append("# Runtime Information").append(NEWLINE);
      sb.append("Jellyfish executed with the following parameters:").append(NEWLINE);
      commandOptions.getParameters().getAllParameters()
            .stream()
            .map(p -> "* " + p.getName() + " = " + p.getValue())
            .forEach(v -> sb.append(v).append(NEWLINE));
   }
}
