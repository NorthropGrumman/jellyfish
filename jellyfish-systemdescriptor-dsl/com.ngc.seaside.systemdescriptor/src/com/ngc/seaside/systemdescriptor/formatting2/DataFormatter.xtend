package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.Data
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration
import org.eclipse.xtext.formatting2.IFormattableDocument

class DataFormatter extends AbstractSystemDescriptorFormatter {

	def dispatch void format(Data data, extension IFormattableDocument document) {
		data.regionFor.keyword(':')
			.prepend[oneSpace]
			.append[oneSpace]

		for (DataFieldDeclaration field : data.fields) {
			field.regionFor.keyword(',').prepend[noSpace]
			field.append[newLine]
			field.format
		}

	}
}
