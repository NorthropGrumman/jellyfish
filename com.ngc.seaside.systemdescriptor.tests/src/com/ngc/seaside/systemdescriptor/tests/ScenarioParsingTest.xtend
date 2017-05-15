package com.ngc.seaside.systemdescriptor.tests

import com.google.inject.Inject
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
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class ScenarioParsingTest {
	
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
				package clocks.models.sub
							
				model Wire {
				}
			''',
			URI.createURI("part.sd")
		)
		validationTester.assertNoIssues(modelResource)
	}
	
	@Test
	def void testDoesParseModelWithEmptyScenario() {
		val source = '''
			package clocks.models
			 
			import clocks.datatypes.Time
			 
			model Speaker {
			  scenario buzz { }
			}
		'''

		val result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		assertFalse(
			"did not parse scenario!",
			model.scenarios.empty
		)
		
		val scenario = model.scenarios.get(0)
		assertEquals(
			"scenario name not correct!",
			"buzz",
			scenario.name
		)
	}
	
	@Test
	def void testDoesNotParseModelWithDuplicateScenarios() {
		val source = '''
			package clocks.models
			 
			import clocks.datatypes.Time
			 
			model Speaker {
			  scenario buzz { }
			  scenario buzz { }
			}
		'''
		
		val invalidResult = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.SCENARIO,
			null
		)
	}
	
	@Test
	def void testDoesNotParseModelWithDuplicateInputAndScenario() {
		val source = '''
			package clocks.models
			 
			import clocks.datatypes.Time
			 
			model Speaker {
			  input {
			  	Time buzz
			  }
				
			  scenario buzz { }
			}
		'''
		
		val invalidResult = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.SCENARIO,
			null
		)
	}
	
	@Test
	def void testDoesNotParseModelWithDuplicateOutputAndScenario() {
		val source = '''
			package clocks.models
			 
			import clocks.datatypes.Time
			 
			model Speaker {
			  output {
			  	Time buzz
			  }
				
			  scenario buzz { }
			}
		'''
		
		val invalidResult = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.SCENARIO,
			null
		)
	}
	
	@Test
	def void testDoesNotParseModelWithDuplicatePartAndScenario() {
		val source = '''
			package clocks.models
			 
			import clocks.models.sub.Wire
			 
			model Speaker {
			  scenario buzz { }
			  
			  parts {
			    Wire buzz
			  }
			}
		'''
		
		val invalidResult = parseHelper.parse(source, modelResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PART_DECLARATION,
			null
		)
	}
	
	@Test
	def void testDoesNotParseModelWithDuplicateRequirementAndScenario() {
		val source = '''
			package clocks.models
			 
			import clocks.models.sub.Wire
			 
			model Speaker {
			  requires {
			  	Wire buzz
			  }
				
			  scenario buzz { }
			}
		'''
		
		val invalidResult = parseHelper.parse(source, modelResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.SCENARIO,
			null
		)
	}
}