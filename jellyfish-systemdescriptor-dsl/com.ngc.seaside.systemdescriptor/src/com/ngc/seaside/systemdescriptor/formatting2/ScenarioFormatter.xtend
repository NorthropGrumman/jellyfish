package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.GivenDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.GivenStep
import com.ngc.seaside.systemdescriptor.systemDescriptor.Step
import com.ngc.seaside.systemdescriptor.systemDescriptor.ThenDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.WhenDeclaration
import java.util.List
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.formatting2.IFormattableDocument

class ScenarioFormatter extends AbstractSystemDescriptorFormatter {

	def dispatch void format(GivenDeclaration given, extension IFormattableDocument document) {
  		formatStep(given, given.steps, document)
	}
	
	def dispatch void format(WhenDeclaration when, extension IFormattableDocument document) {
  		formatStep(when, when.steps, document)
	}
	
	def dispatch void format(ThenDeclaration then, extension IFormattableDocument document) {
  		formatStep(then, then.steps, document)
	}
	
	private def void formatStep(EObject step, List<? extends Step> steps, extension IFormattableDocument document) {
		step.regionFor.keywords('given', 'when', 'then').forEach[prepend[noSpace]]
		for (Step s : steps) {
			s.format
			s.append[newLine]
		}
	}
	
}
