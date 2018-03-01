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
