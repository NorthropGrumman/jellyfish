package com.ngc.seaside.systemdescriptor.tests.scenario

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import com.ngc.seaside.systemdescriptor.tests.resources.Datas
import com.ngc.seaside.systemdescriptor.tests.resources.Models
import org.eclipse.emf.ecore.resource.Resource
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
	
	Resource requiredResources

	@Before
	def void setup() {
		requiredResources = Models.allOf(
			resourceHelper,
			Models.SPEAKER.requiredResources,
			Models.CLOCK,
			Datas.TIME
		)
	}
	
	@Test
	def void testDoesParseModelWithEmptyScenario() {
		val result = parseHelper.parse(Models.SPEAKER.source, requiredResources.resourceSet)
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
		
		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
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
		
		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
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
		
		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
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
			 
			import clocks.models.part.Clock
			 
			model Speaker {
			  scenario buzz { }
			  
			  parts {
			    Clock buzz
			  }
			}
		'''
		
		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
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
			 
			import clocks.models.part.Clock
			 
			model Speaker {
			  requires {
			  	Clock buzz
			  }
				
			  scenario buzz { }
			}
		'''
		
		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.SCENARIO,
			null
		)
	}
	

	@Test
	def void testDoesNotAllowScenarioNamesToEscapeKeywords() {
		val source = '''
			package clocks.models
			 
			model Speaker {
			  scenario ^data { }
			}
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.SCENARIO,
			null
		)
	}
	
	@Test
	def void testDoesScenarioAllowPeriodCharactersInSteps() {
		val source = '''
			package test
			 
			model TestModel {
			  scenario calculateConsolidatedTrackPriority {
			          when receiving trackPriority
			          then willBeginCorrelationEvent identified by prioritizedSystemTracks.header.correlationEventId
			           and willPublish prioritizedSystemTracks
			           and willBeCompleted within 500 milliseconds
			      }
			}
		'''
		
		val result = parseHelper.parse(source, requiredResources.resourceSet)
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
			"calculateConsolidatedTrackPriority",
			scenario.name
		)
	}
}