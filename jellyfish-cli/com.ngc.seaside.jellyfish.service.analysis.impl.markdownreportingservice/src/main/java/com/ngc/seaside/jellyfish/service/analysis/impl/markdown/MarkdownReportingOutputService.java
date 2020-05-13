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

import com.google.common.base.Preconditions;

import com.ngc.seaside.jellyfish.service.analysis.api.IReportingOutputService;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;

/**
 * Uses <a href="https://github.com/vsch/flexmark-java">flexmark-java</a> to convert markdown to HTML.
 */
public class MarkdownReportingOutputService implements IReportingOutputService {

   private final Parser parser;
   private final HtmlRenderer renderer;

   /**
    * Creates a new {@code MarkdownReportingOutputService}.
    */
   public MarkdownReportingOutputService() {
      MutableDataSet options = new MutableDataSet();

      parser = Parser.builder(options).build();
      renderer = HtmlRenderer.builder(options).build();
   }

   @Override
   public String convert(String message) {
      Preconditions.checkNotNull(message, "message may not be null!");
      // Note this will not return a complete HTML document.  In only returns HTML.  You need to wrap the results
      // in <html>, <body> tags, etc.
      Node document = parser.parse(message);
      return renderer.render(document);
   }
}
