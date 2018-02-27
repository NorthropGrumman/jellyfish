package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.RequireDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.Requires
import org.eclipse.xtext.formatting2.IFormattableDocument
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage

class RequiresFormatter extends AbstractSystemDescriptorFormatter {
    def dispatch void format(Requires requires, extension IFormattableDocument document) {
        var begin = requires.regionFor.keyword('requires')
        var end = requires.regionFor.keyword('}').append[newLines = 2]
        interior(begin, end)[indent]

        requires.regionFor.keyword('{').prepend[oneSpace].append[newLine]

        for (RequireDeclaration d : requires.declarations) {
            d.format
            if (d.definition !== null && d != requires.declarations.last) {
                d.append[newLines = 2]
            } else {
                d.append[newLine]
            }
        }
    }

    def dispatch void format(RequireDeclaration declaration, extension IFormattableDocument document) {
        declaration.prepend[noSpace]

		declaration.regionFor.feature(SystemDescriptorPackage.Literals.REQUIRE_DECLARATION__TYPE).append[oneSpace]

		if(declaration.definition !== null) {
			declaration.regionFor.feature(SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME).append[oneSpace]
			declaration.definition.format
		} else {
			declaration.regionFor.feature(SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME).prepend[newLines = 0].append[newLine]
		}
    }
}