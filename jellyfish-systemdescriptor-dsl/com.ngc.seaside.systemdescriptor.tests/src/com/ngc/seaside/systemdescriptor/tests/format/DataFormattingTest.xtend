package com.ngc.  seaside.systemdescriptor.tests.format

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
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
