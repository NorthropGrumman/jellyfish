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