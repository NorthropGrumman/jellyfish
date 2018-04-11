package com.ngc.seaside.systemdescriptor.ui.quickfix;

import com.google.inject.ImplementedBy;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentRewriteSession;
import org.eclipse.jface.text.DocumentRewriteSessionType;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.text.undo.DocumentUndoManagerRegistry;
import org.eclipse.text.undo.IDocumentUndoManager;

/**
 * Interface for making changes to an {@link IDocument}.
 */
@ImplementedBy(DefaultDocumentWriter.class)
public interface IDocumentWriter {

   /**
    * Makes a change to the given document by replacing the given text sequence in the document with the given text.
    *
    * @param document document to change
    * @param offset   offset in document to replace
    * @param length   length of text from offset to replace
    * @param text     replacement text
    */
   void replace(IDocument document, int offset, int length, String text);

}

