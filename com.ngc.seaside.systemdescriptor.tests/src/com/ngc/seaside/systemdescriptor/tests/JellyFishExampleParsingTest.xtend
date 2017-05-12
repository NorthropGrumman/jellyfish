package com.ngc.seaside.systemdescriptor.tests

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import org.eclipse.emf.common.util.URI
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.util.ResourceHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class JellyFishExampleParsingTest {

	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ResourceHelper resourceHelper

	@Inject
	ValidationTestHelper validationTester

	@Test
	def void testDoesParseJellyFishWhitepaperExample() {
		val dataSource = '''
			package clocks.datatypes
			
			data Time {
			  metadata {
			    "name": "Time",
			    "description": "Represents a local time (does not account for timezones)."
			  }
			  
			  int hour {
			    "validation": {
			      "min": "0",
			      "max": "23"
			    }
			  }
			  
			  int minute {
			    "validation": {
			      "min": "0",
			      "max": "60"
			    }
			  }
			  
			  int second {
			    "validation": {
			      "min": "0",
			      "max": "60"
			    }
			  }
			}
		'''

		val timerSource = '''
			package clocks.models
			
			import clocks.datatypes.Time
			
			model Timer {
			  metadata {
			    "name": "Timer",
			    "description": "Outputs the current time.",
			    "stereotypes": ["service"]
			  }
			
			  output {
			    Time currentTime
			  }
			}
		'''

		val clockDisplaySource = '''
			package clocks.models
			
			import clocks.datatypes.Time
			
			model ClockDisplay {
			 metadata {
			    "description": "Displays the current time.",
			    "stereotypes": ["service"]
			 }
			 
			 input {
			     Time currentTime
			 }
			}
		'''

		val dataResource = resourceHelper.resource(
			dataSource,
			URI.createURI("datatypes.sd")
		)
		validationTester.assertNoIssues(dataResource)

		val clockDisplayResource = resourceHelper.resource(
			clockDisplaySource,
			dataResource.resourceSet
		)
		validationTester.assertNoIssues(clockDisplayResource)

		val speakerSource = '''
			package clocks.models
			
			import clocks.datatypes.Time
			
			model Speaker {
			  metadata {
			    "description": "Makes annoying buzzing sounds.",
			    "stereotypes": ["device"]
			  }
			}
		'''
		val speakerResource = resourceHelper.resource(
			speakerSource,
			dataResource.resourceSet
		)
		validationTester.assertNoIssues(speakerResource)

		val result = parseHelper.parse(timerSource, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
	}
}
