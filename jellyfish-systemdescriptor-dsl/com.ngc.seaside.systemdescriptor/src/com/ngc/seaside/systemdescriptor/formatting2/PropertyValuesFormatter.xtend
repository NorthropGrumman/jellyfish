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