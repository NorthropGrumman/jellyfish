package com.ngc.seaside.systemdescriptor.tests

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Cardinality
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.diagnostics.Diagnostic
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.util.ResourceHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*
import com.ngc.seaside.systemdescriptor.systemDescriptor.StringValue

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class ModelParsingTest {

	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ResourceHelper resourceHelper

	@Inject
	ValidationTestHelper validationTester

	Resource dataResource

	Resource modelResource

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
			Diagnostic.LINKING_DIAGNOSTIC
		)
	}

	@Test
	@Ignore("Does not check for duplicate model declarations in the same package yet")
	def void testDoesRequireUnqiueNames() {
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
