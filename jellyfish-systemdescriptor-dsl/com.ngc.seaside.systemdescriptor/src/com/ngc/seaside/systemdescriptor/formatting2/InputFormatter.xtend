package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.Input
import com.ngc.seaside.systemdescriptor.systemDescriptor.InputDeclaration
import org.eclipse.xtext.formatting2.IFormattableDocument
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage

class InputFormatter extends AbstractSystemDescriptorFormatter {
	def dispatch void format(Input input, extension IFormattableDocument document) {
		var begin = input.regionFor.keyword('input')
		var end = input.regionFor.keyword('}').append[newLines = 2]
		interior(begin, end)[indent]

		input.regionFor.keyword('{').prepend[oneSpace].append[newLine]

		for (InputDeclaration d : input.declarations) {
			d.format
			if(d.definition !== null && d != input.declarations.last) {
				d.append[newLines = 2]
			} else {
				d.append[newLine]
			}
		}
	}

	def dispatch void format(InputDeclaration declaration, extension IFormattableDocument document) {
		declaration.prepend[noSpace]

		declaration.regionFor.feature(SystemDescriptorPackage.Literals.INPUT_DECLARATION__TYPE).append[oneSpace]

		if(declaration.definition !== null) {
			declaration.regionFor.feature(SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME).append[oneSpace]
			declaration.definition.format
		} else {
			declaration.regionFor.feature(SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME).prepend[newLines = 0].append[newLine]
		}
	}
}
