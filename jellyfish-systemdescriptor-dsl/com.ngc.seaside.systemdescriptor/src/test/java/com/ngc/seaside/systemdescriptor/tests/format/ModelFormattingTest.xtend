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
class ModelFormattingTest {
	
	@Inject
	FormattingTestUtils formattingUtils
	
	var formattedModel = '''
		package com.ngc.test
		
		import com.ngc.import1.Data1
		import com.ngc.import2.Data2
		import com.ngc.import1.Model1
		import com.ngc.import2.Model2

		model Model {
			metadata {
					"a" : 1
			}
		
			input {
				Data1 input1
				com.ngc.import3.Data3 input2
			}

			output {
				Data2 output1
				com.ngc.import4.Data4 output2
			}
		
			scenario scenario1 {
				given hasReceived input1
				and hasReceived input2
				when receiving input1
				and receiving input2
				then willPublish output1
				and willPublish output2
			}
		
			scenario scenario2 {
				when haveReceivedRequest to input2
				then willRespond with output2
			}
		
			parts {
				Model1 part1
				com.ngc.import3.Model3 part3
			}
		
			requires {
				Model2 require1
				com.ngc.import4.Model4 require4
			}

		}
		'''
	
	@Test
	def void testModelFormatting() {
		formattingUtils.testFormatter(formattedModel)
	}
	
	var refinedFormattedModel = '''
		package com.ngc.test
		
		import com.ngc.import1.BaseModel

		model Model refines BaseModel {
			metadata {
					"a" : 1
			}
		
			parts {
				refine part1
				refine part2 {
						metadata {
								"a" : 1
						}
				}
			}
		
			requires {
				refine requirement1
				refine requirement2 {
						metadata {
								"a" : 1
						}
				}
			}
		
		}
		'''
	
	@Test
	def void testRefinedModelFormatting() {
		formattingUtils.testFormatter(refinedFormattedModel)
	}
}
