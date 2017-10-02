package com.ngc.seaside.systemdescriptor.ui.quickfix.imports;

import com.google.inject.Inject;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Import;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentRewriteSession;
import org.eclipse.jface.text.DocumentRewriteSessionType;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.utils.EditorUtils;
import org.eclipse.xtext.util.ITextRegion;
import org.eclipse.xtext.util.ReplaceRegion;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Organizes the import section of a system descriptor file
 */
public class OrganizeImportsHandler extends AbstractHandler {

   /**
    * Used to construct a QualifiedName from a fually qualified name
    * represented as a string.
    */
   @Inject
   private IQualifiedNameConverter qualifiedNameConverter;
   
   @Inject
   private IImportsResolver importsResolver;

   @Inject
   private IImportsRegionIdentifier importsRegionIdentifier;
   
   @Override
   public Object execute(ExecutionEvent event) throws ExecutionException {
      XtextEditor editor = EditorUtils.getActiveXtextEditor(event);
      if (editor != null) {
         try {
            organizeImports(editor.getDocument());
         } catch (BadLocationException e) {
            throw new ExecutionException("", e);
         }
      }
      return null;
   }

   /**
    * Organizes the imports in the given document.
    * 
    * @param document document
    * @throws BadLocationException
    */
   public void organizeImports(IXtextDocument document) throws BadLocationException {
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
         DocumentRewriteSession session = null;
         if (document instanceof IDocumentExtension4) {
            session = ((IDocumentExtension4) document).startRewriteSession(DocumentRewriteSessionType.UNRESTRICTED);
         }
         document.replace(replacement.getOffset(), replacement.getLength(), replacement.getText());
         if (session != null) {
            ((IDocumentExtension4) document).stopRewriteSession(session);
         }
      }
   }

   public void addImports(IXtextDocument document, QualifiedName... imports) throws BadLocationException {
      ITextRegion importsRegion = document.readOnly(state -> {
         Package pkg = getPackage(state).orElseThrow(() -> new IllegalStateException());
         ITextRegion region = importsRegionIdentifier.getImportsAppendRegion(pkg, state);
         return region;
      });
      if (imports.length > 0) {

         String importsText = System.lineSeparator()
            + Stream.of(imports).map(qualifiedNameConverter::toString).map("import "::concat).collect(
               Collectors.joining(System.lineSeparator()))
            + System.lineSeparator() + System.lineSeparator();
         DocumentRewriteSession session = null;
         if (document instanceof IDocumentExtension4) {
            session = ((IDocumentExtension4) document).startRewriteSession(DocumentRewriteSessionType.UNRESTRICTED);
         }
         document.replace(importsRegion.getOffset(), importsRegion.getLength(), importsText);
         if (session != null) {
            ((IDocumentExtension4) document).stopRewriteSession(session);
         }
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
