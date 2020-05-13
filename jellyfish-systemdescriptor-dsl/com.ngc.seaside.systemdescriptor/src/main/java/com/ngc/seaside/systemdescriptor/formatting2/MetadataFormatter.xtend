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
