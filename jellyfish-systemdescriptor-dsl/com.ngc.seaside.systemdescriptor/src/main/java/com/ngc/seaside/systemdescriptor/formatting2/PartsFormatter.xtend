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

import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.Parts
import org.eclipse.xtext.formatting2.IFormattableDocument
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import com.ngc.seaside.systemdescriptor.systemDescriptor.BasePartDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedPartDeclaration

class PartsFormatter extends AbstractSystemDescriptorFormatter {
    def dispatch void format(Parts parts, extension IFormattableDocument document) {
        var begin = parts.regionFor.keyword('parts')
        var end = parts.regionFor.keyword('}').append[newLines = 2]
        interior(begin, end)[indent]

        parts.regionFor.keyword('{').prepend[oneSpace].append[newLine]

        for (PartDeclaration d : parts.declarations) {
            d.format
            if (d.definition !== null && d != parts.declarations.last) {
                d.append[newLines = 2]
            } else {
                d.append[newLine]
            }
        }
    }

    def dispatch void format(PartDeclaration declaration, extension IFormattableDocument document) {
        formatDeclaration(declaration, document)
    }
    
    def dispatch void format(BasePartDeclaration declaration, extension IFormattableDocument document) {
    	formatDeclaration(declaration, document)
    	declaration.regionFor.feature(SystemDescriptorPackage.Literals.BASE_PART_DECLARATION__TYPE)
    		.prepend[noSpace]
    		.append[oneSpace]
    }
    
    def dispatch void format(RefinedPartDeclaration declaration, extension IFormattableDocument document) {
    	formatDeclaration(declaration, document)
    	declaration.regionFor.keyword('refine')
    		.prepend[noSpace]
    		.append[oneSpace]
    }
    
    private def void formatDeclaration(PartDeclaration declaration, extension IFormattableDocument document) {
    	declaration.prepend[noSpace]

		if(declaration.definition !== null) {
			declaration.regionFor.feature(SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME)
				.append[oneSpace]
			declaration.definition.format
		} else {
			declaration.regionFor.feature(SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME)
				.prepend[newLines = 0]
				.append[newLine]
		}
    }
}