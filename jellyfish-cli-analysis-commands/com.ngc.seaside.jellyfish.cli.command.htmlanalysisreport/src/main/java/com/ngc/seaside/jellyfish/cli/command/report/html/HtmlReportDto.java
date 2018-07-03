package com.ngc.seaside.jellyfish.cli.command.report.html;

public class HtmlReportDto {

   private String reportName;
   private String content;

   public String getReportName() {
      return reportName;
   }

   public HtmlReportDto setReportName(String reportName) {
      this.reportName = reportName;
      return this;
   }

   public String getContent() {
      return content;
   }

   public HtmlReportDto setContent(String content) {
      this.content = content;
      return this;
   }
}
