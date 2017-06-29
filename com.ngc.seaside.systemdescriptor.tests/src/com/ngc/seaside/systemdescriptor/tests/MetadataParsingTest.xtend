package com.ngc.seaside.systemdescriptor.tests

import com.google.inject.Inject
import com.google.inject.Provider
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.util.ResourceHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.eclipse.xtext.resource.XtextResourceSet
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class MetadataParsingTest {

	@Inject
	Provider<XtextResourceSet> resourceSetProvider;

	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ResourceHelper resourceHelper

	@Inject
	ValidationTestHelper validationTester

	ResourceSet resources

	@Before
	def void setup() {
		resources = resourceSetProvider.get()

		var resource = resourceHelper.resource(
			'''
				package clocks.datatypes
							
				data Time {
				}
			''',
			URI.createURI("time.sd"),
			resources
		)
		validationTester.assertNoIssues(resource)

		resource = resourceHelper.resource(
			'''
				package clocks.models
							
				model Timer {
				}
			''',
			URI.createURI("timer.sd"),
			resources
		)
		validationTester.assertNoIssues(resource)

		resource = resourceHelper.resource(
			'''
				package beverages.models
							
				model Gatorade {
				}
			''',
			URI.createURI("gatorade.sd"),
			resources
		)
		validationTester.assertNoIssues(resource)
	}

	@Test
	def void testDoesParseModelWithMetadata() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			import clocks.models.Timer
			import beverages.models.Gatorade
			
			model AlarmClock {
			  input {
			    many Time alarmTimes {
			      "satisifes": "requirement1"
			    }
			  }
			
			  output {
			    Time timeOfAlarmDeactivation {
			       "since": "2.0"
			     }
			  }
			
			  parts {
			    Timer timer {
			      "description": "This is provided by 3rd party software"
			    }
			  }
			
			  requires {
			    Gatorade thristQuencher {
			      "flavor": "purple"
			    }
			  }
			}
		'''

		val result = parseHelper.parse(source, resources)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
	}

}
