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
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*
import com.ngc.seaside.systemdescriptor.systemDescriptor.JsonValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.IntValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.StringValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataModelFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.Cardinality
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import org.eclipse.emf.ecore.resource.Resource

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class DataParsingTest {

	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ResourceHelper resourceHelper

	@Inject
	ValidationTestHelper validationTester
	
	Resource dataResource

	Resource modelResource
	
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
	def void testDoesRequireUnqiueDataNames() {
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
	
		@Test
	def void testDoesParseModelWithComments() {
		val source = '''
			// This is a package comment
			/* this is a package comment */
			/* this
			 * is
			 * a 
			 * comment
			 */
			-- This is another package comment
			package clocks.models
			
			// This is a model comment
			/* this is a model comment */
			/* this
			 * a
			 * comment
			 */
			-- This is another model comment
			model Timer {
			}
		'''

		val result = parseHelper.parse(source)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
	}
	
		@Before
	def void setup() {
		dataResource = resourceHelper.resource(
			'''
				package clocks.datatypes
							
				data Time {
				}
			''',
			URI.createURI("datatypes.sd")
		)
		validationTester.assertNoIssues(dataResource)

		modelResource = resourceHelper.resource(
			'''
				package clocks.models.more
							
				model Extra {
				}
			''',
			dataResource.resourceSet
		)
		validationTester.assertNoIssues(modelResource)
	}

	@Test
	def void testDoesParseEmptyModel() {
		val source = '''
			package clocks.models
			
			model Timer {
			}
		'''

		val result = parseHelper.parse(source)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		assertEquals(
			"package name not correct",
			"clocks.models",
			result.name
		)

		var model = result.element
		assertTrue(
			"did not parse model!",
			model instanceof Model
		)
	}

	@Test
	def void testDoesParseModelWithMetadata() {
		val source = '''
			package clocks.models
			
			model Timer {
				metadata {
					"name": "Timer",
					"description": "Outputs the current time.",
					"stereotypes": ["service"]
				}
			}
		'''

		val result = parseHelper.parse(source)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val metadata = result.element.metadata;
		val firstmember = metadata.json.members.get(0)
		assertEquals(
			"metadata not correct!",
			"name",
			firstmember.key
		)
		assertEquals(
			"metadata not correct!",
			"Timer",
			(firstmember.value as StringValue).value
		)
	}

	@Test
	def void testDoesParseModelWithImportedData() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			
			model Timer {
			}
		'''

		val result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
	}

	@Test
	def void testDoesParseModelWithOutputs() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			
			model Timer {
				
				output {
					Time currentTime
				}
			}
		'''

		val result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		val output = model.output
		val declaration = output.declarations.get(0)

		assertEquals(
			"output name not correct!",
			"currentTime",
			declaration.name
		)
		assertEquals(
			"output type not correct!",
			"Time",
			declaration.type.name
		)
		assertEquals(
			"cardinality is not the default value!",
			Cardinality.DEFAULT,
			declaration.cardinality
		)
	}

	@Test
	def void testDoesParseModelWithOutputsWithCardinality() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			
			model Timer {
				
				output {
					many Time currentTime
				}
			}
		'''

		val result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		val output = model.output
		val declaration = output.declarations.get(0)

		assertEquals(
			"cardinality for output is not correct!",
			Cardinality.MANY,
			declaration.cardinality
		)
	}

	@Test
	def void testDoesNotParseModelWithDuplicateOutputs() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			
			model Timer {
				
				output {
					Time currentTime
					Time currentTime
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.OUTPUT_DECLARATION,
			null
		)
	}

	@Test
	def void testDoesNotParseModelWithMissingOutputDataType() {
		val source = '''
			package clocks.models
			
			model Timer {
				
				output {
					Time currentTime
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.OUTPUT_DECLARATION,
			Diagnostic.LINKING_DIAGNOSTIC
		)
	}

	@Test
	def void testDoesNotParseModelWithOutputTypeOfModel() {
		val source = '''
			package clocks.models
			
			import clocks.models.more.Extra
			
			model Timer {
				
				output {
					Extra extra
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.OUTPUT_DECLARATION,
			Diagnostic.LINKING_DIAGNOSTIC
		)
	}

	@Test
	def void testDoesParseModelWithInput() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			
			model ClockDisplay {
				
				input {
					Time currentTime
				}
			}
		'''

		val result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		val input = model.input
		val declaration = input.declarations.get(0)

		assertEquals(
			"input name not correct!",
			"currentTime",
			declaration.name
		)
		assertEquals(
			"input type not correct!",
			"Time",
			declaration.type.name
		)
		assertEquals(
			"cardinality is not the default value!",
			Cardinality.DEFAULT,
			declaration.cardinality
		)
	}

	@Test
	def void testDoesParseModelWithOutput() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			
			model ClockDisplay {
				
				output {
					Time currentTime
				}
			}
		'''

		val result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		val input = model.output
		val declaration = input.declarations.get(0)

		assertEquals(
			"input name not correct!",
			"currentTime",
			declaration.name
		)
		assertEquals(
			"input type not correct!",
			"Time",
			declaration.type.name
		)
		assertEquals(
			"cardinality is not the default value!",
			Cardinality.DEFAULT,
			declaration.cardinality
		)
	}

	@Test
	def void testDoesParseModelWithInputsWithCardinality() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			
			model Timer {
				
				input {
					many Time currentTime
				}
			}
		'''

		val result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		val input = model.input
		val declaration = input.declarations.get(0)

		assertEquals(
			"cardinality for input is not correct!",
			Cardinality.MANY,
			declaration.cardinality
		)
	}

	@Test
	def void testDoesNotParseModelWithDuplicateInputs() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			
			model ClockDisplay {
				
				input {
					Time currentTime
					Time currentTime
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.INPUT_DECLARATION,
			null
		)
	}

	@Test
	def void testDoesNotParseModelWithMissingInputDataType() {
		val source = '''
			package clocks.models
			
			model ClockDisplay {
				
				input {
					Time currentTime
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.INPUT_DECLARATION,
			Diagnostic.LINKING_DIAGNOSTIC
		)
	}

	@Test
	def void testDoesNotParseModelWithInputTypeOfModel() {
		val source = '''
			package clocks.models
			
			import clocks.models.more.Extra
			
			model Timer {
				
				input {
					Extra extra
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.INPUT_DECLARATION,
			Diagnostic.LINKING_DIAGNOSTIC
		)
	}

	@Test
	def void testDoesNotParseModelWithDuplicateInputAndOutput() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			
			model ClockDisplay {
				
				input {
					Time currentTime
				}
				
				output {
					Time currentTime
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.OUTPUT_DECLARATION,
			null
		)
	}

	@Test
	def void testDoesNotParseWithMissingImports() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.Foo
			
			model Timer {
			}
		'''

		val invalidResult = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.IMPORT,
			null
		)
	}

	@Test
	@Ignore("Does not check for duplicate model declarations in the same package yet")
	def void testDoesRequireUnqiueModelNames() {
		val modelResource = resourceHelper.resource(
			'''
				package clocks.models
							
				model Timer {
				}
			''',
			URI.createURI("models.sd")
		)
		validationTester.assertNoIssues(modelResource)

		val source = '''
			package clocks.models
			
			model Timer {
			}
		'''

		val invalidResult = parseHelper.parse(source, modelResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.MODEL,
			null
		)
	}
}
