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
            value.append[newLine]
        }
    }

    def dispatch void format(EnumerationValueDeclaration value, extension IFormattableDocument document) {
        if (value.definition !== null) {
            value.definition.format;
        }
    }
}
