package com.ngc.seaside.systemdescriptor.formatting2

import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataModelFieldDeclaration
import org.eclipse.xtext.formatting2.IFormattableDocument

class DataFieldDeclarationFormatter extends AbstractSystemDescriptorFormatter {
    def dispatch void format(DataFieldDeclaration field, extension IFormattableDocument document) {
      field.regionFor.keyword('{').prepend[oneSpace].append[newLine]
    }

    def dispatch void format(PrimitiveDataFieldDeclaration field, extension IFormattableDocument document) {
        if (field.definition !== null) {
          field.definition.format
        }
        field.prepend[noSpace].append[newLine]
    }

    def dispatch void format(ReferencedDataModelFieldDeclaration field, extension IFormattableDocument document) {
        if (field.definition !== null) {
          field.definition.format
        }
        field.prepend[noSpace].append[newLine]
    }
}
