package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.Import
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.eclipse.xtext.formatting2.IFormattableDocument

class PackageFormatter extends AbstractSystemDescriptorFormatter {

	def dispatch format(Package packagez, extension IFormattableDocument document) {
		packagez.regionFor.keyword('package').prepend[noSpace]
		packagez.regionFor.feature(SystemDescriptorPackage.Literals.PACKAGE__NAME)
			.prepend[oneSpace]
			.append[setNewLines(2)]
		
		if(!packagez.imports.isEmpty()) {
			for (Import imports : packagez.imports) {
				imports.regionFor.keyword('import').append[oneSpace]
				if(imports == packagez.imports.last) {
					imports.append[newLines = 2];
				} else {
					imports.append[newLine];
				}
			}
		}
		packagez.element.format;
	}
}
