package com.ngc.seaside.systemdescriptor.tests.scenario

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
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
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import com.ngc.seaside.systemdescriptor.tests.resources.Models

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class GivenParsingTest {

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
			Models.GENERIC_MODEL_WITH_MULTIPLE_GIVEN_STEPS.requiredResources
		)
	}

	@Test
	def void testDoesParseScenarioWithGiven() {
		val result = parseHelper.parse(Models.ALARM.source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		val scenario = model.scenarios.get(0)
		val given = scenario.given
		val step = given.steps.get(0)
		assertEquals(
			"subject not correct!",
			"alarmTime",
			step.keyword
		)
		assertEquals(
			"precondition not correct!",
			"hasBeenReceived",
			step.parameters.get(0)
		)
	}

	@Test
	def void testDoesParseScenarioWithMultipleGiven() {
		val result = parseHelper.parse(
			Models.GENERIC_MODEL_WITH_MULTIPLE_GIVEN_STEPS.source,
			requiredResources.resourceSet
		)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		val scenario = model.scenarios.get(0)
		val given = scenario.given
		assertEquals(
			"did not parse all given steps!",
			2,
			given.steps.size
		)
	}

	@Test
	def void testDoesParseScenarioWithMultipleGivensWithPeriodCharacters() {
		val source = '''
			package yet.another.test
			 
			import clocks.datatypes.ZonedTime
			 
			model OfSpecialCharacters {
			  input {
			  	ZonedTime currentTime
			  	ZonedTime alarmTime
			  }
			  
			  scenario triggerAlert {
			  	given secretSpaceTime.alarmTime hasBeenReceived
			  	and secretSpaceTime.alarmTime hasBeenValidated
			  	when validating alarmTime
			  	then doSomething
			  }
			}
		'''

		val result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		val scenario = model.scenarios.get(0)
		val given = scenario.given
		assertEquals(
			"did not parse all given steps!",
			2,
			given.steps.size
		)
	}
}
