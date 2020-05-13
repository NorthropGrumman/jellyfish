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
package com.ngc.seaside.systemdescriptor.tests.data

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Cardinality
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data
import com.ngc.seaside.systemdescriptor.systemDescriptor.IntValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.JsonValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataType
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataModelFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.StringValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import com.ngc.seaside.systemdescriptor.tests.resources.Datas
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.diagnostics.Diagnostic
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.util.ResourceHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class DataParsingTest {

	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ResourceHelper resourceHelper

	@Inject
	ValidationTestHelper validationTester

	Resource requiredResources

	@Before
	def void setup() {
		requiredResources = Datas.allOf(
			resourceHelper,
			Datas.EMPTY_DATA,
			Datas.DATA_WITH_MANY_FIELDS.requiredResources,
			Datas.TIME.requiredResources,
			Datas.DATE_TIME.requiredResources
		)
	}

	@Test
	def void testDoesParseEmptyData() {
		val result = parseHelper.parse(Datas.EMPTY_DATA.source)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		assertEquals(
			"package name not correct",
			"foo",
			result.name
		)

		var data = result.element
		assertTrue(
			"did not parse data!",
			data instanceof Data
		)
	}

	@Test
	def void testDoesParseData() {
		val result = parseHelper.parse(Datas.DATA_WITH_MANY_FIELDS.source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		var data = result.element as Data
		assertEquals(
			"wrong number of fields!",
			3,
			data.fields.size
		)

		var rawField = data.fields.get(0)
		assertTrue(rawField instanceof PrimitiveDataFieldDeclaration)
		var field = rawField as PrimitiveDataFieldDeclaration

		assertEquals(
			"data type not correct!",
			PrimitiveDataType.INT,
			field.type
		)
		assertEquals(
			"field name not correct!",
			"x",
			field.name
		)
		assertEquals(
			"cardinality not correct!",
			Cardinality.MANY,
			field.cardinality
		)
	}

	@Test
	def void testDoesParseDataWithMetadataValidation() {
		val result = parseHelper.parse(
			Datas.DATA_WITH_GENERIC_METADATA.source,
			requiredResources.resourceSet
		)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		var metadata = result.element.metadata
		val firstmember = metadata.json.members.get(0)
		assertEquals(
			"metadata did not parse!",
			"name",
			firstmember.key
		)
		assertEquals(
			"metadata did not parse!",
			"test",
			(firstmember.value as StringValue).value
		)
	}

	@Test
	def void testDoesParseDataWithValidation() {
		val result = parseHelper.parse(Datas.TIME.source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		var data = result.element as Data
		var field = data.fields.get(0)

		assertTrue(field instanceof PrimitiveDataFieldDeclaration)
		field = field as PrimitiveDataFieldDeclaration

		var metadata = field.definition.metadata.json
		var validation = metadata.members.get(0)

		assertEquals(
			"validation did not parse!",
			"validation",
			validation.key
		)

		var metadatavalidation = validation.value as JsonValue
		var minkeyvalue = metadatavalidation.value.members.get(0)
		var maxkeyvalue = metadatavalidation.value.members.get(1)

		assertEquals(
			"validation min did not parse!",
			"min",
			minkeyvalue.key
		)

		assertEquals(
			"validation min did not parse!",
			0,
			(minkeyvalue.value as IntValue).value
		)

		assertEquals(
			"validation max did not parse!",
			"max",
			maxkeyvalue.key
		)

		assertEquals(
			"validation max did not parse!",
			23,
			(maxkeyvalue.value as IntValue).value
		)

	// TODO TH: the JSON is not correct the type ObjectValue only has a string field, it is not recursive.
	// consider https://gist.github.com/nightscape/629651
	}

	@Test
	def void testDoesNotParseMultipleDataElements() {
		val source = '''
			package clocks.datatypes
			
			data Foo {
			}
			
			data Bar {
			}
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PACKAGE,
			Diagnostic.SYNTAX_DIAGNOSTIC
		)
	}

	@Test
	def void testDoesParseImportedDataAsFieldsInData() {
		val result = parseHelper.parse(Datas.DATE_TIME.source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		var resultData = result.element as Data
		assertEquals(
			"wrong number of fields!",
			2,
			resultData.fields.size
		)

		var rawDateField = resultData.fields.get(0)
		assertTrue(rawDateField instanceof ReferencedDataModelFieldDeclaration)
		var dateDateRef = rawDateField as ReferencedDataModelFieldDeclaration

		assertTrue(
			"dateDateRef's dataModel should be a Data instance!",
			dateDateRef.dataModel instanceof Data
		)
		assertEquals(
			"data type not correct!",
			"Date",
			dateDateRef.dataModel.name
		)
		assertEquals(
			"data ref name not correct!",
			"date",
			dateDateRef.name
		)

		var rawTimeField = resultData.fields.get(1)
		assertTrue(rawTimeField instanceof ReferencedDataModelFieldDeclaration)
		var timeDataRef = rawTimeField as ReferencedDataModelFieldDeclaration

		assertTrue(
			"timeDataRef's dataModel should be a Data instance!",
			timeDataRef.dataModel instanceof Data
		)
		assertEquals(
			"data type not correct!",
			"Time",
			timeDataRef.dataModel.name
		)
		assertEquals(
			"data ref name not correct!",
			"time",
			timeDataRef.name
		)
	}
	
	@Test
	def void doesDoesParseDataThatUsesFullyQualifiedNames() {
		val source = '''
			package clocks.datatypes
			
			data FullyQualified extends clocks.datatypes.DateTime {
				clocks.datatypes.TimeZone timeZone
			}
		'''
		
		requiredResources = Datas.allOf(
			resourceHelper,
			Datas.TIME_ZONE,
			Datas.DATE_TIME
		)
		
		val result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
	}

	@Test
	def void testDoesNotAllowDataNameKeywords() {
		val source = '''
			package foo.datatypes
			
			data ^Foo {
				int data
				float model
			}
			
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.ELEMENT,
			null
		)
	}

	@Test
	def void testDoesNotAllowFieldNameKeywords() {
		val source = '''
			package foo.datatypes
			
			data Foo {
				int ^data
				float model
			}
			
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION,
			null
		)
	}
}
