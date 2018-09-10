/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.service.analysis.impl.markdown;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MarkdownReportingOutputServiceTest {

   private MarkdownReportingOutputService markdownReportingOutputService;

   @Before
   public void setup() {
      markdownReportingOutputService = new MarkdownReportingOutputService();
   }

   @Test
   public void testDoesCovertMarkdownToHtml() {
      String md =
            "# This is a heading" + System.lineSeparator()
            + "This is a description of *some errors* and _some warnings_" + System.lineSeparator()
            + "and stuff and stuff and stuff" + System.lineSeparator()
            + "## This is a sub heading" + System.lineSeparator()
            + "This is more sub heading topics with link [www.google.com](www.google.com)";
      String expected =
            "<h1>This is a heading</h1>\n"
            + "<p>This is a description of <em>some errors</em> and <em>some warnings</em>\n"
            + "and stuff and stuff and stuff</p>\n"
            + "<h2>This is a sub heading</h2>\n"
            + "<p>This is more sub heading topics with link <a href=\"www.google.com\">www.google.com</a></p>\n";

      String result = markdownReportingOutputService.convert(md);
      assertEquals("did not convert markdown to HTML correctly!",
                   expected,
                   result);
   }
}
