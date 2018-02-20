package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataModelFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.eclipse.xtext.formatting2.IFormattableDocument

class DataFieldDeclarationFormatter extends AbstractSystemDescriptorFormatter {

	def dispatch void format(DataFieldDeclaration field, extension IFormattableDocument document) {
		field.regionFor.keyword('{')
			.prepend[oneSpace]
			.append[newLine]
	}

	def dispatch void format(ReferencedDataModelFieldDeclaration field, extension IFormattableDocument document) {
		var type = field.regionFor.feature(SystemDescriptorPackage.Literals.REFERENCED_DATA_MODEL_FIELD_DECLARATION__DATA_MODEL)
		type.append[oneSpace]
		var many = field.regionFor.feature(SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION__CARDINALITY)
		if (many === null) {
			type.prepend[noSpace]
		} else {
			type.prepend[oneSpace]
		}
	}
	
	def dispatch void format(PrimitiveDataFieldDeclaration field, extension IFormattableDocument document) {
		var type = field.regionFor.feature(SystemDescriptorPackage.Literals.PRIMITIVE_DATA_FIELD_DECLARATION__TYPE)
		type.append[oneSpace]
		var many = field.regionFor.feature(SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION__CARDINALITY)
		if (many === null) {
			type.prepend[noSpace]
		} else {
			type.prepend[oneSpace]
		}
	}
}
