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

import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueAssignment
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueExpression
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueExpressionPathSegment
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.eclipse.xtext.formatting2.IFormattableDocument
import com.ngc.seaside.systemdescriptor.systemDescriptor.EnumPropertyValue

class PropertyValuesFormatter extends AbstractSystemDescriptorFormatter {

	def dispatch void format(
		PropertyValueAssignment assignment,
		extension IFormattableDocument document
	) {
		assignment.expression.format
		assignment.value.format
	}
	
	def dispatch void format(
		PropertyValueExpression expression,
		extension IFormattableDocument document
	) {
		var declaration = expression.regionFor.feature(SystemDescriptorPackage.Literals.PROPERTY_VALUE_EXPRESSION__DECLARATION)
			.prepend[noSpace]
		if (expression.pathSegments.empty) {
			declaration.append[oneSpace]
		} else {
			declaration.append[noSpace]
		}
			
		for (PropertyValueExpressionPathSegment s : expression.pathSegments) {
			s.prepend[noSpace]

			if (s == expression.pathSegments.last) {
				s.append[oneSpace]
			} else {
				s.append[noSpace]
			}
		}
	}
	
	def dispatch void format(
		EnumPropertyValue value,
		extension IFormattableDocument document
	) {
		value.regionFor.feature(SystemDescriptorPackage.Literals.ENUM_PROPERTY_VALUE__ENUMERATION)
		    .prepend[oneSpace]
			.append[noSpace]
			
		value.regionFor.feature(SystemDescriptorPackage.Literals.ENUM_PROPERTY_VALUE__VALUE)
			.prepend[noSpace]
			.append[noSpace]
	}
}