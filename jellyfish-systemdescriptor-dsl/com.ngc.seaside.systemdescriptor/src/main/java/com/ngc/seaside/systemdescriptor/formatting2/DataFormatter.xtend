/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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

import com.ngc.seaside.systemdescriptor.systemDescriptor.Data
import org.eclipse.xtext.formatting2.IFormattableDocument
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration

class DataFormatter extends AbstractSystemDescriptorFormatter {
    def dispatch void format(Data data, extension IFormattableDocument document) {
		data.regionFor.keyword('data').prepend[noIndentation]

		if (data.extendedDataType !== null) {
			data.regionFor.keyword('extends').prepend[oneSpace].append[oneSpace]
		}

		for (DataFieldDeclaration field : data.fields) {
			field.format
			if (field.definition !== null && field != data.fields.last) {
				field.append[newLines = 2]
			} else {
				field.append[newLine]
			}
		}
    }
}
