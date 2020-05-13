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
