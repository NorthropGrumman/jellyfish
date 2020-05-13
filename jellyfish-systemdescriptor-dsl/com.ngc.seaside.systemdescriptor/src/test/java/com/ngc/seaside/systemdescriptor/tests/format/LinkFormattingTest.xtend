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
package com.ngc.seaside.systemdescriptor.tests.format

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(SystemDescriptorInjectorProvider))
class LinkFormattingTest {
	
	@Inject
	FormattingTestUtils formattingUtils
	
	var formattedLinks = '''
		package com.ngc.test

		model Model {
			links {
				link myLink input1 -> output1
				link input2.field2 -> output2.field3.field4 {
					metadata {
						"a" : 1
					}
				}
			}

		}
		'''
	
	@Test
	def void testLinkFormatting() {
		formattingUtils.testFormatter(formattedLinks)
	}
	
	var formattedRefinedLinks = '''
		package com.ngc.test

		model Model {
			links {
				refine myLink {
					metadata {
						"a" : 1
					}
				}
		
				refine myOtherLink
				refine link input2.field2 -> output2.field3.field4 {
					metadata {
						"a" : 1
					}
				}
			}

		}
		'''
	
	@Test
	def void testRefinedLinkFormatting() {
		formattingUtils.testFormatter(formattedRefinedLinks)
	}
	
}