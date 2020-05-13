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
