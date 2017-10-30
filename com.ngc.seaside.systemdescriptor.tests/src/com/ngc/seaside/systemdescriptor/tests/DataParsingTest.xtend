package com.ngc.seaside.systemdescriptor.tests

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataType
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.eclipse.emf.common.util.URI
import org.eclipse.xtext.diagnostics.Diagnostic
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.util.ResourceHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*
import com.ngc.seaside.systemdescriptor.systemDescriptor.JsonValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.IntValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.StringValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataModelFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.Cardinality

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class DataParsingTest {

	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ResourceHelper resourceHelper

	@Inject
	ValidationTestHelper validationTester

	@Test
	def void testDoesParseEmptyData() {
		val source = '''
			package clocks.datatypes
			
			data Time {
			}
		'''

		val result = parseHelper.parse(source)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		assertEquals(
			"package name not correct",
			"clocks.datatypes",
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
		val source = '''
			package clocks.datatypes
			
			data Time {
				many int hour
				int minute
				int second
			}
		''';

		val result = parseHelper.parse(source)
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
			"hour",
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
		val source = '''
			package clocks.datatypes
			
			data Time {
				metadata {
				    "name": "Time",
				    "description": "Represents a local time (does not account for timezones)."
				}
				
				int hour
				int minute
				int second
			}
		''';

		val result = parseHelper.parse(source)
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
			"Time",
			(firstmember.value as StringValue).value
		)
	}

	@Test
	def void testDoesParseDataWithValidation() {
		val source = '''
			package clocks.datatypes
			
			data Time {
				metadata {
				    "name": "Time",
				    "description": "Represents a local time (does not account for timezones)."
				}
				
				int hour {
					"validation": {
						"min": 0,
						"max": 23
					}
				}
				
				int minute {
					"validation": {
						"min": 0,
						"max": 59
					}
				}
					
				int second {
					"validation": {
						"min": 0,
						"max": 59
					}
				}
			}
		''';

		val result = parseHelper.parse(source)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		var data = result.element as Data
		var field = data.fields.get(0)
		
		assertTrue(field instanceof PrimitiveDataFieldDeclaration)		
		field = field as PrimitiveDataFieldDeclaration		
		
		var metadata = field.metadata
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
			
			data Time {
			}
			
			data LocalTime {
			}
		'''

		val invalidResult = parseHelper.parse(source)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PACKAGE,
			Diagnostic.SYNTAX_DIAGNOSTIC
		)
	}

	@Test
	@Ignore("Does not check for duplicate data declarations in the same package yet")
	def void testDoesRequireUnqiueNames() {
		val dataResource = resourceHelper.resource(
			'''
				package clocks.datatypes
							
				data Time {
				}
			''',
			URI.createURI("datatypes.sd")
		)
		validationTester.assertNoIssues(dataResource)

		val source = '''
			package clocks.datatypes
			
			data Time {
			}
		'''

		val invalidResult = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.DATA,
			null
		)
	}
	
	@Test
	def void testDoesParseImportedDataAsFieldsInData() {
		val dateSource = '''
			package clocks.datatypes
			
			data Date {
				int day
				int month
				int year
			}
		''';
		
		val dataResource = resourceHelper.resource(dateSource, URI.createURI("datatypes.sd"))
		validationTester.assertNoIssues(dataResource)
		
		val timeSource = '''
			package clocks.datatypes
			
			data Time {
				int hour
				int minute
				int second
			}
		''';
		
		val timeResource = resourceHelper.resource(timeSource, dataResource.resourceSet)
		validationTester.assertNoIssues(timeResource)

		val dateTimeSource = '''
			package clocks.otherdatatypes
			
			import clocks.datatypes.Date
			import clocks.datatypes.Time
			
			data DateTime {
			  Date date
			  many Time time
			}
		'''

		val result = parseHelper.parse(dateTimeSource, dataResource.resourceSet)
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
		assertEquals(
			"data cardinality not correct!",
			Cardinality.MANY,
			timeDataRef.cardinality
		)
	}
	
	def void testDoesParseQualifiedDataAsFields() {
		val dataSource1 = '''
			package com.test1
			data Data1 {}
		''';
		
		val dataResource1 = resourceHelper.resource(dataSource1, URI.createURI("datatypes.sd"))
		validationTester.assertNoIssues(dataResource1)
		
		val dataSource2 = '''
			package com.test2
			enum Data1 {}
		''';
		
		val dataResource2 = resourceHelper.resource(dataSource2, dataResource1.resourceSet)
		validationTester.assertNoIssues(dataResource2)
		
		val dataSource3 = '''
			package com.test3
			data Data1 {}
		''';
		
		val dataResource3 = resourceHelper.resource(dataSource3, dataResource1.resourceSet)
		validationTester.assertNoIssues(dataResource3)

		val dataSource4 = '''
			package com.test4
			import com.test3.Data1
			data Data : com.test1.Data1 {
			  com.test1.Data1 data1
			  com.test2.Data1 data2
			  Data1 data3
			  
			}
		'''

		val result = parseHelper.parse(dataSource4, dataResource1.resourceSet)
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

		val invalidResult = parseHelper.parse(source)
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

		val invalidResult = parseHelper.parse(source)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION,
			null
		)
	}
	
}
