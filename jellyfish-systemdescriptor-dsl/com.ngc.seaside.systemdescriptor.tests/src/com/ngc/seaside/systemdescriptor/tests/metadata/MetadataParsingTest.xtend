package com.ngc.seaside.systemdescriptor.tests.metadata

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
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
class MetadataParsingTest {

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
			Models.TIMER,
			Models.CLOCK,
			Datas.TIME
		)
	}

	@Test
	def void testDoesParseModelWithMetadata() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			import clocks.models.part.Timer
			import clocks.models.part.Clock
			
			model AlarmClock {
			  input {
			    Time alarmTimes {
			      metadata {
			        "satisifes": "requirement1"
			      }
			    }
			  }
			
			  output {
			    Time timeOfAlarmDeactivation {
			       metadata {
			         "since": "2.0"
			       }
			     }
			  }
			
			  parts {
			    Timer timer {
			      metadata {
			        "description": "This is provided by 3rd party software"
			      }
			    }
			  }
			
			  requires {
			    Clock clock {
			      metadata {
			        "mustTick": "true"
			      }
			    }
			  }
			}
		'''

		val result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
	}

}
