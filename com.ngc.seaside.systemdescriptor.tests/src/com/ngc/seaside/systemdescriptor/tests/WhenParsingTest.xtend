package com.ngc.seaside.systemdescriptor.tests

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import org.eclipse.emf.common.util.URI
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
class WhenParsingTest {

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
				package clocks.models
							
				model Speaker {
				}
			''',
			dataResource.resourceSet
		)
		validationTester.assertNoIssues(modelResource)
	}

	@Test
	def void testDoesParseScenarioWithWhen() {
		val source = '''
			package clocks.models
			 
			import clocks.datatypes.Time
			import clocks.models.Speaker
			 
			model Alarm {
			  input {
			  	Time currentTime
			  	Time alarmTime
			  }
			  
			  scenario triggerAlert {
			  	when receiving alarmTime
			  	then doSomething
			  }
			}
		'''

		val result = parseHelper.parse(source, dataResource.resourceSet)
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
		val source = '''
			package clocks.models
			 
			import clocks.datatypes.Time
			import clocks.models.Speaker
			 
			model Alarm {
			  input {
			  	Time currentTime
			  	Time alarmTime
			  }
			  
			  scenario triggerAlert {
			  	when receiving alarmTime
			  	and talkingWith yoda
			  	then doSomething
			  }
			}
		'''

		val result = parseHelper.parse(source, dataResource.resourceSet)
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
