package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.Parts
import org.eclipse.xtext.formatting2.IFormattableDocument

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
        if(declaration.definition !== null) {
            declaration.definition.format
        }
    }
}