package com.ngc.seaside.systemdescriptor.tests.data

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Enumeration
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import com.ngc.seaside.systemdescriptor.tests.resources.Datas
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class EnumParsingTest {
	
	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ValidationTestHelper validationTester
	
	@Test
	def void testDoesParseEnum() {
		val result = parseHelper.parse(Datas.TIME_ZONE.source)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
		
		val timeZone = result.element as Enumeration
		assertEquals("timezone value not correct!",
			"CST",
			timeZone.values.get(0).value
		)	
		assertEquals("timezone value not correct!",
			"MST",
			timeZone.values.get(1).value
		)	
		assertEquals("timezone value not correct!",
			"EST",
			timeZone.values.get(2).value
		)	
		assertEquals("timezone value not correct!",
			"PST",
			timeZone.values.get(3).value
		)	
	}
}