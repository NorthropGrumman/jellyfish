package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.Enumeration
import com.ngc.seaside.systemdescriptor.systemDescriptor.EnumerationValueDeclaration
import org.eclipse.xtext.formatting2.IFormattableDocument

class EnumerationFormatter extends AbstractSystemDescriptorFormatter {

	def dispatch void format(Enumeration enumeration, extension IFormattableDocument document) {
//		if (enumeration.metadata != null && !enumeration.values.isEmpty()) {
//			enumeration.metadata.append[newLines = 2]
//		}
		for (EnumerationValueDeclaration value : enumeration.values) {
			value.format
			value.regionFor.keyword(',').prepend[noSpace]
			value.append[newLine]
		}
	}
}
