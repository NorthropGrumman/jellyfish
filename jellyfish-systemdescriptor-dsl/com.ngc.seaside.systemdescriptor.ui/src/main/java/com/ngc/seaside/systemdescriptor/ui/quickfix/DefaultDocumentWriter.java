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
package com.ngc.seaside.systemdescriptor.ui.quickfix;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentRewriteSession;
import org.eclipse.jface.text.DocumentRewriteSessionType;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.text.undo.DocumentUndoManagerRegistry;
import org.eclipse.text.undo.IDocumentUndoManager;

class DefaultDocumentWriter implements IDocumentWriter {

   @Override
   public void replace(IDocument document, int offset, int length, String text) {
      IDocumentUndoManager undoManager = DocumentUndoManagerRegistry.getDocumentUndoManager(document);
      DocumentRewriteSession session = null;
      if (document instanceof IDocumentExtension4) {
         session = ((IDocumentExtension4) document).startRewriteSession(DocumentRewriteSessionType.UNRESTRICTED);
      }
      if (undoManager != null) {
         undoManager.beginCompoundChange();
      }
      try {
         document.replace(offset, length, text);
      } catch (BadLocationException e) {
         throw new IllegalArgumentException(e);
      } finally {
         if (undoManager != null) {
            undoManager.endCompoundChange();
         }
         if (session != null) {
            ((IDocumentExtension4) document).stopRewriteSession(session);
         }
      }
   }

}
