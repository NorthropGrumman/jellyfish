/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
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
