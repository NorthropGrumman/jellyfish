package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.Import
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.eclipse.xtext.formatting2.IFormattableDocument

class PackageFormatter extends AbstractSystemDescriptorFormatter {
    def dispatch format(Package pkg, extension IFormattableDocument document) {
        pkg.regionFor.keyword('package').prepend[noIndentation]
        pkg.regionFor.feature(SystemDescriptorPackage.Literals.PACKAGE__NAME).append[setNewLines(2)]

        if(pkg.getImports().size != 0) {
            for (Import imports : pkg.imports) {
                if(imports == pkg.imports.last) {
                    imports.append[newLines = 2];
                } else {
                    imports.append[newLine];
                }
            }
        }

        pkg.element.format;
    }
}
