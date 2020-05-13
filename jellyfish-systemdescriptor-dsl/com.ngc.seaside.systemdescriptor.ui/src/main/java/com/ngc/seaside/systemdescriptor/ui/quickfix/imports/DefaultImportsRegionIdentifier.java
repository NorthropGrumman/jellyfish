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

import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.xtext.resource.ILocationInFileProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.ITextRegion;
import org.eclipse.xtext.util.TextRegion;

class DefaultImportsRegionIdentifier implements IImportsRegionIdentifier {

   /**
    * A provider that is used to determine the location of elements in a document.
    */
   @Inject
   private ILocationInFileProvider locationInFileProvider;

   @Override
   public ITextRegion getImportsRegion(Package pkg, XtextResource resource, boolean fullRegion) {
      ITextRegion packageRegion = locationInFileProvider.getFullTextRegion(
            pkg,
            SystemDescriptorPackage.Literals.PACKAGE__NAME,
            0);
      ITextRegion bodyRegion = locationInFileProvider.getFullTextRegion(
            pkg,
            SystemDescriptorPackage.Literals.PACKAGE__ELEMENT,
            0);
      int start = packageRegion.getOffset() + packageRegion.getLength();
      int end = bodyRegion.getOffset();
      if (!fullRegion) {
         ITextRegion importsRegion = locationInFileProvider.getFullTextRegion(
               pkg,
               SystemDescriptorPackage.Literals.PACKAGE__IMPORTS,
               0);
         for (int i = 0; i < pkg.getImports().size(); i++) {
            importsRegion = importsRegion.merge(locationInFileProvider.getSignificantTextRegion(
                  pkg,
                  SystemDescriptorPackage.Literals.PACKAGE__IMPORTS,
                  i + 1));
         }
         start = Math.max(start, importsRegion.getOffset());
         end = Math.min(end, importsRegion.getOffset() + importsRegion.getLength());
      }
      return new TextRegion(Math.max(0, start), Math.max(0, end - start));
   }

   @Override
   public ITextRegion getImportsAppendRegion(Package pkg, XtextResource resource) {
      ITextRegion bodyRegion = locationInFileProvider.getFullTextRegion(
            pkg,
            SystemDescriptorPackage.Literals.PACKAGE__ELEMENT,
            0);

      final int start;
      if (pkg.getImports().isEmpty()) {
         ITextRegion packageRegion = locationInFileProvider.getFullTextRegion(
               pkg,
               SystemDescriptorPackage.Literals.PACKAGE__NAME,
               0);
         start = packageRegion.getOffset() + packageRegion.getLength() + 1;
      } else {
         ITextRegion importsRegion = locationInFileProvider.getFullTextRegion(
               pkg,
               SystemDescriptorPackage.Literals.PACKAGE__IMPORTS,
               0);
         for (int i = 0; i < pkg.getImports().size(); i++) {
            importsRegion = importsRegion.merge(locationInFileProvider.getFullTextRegion(
                  pkg,
                  SystemDescriptorPackage.Literals.PACKAGE__IMPORTS,
                  i));
         }
         start = importsRegion.getOffset() + importsRegion.getLength();
      }

      final int end = bodyRegion.getOffset();
      return new TextRegion(Math.max(0, start), Math.max(0, end - start));
   }

}
