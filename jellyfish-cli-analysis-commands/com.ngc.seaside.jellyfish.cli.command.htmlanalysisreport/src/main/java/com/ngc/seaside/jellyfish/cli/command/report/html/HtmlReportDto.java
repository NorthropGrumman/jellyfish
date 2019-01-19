/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
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
