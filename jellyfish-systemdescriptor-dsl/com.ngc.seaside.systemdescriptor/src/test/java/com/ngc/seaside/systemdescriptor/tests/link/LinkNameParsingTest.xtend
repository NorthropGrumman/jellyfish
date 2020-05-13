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