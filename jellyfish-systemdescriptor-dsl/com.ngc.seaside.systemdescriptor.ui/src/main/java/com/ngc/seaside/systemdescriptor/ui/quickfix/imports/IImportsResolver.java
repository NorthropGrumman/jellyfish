package com.ngc.seaside.systemdescriptor.ui.quickfix.imports;

import com.google.inject.ImplementedBy;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Import;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

import org.eclipse.xtext.resource.XtextResource;

import java.util.List;

/**
 * Interface for determining the list of imports that a package needs.
 */
@FunctionalInterface
@ImplementedBy(DefaultImportsResolver.class)
public interface IImportsResolver {

   /**
    * Returns a list of imports that the given package needs.
    *
    * @param pkg      package
    * @param resource xtext resource
    * @return a list of imports that the given package needs
    */
   List<Import> resolveImports(Package pkg, XtextResource resource);

}
