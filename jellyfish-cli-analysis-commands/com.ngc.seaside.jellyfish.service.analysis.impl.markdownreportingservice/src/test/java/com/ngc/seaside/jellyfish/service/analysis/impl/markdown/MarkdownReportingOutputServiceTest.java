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
            + "and stuff and stuff and stuff";
      String expected =
            "<h1>This is a heading</h1>\n"
            + "<p>This is a description of <em>some errors</em> and <em>some warnings</em>\n"
            + "and stuff and stuff and stuff</p>\n";

      String result = markdownReportingOutputService.convert(md);
      assertEquals("did not convert markdown to HTML correctly!",
                   expected,
                   result);
   }
}
