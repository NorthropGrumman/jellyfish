package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.Input
import com.ngc.seaside.systemdescriptor.systemDescriptor.InputDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.Output
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.Parts
import com.ngc.seaside.systemdescriptor.systemDescriptor.RequireDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.Requires
import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario
import java.util.List
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.formatting2.IFormattableDocument
import com.ngc.seaside.systemdescriptor.systemDescriptor.Links

class ModelFormatter extends AbstractSystemDescriptorFormatter {

	def dispatch void format(Model model, extension IFormattableDocument document) {
		model.input.format
		model.output.format
		model.scenarios.forEach[format]
		model.parts.format
		model.requires.format
		model.links.format
	}
	
	def dispatch void format(Input input, extension IFormattableDocument document) {
		formatBlock(input, document)
		formatFields(input.declarations, document)
	}
	
	def dispatch void format(Output output, extension IFormattableDocument document) {
		formatBlock(output, document)
		formatFields(output.declarations, document)
	}
	
	def dispatch void format(Scenario scenario, extension IFormattableDocument document) {
		formatBlock(scenario, document)
		if(scenario.metadata !== null) {
			scenario.metadata.format;
			scenario.metadata.append[newLines = 2; highPriority]
		}
		scenario.given.format
		scenario.when.format
		scenario.then.format
	}
	
	def dispatch void format(Parts parts, extension IFormattableDocument document) {
		formatBlock(parts, document)
		formatFields(parts.declarations, document)
	}
	
	def dispatch void format(Requires require, extension IFormattableDocument document) {
		formatBlock(require, document)
		formatFields(require.declarations, document)
	}
	
	def dispatch void format(Links links, extension IFormattableDocument document) {
		formatBlock(links, document)
		formatFields(links.declarations, document)
	}
	
	
	private def void formatBlock(EObject block, extension IFormattableDocument document) {
		block.regionFor.keyword('{').prepend[oneSpace].append[newLine]
		interior(
			block.regionFor.keyword('{'),
			block.regionFor.keyword('}').append[newLines = 2],
			[indent]
		)
	}
	
	private def void formatFields(List<? extends EObject> fields, extension IFormattableDocument document) {
		for (EObject field : fields) {
			field.format
			field.append[newLine]
		}
	}
	
}
