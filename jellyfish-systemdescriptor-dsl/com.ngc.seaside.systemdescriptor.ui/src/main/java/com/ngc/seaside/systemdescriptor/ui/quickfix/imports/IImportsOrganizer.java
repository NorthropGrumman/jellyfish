/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.systemdescriptor.ui.quickfix.imports;

import com.google.inject.ImplementedBy;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Import;

import org.eclipse.xtext.ui.editor.model.IXtextDocument;

/**
 * Interface for organizing imports in a document.
 */
@ImplementedBy(DefaultImportsOrganizer.class)
public interface IImportsOrganizer {

   /**
    * Organizes the imports in the given document.
    *
    * @param document document
    */
   void organizeImports(IXtextDocument document);

   /**
    * Adds the given imports to the given document.
    *
    * @param document document
    * @param imports  list of imports
    */
   void addImports(IXtextDocument document, Import... imports);

}

