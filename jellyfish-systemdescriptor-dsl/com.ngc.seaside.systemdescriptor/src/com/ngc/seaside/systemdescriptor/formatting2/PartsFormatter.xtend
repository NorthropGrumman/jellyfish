package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.Parts
import org.eclipse.xtext.formatting2.IFormattableDocument
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import com.ngc.seaside.systemdescriptor.systemDescriptor.BasePartDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedPartDeclaration

class PartsFormatter extends AbstractSystemDescriptorFormatter {
    def dispatch void format(Parts parts, extension IFormattableDocument document) {
        var begin = parts.regionFor.keyword('parts')
        var end = parts.regionFor.keyword('}').append[newLines = 2]
        interior(begin, end)[indent]

        parts.regionFor.keyword('{').prepend[oneSpace].append[newLine]

        for (PartDeclaration d : parts.declarations) {
            d.format
            if (d.definition !== null && d != parts.declarations.last) {
                d.append[newLines = 2]
            } else {
                d.append[newLine]
            }
        }
    }

    def dispatch void format(PartDeclaration declaration, extension IFormattableDocument document) {
        formatDeclaration(declaration, document)
    }
    
    def dispatch void format(BasePartDeclaration declaration, extension IFormattableDocument document) {
    	formatDeclaration(declaration, document)
    	declaration.regionFor.feature(SystemDescriptorPackage.Literals.BASE_PART_DECLARATION__TYPE)
    		.prepend[noSpace]
    		.append[oneSpace]
    }
    
    def dispatch void format(RefinedPartDeclaration declaration, extension IFormattableDocument document) {
    	formatDeclaration(declaration, document)
    	// TODO TH: IMplement this
    }
    
    private def void formatDeclaration(PartDeclaration declaration, extension IFormattableDocument document) {
    	declaration.prepend[noSpace]

		if(declaration.definition !== null) {
			declaration.regionFor.feature(SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME)
				.append[oneSpace]
			declaration.definition.format
		} else {
			declaration.regionFor.feature(SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME)
				.prepend[newLines = 0]
				.append[newLine]
		}
    }
}