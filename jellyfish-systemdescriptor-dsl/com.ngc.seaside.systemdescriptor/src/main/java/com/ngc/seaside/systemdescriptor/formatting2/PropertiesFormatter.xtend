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

import org.eclipse.xtext.formatting2.IFormattableDocument
import com.ngc.seaside.systemdescriptor.systemDescriptor.Properties
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitivePropertyFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedPropertyFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueAssignment

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
			if(d == properties.declarations.last && !properties.assignments.empty) {
				d.append[newLines = 2]
			} else {
				d.append[newLine]
			}
		}
		
		for(PropertyValueAssignment a : properties.assignments) {
			a.format
			a.append[newLine]
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
	}

}
