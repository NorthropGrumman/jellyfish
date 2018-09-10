/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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