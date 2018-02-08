package com.ngc.seaside.systemdescriptor.tests

import org.junit.Test
import com.google.inject.Inject

import org.junit.runner.RunWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.InjectWith
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import org.eclipse.xtext.junit4.formatter.FormatterTester

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(SystemDescriptorInjectorProvider))

class FormattingTest {
	
	@Inject extension FormatterTester
	
	@Test def void testAlreadyFormattedExample1() {
		assertFormatted[
			toBeFormatted = formattedExample1
		]
	}
	
	@Test def void testJumbledExample1() {
		assertFormatted[
			expectation = formattedExample1
			toBeFormatted = '''
				package com.ngc.seaside.threateval import com.ngc.seaside.threateval.datatype.TrackPriority
				import com.ngc.seaside.threateval.datatype.TrackPriorityRequest
				import com.ngc.seaside.threateval.datatype.PrioritizedSystemTrackIdentifiers import com.ngc.seaside.common.datatype.DroppedSystemTrack
				
				model TrackPriorityService { metadata {
						"description": "Aggregates track priorities from 1..n sources and upon request will provide a set of prioritized track IDs.",
						"stereotypes": ["service", "aggregator"], "satisfies": ["TE001.1", "TE001.2", "TE001.3", "TE001.4", "TE001.5"] }
				
				input {
				TrackPriority trackPriority
				DroppedSystemTrack droppedSystemTrack
				}
				
				output { PrioritizedSystemTrackIdentifiers prioritizedSystemTracks }
				
				scenario calculateConsolidatedTrackPriority {
				metadata {
				"see": {"url": "http://10.207.42.43/confluence/display/SEAS/Track+Priority+Consolidation" }
				} when receiving trackPriority then willPublish prioritizedSystemTracks and willBeCompleted within 500 milliseconds }
				
				scenario calculateConsolidatedTrackPriorityWhenTrackDropped {
				when receiving droppedSystemTrack
				then willPublish prioritizedSystemTracks
				and willBeCompleted within 500 milliseconds
				} }
			'''
		]
	}
	
	@Test def void testOneLineExample1() {
		assertFormatted[
			expectation = formattedExample1
			toBeFormatted = '''
				package com.ngc.seaside.threateval import com.ngc.seaside.threateval.datatype.TrackPriority import com.ngc.seaside.threateval.datatype.TrackPriorityRequest import com.ngc.seaside.threateval.datatype.PrioritizedSystemTrackIdentifiers import com.ngc.seaside.common.datatype.DroppedSystemTrack model TrackPriorityService { metadata {"description": "Aggregates track priorities from 1..n sources and upon request will provide a set of prioritized track IDs.", "stereotypes": ["service", "aggregator"], "satisfies": ["TE001.1", "TE001.2", "TE001.3", "TE001.4", "TE001.5"] } input { TrackPriority trackPriority DroppedSystemTrack droppedSystemTrack } output { PrioritizedSystemTrackIdentifiers prioritizedSystemTracks } scenario calculateConsolidatedTrackPriority { metadata { "see": {"url": "http://10.207.42.43/confluence/display/SEAS/Track+Priority+Consolidation" } } when receiving trackPriority then willPublish prioritizedSystemTracks and willBeCompleted within 500 milliseconds } scenario calculateConsolidatedTrackPriorityWhenTrackDropped { when receiving droppedSystemTrack then willPublish prioritizedSystemTracks and willBeCompleted within 500 milliseconds } }
			'''
		]
	}
	
	@Test def void testAlreadyFormattedExample2() {
		assertFormatted[
			toBeFormatted = formattedExample2
		]
	}
	
	@Test def void testJumbledExample2() {
		assertFormatted[
			expectation = formattedExample2
			toBeFormatted = '''
				package com.ngc.seaside.threateval
				
				import com.ngc.seaside.threateval.DefendedAreaTrackPriorityService
				import com.ngc.seaside.threateval.ClassificationTrackPriorityService
				import com.ngc.seaside.threateval.EngagementTrackPriorityService 				import com.ngc.seaside.threateval.TrackPriorityService
				import com.ngc.seaside.common.datatype.SystemTrack
				import com.ngc.seaside.classifier.datatype.Classification import com.ngc.seaside.engagementplanning.datatype.TrackEngagementStatus
				import com.ngc.seaside.common.datatype.SystemTrackIdentifier
				import com.ngc.seaside.threateval.datatype.PrioritizedSystemTrackIdentifiers
				
				model ThreatEvaluation {
					metadata { "description": "Prioritizes system tracks into a list of System Track Identifiers",
						"stereotypes": ["virtual", "system"],
						"satisfies": ["TE001"] }
				
					input {
						SystemTrack systemTrack { "satisfies": ["TE001"] }
						Classification systemTrackClassification { "satisfies": ["TE001"] }
						TrackEngagementStatus trackEngagementStatus { "satisfies": ["TE001"] } }
				
					output { PrioritizedSystemTrackIdentifiers prioritizedSystemTracks { "satisfies": ["TE001"] } }
				
					parts {
						DefendedAreaTrackPriorityService defendedAreaTrackPriorityService
				ClassificationTrackPriorityService classificationTrackPriorityService
				EngagementTrackPriorityService engagementTrackPriorityService
				TrackPriorityService trackPriorityService }
				
					scenario calculateTrackPriority { when receiving systemTrack and receiving systemTrackClassification and receiving trackEngagementStatus then willPublish prioritizedSystemTracks
						and willBeCompleted within 1 seconds }
				
					links { link systemTrackToDATPS systemTrack -> defendedAreaTrackPriorityService.systemTrack
						link
						systemTrackClassificationToCTPS
						systemTrackClassification -> classificationTrackPriorityService.systemTrackClassification link trackEngagementStatus -> engagementTrackPriorityService.trackEngagementStatus
						link prioritizedSystemTracks
						-> trackPriorityService.prioritizedSystemTracks link defendedAreaTrackPriorityService.trackPriority -> trackPriorityService.trackPriority
						link classificationTrackPriorityService.trackPriority -> trackPriorityService.trackPriority
						link engagementTrackPriorityService.trackPriority -> trackPriorityService.trackPriority }
				
				}
			'''
		]
	}
	
