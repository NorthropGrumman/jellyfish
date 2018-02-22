package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario
import org.eclipse.xtext.formatting2.IFormattableDocument

class ModelFormatter extends AbstractSystemDescriptorFormatter {
    def dispatch void format(Model model, extension IFormattableDocument document) {
        model.regionFor.keyword('model').prepend[noIndentation]
        model.regionFor.keyword('{').prepend[oneSpace].append[newLine]

        if(model.metadata !== null) {
            model.metadata.format;
            model.metadata.append[setNewLines(2); highPriority()]
        }

        var modelInput = model.getInput();
        modelInput.format;

        var modelOutput = model.getOutput();
        modelOutput.format;

        var modelParts = model.getParts();
        modelParts.format;

        var modelLinks = model.getLinks();
        modelLinks.format;

        var modelScenarios = model.getScenarios();
        for (Scenario scenario : modelScenarios) {
            scenario.format;
        }

        var begin = model.regionFor.keyword('model')
        var end = model.regionFor.keyword('}')
        interior(begin, end)[indent]

    }
}
