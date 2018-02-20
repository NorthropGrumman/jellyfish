package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.Element
import org.eclipse.xtext.formatting2.IFormattableDocument
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage

class ElementFormatter extends AbstractSystemDescriptorFormatter {

	def dispatch void format(Element element, extension IFormattableDocument document) {
		element.regionFor.feature(SystemDescriptorPackage.Literals.ELEMENT__NAME).prepend[oneSpace]
		element.regionFor.keyword('{').prepend[oneSpace].append[newLine]

		if(element.metadata !== null) {
			element.metadata.format;
			element.metadata.append[setNewLines(2); highPriority()]
		}

		var begin = element.regionFor.keyword('{')
		var end = element.regionFor.keyword('}').append[newLine]
		interior(begin, end)[indent]
	}
}
