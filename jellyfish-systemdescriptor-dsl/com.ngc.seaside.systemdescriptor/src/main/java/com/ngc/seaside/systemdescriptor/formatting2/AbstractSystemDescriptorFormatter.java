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
