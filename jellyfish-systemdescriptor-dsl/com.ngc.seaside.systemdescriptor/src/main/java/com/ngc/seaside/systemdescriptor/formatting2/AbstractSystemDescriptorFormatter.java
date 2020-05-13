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
package com.ngc.seaside.systemdescriptor.formatting2;

import com.google.common.base.Preconditions;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.formatting2.AbstractFormatter2;
import org.eclipse.xtext.formatting2.FormatterRequest;
import org.eclipse.xtext.formatting2.IFormattableDocument;

/**
 * Base class for System Descriptor formatters. All formatters should extend
 * this class.
 */
public class AbstractSystemDescriptorFormatter extends AbstractFormatter2 {

   private SystemDescriptorFormatter rootFormatter;

   @Override
   public void format(Object obj, IFormattableDocument document) {
      doFormat(obj, document);
   }

   @Override
   public void initialize(FormatterRequest request) {
      // Increase visibility of this method.
      super.initialize(request);
   }

   @Override
   public void reset() {
      // Increase visibility of this method.
      super.reset();
   }

   public void setRootFormatter(SystemDescriptorFormatter rootFormatter) {
      // This is called by SystemDescriptorFormatter at registration time.
      this.rootFormatter = rootFormatter;
   }

   @Override
   protected void _format(Object obj, IFormattableDocument document) {
      doFormat(obj, document);
   }

   @Override
   protected void _format(EObject obj, IFormattableDocument document) {
      doFormat(obj, document);
   }

   private void doFormat(Object obj, IFormattableDocument document) {
      Preconditions.checkState(
            rootFormatter != null,
            "rootFormatter not set!  Ensure this formatter has been registered with SystemDescriptorFormatter"
                  + " correctly!");
      rootFormatter.format(obj, document);
   }
}
