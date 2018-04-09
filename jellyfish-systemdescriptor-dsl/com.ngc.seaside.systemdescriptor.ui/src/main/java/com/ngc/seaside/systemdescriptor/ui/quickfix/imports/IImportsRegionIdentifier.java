package com.ngc.seaside.systemdescriptor.ui.quickfix.imports;

import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.xtext.resource.ILocationInFileProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.ITextRegion;
import org.eclipse.xtext.util.TextRegion;

/**
 * Interface for determining the text location of regions for imports in a package.
 */
@ImplementedBy(DefaultImportsRegionIdentifier.class)
public interface IImportsRegionIdentifier {

   /**
    * Returns the text region of the imports section of the given package. If the package does not have imports,
    * returns the region where the imports can be placed.
    *
    * @param pkg        package
    * @param resource   xtext resource
    * @param fullRegion if true, returns the region immediately following the package's package declaration and up to
    *                   the start of the package's element body
    * @return the text region of the imports section
    */
   ITextRegion getImportsRegion(Package pkg, XtextResource resource, boolean fullRegion);

   /**
    * Returns the text region immediately following the the imports section and up to the start of the package's element
    * body.
    *
    * @param pkg      package
    * @param resource xtext resource
    * @return the text region between the imports section and the package's element body
    */
   ITextRegion getImportsAppendRegion(Package pkg, XtextResource resource);

}
