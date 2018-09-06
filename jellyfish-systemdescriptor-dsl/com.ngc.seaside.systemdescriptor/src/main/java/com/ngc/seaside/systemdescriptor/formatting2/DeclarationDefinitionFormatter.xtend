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

import com.ngc.seaside.systemdescriptor.systemDescriptor.DeclarationDefinition
import org.eclipse.xtext.formatting2.IFormattableDocument

class DeclarationDefinitionFormatter extends AbstractSystemDescriptorFormatter {
    def dispatch void format(DeclarationDefinition definition, extension IFormattableDocument document) {
        definition.regionFor.keyword('{').prepend[noIndentation]
        var begin = definition.regionFor.keyword('{').prepend[oneSpace].append[newLine]
        var end = definition.regionFor.keyword('}')
        interior(begin, end)[indent]

        definition.metadata?.format
        definition.properties?.format
    }
}