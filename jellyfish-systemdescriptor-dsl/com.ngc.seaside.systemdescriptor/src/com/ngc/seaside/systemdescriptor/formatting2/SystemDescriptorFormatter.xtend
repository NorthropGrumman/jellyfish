package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.EnumerationValueDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.Import
import org.eclipse.xtext.formatting2.AbstractFormatter2
import org.eclipse.xtext.formatting2.IFormattableDocument
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.Member
import com.ngc.seaside.systemdescriptor.systemDescriptor.Metadata
import com.ngc.seaside.systemdescriptor.systemDescriptor.JsonObject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Input
import com.ngc.seaside.systemdescriptor.systemDescriptor.InputDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.Output
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario
import com.ngc.seaside.systemdescriptor.systemDescriptor.GivenDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.ThenDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.WhenDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.Parts
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.Links
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.WhenStep
import com.ngc.seaside.systemdescriptor.systemDescriptor.ThenStep
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataModelFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.JsonValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.GivenStep
import com.ngc.seaside.systemdescriptor.systemDescriptor.Enumeration
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import com.ngc.seaside.systemdescriptor.systemDescriptor.DeclarationDefinition

class SystemDescriptorFormatter extends AbstractFormatter2 {

	def dispatch void format(
		com.ngc.seaside.systemdescriptor.systemDescriptor.Package packagez,
		extension IFormattableDocument document
	) {

		packagez.regionFor.keyword('package').prepend[noIndentation]
		packagez.regionFor.feature(SystemDescriptorPackage.Literals.PACKAGE__NAME).append[setNewLines(2)]

		if(packagez.getImports().size != 0) {

			for (Import imports : packagez.imports) {
				if(imports == packagez.imports.last) {
					imports.append[setNewLines(2)];
				} else {
					imports.append[setNewLines(1)];
				}
			}
		}

		packagez.element.format;
	}

