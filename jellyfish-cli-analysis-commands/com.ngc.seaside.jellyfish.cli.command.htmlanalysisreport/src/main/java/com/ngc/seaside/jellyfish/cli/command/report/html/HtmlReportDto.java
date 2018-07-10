package com.ngc.seaside.jellyfish.cli.command.report.html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * The DTO used to generate the HTML report.
 */
public class HtmlReportDto {

   /**
    * The file name of the report.
    */
   private String reportName;

   /**
    * The title of the report.
    */
   private String title;

   /**
    * The HTML contents of the report.
    */
   private final List<String> contents = new ArrayList<>();

   /**
    * Gets the file name of the report.
    */
   public String getReportName() {
      return reportName;
   }

   /**
    * Sets the file name of the report.
    */
   public HtmlReportDto setReportName(String reportName) {
      this.reportName = reportName;
      return this;
   }

   /**
    * Gets the title of the report.
    */
   public String getTitle() {
      return title;
   }

   /**
    * Sets title of the report.
    */
   public HtmlReportDto setTitle(String title) {
      this.title = title;
      return this;
   }

   /**
    * Gets the contents of the report.
    */
   public List<String> getContents() {
      return contents;
   }

   /**
    * Adds some HTML content to the report.
    */
   public HtmlReportDto addContent(String content) {
      contents.add(content);
      return this;
   }

   /**
    * Adds some HTML content to the report.
    */
   public HtmlReportDto addContent(String content, String... contents) {
      this.contents.add(content);
      if (contents != null) {
         this.contents.addAll(Arrays.asList(contents));
      }
      return this;
   }

   /**
    * Adds some HTML content to the report.
    */
   public HtmlReportDto addContent(Collection<String> contents) {
      this.contents.addAll(contents);
      return this;
   }
}
