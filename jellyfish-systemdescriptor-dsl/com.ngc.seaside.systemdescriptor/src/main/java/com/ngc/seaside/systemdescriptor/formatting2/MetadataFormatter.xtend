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

import com.ngc.seaside.systemdescriptor.systemDescriptor.ArrayValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.JsonObject
import com.ngc.seaside.systemdescriptor.systemDescriptor.JsonValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.Member
import com.ngc.seaside.systemdescriptor.systemDescriptor.Metadata
import org.eclipse.xtext.formatting2.IFormattableDocument
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.eclipse.xtext.EcoreUtil2
import com.ngc.seaside.systemdescriptor.systemDescriptor.StringValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.IntValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.DblValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.BooleanValue

class MetadataFormatter extends AbstractSystemDescriptorFormatter {
	def dispatch void format(Metadata metadata, extension IFormattableDocument document) {
		metadata.prepend[noSpace]
		metadata.regionFor.keyword('metadata').prepend[newLine]

		metadata.json.format
		if (EcoreUtil2.getAllSuperTypes(metadata.eContainer.eClass).contains(SystemDescriptorPackage.Literals.ELEMENT)) {
			metadata.append[setNewLines(2)]
		} else {
			metadata.append[setNewLines(1)]
		}
	}

	def dispatch void format(JsonObject json, extension IFormattableDocument document) {
		var begin = json.regionFor.keyword('{').prepend[noSpace ; oneSpace]
		var end = json.regionFor.keyword('}').append[noSpace]
		interior(begin, end)[indent]

		for (Member member : json.members) {
			member.format
			if(member == json.members.last) {
				member.append[newLine]
			}
		}
	}

	def dispatch void format(Member member, extension IFormattableDocument document) {
		member.prepend[newLine]
		member.regionFor.feature(SystemDescriptorPackage.Literals.MEMBER__KEY)
			.prepend[noSpace]

		member.regionFor.keyword(':').prepend[oneSpace]
		member.value.format
	}

	def dispatch void format(JsonValue json, extension IFormattableDocument document) {
		json.value.format
	}

	def dispatch void format(ArrayValue array, extension IFormattableDocument document) {
		array.value.regionFor.keyword('[').append[oneSpace]
		array.value.regionFor.keyword(']').prepend[oneSpace]
	}
	
	def dispatch void format(StringValue value, extension IFormattableDocument document) {
		value.regionFor.feature(SystemDescriptorPackage.Literals.STRING_VALUE__VALUE)
			.prepend[oneSpace]
			.append[noSpace]
	}
	
	def dispatch void format(IntValue value, extension IFormattableDocument document) {
		value.regionFor.feature(SystemDescriptorPackage.Literals.INT_VALUE__VALUE)
			.prepend[oneSpace]
			.append[noSpace]
	}
	
	def dispatch void format(DblValue value, extension IFormattableDocument document) {
		value.regionFor.feature(SystemDescriptorPackage.Literals.DBL_VALUE__VALUE)
			.prepend[oneSpace]
			.append[noSpace]
	}
	
	def dispatch void format(BooleanValue value, extension IFormattableDocument document) {
		value.regionFor.feature(SystemDescriptorPackage.Literals.BOOLEAN_VALUE__VALUE)
			.prepend[oneSpace]
			.append[noSpace]
	}
}
