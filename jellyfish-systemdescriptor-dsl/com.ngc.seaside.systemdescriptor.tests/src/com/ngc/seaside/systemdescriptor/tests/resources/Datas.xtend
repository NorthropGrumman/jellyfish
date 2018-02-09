package com.ngc.seaside.systemdescriptor.tests.resources

import static com.ngc.seaside.systemdescriptor.tests.resources.ParsingTestResource.resource;
import org.eclipse.emf.ecore.resource.Resource
import java.util.Arrays
import org.eclipse.xtext.junit4.util.ResourceHelper

class Datas {

	public static final ParsingTestResource TIME = resource('''
		package clocks.datatypes
		
		data Time {
		}
	''')

	public static final ParsingTestResource ZONED_TIME = resource('''
		package clocks.datatypes
		
		data ZonedTime {
		}
	''')

	public static final ParsingTestResource ALARM_ACKNOWLEDGEMENT = resource('''
		package clocks.datatypes
		
		data AlarmAcknowledgement {
		}
	''')

	public static final ParsingTestResource ALARM_STATUS = resource('''
		package clocks.datatypes
		
		data AlarmStatus {
		}
	''')
	
	public static final ParsingTestResource DATA_WITH_GENERIC_METADATA = resource('''
		package blah
		
		data Foo { 
			metadata {
			  "name" : "test",
			  "description" : "A test metadata object",
			  "arraydata" : ["metadata", "test"],
			  "boolvalue" : true
			}  
		}	
	''')
	
	def static Resource allOf(ResourceHelper resourceHelper, ParsingTestResource... resources) {
		return ParsingTestResource.preparedForParse(resourceHelper, Arrays.asList(resources))
	}
}
