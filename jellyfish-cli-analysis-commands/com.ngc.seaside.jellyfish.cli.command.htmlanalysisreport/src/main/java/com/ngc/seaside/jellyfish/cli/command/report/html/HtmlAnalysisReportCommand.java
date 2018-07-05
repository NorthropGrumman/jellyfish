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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

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

         HtmlReportDto dto = new HtmlReportDto()
               .setReportName(commandOptions.getParameters()
                                    .getParameter(REPORT_FILE_NAME_PARAMETER_NAME)
                                    .getStringValue());
         addSummary(findings, dto);
         addErrors(findings, dto);
         addWarnings(findings, dto);
         addRuntimeInformation(commandOptions, dto);

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

   private void addSummary(Multimap<ISystemDescriptorFindingType.Severity, SystemDescriptorFinding<?>> findings,
                           HtmlReportDto dto) {
      String section = "<div class=\"summary\">\n"
                       + "<h1>Summary</h1>\n"
                       + "<p>"
                       + findings.get(ISystemDescriptorFindingType.Severity.ERROR).size()
                       + " "
                       + ISystemDescriptorFindingType.Severity.ERROR.toString().toLowerCase()
                       + "s"
                       + "</p>\n"
                       + "<p>"
                       + findings.get(ISystemDescriptorFindingType.Severity.WARNING).size()
                       + " "
                       + ISystemDescriptorFindingType.Severity.WARNING.toString().toLowerCase()
                       + "s"
                       + "</p>\n"
                       + "</div>\n";
      dto.addContent(section);
   }

   private void addErrors(Multimap<ISystemDescriptorFindingType.Severity, SystemDescriptorFinding<?>> findings,
                          HtmlReportDto dto) {
      StringBuilder section = new StringBuilder();
      section.append("<div class=\"errors\">\n");
      section.append("<h1>Errors</h1>\n");
      appendFindings(findings.get(ISystemDescriptorFindingType.Severity.ERROR), section);
      section.append("</div>\n");
      dto.addContent(section.toString());
   }

   private void addWarnings(Multimap<ISystemDescriptorFindingType.Severity, SystemDescriptorFinding<?>> findings,
                            HtmlReportDto dto) {
      StringBuilder section = new StringBuilder();
      section.append("<div class=\"warnings\">\n");
      section.append("<h1>Warnings</h1>\n");
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
            sb.append(reportingOutputService.convert(finding.getMessage()));
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
      templateService.unpack(getClass().getPackage().getName() + "-" + HTML_REPORT_TEMPLATE_SUFFIX,
                             params,
                             outputDirectory,
                             true);
   }

   private static String getLocationString(ISourceLocation location) {
      return "<div class=\"source-location\">\n"
             + "<div class=\"position-information\">\n"
             + "<span class=\"file-name\">" + location.getPath() + "</span>\n"
             + "<span class=\"line\">" + location.getLineNumber() + "</span>\n"
             + "<span class=\"col\">" + location.getColumn() + "</span>\n"
             + "</div>\n"
             + "</div>\n";
   }
}
