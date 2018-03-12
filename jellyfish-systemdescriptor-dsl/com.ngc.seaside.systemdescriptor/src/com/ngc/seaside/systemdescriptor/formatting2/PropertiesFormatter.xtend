package com.ngc.seaside.systemdescriptor.formatting2

import org.eclipse.xtext.formatting2.IFormattableDocument
import com.ngc.seaside.systemdescriptor.systemDescriptor.Properties
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitivePropertyFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedPropertyFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage

class PropertiesFormatter extends AbstractSystemDescriptorFormatter {

	def dispatch void format(
		Properties properties,
		extension IFormattableDocument document
	) {
		var begin = properties.regionFor.keyword('properties')
		var end = properties.regionFor.keyword('}')
			.append[newLine]
		interior(begin, end)[indent]

		properties.regionFor.keyword('{')
			.prepend[oneSpace]
			.append[newLine]
			
		for (PropertyFieldDeclaration d : properties.declarations) {
			d.format
			if (d != properties.declarations.last) {
				d.append[newLine]
			} else {
				d.append[newLine]
			}
		}	
	}

	def dispatch void format(
		PrimitivePropertyFieldDeclaration declaration,
		extension IFormattableDocument document
	) {
		declaration.regionFor.feature(SystemDescriptorPackage.Literals.PROPERTY_FIELD_DECLARATION__CARDINALITY)
			.prepend[noSpace]
			.append[oneSpace]
		
		declaration.regionFor.feature(SystemDescriptorPackage.Literals.PRIMITIVE_DATA_FIELD_DECLARATION__TYPE)
			.prepend[noSpace]
			.append[noSpace]
			
		declaration.regionFor.feature(SystemDescriptorPackage.Literals.PROPERTY_FIELD_DECLARATION__NAME)
			.prepend[oneSpace ; newLines = 0]
			.append[newLine]
	}

	def dispatch void format(
		ReferencedPropertyFieldDeclaration declaration,
		extension IFormattableDocument document
	) {
		declaration.regionFor.feature(SystemDescriptorPackage.Literals.PROPERTY_FIELD_DECLARATION__CARDINALITY)
			.prepend[noSpace]
			.append[oneSpace]
		
		declaration.dataModel
			.prepend[noSpace]
			.append[noSpace]
			
		declaration.regionFor.feature(SystemDescriptorPackage.Literals.PROPERTY_FIELD_DECLARATION__NAME)
			.prepend[oneSpace ; newLines = 0]
			.append[newLine]
	}

}
