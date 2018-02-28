package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataModelFieldDeclaration
import org.eclipse.xtext.formatting2.IFormattableDocument
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage

class DataFieldDeclarationFormatter extends AbstractSystemDescriptorFormatter {
	def dispatch void format(DataFieldDeclaration field, extension IFormattableDocument document) {
		formatField(field, document)
	}

	def dispatch void format(PrimitiveDataFieldDeclaration field, extension IFormattableDocument document) {
		formatField(field, document)
		field.regionFor.feature(SystemDescriptorPackage.Literals.PRIMITIVE_DATA_FIELD_DECLARATION__TYPE)
			.append[oneSpace]
	}

	def dispatch void format(ReferencedDataModelFieldDeclaration field, extension IFormattableDocument document) {
		formatField(field, document)
		field.regionFor.feature(SystemDescriptorPackage.Literals.REFERENCED_DATA_MODEL_FIELD_DECLARATION__DATA_MODEL)
			.append[oneSpace]
	}
	
	def private void formatField(DataFieldDeclaration field, extension IFormattableDocument document) {
		field.prepend[noSpace]
		
		field.regionFor.feature(SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION__CARDINALITY)
			.prepend[noSpace]
			.append[oneSpace]
		
		if (field.definition !== null) {
			field.regionFor.feature(SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION__NAME)
				.prepend[oneSpace]
				.append[oneSpace]
			field.definition.format
		} else {
			field.regionFor.feature(SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION__NAME)
				.prepend[newLines = 0]
				.append[newLine]
		}
	} 
}
