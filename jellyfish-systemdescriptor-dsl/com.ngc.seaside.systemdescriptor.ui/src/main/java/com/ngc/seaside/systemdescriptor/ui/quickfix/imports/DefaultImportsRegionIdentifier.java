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
