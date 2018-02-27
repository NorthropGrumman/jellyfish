package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.Element
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.eclipse.xtext.formatting2.IFormattableDocument

class ElementFormatter extends AbstractSystemDescriptorFormatter {
    def dispatch void format(Element element, extension IFormattableDocument document) {
    	element.prepend[noSpace]
        element.regionFor.feature(SystemDescriptorPackage.Literals.ELEMENT__NAME).prepend[oneSpace]
        element.regionFor.keyword('{').prepend[oneSpace].append[newLine]

        element.metadata?.format;

        var begin = element.regionFor.keyword('{')
        var end = element.regionFor.keyword('}').append[newLine]
        interior(begin, end)[indent]
    }
}
