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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.gherkin;

import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.IGherkinUnwrappable;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.DetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.AbstractChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;

import gherkin.ast.Node;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract class for implementations of {@link IChainedMethodCall} for Gherkin elements.
 */
public class AbstractGherkinChainedMethodCall<T extends IGherkinUnwrappable<N>, N extends Node>
         extends AbstractChainedMethodCall<T> {

   protected final T element;
   protected final N gherkinElement;

   /**
    * @param element element
    * @param context context
    */
   public AbstractGherkinChainedMethodCall(T element, ChainedMethodCallContext context) {
      super(element, context);
      this.element = element;
      this.gherkinElement = element.unwrap();
   }

   @Override
   public IDetailedSourceLocation getLocation() {
      return DetailedSourceLocation.of(element.getPath(), element.getLineNumber(), getColumn(), getLength());
   }

   /**
    * Returns the column of the element's source location.
    * 
    * @return the column of the element's source location
    */
   protected int getColumn() {
      return element.getColumn();
   }

   /**
    * Returns the length of the element's source location. By default this is {@code -1} since length information is not
    * available from Gherkin.
    * 
    * @return the length of the element's source location
    */
   protected int getLength() {
      return -1;
   }

   /**
    * Returns the text of the line at the given line number.
    * 
    * @param lineNumber line number (1-based)
    * @return the text of the line at the given line number
    */
   protected String getLine(int lineNumber) {
      try {
         return Files.lines(element.getPath())
                  .skip(lineNumber - 1)
                  .findFirst()
                  .orElseThrow(() -> new IllegalStateException(
                           element.getPath() + " does not have " + lineNumber + " lines"));
      } catch (IOException e) {
         throw new UncheckedIOException(e);
      }
   }

   /**
    * Returns the list of the lines between the given line numbers. Returns an empty list if the lineNumberStart is
    * too large.
    * 
    * @param lineNumberStart start line number (inclusive 1-based)
    * @param lineNumberEnd end line number (inclusive 1-based), can be larger than the number of lines in the file
    * @return the list of the lines between the given line numbers
    */
   protected List<String> getLines(int lineNumberStart, int lineNumberEnd) {
      try {
         return Files.lines(element.getPath())
                  .skip(lineNumberStart - 1)
                  .limit(lineNumberEnd - lineNumberStart + 1)
                  .collect(Collectors.toList());
      } catch (IOException e) {
         throw new UncheckedIOException(e);
      }
   }
}