	@Test def void testOneLineExample2() {
		assertFormatted[
			expectation = formattedExample2
			toBeFormatted = '''
				package com.ngc.seaside.threateval import com.ngc.seaside.threateval.DefendedAreaTrackPriorityService import com.ngc.seaside.threateval.ClassificationTrackPriorityService import com.ngc.seaside.threateval.EngagementTrackPriorityService import com.ngc.seaside.threateval.TrackPriorityService import com.ngc.seaside.common.datatype.SystemTrack import com.ngc.seaside.classifier.datatype.Classification import com.ngc.seaside.engagementplanning.datatype.TrackEngagementStatus import com.ngc.seaside.common.datatype.SystemTrackIdentifier import com.ngc.seaside.threateval.datatype.PrioritizedSystemTrackIdentifiers model ThreatEvaluation { metadata { "description": "Prioritizes system tracks into a list of System Track Identifiers", "stereotypes": ["virtual", "system"], "satisfies": ["TE001"] } input { SystemTrack systemTrack { "satisfies": ["TE001"] } Classification systemTrackClassification { "satisfies": ["TE001"] } TrackEngagementStatus trackEngagementStatus { "satisfies": ["TE001"] } } output { PrioritizedSystemTrackIdentifiers prioritizedSystemTracks { "satisfies": ["TE001"] } } parts { DefendedAreaTrackPriorityService defendedAreaTrackPriorityService ClassificationTrackPriorityService classificationTrackPriorityService EngagementTrackPriorityService engagementTrackPriorityService TrackPriorityService trackPriorityService } scenario calculateTrackPriority { when receiving systemTrack and receiving systemTrackClassification and receiving trackEngagementStatus then willPublish prioritizedSystemTracks and willBeCompleted within 1 seconds } links { link systemTrackToDATPS systemTrack -> defendedAreaTrackPriorityService.systemTrack link systemTrackClassificationToCTPS systemTrackClassification -> classificationTrackPriorityService.systemTrackClassification link trackEngagementStatus -> engagementTrackPriorityService.trackEngagementStatus link prioritizedSystemTracks -> trackPriorityService.prioritizedSystemTracks link defendedAreaTrackPriorityService.trackPriority -> trackPriorityService.trackPriority link classificationTrackPriorityService.trackPriority -> trackPriorityService.trackPriority link engagementTrackPriorityService.trackPriority -> trackPriorityService.trackPriority }  }
			'''
		]
	}
	
	@Test def void testAlreadyFormattedExample3() {
		assertFormatted[
			toBeFormatted = formattedExample3
		]
	}
	
	@Test def void testJumbledExample3() {
		assertFormatted[
			expectation = formattedExample3
			toBeFormatted = '''
				package com.ngc.seaside.common.datatype
				data GPSTime {
				int secondsFromGPS
				int microseconds { "validation": { "min": 0, "max": 999999 }
					}
									Vector3 ecefVelocity
				}
			'''
		]
	}
	
	@Test def void testOneLineExample3() {
		assertFormatted[
			expectation = formattedExample3
			toBeFormatted = '''
				package com.ngc.seaside.common.datatype data GPSTime { int secondsFromGPS int microseconds { "validation": { "min": 0, "max": 999999 } } Vector3 ecefVelocity }
			'''
		]
	}

	@Test def void testJumbledExample4() {
		assertFormatted[
			expectation = formattedExample4
			toBeFormatted = '''
			package com.ngc.test
				enum Enumeration{
							FIELD1 FIELD2
			FIELD3
			{
				"name":null
			} FIELD4
				}
			'''
		]
	}

	@Test def void testOneLineExample4() {
		assertFormatted[
			expectation = formattedExample4
			toBeFormatted = '''
			package com.ngc.test
			
			enum Enumeration {
				FIELD1
				FIELD2
				FIELD3 {
					"name": null
				}
				FIELD4
			}
			'''
		]
	}
	
