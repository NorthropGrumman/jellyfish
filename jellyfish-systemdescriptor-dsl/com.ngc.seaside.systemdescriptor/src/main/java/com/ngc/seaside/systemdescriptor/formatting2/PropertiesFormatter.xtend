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
