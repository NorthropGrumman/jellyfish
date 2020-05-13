/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.GivenDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.GivenStep
import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario
import com.ngc.seaside.systemdescriptor.systemDescriptor.ThenDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.ThenStep
import com.ngc.seaside.systemdescriptor.systemDescriptor.WhenDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.WhenStep
import org.eclipse.xtext.formatting2.IFormattableDocument
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import com.ngc.seaside.systemdescriptor.systemDescriptor.Step
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegion

class ScenarioFormatter extends AbstractSystemDescriptorFormatter {
	def dispatch void format(Scenario scenario, extension IFormattableDocument document) {
		var begin = scenario.regionFor.keyword('scenario')
		var end = scenario.regionFor.keyword('}')
		interior(begin, end)[indent]

		scenario.regionFor.feature(SystemDescriptorPackage.Literals.SCENARIO__NAME)
			.prepend[newLines = 0; oneSpace]
			.append[oneSpace]

		scenario.regionFor.keyword('{').prepend[oneSpace].append[newLine]

		scenario.metadata?.format

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

	def dispatch void format(WhenDeclaration when, extension IFormattableDocument document) {
		for (WhenStep step : when.steps) {
			when.append[newLine]
			step.format
		}

		when.append[newLine]
	}

	def dispatch void format(ThenDeclaration then, extension IFormattableDocument document) {
		for (ThenStep step : then.steps) {
			then.append[newLine]
			step.format
		}

		then.append[newLine]
	}

	def dispatch void format(Step step, extension IFormattableDocument document) {
		step.append[newLine]
		step.regionFor.feature(SystemDescriptorPackage.Literals.STEP__KEYWORD)
			.prepend[newLines = 0; oneSpace]
			.append[oneSpace]

		for (ISemanticRegion param : step.regionFor.features(SystemDescriptorPackage.Literals.STEP__PARAMETERS)) {
			param.append[oneSpace]
		}
	}
}
