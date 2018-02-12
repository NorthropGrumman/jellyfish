package com.ngc.seaside.systemdescriptor.tests.resources

import static com.ngc.seaside.systemdescriptor.tests.resources.ParsingTestResource.resource;
import org.eclipse.emf.ecore.resource.Resource
import java.util.Arrays
import org.eclipse.xtext.junit4.util.ResourceHelper
import java.util.ArrayList
import java.util.Collection

class Datas {

	public static final ParsingTestResource EMPTY_DATA = resource(
		'''
			package foo
			
			data EmptyData {
			}
		'''
	)

	public static final ParsingTestResource DATE = resource(
		'''
			package clocks.datatypes
			
			data Date {
				int day
				int month
				int year
			}
		'''
	)

	public static final ParsingTestResource TIME = resource(
		'''
			package clocks.datatypes
			
			data Time {
				int hour {
					"validation": {
						"min": 0,
						"max": 23
					}
				}
				int minute {
					"validation": {
						"min": 0,
						"max": 59
					}
				}
				int second {
					"validation": {
						"min": 0,
						"max": 59
					}
				}
			}
		'''
	)

	public static final ParsingTestResource TIME_ZONE = resource(
		'''
			package clocks.datatypes
			
			enum TimeZone {
				CST MST EST PST
			}
		'''
	)

	public static final ParsingTestResource DATE_TIME = resource(
		'''
			package clocks.datatypes
			
			import clocks.datatypes.Date
			import clocks.datatypes.Time
			
			data DateTime {
				Date date
				many Time time
			}
		''',
		TIME,
		DATE
	)

	public static final ParsingTestResource ZONED_TIME = resource(
		'''
			package clocks.datatypes
			
			import clocks.datatypes.TimeZone
			import clocks.datatypes.DateTime
			
			data ZonedTime {
				DateTime dataTime
				TimeZone timeZone
			}
		''',
		TIME_ZONE,
		DATE_TIME
	)

	public static final ParsingTestResource ALARM_ACKNOWLEDGEMENT = resource(
		'''
			package clocks.datatypes
			
			data AlarmAcknowledgement {
			}
		'''
	)

	public static final ParsingTestResource ALARM_STATUS = resource(
		'''
			package clocks.datatypes
			
			data AlarmStatus {
			}
		'''
	)

	public static final ParsingTestResource DATA_WITH_GENERIC_METADATA = resource(
		'''
			package blah
			
			data Foo { 
				metadata {
				  "name" : "test",
				  "description" : "A test metadata object",
				  "arraydata" : ["metadata", "test"],
				  "boolvalue" : true
				}  
			}	
		'''
	)

	public static final ParsingTestResource DATA_WITH_MANY_FIELDS = resource(
		'''
			package blah
			
			data LotsOfManys { 
				many int x
				many int y
				many int z 
			}	
		'''
	)

	public static final ParsingTestResource FOOD = resource(
		'''
			package food
			
			data Food {
				int calories {
					"maxPerMeal": 500
				}
			}
		'''
	)

	public static final ParsingTestResource HAMBURGER = resource(
		'''
			package food
			
			import food.Food
			
			data Hamburger : Food {
				boolean withFries
			}
		''',
		FOOD
	)

	public static final ParsingTestResource SLIDERS_MEAL = resource(
		'''
			package food
			
			import food.Hamburger
			
			data SlidersMeal : Hamburger {
				many Hamburger smallBurgers
			}
		''',
		HAMBURGER
	)

	def static Resource allOf(ResourceHelper resourceHelper, ParsingTestResource... resources) {
		return ParsingTestResource.preparedForParse(resourceHelper, Arrays.asList(resources))
	}

	def static Resource allOf(ResourceHelper resourceHelper, Object... resources) {
		val all = new ArrayList()
		for (Object o : resources) {
			if (o instanceof ParsingTestResource) {
				all.add(o as ParsingTestResource)
			} else if (o instanceof Collection) {
				all.addAll(asCollectionOfResources(o as Collection<?>))
			} else {
				throw new IllegalArgumentException(
					"resources must be a ParsingTestResource or a collection of ParsingTestResources!")
			}
		}
		return ParsingTestResource.preparedForParse(resourceHelper, all)
	}

	def private static Collection<ParsingTestResource> asCollectionOfResources(Collection<?> collection) {
		if (!collection.isEmpty()) {
			for (Object o : collection) {
				if (!(o instanceof ParsingTestResource)) {
					throw new IllegalArgumentException(
						"collection must contain only instances of ParsingTestResources!")
				}
			}
		}
		return collection as Collection<ParsingTestResource>
	}
}
