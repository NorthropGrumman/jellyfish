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
