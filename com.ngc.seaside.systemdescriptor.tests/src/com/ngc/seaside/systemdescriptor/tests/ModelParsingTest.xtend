package com.ngc.seaside.systemdescriptor.tests

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*
import org.eclipse.xtext.junit4.util.ResourceHelper
import org.junit.Before
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.diagnostics.Diagnostic
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.junit.Ignore

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
		val name = metadata.json.firstObject;
		assertEquals(
			"metadata not correct!",
			"name",
			name.element
		)
		assertEquals(
			"metadata not correct!",
			"Timer",
			name.content.value
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
	def void testDoesParseCompleteJellyFishWhitepaperExample() {
		val dataSource = 
		'''
		package clocks.datatypes
		
		data Time {
		  metadata {
		    "name": "Time",
		    "description": "Represents a local time (does not account for timezones)."
		  }
		  
		  int hour {
		    "validation": {
		      "min": "0",
		      "max": "23"
		    }
		  }
		  
		  int minute {
		    "validation": {
		      "min": "0",
		      "max": "60"
		    }
		  }
		  
		  int second {
		    "validation": {
		      "min": "0",
		      "max": "60"
		    }
		  }
		}
		'''
		
		val timerSource = 
		'''
		package clocks.models
		
		import clocks.datatypes.Time
		
		model Timer {
		  metadata {
		    "name": "Timer",
		    "description": "Outputs the current time.",
		    "stereotypes": ["service"]
		  }
		
		  output {
		    Time currentTime
		  }
		}
		'''
		
		val clockDisplaySource = 
		'''
		package clocks.models
		
		import clocks.datatypes.Time
		
		model ClockDisplay {
		 metadata {
		    "description": "Displays the current time.",
		    "stereotypes": ["service"]
		 }
		 
		 input {
		     Time currentTime
		 }
		}
		'''
		
		dataResource = resourceHelper.resource(
			dataSource,
			URI.createURI("datatypes.sd")
		)
		validationTester.assertNoIssues(dataResource)

		val clockDisplayResource = resourceHelper.resource(
			clockDisplaySource,
			dataResource.resourceSet
		)
		validationTester.assertNoIssues(clockDisplayResource)
		
		val speakerSource = 
		'''
		package clocks.models
		
		import clocks.datatypes.Time
		
		model Speaker {
		  metadata {
		    "description": "Makes annoying buzzing sounds.",
		    "stereotypes": ["device"]
		  }
		}
		'''
		val speakerResource = resourceHelper.resource(
			speakerSource,
			dataResource.resourceSet
		)
		validationTester.assertNoIssues(speakerResource)
		
		val result = parseHelper.parse(timerSource, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
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
