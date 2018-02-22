package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario
import org.eclipse.xtext.formatting2.IFormattableDocument
import com.ngc.seaside.systemdescriptor.systemDescriptor.ThenDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.ThenStep
import com.ngc.seaside.systemdescriptor.systemDescriptor.WhenStep
import com.ngc.seaside.systemdescriptor.systemDescriptor.WhenDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.GivenStep
import com.ngc.seaside.systemdescriptor.systemDescriptor.GivenDeclaration

class ScenarioFormatter extends AbstractSystemDescriptorFormatter {
    def dispatch void format(Scenario scenario, extension IFormattableDocument document) {
        var begin = scenario.regionFor.keyword('scenario')
        var end = scenario.regionFor.keyword('}')
        interior(begin, end)[indent]

        scenario.regionFor.keyword('{').prepend[oneSpace].append[newLine]

        if (scenario.metadata !== null) {
            scenario.metadata.format
            scenario.metadata.append[setNewLines = 2 highPriority]
        }

        scenario.given?.format
        scenario.when?.format
        scenario.then?.format

        scenario.append[setNewLines = 2]
    }

    def dispatch void format(GivenDeclaration given, extension IFormattableDocument document) {
        for (GivenStep step : given.steps) {
            given.append[newLine]
            step.format
        }

        given.append[newLine]
    }

    def dispatch void format(GivenStep step, extension IFormattableDocument document) {
        step.append[newLine]
    }

    def dispatch void format(WhenDeclaration when, extension IFormattableDocument document) {
        for (WhenStep step : when.steps) {
            when.append[newLine]
            step.format
        }

        when.append[newLine]
    }

    def dispatch void format(WhenStep step, extension IFormattableDocument document) {
        step.append[newLine]
    }

    def dispatch void format(ThenDeclaration then, extension IFormattableDocument document) {
        for (ThenStep step : then.steps) {
            step.append[newLine]
        }

        then.append[newLine]
    }
}