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
