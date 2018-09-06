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