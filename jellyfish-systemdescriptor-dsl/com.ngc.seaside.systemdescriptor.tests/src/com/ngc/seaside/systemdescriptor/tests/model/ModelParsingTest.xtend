package com.ngc.seaside.systemdescriptor.tests.model

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
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider

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
	
	@Test
	def void testDoesParseQualifiedFields() {
		val dataSource1 = '''
			package com.test1
			data Data1 {}
		''';
		
		val dataResource1 = resourceHelper.resource(dataSource1, URI.createURI("datatypes.sd"))
		validationTester.assertNoIssues(dataResource1)
		
		val dataSource2 = '''
			package com.test2
			data Data1 {}
		''';
		
		val dataResource2 = resourceHelper.resource(dataSource2, dataResource1.resourceSet)
		validationTester.assertNoIssues(dataResource2)
		
		val dataSource3 = '''
			package com.test3
			data Data1 {}
		''';
		
		val dataResource3 = resourceHelper.resource(dataSource3, dataResource1.resourceSet)
		validationTester.assertNoIssues(dataResource3)
		
		val modelSource1 = '''
			package com.test1
			model Model1 {}
		''';
		
		val modelResource1 = resourceHelper.resource(modelSource1, dataResource1.resourceSet)
		validationTester.assertNoIssues(modelResource1)
		
		val modelSource2 = '''
			package com.test2
			model Model1 {}
		''';
		
		val modelResource2 = resourceHelper.resource(modelSource2, dataResource1.resourceSet)
		validationTester.assertNoIssues(modelResource2)
		
		val modelSource3 = '''
			package com.test3
			model Model1 {}
		''';
		
		val modelResource3 = resourceHelper.resource(modelSource3, dataResource1.resourceSet)
		validationTester.assertNoIssues(modelResource3)

		val modelSource4 = '''
			package com.test4
			import com.test3.Data1
			import com.test3.Model1
			model Model {
			  input {
			    com.test1.Data1 input1
			    com.test2.Data1 input2
			    Data1 input3
			  }
			  output {
			    com.test1.Data1 output1
			  	com.test2.Data1 output2
			  	Data1 output3
			  }
			  requires {
			  	com.test1.Model1 require1
			  	com.test2.Model1 require2
			  	Model1 require3
			  }
			  parts {
			    com.test1.Model1 part1
			    com.test2.Model1 part2
			    Model1 part3
			  }
			}
		'''

		val result = parseHelper.parse(modelSource4, dataResource1.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)	
	}
	

	@Test
	def void testDoesNotAllowModelNameKeywords() {
		val source = '''
			package clocks.models
			
			model ^Timer {
				
			}
		'''

		val invalidResult = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.ELEMENT,
			null
		)
	}
	

	@Test
	def void testDoesNotParseModelWithEscapedInputFieldNames() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			
			model ClockDisplay {
				
				input {
					Time ^int
				}
				
				output {
					Time outT
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
	def void testDoesNotParseModelWithEscapedOutputFieldNames() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			
			model ClockDisplay {
				
				input {
					Time inT
				}
				
				output {
					Time ^float
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
}
