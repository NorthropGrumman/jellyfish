package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.ArrayValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.JsonObject
import com.ngc.seaside.systemdescriptor.systemDescriptor.JsonValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.Member
import com.ngc.seaside.systemdescriptor.systemDescriptor.Metadata
import org.eclipse.xtext.formatting2.IFormattableDocument

class MetadataFormatter extends AbstractSystemDescriptorFormatter {
    def dispatch void format(Metadata metadata, extension IFormattableDocument document) {
    	metadata.regionFor.keyword('metadata').prepend[newLine]    	
    	
        metadata.json.format
        metadata.append[setNewLines(2)]
    }

    def dispatch void format(JsonObject json, extension IFormattableDocument document) {
        var begin = json.regionFor.keyword('{').prepend[oneSpace]
        var end = json.regionFor.keyword('}')
       interior(begin, end)[indent]

        for (Member member : json.members) {
            member.format
            if (member == json.members.last) {
                member.append[newLine]
            }
        }
    }

    def dispatch void format(Member member, extension IFormattableDocument document) {
        member.regionFor.keyword(':').prepend[oneSpace]
        member.value.format.prepend[oneSpace]
        member.prepend[newLine]
    }

    def dispatch void format(JsonValue json, extension IFormattableDocument document) {
        json.value.format
    }

    def dispatch void format(ArrayValue array, extension IFormattableDocument document) {
        array.value.regionFor.keyword('[').append[oneSpace]
        array.value.regionFor.keyword(']').prepend[oneSpace]
    }
}
