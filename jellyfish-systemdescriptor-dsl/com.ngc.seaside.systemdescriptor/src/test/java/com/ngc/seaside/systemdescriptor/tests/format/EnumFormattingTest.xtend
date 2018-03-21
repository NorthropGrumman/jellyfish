package com.ngc.  seaside.systemdescriptor.tests.format

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(SystemDescriptorInjectorProvider))
class EnumFormattingTest {
    @Inject FormattingTestUtils formattingUtils

    var formattedEnum = '''
		package com.ngc.test

		enum Enumeration {
			metadata {
				"a" : 1
			}

			FIELD1
			FIELD2
			FIELD3 {
				metadata {
					"a" : 1
				}
			}
		}
  '''

    @Test
    def void testEnumFormatting() {
        formattingUtils.testFormatter(formattedEnum)
    }
}
