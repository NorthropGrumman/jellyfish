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
