/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.systemdescriptor.ui.quickfix.imports;

import com.google.inject.Inject;

import com.ngc.seaside.systemdescriptor.systemDescriptor.impl.ImportImpl;
import com.ngc.seaside.systemdescriptor.ui.quickfix.IDocumentWriter;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.diagnostics.Diagnostic;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.ui.editor.model.edit.IssueModificationContext;
import org.eclipse.xtext.ui.editor.quickfix.DefaultQuickfixProvider;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.xbase.validation.IssueCodes;

import java.util.Set;

public class ImportQuickfixProvider extends DefaultQuickfixProvider {

   @Inject
   private IImportsOrganizer importsOrganizer;

   @Inject
   private IssueModificationContext.Factory factory;

   @Inject
   private IReferenceResolver referenceResolver;

   @Inject
   private IQualifiedNameConverter qualifiedNameConverter;

   @Inject
   private IDocumentWriter writer;

   @Fix(IssueCodes.IMPORT_UNUSED)
   public void fixUnusedImport(Issue issue, IssueResolutionAcceptor acceptor) {
      removeImport(issue, acceptor);
      organizeImports(issue, acceptor);
   }

   @Fix(IssueCodes.IMPORT_DUPLICATE)
   public void fixDuplicateImports(Issue issue, IssueResolutionAcceptor acceptor) {
      removeImport(issue, acceptor);
      organizeImports(issue, acceptor);
   }

   @Fix(IssueCodes.IMPORT_COLLISION)
   public void fixImportCollision(Issue issue, IssueResolutionAcceptor acceptor) {
      removeImport(issue, acceptor);
      organizeImports(issue, acceptor);
   }

   @Fix(IssueCodes.IMPORT_CONFLICT)
   public void fixImportConflict(Issue issue, IssueResolutionAcceptor acceptor) {
      removeImport(issue, acceptor);
      organizeImports(issue, acceptor);
   }

   @Fix(IssueCodes.IMPORT_UNRESOLVED)
   public void fixUnresolvedImports(Issue issue, IssueResolutionAcceptor acceptor) {
      removeImport(issue, acceptor);
      organizeImports(issue, acceptor);
   }

   @Fix(Diagnostic.LINKING_DIAGNOSTIC)
   public void fixedMissingImports(Issue issue, IssueResolutionAcceptor acceptor) throws BadLocationException {
      addImports(issue, acceptor);
   }

   private void removeImport(Issue issue, IssueResolutionAcceptor acceptor) {
      acceptor.accept(issue, "Remove unused import", "", getRemoveImportImage(), context -> {
         IXtextDocument document = context.getXtextDocument();
         writer.replace(document, issue.getOffset(), issue.getLength() + 1, "");
      });
   }

   private void organizeImports(Issue issue, IssueResolutionAcceptor acceptor) {
      acceptor.accept(issue, "Organize imports", "", getOrganizeImportsImage(), context -> {
         IXtextDocument document = context.getXtextDocument();
         importsOrganizer.organizeImports(document);
      });
   }

   private void addImports(Issue issue, IssueResolutionAcceptor acceptor) throws BadLocationException {
      IModificationContext modificationContext = factory.createModificationContext(issue);
      IXtextDocument document = modificationContext.getXtextDocument();
      String reference = document.get(issue.getOffset(), issue.getLength());
      ResourceSet resourceSet = document.readOnly(state -> {
         return state.getResourceSet();
      });
      Set<QualifiedName> names = referenceResolver.findPossibleTypes(reference, resourceSet, __ -> true);
      for (QualifiedName name : names) {
         String qualifiedName = name.toString();
         String qualifiedPackage = qualifiedName.substring(0, qualifiedName.lastIndexOf('.'));
         acceptor.accept(issue,
                         "Import " + reference + " (" + qualifiedPackage + ")",
                         "",
                         getAddImportImage(),
                         context -> importsOrganizer.addImports(context.getXtextDocument(), new ImportImpl() {
                            @Override
                            public String getImportedNamespace() {
                               return qualifiedNameConverter.toString(name);
                            }

                            @Override
                            public String toString() {
                               return getImportedNamespace();
                            }
                         }));
      }
   }

   private String getRemoveImportImage() {
      return "";
   }

   private String getOrganizeImportsImage() {
      return "";
   }

   private String getAddImportImage() {
      return "";
   }
}
