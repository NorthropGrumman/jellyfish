package com.ngc.seaside.jellyfish.cli.command.report.html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class HtmlReportDto {

   private String reportName;
   private final List<String> contents = new ArrayList<>();

   public String getReportName() {
      return reportName;
   }

   public HtmlReportDto setReportName(String reportName) {
      this.reportName = reportName;
      return this;
   }

   public List<String> getContents() {
      return contents;
   }

   public HtmlReportDto addContent(String content) {
      contents.add(content);
      return this;
   }

   public HtmlReportDto addContent(String content, String... contents) {
      this.contents.add(content);
      if (contents != null) {
         this.contents.addAll(Arrays.asList(contents));
      }
      return this;
   }

   public HtmlReportDto addContent(Collection<String> contents) {
      this.contents.addAll(contents);
      return this;
   }
}
