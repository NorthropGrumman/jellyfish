/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.tests.link

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import com.ngc.seaside.systemdescriptor.tests.resources.Datas
import com.ngc.seaside.systemdescriptor.tests.resources.Models
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.util.ResourceHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class LinkNameParsingTest {
	
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
			Models.ALARM,
			Models.CLOCK,
			Datas.ALARM_ACKNOWLEDGEMENT
		)
		validationTester.assertNoIssues(requiredResources)
	}
	
    @Test
    def void testDoesParseModelWithUnnamedLink() {
		var source = '''
			package clocks.models
			
			import clocks.models.part.Alarm
			import clocks.models.part.Clock
			
			model AlarmClock {
			
			    parts {
			    	Alarm alarm
			    	Clock clock
			    }
			
			    links {
			    	link clock.currentTime -> alarm.currentTime
			    }
			}
		'''

        var result = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(result)
        validationTester.assertNoIssues(result)

        var model = result.element as Model
        var unnamedLink = model.links.declarations.get(0)
        assertNull(
            "unnamedLink not correct!",
            unnamedLink.name
        )
     }
	
	@Test
    def void testDoesParseModelWithNamedLink() {
		var source = '''
			package clocks.models
			
			import clocks.models.part.Alarm
			import clocks.models.part.Clock
			
			model AlarmClock {
			
			    parts {
			    	Alarm alarm
			    	Clock clock
			    }
			
			    links {
			    	link myLink clock.currentTime -> alarm.currentTime
			    }
			}
		'''

        var result = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(result)
        validationTester.assertNoIssues(result)

        var model = result.element as Model;
        var link = model.links.declarations.get(0)
        assertEquals(
            "link name not correct!",
            "myLink",
            link.name
        )
    }

	@Test
    def void testDoesNotParseModelWithNamedLinksWhereLinkNamesAreDuplicated() {
		var source = '''
			package clocks.models
			
			import clocks.models.part.Alarm
			import clocks.models.part.Clock
			import clocks.datatypes.AlarmAcknowledgement
			
			model AlarmClock {
			
				input {
					AlarmAcknowledgement alarmAcknowledgement
				}
			
			    parts {
			    	Alarm alarm
			    	Clock clock
			    }
			
			    links {
			    	link myLink clock.currentTime -> alarm.currentTime
			    	link myLink alarmAcknowledgement -> alarm.alarmAcknowledgement
			    }
			}
		'''

        var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(invalidResult)
        validationTester.assertError(
            invalidResult,
            SystemDescriptorPackage.Literals.LINK_DECLARATION,
            null
        )
    }

    @Test
    def void testDoesNotParseModelWithNamedLinksWhereLinkNameDuplicatesInputName() {
		var source = '''
			package clocks.models
			
			import clocks.models.part.Alarm
			import clocks.models.part.Clock
			import clocks.datatypes.AlarmAcknowledgement
			
			model AlarmClock {
				input {
					AlarmAcknowledgement alarmAcknowledgement
				}
			
			    parts {
			    	Alarm alarm
			    	Clock clock
			    }
			
			    links {
			    	link alarmAcknowledgement clock.currentTime -> alarm.currentTime
			    }
			}
		'''

        var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(invalidResult)
        validationTester.assertError(
            invalidResult,
            SystemDescriptorPackage.Literals.LINK_DECLARATION,
            null
        )
    }

    @Test
    def void testDoesNotParseModelWithNamedLinksWhereLinkNameDuplicatesOutputName() {
		var source = '''
			package clocks.models
			
			import clocks.models.part.Alarm
			import clocks.models.part.Clock
			import clocks.datatypes.AlarmAcknowledgement
			
			model AlarmClock {
				output {
					AlarmAcknowledgement alarmAcknowledgement
				}
			
			    parts {
			    	Alarm alarm
			    	Clock clock
			    }
			
			    links {
			    	link alarmAcknowledgement clock.currentTime -> alarm.currentTime
			    }
			}
		'''

        var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(invalidResult)
        validationTester.assertError(
            invalidResult,
            SystemDescriptorPackage.Literals.LINK_DECLARATION,
            null
        )
    }

    @Test
    def void testDoesNotParseModelWithNamedLinksWhereLinkNameDuplicatesPartName() {
		var source = '''
			package clocks.models
			
			import clocks.models.part.Alarm
			import clocks.models.part.Clock
			
			model AlarmClock {
			    parts {
			    	Alarm alarm
			    	Clock clock
			    }
			
			    links {
			    	link alarm clock.currentTime -> alarm.currentTime
			    }
			}
		'''

        var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(invalidResult)
        validationTester.assertError(
            invalidResult,
            SystemDescriptorPackage.Literals.LINK_DECLARATION,
            null
        )
    }

    @Test
    def void testDoesNotParseModelWithNamedLinksWhereLinkNameDuplicatesRequirementName() {
		var source = '''
			package clocks.models
			
			import clocks.models.part.Alarm
			import clocks.models.part.Clock
			
			model AlarmClock {
			    parts {
			    	Alarm alarm
			    	Clock clock
			    }
			    
			    requires {
			    	Clock externalClock
			    }
			
			    links {
			    	link externalClock clock.currentTime -> alarm.currentTime
			    }
			}
		'''

        var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(invalidResult)
        validationTester.assertError(
            invalidResult,
            SystemDescriptorPackage.Literals.LINK_DECLARATION,
            null
        )
    }
    
    @Test
    def void testDoesNotParseModelWithWithEscapedLinkName() {
		var source = '''
			package clocks.models
			
			import clocks.models.part.Alarm
			import clocks.models.part.Clock
			import clocks.datatypes.AlarmAcknowledgement
			
			model AlarmClock {
			
				input {
					AlarmAcknowledgement alarmAcknowledgement
				}
			
			    parts {
			    	Alarm alarm
			    	Clock clock
			    }
			
			    links {
			    	link ^link clock.currentTime -> alarm.currentTime
			    }
			}
		'''

        var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
        validationTester.assertError(
            invalidResult,
            SystemDescriptorPackage.Literals.LINK_DECLARATION,
            null
        )
    }
    
}