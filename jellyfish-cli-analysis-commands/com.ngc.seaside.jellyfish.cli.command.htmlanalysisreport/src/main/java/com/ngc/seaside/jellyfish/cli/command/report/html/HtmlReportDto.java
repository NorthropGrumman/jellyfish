/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
