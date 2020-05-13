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

import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinCell;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.GherkinCell;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.DetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.StringChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;

import gherkin.ast.TableCell;

public class GherkinCellChainedMethodCall
         extends AbstractGherkinChainedMethodCall<GherkinCell, TableCell> {

   /**
    * @param cell cell
    * @param context context
    */
   public GherkinCellChainedMethodCall(GherkinCell cell, ChainedMethodCallContext context) {
      super(cell, context);
      try {
         register(IGherkinCell.class.getMethod("getValue"), this::thenGetValue);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<String> thenGetValue() {
      IDetailedSourceLocation location = context.getSourceLocation(element);
      location = new DetailedSourceLocation.Builder(location).setLength(element.getValue().length()).build();
      return new StringChainedMethodCall(location, context);
   }

   @Override
   protected int getColumn() {
      String line = getLine(element.getLineNumber());
      return line.lastIndexOf('|', element.getColumn()) + 1;
   }

   @Override
   protected int getLength() {
      String line = getLine(element.getLineNumber());
      int length = line.indexOf('|', element.getColumn()) - line.lastIndexOf('|', element.getColumn()) + 1;
      return length;
   }

}
