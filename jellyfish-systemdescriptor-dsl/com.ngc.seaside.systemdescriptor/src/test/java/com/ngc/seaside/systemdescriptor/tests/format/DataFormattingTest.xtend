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
package com.ngc.  seaside.systemdescriptor.tests.format

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(SystemDescriptorInjectorProvider))
class DataFormattingTest {
	@Inject FormattingTestUtils formattingUtils
	
	var formattedData = '''
		package com.ngc.test
		
		import com.ngc.import1.Data1
		import com.ngc.import2.Data2

		data Data {
			metadata {
				"a" : 1
			}
		
			int field1
			many int field2
			float field3
			many float field4
			string field5
			many string field6
			boolean field7
			many boolean field8
			Data1 field9
			many Data2 field10
			com.ngc.import3.Data3 field11
			many com.ngc.import4.Data4 field12 {
					metadata {
							"a" : 1
					}
			}
		}
	'''
	
	@Test
	def void testDataFormatting() {
		formattingUtils.testFormatter(formattedData)
	}
	
	var formattedDataWithExtension = '''
		package com.ngc.test
		
		data Data extends ParentData {
		}
	'''
	
	@Test
	def void testDataFormattingWithExtension() {
		formattingUtils.testFormatter(formattedDataWithExtension)
	}
}
