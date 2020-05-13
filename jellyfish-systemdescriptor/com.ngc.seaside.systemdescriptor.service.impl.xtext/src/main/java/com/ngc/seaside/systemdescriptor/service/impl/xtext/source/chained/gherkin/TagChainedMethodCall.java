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

import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinTag;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.GherkinTag;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.DetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.StringChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;

import gherkin.ast.Tag;

public class TagChainedMethodCall extends AbstractGherkinChainedMethodCall<GherkinTag, Tag> {

   /**
    * @param tag tag
    * @param context context
    */
   public TagChainedMethodCall(GherkinTag tag, ChainedMethodCallContext context) {
      super(tag, context);
      try {
         register(IGherkinTag.class.getMethod("getName"), this::thenGetName);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   @Override
   protected int getLength() {
      return element.getName().length() + 1;
   }
   
   private IChainedMethodCall<String> thenGetName() {
      String line = getLine(element.getLineNumber());
      int column = line.indexOf("@" + element.getName()) + 2;
      DetailedSourceLocation location = DetailedSourceLocation.of(element.getPath(), element.getLineNumber(), column,
               element.getName().length());
      return new StringChainedMethodCall(location, context);
   }

}
