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
