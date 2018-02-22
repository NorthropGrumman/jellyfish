package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.Data
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration
import org.eclipse.xtext.formatting2.IFormattableDocument
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataModelFieldDeclaration

class DataFormatter extends AbstractSystemDescriptorFormatter {
    def dispatch void format(Data data, extension IFormattableDocument document) {
        var begin = data.regionFor.keyword('data')
        var end = data.regionFor.keyword('}')
        interior(begin, end)[indent]

        data.prepend[newLines = 2]

        data.regionFor.keyword(':').prepend[oneSpace].append[newLine]

        if (data.metadata !== null) {
            data.metadata.format;
            data.metadata.append[newLines = 2; highPriority]
        }

        for (DataFieldDeclaration field : data.fields) {
            field.prepend[newLine]
            field.format
        }
    }
}
