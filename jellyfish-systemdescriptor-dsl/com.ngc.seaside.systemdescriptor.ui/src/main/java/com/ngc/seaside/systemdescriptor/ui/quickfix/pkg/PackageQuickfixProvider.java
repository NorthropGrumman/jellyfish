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
package com.ngc.seaside.systemdescriptor.ui.quickfix.pkg;

import com.google.inject.Inject;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.ui.quickfix.IDocumentWriter;
import com.ngc.seaside.systemdescriptor.validation.SdIssueCodes;

import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.ui.editor.model.edit.IssueModificationContext;
import org.eclipse.xtext.ui.editor.quickfix.DefaultQuickfixProvider;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;

import java.util.List;

public class PackageQuickfixProvider extends DefaultQuickfixProvider {

   @Inject
   private IssueModificationContext.Factory factory;

   @Inject
   private IDocumentWriter writer;

   /**
    * Fixes the name of a package.
    */
   @Fix(SdIssueCodes.MISMATCHED_PACKAGE)
   public void fixPackageName(Issue issue, IssueResolutionAcceptor acceptor) {
      IModificationContext modificationContext = factory.createModificationContext(issue);
      IXtextDocument xtextDocument = modificationContext.getXtextDocument();
      final Package
            pkg =
            (Package) xtextDocument.readOnly(state -> state.getEObject(issue.getUriToProblem().fragment()));

      String firstPackageNameElement = pkg.getName().substring(0, pkg.getName().indexOf("."));
      List<String> uriSegments = issue.getUriToProblem().segmentsList();
      StringBuffer sb = new StringBuffer();

      for (int i = uriSegments.size() - 1; i > 0; i--) {
         if (uriSegments.get(i).indexOf(".sd") > 0) {
            continue;
         }
         String curr = uriSegments.get(i);
         sb.append(curr);
         if ((curr.indexOf(".sd") < 0) && (curr.equals(firstPackageNameElement))) {
            break;
         }
         sb.append(".");
      }

      String[] newPackage = sb.toString().split("\\.");

      StringBuilder pack = new StringBuilder();
      for (int i = newPackage.length - 1; i >= 0; i--) {
         pack.append(newPackage[i]);
         if (i != 0) {
            pack.append(".");
         }
      }

      String packagePath = pack.toString();

      // TODO: Implement this fix.
      // acceptor.accept(issue,
      // String.format("Move '%s.sd' to '%s'", pkg.getElement().getName(), pkg.getName()),
      // "",
      // "",
      // context -> pkg.setName(packagePath));

      acceptor.accept(issue, String.format("Change package declaration to '%s'", packagePath), "", "", context -> {
         IXtextDocument document = context.getXtextDocument();
         writer.replace(document, issue.getOffset(), issue.getLength(), packagePath);
      });
   }

}
