package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.DeclarationDefinition
import org.eclipse.xtext.formatting2.IFormattableDocument

class DeclarationDefinitionFormatter extends AbstractSystemDescriptorFormatter {
    def dispatch void format(DeclarationDefinition definition, extension IFormattableDocument document) {
        var begin = definition.regionFor.keyword('{').prepend[oneSpace].append[newLine]
        var end = definition.regionFor.keyword('}')
        interior(begin, end)[indent]

        definition.metadata?.format
    }
}