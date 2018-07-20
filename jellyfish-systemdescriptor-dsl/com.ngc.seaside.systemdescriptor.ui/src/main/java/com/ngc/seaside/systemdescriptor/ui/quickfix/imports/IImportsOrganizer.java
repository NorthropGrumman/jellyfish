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

