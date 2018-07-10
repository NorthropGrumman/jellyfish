package com.ngc.seaside.jellyfish.cli.command.report.html;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.api.ICommandOptions;
import com.ngc.seaside.jellyfish.api.IParameter;
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
import java.util.Collection;
import java.util.List;

/**
 * A report command that outputs all findings added to the {@link IAnalysisService} to an HTML report.
 */
public class HtmlAnalysisReportCommand implements ICommand<ICommandOptions> {

   /**
    * The name of the command.
    */
   public static final String NAME = "html-report";

   /**
    * The name of the argument that configures the name of the report.
    */
   static final String REPORT_FILE_NAME_PARAMETER_NAME = "reportFileName";

   /**
    * The prefix of the template name for the report.
    */
   static final String HTML_REPORT_TEMPLATE_PREFIX = "com.ngc.seaside.jellyfish.cli.command.htmlanalysisreport";

   /**
    * The suffix of the template name for the report.
    */
   static final String HTML_REPORT_TEMPLATE_SUFFIX = "report";

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
      }

      // Sort by severity first.
      Multimap<ISystemDescriptorFindingType.Severity, SystemDescriptorFinding<?>> findings =
            ArrayListMultimap.create();
      for (SystemDescriptorFinding<?> finding : all) {
         findings.put(finding.getType().getSeverity(), finding);
      }

      HtmlReportDto dto = new HtmlReportDto()
            .setReportName(commandOptions.getParameters()
                                 .getParameter(REPORT_FILE_NAME_PARAMETER_NAME)
                                 .getStringValue())
            .setTitle("Jellyfish Analysis Report");
      addSummary(findings, dto);
      addErrors(findings, dto);
      addWarnings(findings, dto);
      addRuntimeInformation(commandOptions, dto);

      outputReport(commandOptions, dto);
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

   private void addSummary(Multimap<ISystemDescriptorFindingType.Severity, SystemDescriptorFinding<?>> findings,
                           HtmlReportDto dto) {
      String section = "<div class=\"summary\">\n"
                       + "<h1 id=\"summary\">Summary</h1>\n"
                       + "<p><i class=\"error fas fa-times\"></i><a href=\"#errors\"> "
                       + findings.get(ISystemDescriptorFindingType.Severity.ERROR).size()
                       + " "
                       + ISystemDescriptorFindingType.Severity.ERROR.toString().toLowerCase()
                       + "s"
                       + "</a></p>\n"
                       + "<p><i class=\"warning fas fa-exclamation-triangle\"></i><a href=\"#warnings\"> "
                       + findings.get(ISystemDescriptorFindingType.Severity.WARNING).size()
                       + " "
                       + ISystemDescriptorFindingType.Severity.WARNING.toString().toLowerCase()
                       + "s"
                       + "</a></p>\n"
                       + "</div>\n";
      dto.addContent(section);
   }

   private void addErrors(Multimap<ISystemDescriptorFindingType.Severity, SystemDescriptorFinding<?>> findings,
                          HtmlReportDto dto) {
      StringBuilder section = new StringBuilder();
      section.append("<div class=\"errors\">\n");
      section.append("<h1 id=\"errors\">Errors</h1>\n");
      appendFindings(findings.get(ISystemDescriptorFindingType.Severity.ERROR), section);
      section.append("</div>\n");
      dto.addContent(section.toString());
   }

   private void addWarnings(Multimap<ISystemDescriptorFindingType.Severity, SystemDescriptorFinding<?>> findings,
                            HtmlReportDto dto) {
      StringBuilder section = new StringBuilder();
      section.append("<div class=\"warnings\">\n");
      section.append("<h1 id=\"warnings\">Warnings</h1>\n");
      appendFindings(findings.get(ISystemDescriptorFindingType.Severity.WARNING), section);
      section.append("</div>\n");
      dto.addContent(section.toString());
   }

   private void appendFindings(Collection<SystemDescriptorFinding<?>> findings,
                               StringBuilder sb) {
      // Group into types.
      Multimap<ISystemDescriptorFindingType, SystemDescriptorFinding<?>> sorted = LinkedListMultimap.create();
      findings.forEach(f -> sorted.put(f.getType(), f));

      for (ISystemDescriptorFindingType type : sorted.keySet()) {
         sb.append("<div class=\"finding-type\">\n");
         sb.append(reportingOutputService.convert(type.getDescription()));

         for (SystemDescriptorFinding<?> finding : sorted.get(type)) {
            sb.append("<div class=\"finding\">\n");
            sb.append(getLocationString(finding.getLocation().orElse(null)));
            sb.append("<div class=\"finding-details\">\n");
            sb.append(reportingOutputService.convert(finding.getMessage()));
            sb.append("</div>\n");
            sb.append("</div>\n");
         }

         sb.append("</div>\n");
      }
   }

   private void addRuntimeInformation(ICommandOptions commandOptions, HtmlReportDto dto) {
      StringBuilder section = new StringBuilder();
      section.append("<div class=\"runtime-info\">\n")
            .append("<h1>Runtime Information</h1>\n")
            .append("<p>")
            .append("Jellyfish executed with the following parameters:\n")
            .append("<ul>\n");

      for (IParameter<?> param : commandOptions.getParameters().getAllParameters()) {
         section.append("<li>")
               .append(param.getName())
               .append(" = ")
               .append(param.getStringValue())
               .append("</li>\n");
      }

      section.append("</ul>\n")
            .append("</p>\n")
            .append("</div>\n");
      dto.addContent(section.toString());
   }

   private void outputReport(ICommandOptions options, HtmlReportDto dto) {
      Path outputDirectory = Paths.get(options.getParameters()
                                             .getParameter(CommonParameters.OUTPUT_DIRECTORY.getName())
                                             .getStringValue());

      DefaultParameterCollection params = new DefaultParameterCollection(options.getParameters());
      params.addParameter(new DefaultParameter<>("dto", dto));
      templateService.unpack(HTML_REPORT_TEMPLATE_PREFIX + "-" + HTML_REPORT_TEMPLATE_SUFFIX,
                             params,
                             outputDirectory,
                             true);
   }

   private String getLocationString(ISourceLocation location) {
      return "<div class=\"source-location\">\n"
             + "<span class=\"file-name\">" + location.getPath() + "</span>\n"
             + "<span class=\"line-number\">line " + location.getLineNumber() + "</span>\n"
             + "<span class=\"col\">col " + location.getColumn() + "</span>\n"
             + "</div>\n"
             + "<div class=\"source-snippet\">\n"
             + getLocationContents(location)
             + "</div>\n";
   }

   private String getLocationContents(ISourceLocation location) {
      StringBuilder sb = new StringBuilder();
      sb.append("<div class=\"source-code language-sd\">\n");

      try {
         List<String> lines = Files.readAllLines(location.getPath());
         int line = location.getLineNumber() - 1;

         for (int i = Math.max(0, line - PRECEDING_LINES_TO_SHOW);
                 i < Math.min(line + 1 + SUCCEEDING_LINES_TO_SHOW, lines.size());
                 i++) {
            if (i == line) {
               sb.append("<pre class=\"line offending-line\">")
                     .append(getOffendingLineContents(lines.get(i), location))
                     .append("</pre>\n");
            } else {
               sb.append("<pre class=\"line\">")
                     .append(lines.get(i))
                     .append("</pre>\n");
            }
         }

         sb.append("</div>\n");
      } catch (IOException e) {
         logService.error(getClass(), e, "Unable to read source from %s for finding location.", location.getPath());
         sb = new StringBuilder("source not available");
      }

      return sb.toString();
   }

   private static String getOffendingLineContents(String line, ISourceLocation location) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < line.length(); i++) {
         if (i + 1 == location.getColumn()) {
            sb.append("<span class=\"offending\">");
         }
         sb.append(line.charAt(i));
         if (i + 1 == location.getColumn() + location.getLength() - 1) {
            sb.append("</span>");
         }
      }
      return sb.toString();
   }
}
