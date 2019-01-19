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

import com.google.inject.ImplementedBy;

import org.eclipse.jface.text.IDocument;

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

