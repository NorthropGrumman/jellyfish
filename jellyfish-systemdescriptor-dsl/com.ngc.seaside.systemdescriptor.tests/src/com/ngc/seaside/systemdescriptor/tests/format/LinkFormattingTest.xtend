package com.ngc.seaside.systemdescriptor.tests.format

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
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