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

import com.ngc.seaside.systemdescriptor.systemDescriptor.Output
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration
import org.eclipse.xtext.formatting2.IFormattableDocument
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage

class OutputFormatter extends AbstractSystemDescriptorFormatter {
    def dispatch void format(Output output, extension IFormattableDocument document) {
        var begin = output.regionFor.keyword('output')
        var end = output.regionFor.keyword('}').append[newLines = 2]
        interior(begin, end)[indent]

        output.regionFor.keyword('{').prepend[oneSpace].append[newLine]

        for (OutputDeclaration d : output.declarations) {
            d.format
            if (d.definition !== null && d != output.declarations.last) {
                d.append[newLines = 2]
            } else {
                d.append[newLine]
            }
        }
    }

    def dispatch void format(OutputDeclaration declaration, extension IFormattableDocument document) {
        declaration.prepend[noSpace]

		declaration.regionFor.feature(SystemDescriptorPackage.Literals.OUTPUT_DECLARATION__TYPE).append[oneSpace]

		if(declaration.definition !== null) {
			declaration.regionFor.feature(SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME).prepend[newLines = 0].append[oneSpace]
			declaration.definition.format
		} else {
			declaration.regionFor.feature(SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME).prepend[newLines = 0].append[newLine]
		}
    }
}
