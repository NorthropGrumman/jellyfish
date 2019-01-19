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

import com.ngc.seaside.systemdescriptor.systemDescriptor.BaseLinkDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.Links
import org.eclipse.xtext.formatting2.IFormattableDocument
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedLinkDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedLinkNameDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage

class LinksFormatter extends AbstractSystemDescriptorFormatter {
	
	def dispatch void format(Links links, extension IFormattableDocument document) {
		var begin = links.regionFor.keyword('links')
		var end = links.regionFor.keyword('}')
			.append[newLines = 2]
		interior(begin, end)[indent]

		links.regionFor.keyword('{')
			.prepend[oneSpace]
			.append[newLine]

		for (LinkDeclaration d : links.declarations) {
			d.format
			if (d.definition !== null && d != links.declarations.last) {
				d.append[newLines = 2]
			} else {
				d.append[newLine]
			}
		}
	}
	
	def dispatch void format(RefinedLinkNameDeclaration declaration, extension IFormattableDocument document) {
		declaration.regionFor.keyword('refine')
			.append[oneSpace]
		declaration.regionFor.keyword('link')
			.append[oneSpace]
		
		if (declaration.definition !== null) {
			declaration.regionFor.feature(SystemDescriptorPackage.Literals.LINK_DECLARATION__NAME)
				.append[newLines = 0]
			declaration.definition.format
		} else {
			declaration.regionFor.feature(SystemDescriptorPackage.Literals.LINK_DECLARATION__NAME)
				.append[newLine]
		}
	}

	def dispatch void format(BaseLinkDeclaration declaration, extension IFormattableDocument document) {
		declaration.regionFor.keyword('link')
			.append[oneSpace]

		if (declaration.name !== null && !declaration.name.empty) {
			declaration.source.prepend[declaration.name]
			declaration.source.prepend[oneSpace]
		}

		declaration.regionFor.keyword('->')
			.prepend[oneSpace]
			.append[oneSpace]
		declaration.source.append[oneSpace]
		
		if (declaration.definition !== null) {
			declaration.target
				.prepend[oneSpace]
				.append[newLines = 0]
			declaration.definition.format
		} else {
			declaration.target.append[newLine]
		}
	}
	
	def dispatch void format(RefinedLinkDeclaration declaration, extension IFormattableDocument document) {
		// Note this code is the same as for BaseLinkDeclaration.  However, the generated 
		// classes RefinedLinkDeclaration and BaseLinkDeclaration have not common 
		// base type with both the source and target attributes, so we just cut and paste.
		declaration.regionFor.keyword('refine')
			.append[oneSpace]
		declaration.regionFor.keyword('link')
			.append[oneSpace]

		if (declaration.name !== null && !declaration.name.empty) {
			declaration.source.prepend[declaration.name]
			declaration.source.prepend[oneSpace]
		}

		declaration.regionFor.keyword('->')
			.prepend[oneSpace]
			.append[oneSpace]
		declaration.source.append[oneSpace]
		
		if (declaration.definition !== null) {
			declaration.target
				.prepend[oneSpace]
				.append[newLines = 0]
			declaration.definition.format
		} else {
			declaration.target.append[newLine]
		}
	}
}