	def dispatch void format(
		Enumeration enumeration,
		extension IFormattableDocument document
	) {
		enumeration.regionFor.keyword('enum').prepend[noIndentation]
		enumeration.regionFor.keyword('{').prepend[oneSpace].append[newLine]

		if(enumeration.metadata !== null) {
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

	def dispatch void format(
		EnumerationValueDeclaration value,
		extension IFormattableDocument document
	) {
		if(value.definition !== null) {
			value.definition.format;
		}
	}

	def dispatch void format(
		Model model,
		extension IFormattableDocument document
	) {
		model.regionFor.keyword('model').prepend[noIndentation]
		model.regionFor.keyword('{').prepend[oneSpace].append[newLine]

		if(model.metadata !== null) {
			model.metadata.format;
			model.metadata.append[setNewLines(2) ; highPriority()]
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
		
		model.regionFor.keyword('}').append[noSpace].append[newLine]
	}

	def dispatch void format(
		Metadata metadata,
		extension IFormattableDocument document
	) {
		metadata.getJson().format
		metadata.append[setNewLines(1) ; lowPriority()]
	}

	def dispatch void format(
		JsonObject json,
		extension IFormattableDocument document
	) {

		var begin = json.regionFor.keyword('{').prepend[oneSpace]
		var end = json.regionFor.keyword('}')
		interior(begin, end)[indent]

		for (Member member : json.members) {
			member.format
			if(member == json.members.last) {
				member.append[newLine]
			}
		}
	}

	def dispatch void format(
		Member member,
		extension IFormattableDocument document
	) {
		member.regionFor.keyword(':').prepend[noSpace]
		member.getValue.format.prepend[oneSpace]
		member.prepend[newLine]
	}

	def dispatch void format(
		Input input,
		extension IFormattableDocument document
	) {
		var begin = input.regionFor.keyword('input')
		var end = input.regionFor.keyword('}')

		input.regionFor.keyword('{').prepend[oneSpace].append[newLine]

		for (InputDeclaration dec : input.declarations) {
			dec.format;
			if (dec.definition !== null && dec != input.declarations.last) {
				dec.append[setNewLines(2)]	
			} else {
				dec.append[newLine]		
			}
		}

		interior(begin, end)[indent]

		input.regionFor.keyword('}').append[setNewLines(2)];
	}

	def dispatch void format(
		InputDeclaration inputDec,
		extension IFormattableDocument document
	) {
		if (inputDec.definition !== null) {
			inputDec.definition.format
		}
	}

	def dispatch void format(
		DeclarationDefinition definition,
		extension IFormattableDocument document
	) {
		definition.regionFor.keyword('{').prepend[oneSpace].append[newLine]
		
		if (definition.metadata !== null) {			
			definition.metadata.format
		} 
		
		definition.interior[indent]
	}

	def dispatch void format(
		Output output,
		extension IFormattableDocument document
	) {
		var begin = output.regionFor.keyword('output')
		var end = output.regionFor.keyword('}')

		output.regionFor.keyword('{').prepend[oneSpace].append[newLine]

		for (OutputDeclaration dec : output.declarations) {
			dec.format;
			if (dec.definition !== null && dec != output.declarations.last) {
				dec.append[setNewLines(2)]	
			} else {
				dec.append[newLine]		
			}
		}


		interior(begin, end)[indent]

		output.regionFor.keyword('}').append[setNewLines(2)];

	}

	def dispatch void format(
		OutputDeclaration outputDec,
		extension IFormattableDocument document
	) {
		if(outputDec.definition !== null) {
			outputDec.definition.format
		}
	}

	def dispatch void format(
		Scenario scenario,
		extension IFormattableDocument document
	) {
		scenario.regionFor.keyword('{').prepend[oneSpace].append[newLine]

		if(scenario.metadata !== null) {
			scenario.metadata.format;
			scenario.metadata.append[setNewLines(2) ; highPriority()]
		}

		if(scenario.given !== null) {
			scenario.given.format;
		}

		if(scenario.when !== null) {
			scenario.when.format;
		}

		if(scenario.then !== null) {
			scenario.then.format;
		}

		var begin = scenario.regionFor.keyword('scenario')
		var end = scenario.regionFor.keyword('}')
		interior(begin, end)[indent]

		scenario.append[setNewLines(2)]
	}

	def dispatch void format(
		GivenDeclaration given,
		extension IFormattableDocument document
	) {
		for (GivenStep step : given.steps) {
			given.append[newLine]
			step.format
		}

		given.append[newLine];
	}

	def dispatch void format(
		GivenStep step,
		extension IFormattableDocument document
	) {
		step.append[newLine]
	}

	def dispatch void format(
		WhenDeclaration when,
		extension IFormattableDocument document
	) {

		for (WhenStep step : when.steps) {
			when.append[newLine]
			step.format
		}

		when.append[newLine];
	}

	def dispatch void format(
		WhenStep step,
		extension IFormattableDocument document
	) {
		step.append[newLine]
	}

	def dispatch void format(
		ThenDeclaration then,
		extension IFormattableDocument document
	) {
		for (ThenStep step : then.steps) {
			step.append[newLine]
		}

		then.append[newLine]
	}

	def dispatch void format(
		Parts parts,
		extension IFormattableDocument document
	) {		
		var begin = parts.regionFor.keyword('parts')
		var end = parts.regionFor.keyword('}')

		parts.regionFor.keyword('{').prepend[oneSpace].append[newLine]

		for (PartDeclaration dec : parts.declarations) {
			dec.format;
			if (dec.definition !== null && dec != parts.declarations.last) {
				dec.append[setNewLines(2)]	
			} else {
				dec.append[newLine]		
			}
		}

		interior(begin, end)[indent]

		parts.regionFor.keyword('}').append[setNewLines(2)];
	}

	def dispatch void format(
		PartDeclaration dec,
		extension IFormattableDocument document
	) {
		if(dec.definition !== null) {
			dec.definition.format
		}
	}

	def dispatch void format(
		Links links,
		extension IFormattableDocument document
	) {
		var begin = links.regionFor.keyword('links')
		var end = links.regionFor.keyword('}')

		links.regionFor.keyword('{').prepend[oneSpace].append[newLine]

		for (LinkDeclaration dec : links.declarations) {
			dec.format;
		}

		interior(begin, end)[indent]

		links.regionFor.keyword('}').append[setNewLines(2)];
	}

	def dispatch void format(
		LinkDeclaration dec,
		extension IFormattableDocument document
	) {
		dec.regionFor.keyword('link').append[oneSpace]
		if(dec.name != null && !dec.name.isEmpty()) {
			dec.source.prepend[dec.name]
			dec.source.prepend[oneSpace]
		}
		dec.regionFor.keyword('->').prepend[oneSpace].append[oneSpace]
		dec.source.append[oneSpace]
		dec.target.append[newLine]

		if(dec.definition != null) {
			dec.definition.format
		}
	}

	def dispatch void format(
		Data data,
		extension IFormattableDocument document
	) {
		data.regionFor.keyword('data').prepend[noIndentation]
		data.regionFor.keyword('{').prepend[oneSpace].append[newLine]


		if(data.metadata !== null) {
			data.metadata.format;
			data.metadata.append[setNewLines(2) ; highPriority()]
		}

		for (DataFieldDeclaration dec : data.fields) {
			dec.prepend[newLine]
			dec.format;
		}
		
		data.regionFor.keyword('}').append[noSpace].append[newLine]

		var begin = data.regionFor.keyword('data')
		var end = data.regionFor.keyword('}')
		interior(begin, end)[indent]
	}

	def dispatch void format(
		PrimitiveDataFieldDeclaration dec,
		extension IFormattableDocument document
	) {
		if(dec.definition !== null) {
			dec.definition.format
		}

		dec.prepend[noSpace].append[newLine]

	}

	def dispatch void format(
		ReferencedDataModelFieldDeclaration dec,
		extension IFormattableDocument document
	) {
		if(dec.definition !== null) {
			dec.definition.format
		}
		dec.prepend[noSpace].append[newLine]
	}

	def dispatch void format(
		JsonValue jsonVal,
		extension IFormattableDocument document
	) {
		jsonVal.value.format
	}
}
