package com.ngc.seaside.systemdescriptor.tests.format

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(SystemDescriptorInjectorProvider))
class PropertiesValuesFormattingTest {
	
	@Inject
	FormattingTestUtils formattingUtils
	
	var formattedModel = '''
		package com.ngc.test
		
		import com.ngc.imports.Model1
		import com.ngc.imports.Model2
		import com.ngc.imports.MyEnum

		model Model {
			properties {
				int a
				float b
				string c
				boolean d
				MyEnum status
				com.ngc.imports.Config config
				many int lots
				many MyEnum ofEnums
		
				a = 1
				b = 1.2
				c = "hello"
				d = true
				status = MyEnum.ENABLED
				config.x = "foo"
			}
		}
		'''
	
	@Test
	def void testModelPropertyValuesFormatting() {
		formattingUtils.testFormatter(formattedModel)
	}
}