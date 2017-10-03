package com.ngc.seaside.systemdescriptor.ui.quickfix.imports;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.util.ITextRegion;
import org.eclipse.xtext.util.ReplaceRegion;

import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Import;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

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
    * @param imports list of imports
    */
   void addImports(IXtextDocument document, Import... imports);

}

class DefaultImportsOrganizer implements IImportsOrganizer {

   @Inject
   private IImportsResolver importsResolver;

   @Inject
   private IImportsRegionIdentifier importsRegionIdentifier;

   @Inject
   private IDocumentWriter writer;

   public void organizeImports(IXtextDocument document) {
      ReplaceRegion replacement = document.readOnly(state -> {
         Package pkg = getPackage(state).orElseThrow(() -> new IllegalStateException());
         ITextRegion oldImports = importsRegionIdentifier.getImportsRegion(pkg, state, true);
         try {
            List<Import> imports = importsResolver.resolveImports(pkg, state);
            return new ReplaceRegion(oldImports,
               System.lineSeparator() + System.lineSeparator() + imports.stream()
                                                                        .map(Import::getImportedNamespace)
                                                                        .map("import "::concat)
                                                                        .collect(
                                                                           Collectors.joining(System.lineSeparator()))
                  + System.lineSeparator() + System.lineSeparator());
         } catch (CancellationException e) {

         }
         return null;
      });

      if (replacement != null) {
         writer.replace(document, replacement.getOffset(), replacement.getLength(), replacement.getText());
      }
   }

   public void addImports(IXtextDocument document, Import... imports) {
      ITextRegion importsRegion = document.readOnly(state -> {
         Package pkg = getPackage(state).orElseThrow(() -> new IllegalStateException());
         ITextRegion region = importsRegionIdentifier.getImportsAppendRegion(pkg, state);
         return region;
      });
      if (imports.length > 0) {
         String importsText = System.lineSeparator()
            + Stream.of(imports).map(Import::getImportedNamespace).map("import "::concat).collect(
               Collectors.joining(System.lineSeparator()))
            + System.lineSeparator() + System.lineSeparator();
         writer.replace(document, importsRegion.getOffset(), importsRegion.getLength(), importsText);
      }
   }

   private Optional<Package> getPackage(XtextResource state) {
      List<EObject> objects = state.getContents();
      EObject object;
      if (objects.size() != 1 || !((object = objects.get(0)) instanceof Package)) {
         return Optional.empty();
      } else {
         return Optional.of((Package) object);
      }
   }

}
