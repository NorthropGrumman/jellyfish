/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.systemdescriptor.tests.resources

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.testing.util.ResourceHelper

import static com.ngc.seaside.systemdescriptor.tests.resources.ParsingTestResource.resource

class Models {
    public static final ParsingTestResource EMPTY_MODEL = resource(
        '''
            package foo
            
            model AnEmptyModel {
            }
        '''
    )

    public static final ParsingTestResource CLOCK = resource(
        '''
            package clocks.models.part
            
            import clocks.datatypes.ZonedTime
            import foo.AnEmptyModel
            
            model Clock {
            
                input {
                   ZonedTime inputTime {
                   	}
                }
                
                output {
                    ZonedTime currentTime {
                    }
                }
                
                parts {
                	AnEmptyModel emptyModel
                }
                
                requires {
                	AnEmptyModel requiresEmptyModel
                }
                
                properties {
                	ZonedTime releaseDate
                }
            }
        ''',
        Datas.ZONED_TIME,
        Models.EMPTY_MODEL
    )

    public static final ParsingTestResource LINKED_CLOCK = resource(
        '''
            package clocks.models.part
            
            import clocks.datatypes.ZonedTime
            import clocks.models.part.Clock
            import foo.AnEmptyModel
            
            model LinkedClock {
            
               input {
                    ZonedTime currentTime
                }
                
                parts {
                    Clock clock
                    Clock clockA
                    Clock clockB
                    Clock clockC
                    Clock clockD {
                         properties {
                            int intPartsField
                        }
                            
                }
                
                requires {
                    AnEmptyModel requiresEmptyModel
                    Clock reqClock {
                        properties {
                            int intRequiredField
                            intRequiredField = 10
                        }
                    }
                }
                
                links {
                    link namedLink currentTime -> clock.inputTime
                    link currentTime -> clockA.inputTime {
                    	properties {
                    		int anotherIntField
                    	}
                    }
                    link propNamedLink 	currentTime -> clockC.inputTime {
                        properties {
                            int intLinkedClockField
                        }
                    }
                    link valueNamedLink  currentTime -> clockD.inputTime {
                        properties {
                            int intValueClockField
                            intValueClockField = 100
                        }
                    }
                }
            }
        ''',
        Datas.ZONED_TIME,
        Models.EMPTY_MODEL,
        Models.CLOCK
    )

    public static final ParsingTestResource ALARM = resource(
        '''
            package clocks.models.part
            
            import clocks.datatypes.ZonedTime
            import clocks.datatypes.AlarmAcknowledgement
            import clocks.datatypes.AlarmStatus
            import foo.AnEmptyModel
            
            model Alarm {
            
                input {
                    ZonedTime currentTime
                    ZonedTime alarmTime
                    AlarmAcknowledgement alarmAcknowledgement
                }
            
                output {
                AlarmStatus alarmStatus
                ZonedTime outputTime
                }
            
                requires {
                AnEmptyModel emptyModel
                }
            
                scenario triggerAlert {
                given alarmTime hasBeenReceived
                when receiving alarmTime
                then doSomething withThis
                }
            }
        ''',
        Datas.ZONED_TIME,
        Datas.ALARM_ACKNOWLEDGEMENT,
        Datas.ALARM_STATUS,
        Models.EMPTY_MODEL
    )

    public static final ParsingTestResource TIMER = resource(
        '''
            package clocks.models.part
            
            model Timer {
            }
        '''
    )

    public static final ParsingTestResource SPEAKER = resource(
        '''
            package clocks.models.part
            
            import clocks.datatypes.ZonedTime
            
            model Speaker {
            
                input {
                    ZonedTime alarmTime
                }
            
                scenario buzz {
                metadata {
                    "name": "someName",
                    "description": "someDescription"
                }
                when receiving alarmTime
                then doSomething withThis
                }
            }
        ''',
        Datas.ZONED_TIME
    )
    
    public static final ParsingTestResource WIRED_SPEAKER = resource(
        '''
            package clocks.models.part
            
            import clocks.datatypes.ZonedTime
            
            model WiredSpeaker {
            
                input {
                    ZonedTime alarmTime
                }
            
                scenario buzz {
                    when receiving alarmTime
                    then doSomething withThis
                }
                
                properties {
                	int wireGauge
                }
            }
        ''',
        Datas.ZONED_TIME
    )
    

    public static final ParsingTestResource GENERIC_MODEL_WITH_MULTIPLE_GIVEN_STEPS = resource(
        '''
            package scenarios.test
            
            import clocks.datatypes.ZonedTime
            import clocks.datatypes.AlarmAcknowledgement
            import clocks.datatypes.AlarmStatus
            
            model ScenarioTestWithManyGivens {
            
                input {
                    ZonedTime currentTime
                    ZonedTime alarmTime
                    AlarmAcknowledgement alarmAcknowledgement
                }
            
                output {
                AlarmStatus alarmStatus
                }
            
                scenario triggerAlert {
                given alarmTime hasBeenReceived
                and alarmTime is not too early
                when receiving alarmTime
                then doSomething
                }
            }
        ''',
        Datas.ZONED_TIME,
        Datas.ALARM_ACKNOWLEDGEMENT,
        Datas.ALARM_STATUS
    )

    public static final ParsingTestResource GENERIC_MODEL_WITH_MULTIPLE_WHEN_STEPS = resource(
        '''
            package scenarios.test
            
            import clocks.datatypes.ZonedTime
            import clocks.datatypes.AlarmAcknowledgement
            import clocks.datatypes.AlarmStatus
            
            model ScenarioTestWithManyGivens {
            
                input {
                    ZonedTime currentTime
                    ZonedTime alarmTime
                    AlarmAcknowledgement alarmAcknowledgement
                }
            
                output {
                AlarmStatus alarmStatus
                }
            
                scenario triggerAlert {
                given alarmTime hasBeenReceived
                when receiving alarmTime
                and something is cool
                then doSomething
                }
            }
        ''',
        Datas.ZONED_TIME,
        Datas.ALARM_ACKNOWLEDGEMENT,
        Datas.ALARM_STATUS
    )

    public static final ParsingTestResource GENERIC_MODEL_WITH_MULTIPLE_THEN_STEPS = resource(
        '''
                package scenarios.test
            
                import clocks.datatypes.ZonedTime
                import clocks.datatypes.AlarmAcknowledgement
                import clocks.datatypes.AlarmStatus
            
                model ScenarioTestWithManyGivens {
            
                input {
                    ZonedTime currentTime
                    ZonedTime alarmTime
                    AlarmAcknowledgement alarmAcknowledgement
                }
            
                    output {
                AlarmStatus alarmStatus
                    }
            
                    scenario triggerAlert {
                given alarmTime hasBeenReceived
                when receiving alarmTime
                then doSomething
                and something is cool
                    }
            }
        ''',
        Datas.ZONED_TIME,
        Datas.ALARM_ACKNOWLEDGEMENT,
        Datas.ALARM_STATUS
    )

    public static final ParsingTestResource INVALID_REFINED_MODEL = resource(
        '''
            package refinement.test
            
            import refinement.test.AnotherInvalidRefinedModel
            
            model InvalidRefinedModel refines AnotherInvalidRefinedModel {
            }
        '''
    )

    def static Resource allOf(ResourceHelper resourceHelper, ParsingTestResource... resources) {
        return Datas.allOf(resourceHelper, resources)
    }

    def static Resource allOf(ResourceHelper resourceHelper, Object... resources) {
        return Datas.allOf(resourceHelper, resources)
    }
}
