package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import org.eclipse.xtext.formatting2.IFormattableDocument

class ModelFormatter extends AbstractSystemDescriptorFormatter {
    def dispatch void format(Model model, extension IFormattableDocument document) {
        model.regionFor.keyword('model').prepend[noIndentation]
        model.regionFor.keyword('{').prepend[oneSpace].append[newLine]

        if (model.metadata !== null) {
            model.metadata.format
            model.metadata.append[newLines = 2; highPriority]
        }

        model.input.format
        model.output.format
        model.requires.format
        model.parts.format
        model.links.format
        model.scenarios.forEach[format]

        var begin = model.regionFor.keyword('model')
        var end = model.regionFor.keyword('}')
        interior(begin, end)[indent]
    }
}
