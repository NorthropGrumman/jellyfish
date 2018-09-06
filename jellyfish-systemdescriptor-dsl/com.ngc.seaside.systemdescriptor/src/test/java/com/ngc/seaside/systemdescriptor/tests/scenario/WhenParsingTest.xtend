/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
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
class WhenParsingTest {

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
			Models.GENERIC_MODEL_WITH_MULTIPLE_WHEN_STEPS.requiredResources
		)
	}

	@Test
	def void testDoesParseScenarioWithWhen() {
		val result = parseHelper.parse(Models.ALARM.source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		val scenario = model.scenarios.get(0)
		val when = scenario.when
		val step = when.steps.get(0)
		assertEquals(
			"keyword not correct!",
			"receiving",
			step.keyword
		)
		assertEquals(
			"parameters not correct!",
			"alarmTime",
			step.parameters.get(0)
		)
	}

	@Test
	def void testDoesParseScenarioWithMultipleWhens() {
		val result = parseHelper.parse(
			Models.GENERIC_MODEL_WITH_MULTIPLE_WHEN_STEPS.source,
			requiredResources.resourceSet
		)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		val scenario = model.scenarios.get(0)
		val when = scenario.when
		assertEquals(
			"did not parse all when fragments!",
			2,
			when.steps.size
		)
	}

	@Test
	def void testDoesParseScenarioWithMultipleWhensWithPeriodsCharacters() {
		val source = '''
			package clocks.models
			 
			import clocks.datatypes.ZonedTime
			 
			model Alarm {
			  input {
			  	ZonedTime currentTime
			  	ZonedTime alarmTime
			  }
			  
			  scenario triggerAlert {
			  	when receiving Time.alarmTime
			  	and talkingWith Person.yoda
			  	then doSomething
			  }
			}
		'''

		val result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		val scenario = model.scenarios.get(0)
		val when = scenario.when
		assertEquals(
			"did not parse all when fragments!",
			2,
			when.steps.size
		)
	}
}
