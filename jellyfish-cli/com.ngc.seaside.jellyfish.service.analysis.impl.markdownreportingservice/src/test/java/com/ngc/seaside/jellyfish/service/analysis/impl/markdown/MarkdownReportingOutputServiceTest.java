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
