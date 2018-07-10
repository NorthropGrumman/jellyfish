package com.ngc.seaside.jellyfish.service.analysis.impl.markdown;

import com.google.common.base.Preconditions;

import com.ngc.seaside.jellyfish.service.analysis.api.IReportingOutputService;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
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
