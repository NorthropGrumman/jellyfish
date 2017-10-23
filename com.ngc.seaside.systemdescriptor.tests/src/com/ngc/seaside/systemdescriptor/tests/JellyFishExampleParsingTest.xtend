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
			      "min": 0,
			      "max": 23
			    }
			  }
			  
			  int minute {
			    "validation": {
			      "min": 0,
			      "max": 60
			    }
			  }
			  
			  int second {
			    "validation": {
			      "min": 0,
			      "max": 60
			    }
			  }
			}
		'''
		val dataResource = resourceHelper.resource(
			dataSource,
			URI.createURI("datatypes.sd")
		)
		validationTester.assertNoIssues(dataResource)

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
			  
			  scenario tick {
			    when periodOf 1 sec elapses
			    then publish currentTime
			  }
			}
		'''
		val timerResource = resourceHelper.resource(
			timerSource,
			dataResource.resourceSet
		)
		validationTester.assertNoIssues(timerResource)

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
			 
			 //Left blank intentionally. This might be as far as we can go until we see a graphic design.
			 //This might need to display the time in AM/PM vs a 24-hour representation.
			 scenario displayCurrentTime {
			 }
			}
		'''
		val clockDisplayResource = resourceHelper.resource(
			clockDisplaySource,
			dataResource.resourceSet
		)
		validationTester.assertNoIssues(clockDisplayResource)

		val speakerSource = '''
			package clocks.models
			
			model Speaker {
			  metadata {
			    "description": "Makes annoying buzzing sounds.",
			    "stereotypes": ["device"]
			  }
			  
			  scenario buzz { }
			}
		'''
		val speakerResource = resourceHelper.resource(
			speakerSource,
			dataResource.resourceSet
		)
		validationTester.assertNoIssues(speakerResource)

		val alarmSource = '''
			package clocks.models
			
			import clocks.datatypes.Time
			import clocks.models.Speaker
			
			model Alarm {
			  metadata {
				"description": "Generates alerts based on times set for alarms.",
				"stereotypes": ["service"]
				 }
				  
				 requires {
				 	Speaker speaker
				 }
				 
				 input {
				   Time currentTime
				   many Time alarmTimes
				 }
				 
				 scenario triggerAlerts {
				     given alarmTimes hasBeenReceived
				     when receiving currentTime
				     and anyOf alarmsTimes equals currentTime
				     then ask speaker ^to buzz
				 }
			}
		'''
		val alarmResource = resourceHelper.resource(
			alarmSource,
			dataResource.resourceSet
		)
		validationTester.assertNoIssues(alarmResource)

		val alarmClockSource = 
		'''
		package clocks
		
		import clocks.datatypes.Time
		import clocks.models.Timer
		import clocks.models.ClockDisplay
		import clocks.models.Alarm
		import clocks.models.Speaker
		
		model AlarmClock {
		  metadata {
		    "description": "The top level alarm clock system.  It requires the alarmTimes from some other component.",
		    "stereotypes": ["virtual", "system"]
		  }
		  
		  input {
		    many Time alarmTimes
		  }
		  
		  parts {
		    Timer timer
		    ClockDisplay display
		    Alarm alarm
		    Speaker speaker
		  }
		  
		  links {
		      link timer.currentTime -> display.currentTime
		      link timer.currentTime -> alarm.currentTime
		      link speaker -> alarm.speaker
		      link alarmTimes -> alarm.alarmTimes
		  }
		}
		'''

		val result = parseHelper.parse(alarmClockSource, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
	}
}
