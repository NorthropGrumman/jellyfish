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

import com.ngc.seaside.systemdescriptor.systemDescriptor.Element
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.eclipse.xtext.formatting2.IFormattableDocument

class ElementFormatter extends AbstractSystemDescriptorFormatter {
    def dispatch void format(Element element, extension IFormattableDocument document) {
    	element.prepend[noSpace]
        element.regionFor.feature(SystemDescriptorPackage.Literals.ELEMENT__NAME).prepend[oneSpace]
        element.regionFor.keyword('{').prepend[oneSpace].append[newLine]

        element.metadata?.format

        var begin = element.regionFor.keyword('{')
        var end = element.regionFor.keyword('}').append[newLine]
        interior(begin, end)[indent]
    }
}
