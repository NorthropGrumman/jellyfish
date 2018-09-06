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
package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.Import
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.eclipse.xtext.formatting2.IFormattableDocument

class PackageFormatter extends AbstractSystemDescriptorFormatter {
    def dispatch format(Package pkg, extension IFormattableDocument document) {
        pkg.regionFor.keyword('package')
        	.prepend[noSpace]
        
        pkg.regionFor.feature(SystemDescriptorPackage.Literals.PACKAGE__NAME)
            .prepend[oneSpace]
        	.append[newLines = 2]

        if (pkg.getImports().size != 0) {
            for (Import import : pkg.imports) {
            	import.prepend[noSpace]
            	import.regionFor.feature(SystemDescriptorPackage.Literals.IMPORT__IMPORTED_NAMESPACE)
            		.prepend[oneSpace]
            	
                if (import == pkg.imports.last) {
                    import.append[newLines = 2];
                } else {
                    import.append[newLine];
                }
            }
        }

        pkg.element.format;
    }
}
