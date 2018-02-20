package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.Import
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.eclipse.xtext.formatting2.IFormattableDocument

class PackageFormatter extends AbstractSystemDescriptorFormatter {

	def dispatch format(Package packagez, extension IFormattableDocument document) {
		packagez.regionFor.keyword('package').prepend[noIndentation]
		packagez.regionFor.feature(SystemDescriptorPackage.Literals.PACKAGE__NAME).append[setNewLines(2)]

		if(packagez.getImports().size != 0) {

			for (Import imports : packagez.imports) {
				if(imports == packagez.imports.last) {
					imports.append[setNewLines(2)];
				} else {
					imports.append[setNewLines(1)];
				}
			}
		}

		packagez.element.format;
	}
}
