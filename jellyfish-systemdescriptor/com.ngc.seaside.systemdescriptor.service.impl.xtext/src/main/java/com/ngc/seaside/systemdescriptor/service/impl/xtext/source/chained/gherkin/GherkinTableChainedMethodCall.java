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

import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinRow;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinTable;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.GherkinTable;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.CollectionChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.OptionalChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.UnknownSourceLocationException;

import gherkin.ast.Node;

import java.util.List;
import java.util.Optional;

public class GherkinTableChainedMethodCall
         extends AbstractGherkinChainedMethodCall<GherkinTable, Node> {

   /**
    * @param table table
    * @param context context
    */
   public GherkinTableChainedMethodCall(GherkinTable table, ChainedMethodCallContext context) {
      super(table, context);
      try {
         register(IGherkinTable.class.getMethod("getHeader"), this::thenGetHeader);
         register(IGherkinTable.class.getMethod("getRows"), this::thenGetRows);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<Optional<IGherkinRow>> thenGetHeader() {
      IGherkinRow row = element.getHeader()
               .orElseThrow(() -> new UnknownSourceLocationException("Table does not have a header row"));
      IChainedMethodCall<IGherkinRow> methodCall = context.getChainedMethodCallForElement(row);
      return new OptionalChainedMethodCall<>(methodCall, context);
   }

   private IChainedMethodCall<List<IGherkinRow>> thenGetRows() {
      List<IGherkinRow> rows = element.getRows();
      return new CollectionChainedMethodCall<>(getLocation(), rows, context::getChainedMethodCallForElement, context);
   }

}
