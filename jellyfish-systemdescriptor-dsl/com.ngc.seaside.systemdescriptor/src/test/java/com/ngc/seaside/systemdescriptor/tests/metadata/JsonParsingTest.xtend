package com.ngc.seaside.systemdescriptor.tests.metadata

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*
import com.ngc.seaside.systemdescriptor.systemDescriptor.ArrayValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.StringValue
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import com.ngc.seaside.systemdescriptor.tests.resources.Datas

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class JsonParsingTest {

	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ValidationTestHelper validationTester

	@Test
	def void testDoesParseMetadataAsJson() {
		val result = parseHelper.parse(Datas.DATA_WITH_GENERIC_METADATA.getSource())
		Assert.assertNotNull(result)
		validationTester.assertNoIssues(result);
		
		// get the array data
		var metadata = result.element.metadata
		val arraymember = metadata.json.members.get(2)
		assertEquals(
			"metadata did not parse!",
			"arraydata",
			arraymember.key
		)
		
		var arrayvalue = (arraymember.value as ArrayValue).value
		assertEquals(
			"metadata did not parse!",
			2,
			arrayvalue.values.size()
		)	
		assertEquals(
			"metadata did not parse!",
			"metadata",
			(arrayvalue.values.get(0) as StringValue).value
		)	
		assertEquals(
			"metadata did not parse!",
			"test",
			(arrayvalue.values.get(1) as StringValue).value
		)			
	}

}
