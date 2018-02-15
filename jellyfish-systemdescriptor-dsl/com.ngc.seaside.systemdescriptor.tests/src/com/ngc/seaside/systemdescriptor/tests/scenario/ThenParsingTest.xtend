package com.ngc.seaside.systemdescriptor.tests.scenario

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
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
class ThenParsingTest {

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
			Models.ALARM.requiredResources,
			Models.GENERIC_MODEL_WITH_MULTIPLE_THEN_STEPS.requiredResources
		)
	}

	@Test
	def void testDoesParseScenarioWithThen() {
		val result = parseHelper.parse(Models.ALARM.source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		val scenario = model.scenarios.get(0)
		val then = scenario.then
		val step = then.steps.get(0)
		assertEquals(
			"keyword not correct!",
			"doSomething",
			step.keyword
		)
		assertEquals(
			"keyword parameters not correct!",
			"withThis",
			step.parameters.get(0)
		)
	}

	@Test
	def void testDoesParseScenarioWithMultipleThens() {
		val result = parseHelper.parse(
			Models.GENERIC_MODEL_WITH_MULTIPLE_THEN_STEPS.source,
			requiredResources.resourceSet
		)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		val scenario = model.scenarios.get(0)
		val then = scenario.then
		assertEquals(
			"did not parse all then fragments!",
			2,
			then.steps.size
		)
	}

	@Test
	def void testDoesParseScenarioWithMultipleThensWithPeriodCharacters() {
		val source = '''
			package clocks.models
			 
			import clocks.datatypes.ZonedTime
			 
			model Alarm {
			  input {
			  	ZonedTime currentTime
			  	ZonedTime alarmTime
			  }
			  
			  scenario triggerAlert {
			  	when receiving alarmTime
			  	then Important.doSomething
			  	and NotSoImportant.doSomethingElse
			  }
			}
		'''

		val result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		val scenario = model.scenarios.get(0)
		val then = scenario.then
		assertEquals(
			"did not parse all then fragments!",
			2,
			then.steps.size
		)
	}
}
