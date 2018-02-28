package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.Data
import org.eclipse.xtext.formatting2.IFormattableDocument
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration

class DataFormatter extends AbstractSystemDescriptorFormatter {
    def dispatch void format(Data data, extension IFormattableDocument document) {
		data.regionFor.keyword('data').prepend[noIndentation]
		
		if (data.extendedDataType !== null) {
			data.regionFor.keyword('extends').prepend[oneSpace].append[oneSpace]
		}
		
		data.metadata?.format
		
		for (DataFieldDeclaration field : data.fields) {
			field.format
			if (field.definition !== null && field != data.fields.last) {
				field.append[newLines = 2]
			} else {
				field.append[newLine]
			}
		}
    }
}
