/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
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
