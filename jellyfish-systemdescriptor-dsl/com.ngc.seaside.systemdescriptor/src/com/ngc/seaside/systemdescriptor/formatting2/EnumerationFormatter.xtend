package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.Enumeration
import com.ngc.seaside.systemdescriptor.systemDescriptor.EnumerationValueDeclaration
import org.eclipse.xtext.formatting2.IFormattableDocument

class EnumerationFormatter extends AbstractSystemDescriptorFormatter {
    def dispatch void format(Enumeration enumeration, extension IFormattableDocument document) {
        enumeration.regionFor.keyword('enum').prepend[noIndentation]
        enumeration.regionFor.keyword('{').prepend[oneSpace].append[newLine]

        if (enumeration.metadata !== null) {
            enumeration.metadata.format
        }

        for (EnumerationValueDeclaration value : enumeration.values) {
            value.format
            value.regionFor.keyword(',').prepend[noSpace]
            value.append[newLine]
        }

        var begin = enumeration.regionFor.keyword('enum')
        var end = enumeration.regionFor.keyword('}')
        interior(begin, end)[indent]
    }
}
