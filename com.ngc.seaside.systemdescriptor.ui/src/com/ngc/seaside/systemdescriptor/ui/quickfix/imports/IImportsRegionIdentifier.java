package com.ngc.seaside.systemdescriptor.ui.quickfix.imports;

import org.eclipse.xtext.resource.ILocationInFileProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.ITextRegion;
import org.eclipse.xtext.util.TextRegion;

import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

/**
 * Interface for determining the text location of regions for imports in a package.
 */
@ImplementedBy(DefaultImportsRegionIdentifier.class)
public interface IImportsRegionIdentifier {

   /**
    * Returns the text region of the imports section of the given package. If the package does not have imports, returns the region where the imports can be placed.
    * 
    * @param pkg package
    * @param resource xtext resource
    * @full if true, returns the region immediately following the package's package declaration and up to the start of the package's element body
    * @return the text region of the imports section
    */
   public ITextRegion getImportsRegion(Package pkg, XtextResource resource, boolean fullRegion);

   /**
    * Returns the text region immediately following the the imports section and up to the start of the package's element body.
    * 
    * @param pkg package
    * @param resource xtext resource
    * @return the text region between the imports section and the package's element body
    */
   public ITextRegion getImportsAppendRegion(Package pkg, XtextResource resource);

}

class DefaultImportsRegionIdentifier implements IImportsRegionIdentifier {

   /**
    * A provider that is used to determine the location of elements in a document.
    */
   @Inject
   private ILocationInFileProvider locationInFileProvider;

   @Override
   public ITextRegion getImportsRegion(Package pkg, XtextResource resource, boolean fullRegion) {
      ITextRegion packageRegion = locationInFileProvider.getFullTextRegion(pkg,
         SystemDescriptorPackage.Literals.PACKAGE__NAME,
         0);
      ITextRegion bodyRegion = locationInFileProvider.getFullTextRegion(pkg,
         SystemDescriptorPackage.Literals.PACKAGE__ELEMENT,
         0);
      int start = packageRegion.getOffset() + packageRegion.getLength();
      int end = bodyRegion.getOffset();
      if (!fullRegion) {
         ITextRegion importsRegion = locationInFileProvider.getFullTextRegion(pkg,
            SystemDescriptorPackage.Literals.PACKAGE__IMPORTS,
            0);
         for (int i = 0; i < pkg.getImports().size(); i++) {
            importsRegion = importsRegion.merge(locationInFileProvider.getSignificantTextRegion(pkg,
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
      ITextRegion bodyRegion = locationInFileProvider.getFullTextRegion(pkg,
         SystemDescriptorPackage.Literals.PACKAGE__ELEMENT,
         0);

      final int start;
      if (pkg.getImports().isEmpty()) {
         ITextRegion packageRegion = locationInFileProvider.getFullTextRegion(pkg,
            SystemDescriptorPackage.Literals.PACKAGE__NAME,
            0);
         start = packageRegion.getOffset() + packageRegion.getLength() + 1;
      } else {
         ITextRegion importsRegion = locationInFileProvider.getFullTextRegion(pkg,
            SystemDescriptorPackage.Literals.PACKAGE__IMPORTS,
            0);
         for (int i = 0; i < pkg.getImports().size(); i++) {
            importsRegion = importsRegion.merge(locationInFileProvider.getFullTextRegion(pkg,
               SystemDescriptorPackage.Literals.PACKAGE__IMPORTS,
               i));
         }
         start = importsRegion.getOffset() + importsRegion.getLength();
      }

      final int end = bodyRegion.getOffset();
      return new TextRegion(Math.max(0, start), Math.max(0, end - start));
   }

}
