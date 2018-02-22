package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.DeclarationDefinition
import org.eclipse.xtext.formatting2.IFormattableDocument

class DeclarationDefinitionFormatter extends AbstractSystemDescriptorFormatter {
    def dispatch void format(DeclarationDefinition definition, extension IFormattableDocument document) {
        definition.regionFor.keyword('{').prepend[oneSpace].append[newLine]

        if (definition.metadata !== null) {
            definition.metadata.format
        }

        definition.interior[indent]
    }
}