package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.Links
import org.eclipse.xtext.formatting2.IFormattableDocument

class LinksFormatter extends AbstractSystemDescriptorFormatter {
    def dispatch void format(Links links, extension IFormattableDocument document) {
        var begin = links.regionFor.keyword('links')
        var end = links.regionFor.keyword('}').append[newLines = 2]
        interior(begin, end)[indent]

        links.regionFor.keyword('{').prepend[oneSpace].append[newLine]

        for (LinkDeclaration d : links.declarations) {
            d.format
            if (d.definition !== null && d != links.declarations.last) {
                d.append[newLines = 2]
            } else {
                d.append[newLine]
            }
        }
    }

    def dispatch void format(LinkDeclaration declaration, extension IFormattableDocument document) {
        declaration.regionFor.keyword('link').append[oneSpace]

        if(declaration.name != null && !declaration.name.empty) {
            declaration.source.prepend[declaration.name]
            declaration.source.prepend[oneSpace]
        }

        declaration.regionFor.keyword('->').prepend[oneSpace].append[oneSpace]
        declaration.source.append[oneSpace]
        declaration.target.append[newLine]

        if(declaration.definition !== null) {
            declaration.definition.format
        }
    }
}