	@Test def void testAlreadyFormattedExample4() {
		assertFormatted[
			expectation = formattedExample4
			toBeFormatted = '''
			package com.ngc.test enum Enumeration{FIELD1 FIELD2 FIELD3{"name":null} FIELD4}
			'''
		]
	}

	var formattedExample1 = '''
		package com.ngc.seaside.threateval
		
		import com.ngc.seaside.threateval.datatype.TrackPriority
		import com.ngc.seaside.threateval.datatype.TrackPriorityRequest
		import com.ngc.seaside.threateval.datatype.PrioritizedSystemTrackIdentifiers
		import com.ngc.seaside.common.datatype.DroppedSystemTrack
		
		model TrackPriorityService {
			metadata {
				"description": "Aggregates track priorities from 1..n sources and upon request will provide a set of prioritized track IDs.",
				"stereotypes": ["service", "aggregator"],
				"satisfies": ["TE001.1", "TE001.2", "TE001.3", "TE001.4", "TE001.5"]
			}
		
			input {
				TrackPriority trackPriority
				DroppedSystemTrack droppedSystemTrack
			}
		
			output {
				PrioritizedSystemTrackIdentifiers prioritizedSystemTracks
			}
		
			scenario calculateConsolidatedTrackPriority {
				metadata {
					"see": {
						"url": "http://10.207.42.43/confluence/display/SEAS/Track+Priority+Consolidation"
					}
				}
		
				when receiving trackPriority
				then willPublish prioritizedSystemTracks
				and willBeCompleted within 500 milliseconds
			}
		
			scenario calculateConsolidatedTrackPriorityWhenTrackDropped {
				when receiving droppedSystemTrack
				then willPublish prioritizedSystemTracks
				and willBeCompleted within 500 milliseconds
			}
		
		}
	'''
	
	var formattedExample2 = '''
		package com.ngc.seaside.threateval
		
		import com.ngc.seaside.threateval.DefendedAreaTrackPriorityService
		import com.ngc.seaside.threateval.ClassificationTrackPriorityService
		import com.ngc.seaside.threateval.EngagementTrackPriorityService
		import com.ngc.seaside.threateval.TrackPriorityService
		import com.ngc.seaside.common.datatype.SystemTrack
		import com.ngc.seaside.classifier.datatype.Classification
		import com.ngc.seaside.engagementplanning.datatype.TrackEngagementStatus
		import com.ngc.seaside.common.datatype.SystemTrackIdentifier
		import com.ngc.seaside.threateval.datatype.PrioritizedSystemTrackIdentifiers
		
		model ThreatEvaluation {
			metadata {
				"description": "Prioritizes system tracks into a list of System Track Identifiers",
				"stereotypes": ["virtual", "system"],
				"satisfies": ["TE001"]
			}
		
			input {
				SystemTrack systemTrack {
					"satisfies": ["TE001"]
				}
		
				Classification systemTrackClassification {
					"satisfies": ["TE001"]
				}
		
				TrackEngagementStatus trackEngagementStatus {
					"satisfies": ["TE001"]
				}
			}
		
			output {
				PrioritizedSystemTrackIdentifiers prioritizedSystemTracks {
					"satisfies": ["TE001"]
				}
			}
		
			parts {
				DefendedAreaTrackPriorityService defendedAreaTrackPriorityService
				ClassificationTrackPriorityService classificationTrackPriorityService
				EngagementTrackPriorityService engagementTrackPriorityService
				TrackPriorityService trackPriorityService
			}
		
			scenario calculateTrackPriority {
				when receiving systemTrack
				and receiving systemTrackClassification
				and receiving trackEngagementStatus
				then willPublish prioritizedSystemTracks
				and willBeCompleted within 1 seconds
			}
		
			links {
				link systemTrackToDATPS systemTrack -> defendedAreaTrackPriorityService.systemTrack
				link systemTrackClassificationToCTPS systemTrackClassification -> classificationTrackPriorityService.systemTrackClassification
				link trackEngagementStatus -> engagementTrackPriorityService.trackEngagementStatus
				link prioritizedSystemTracks -> trackPriorityService.prioritizedSystemTracks
				link defendedAreaTrackPriorityService.trackPriority -> trackPriorityService.trackPriority
				link classificationTrackPriorityService.trackPriority -> trackPriorityService.trackPriority
				link engagementTrackPriorityService.trackPriority -> trackPriorityService.trackPriority
			}
		
		}
	'''
	
	var formattedExample3 = '''
		package com.ngc.seaside.common.datatype
		
		data GPSTime {
			int secondsFromGPS
			int microseconds {
				"validation": {
					"min": 0,
					"max": 999999
				}
			}
			Vector3 ecefVelocity
		}
	'''

	var formattedExample4 = '''
		package com.ngc.test

		enum Enumeration {
			FIELD1
			FIELD2
			FIELD3 {
				"name": null
			}
			FIELD4
		}
	'''

}