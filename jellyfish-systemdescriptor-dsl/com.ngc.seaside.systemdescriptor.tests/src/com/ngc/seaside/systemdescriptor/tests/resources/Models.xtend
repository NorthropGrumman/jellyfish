package com.ngc.seaside.systemdescriptor.tests.resources

import static com.ngc.seaside.systemdescriptor.tests.resources.ParsingTestResource.resource;
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.junit4.util.ResourceHelper
import java.util.Arrays

class Models {

	public static final ParsingTestResource CLOCK = resource('''
		package clocks.models.part
		
		import clocks.datatypes.ZonedTime
		
		model Clock {
		
			output {
				ZonedTime currentTime {
				}
			}
		}
	''', Datas.ZONED_TIME)

	public static final ParsingTestResource ALARM = resource('''
		package clocks.models.part
		
		import clocks.datatypes.ZonedTime
		import clocks.datatypes.AlarmAcknowledgement
		import clocks.datatypes.AlarmStatus
		
		model Alarm {
		
			input {
				ZonedTime currentTime
				ZonedTime alarmTime
				AlarmAcknowledgement alarmAcknowledgement
			}
		
			output {
				AlarmStatus alarmStatus
			}
		}
	''', Datas.ZONED_TIME, Datas.ALARM_ACKNOWLEDGEMENT, Datas.ALARM_STATUS)
	
	def static Resource allOf(ResourceHelper resourceHelper, ParsingTestResource... resources) {
		return ParsingTestResource.preparedForParse(resourceHelper, Arrays.asList(resources))
	}
}
