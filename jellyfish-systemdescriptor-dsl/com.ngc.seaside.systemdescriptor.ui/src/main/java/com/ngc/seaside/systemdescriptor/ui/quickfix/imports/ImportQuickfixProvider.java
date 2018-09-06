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
