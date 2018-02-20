package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.JsonObject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Member
import com.ngc.seaside.systemdescriptor.systemDescriptor.Metadata
import org.eclipse.xtext.formatting2.IFormattableDocument

class MetadataFormatter extends AbstractSystemDescriptorFormatter {

	def dispatch void format(Metadata metadata, extension IFormattableDocument document) {
		metadata.getJson().format
		metadata.append[setNewLines(1); lowPriority()]
	}

	def dispatch void format(JsonObject json, extension IFormattableDocument document) {
		var begin = json.regionFor.keyword('{').prepend[oneSpace].append[newLine]
		var end = json.regionFor.keyword('}')
		interior(begin, end)[indent]

		for (Member member : json.members) {
			member.regionFor.keyword(':').prepend[oneSpace].append[oneSpace]
			member.value.format
			if(member == json.members.last) {
				member.append[newLine]
			}
		}
	}
	
}
