/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.tests.scenario

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import com.ngc.seaside.systemdescriptor.tests.resources.Datas
import com.ngc.seaside.systemdescriptor.tests.resources.Models
import org.eclipse.emf.ecore.resource.Resource
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