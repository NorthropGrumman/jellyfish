package com.ngc.seaside.systemdescriptor.ui.quickfix.imports;

import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Import;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.ui.quickfix.IDocumentWriter;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.util.ITextRegion;
import org.eclipse.xtext.util.ReplaceRegion;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

