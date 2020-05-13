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
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
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
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import com.ngc.seaside.systemdescriptor.tests.resources.Datas

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class BasicLinkParsingTest {

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
			Models.SPEAKER,
			Datas.TIME
		)
		validationTester.assertNoIssues(requiredResources)
	}
	
	@Test
	def void testDoesParseModelWithLink_From_Part_To_Part() {

		var source = '''
            package clocks.models

            import clocks.datatypes.Time
            import clocks.models.part.Alarm
            import clocks.models.part.Speaker

            model AlarmClock {
                output {
                    Time currentTime
                    Time otherTime
                }

                parts {
                    Alarm a
                    Speaker s
                }

                links {
                    link a -> s
                }
            }
        '''
        
        var result = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(result)
        validationTester.assertNoIssues(result)
	}
	
	@Test
	def void testDoesNotParseModelWithLink_From_Field_To_Same_Field() {

		var source = '''
            package clocks.models

            import clocks.datatypes.Time
            import clocks.models.part.Alarm

            model AlarmClock {
                input {
                    Time currentTime
                }

                links {
                    link currentTime -> currentTime
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
	def void testDoesNotParseModelWithLink_From_Requirement_To_Requirement() {
		var source = '''
            package clocks.models

            import clocks.datatypes.Time
            import clocks.models.part.Alarm

            model AlarmClock {
                output {
                    Time currentTime
                    Time otherTime
                }

                parts {
                    Alarm a1
                    Alarm a2
                }
                
                requires {
                	Speaker speaker
                	Speaker speaker1
                }
                

                links {
                    link  speaker -> speaker1
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
	def void testDoesNotParseModelWithLink_From_InputField_To_InputField() {

        var source = '''
            package clocks.models

            import clocks.datatypes.Time
            import clocks.models.part.Alarm

            model AlarmClock {
                input {
                    Time currentTime
                    Time otherTime
                }

                links {
                    link currentTime -> otherTime
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
	def void testDoesNotParseModelWithLink_From_OutputField_To_OutputField() {
		
		var source = '''
            package clocks.models

            import clocks.datatypes.ZonedTime

            model AlarmClock {
                output {
                    ZonedTime currentTime
                    ZonedTime otherTime
                }

                links {
                    link currentTime -> otherTime
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
	def void testDoesNotParseModelWithLink_From_InputField_To_OutputField() {
		var source = '''
            package clocks.models

            import clocks.datatypes.ZonedTime

            model AlarmClock {
                input {
                    ZonedTime currentTime
                }
                output {
                	ZonedTime otherTime
                }

                links {
                    link currentTime -> otherTime
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
	def void testDoesNotParseModelWithLink_From_OutputField_To_InputField() {
	var source = '''
            package clocks.models

            import clocks.datatypes.ZonedTime

            model AlarmClock {
                input {
                    ZonedTime currentTime
                }
                output {
                	ZonedTime otherTime
                }

                links {
                    link currentTime -> otherTime
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
}
