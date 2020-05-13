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
