package com.ngc.seaside.systemdescriptor.tests.link

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldReference
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableExpression
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.BaseLinkDeclaration
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.util.ResourceHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*
import com.ngc.seaside.systemdescriptor.tests.resources.Models
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import com.ngc.seaside.systemdescriptor.tests.resources.Datas

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class LinkSourceAndTargetParsingTest {
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
			Datas.TIME
		)
		validationTester.assertNoIssues(requiredResources)
	}

	// -----------
	// VALID LINKS
	// -----------

	@Test
	def void testDoesParseModelWithLink_From_InputField_To_PartInputField() {
		var source = '''
			package clocks.models
			
			import clocks.datatypes.ZonedTime
			import clocks.models.part.Alarm
			
			model AlarmClock {
			    input {
					ZonedTime currentTime
					  }
			
			    parts {
			    	Alarm alarm
			    }
			
			    links {
			    	link currentTime -> alarm.currentTime
			    }
			}
		'''

		var result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		var model = result.element as Model;
		var link = model.links.declarations.get(0) as BaseLinkDeclaration
		var linkSource = link.source as FieldReference
		var linkTarget = link.target as LinkableExpression

		assertEquals(
			"linkSource not correct!",
			"currentTime",
			linkSource.fieldDeclaration.name
		)
		assertEquals(
			"linkTarget not correct!",
			"alarm",
			(linkTarget.ref as FieldReference).fieldDeclaration.name
		)
		assertEquals(
			"linkTarget not correct!",
			"currentTime",
			linkTarget.tail.name
		)
	}

	@Test
	def void testDoesParseModelWithLink_From_PartOutputField_To_PartInputField() {
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

		var model = result.element as Model;
		var link = model.links.declarations.get(0) as BaseLinkDeclaration
		var linkSource = link.source as LinkableExpression
		var linkTarget = link.target as LinkableExpression
		assertEquals(
			"linkSource not correct!",
			"clock",
			(linkSource.ref as FieldReference).fieldDeclaration.name
		)
		assertEquals(
			"linkSource not correct!",
			"currentTime",
			linkSource.tail.name
		)
		assertEquals(
			"linkTarget not correct!",
			"alarm",
			(linkTarget.ref as FieldReference).fieldDeclaration.name
		)
		assertEquals(
			"linkTarget not correct!",
			"currentTime",
			linkTarget.tail.name
		)
	}

	@Test
	def void testDoesParseModelWithLink_From_PartOutputField_To_OutputField() {
		var source = '''
			package clocks.models
			
			import clocks.datatypes.ZonedTime
			import clocks.models.part.Clock
			
			model AlarmClock {
			    output {
			    	ZonedTime currentTime
			    }
			
			    parts {
			    	Clock clock
			    }
			
			    links {
			    	link clock.currentTime -> currentTime
			    }
			}
		'''

		var result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		var model = result.element as Model;
		var link = model.links.declarations.get(0) as BaseLinkDeclaration
		var linkSource = link.source as LinkableExpression
		var linkTarget = link.target as FieldReference
		assertEquals(
			"linkSource not correct!",
			"clock",
			(linkSource.ref as FieldReference).fieldDeclaration.name
		)
		assertEquals(
			"linkSource not correct!",
			"currentTime",
			linkSource.tail.name
		)
		assertEquals(
			"linkTarget not correct!",
			"currentTime",
			linkTarget.fieldDeclaration.name
		)
	}

	// --------------------------------
	// INVALID LINKS DUE TO TYPE ISSUES
	// --------------------------------

	@Test
	def void testDoesNotParseModelWithLinkIfTypesAre_Not_TheSame() {

        var source = '''
            package clocks.models

            import clocks.datatypes.Time
            import clocks.models.part.Alarm

            model AlarmClock {
                input {
                    Time currentTime
                }

                output {
                    Time alarmTime
                }

                parts {
                    Alarm alarm
                }

                links {
                    link currentTime -> alarm.alarmAcknowledgement
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


	// ------------------------------------------------
	// INVALID LINKS DUE TO PART FIELDS BEING INCORRECT
	// ------------------------------------------------

	@Test
	def void testDoesNotParseModelWithLink_From_PartOutputField_To_PartOutputField() {
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
			    	link clock.currentTime -> alarm.outputTime
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
	def void testDoesNotParseModelWithLink_From_PartInputField_To_PartOutputField() {
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
			    	link alarm.currentTime -> clock.currentTime
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
	def void testDoesNotParseModelWithLink_From_PartOutputField_To_InputField() {
		 var source = '''
            package clocks.models

            import clocks.datatypes.ZonedTime
            import clocks.models.part.Alarm
            import clocks.models.part.Clock

            model AlarmClock {
                 input {
                      ZonedTime timeOverride
                }

                output {
                    ZonedTime currentTime
                }

                parts {
                    Alarm alarm
                    Clock clock
                }

                links {
                    link clock.currentTime -> timeOverride
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
	def void testDoesNotParseModelWithLink_From_InputField_To_PartOutputField() {
	
        var source = '''
            package clocks.models

            import clocks.datatypes.ZonedTime
            import clocks.models.part.Alarm
            import clocks.models.part.Clock

            model AlarmClock {
                 input {
                      ZonedTime timeOverride
                }

                output {
                    ZonedTime currentTime
                }

                parts {
                    Alarm alarm
                    Clock clock
                }

                links {
                    link timeOverride -> clock.currentTime
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
	def void testDoesNotParseModelWithLink_From_OutputField_To_PartInputField() {
		var source = '''
            package clocks.models

            import clocks.datatypes.ZonedTime
            import clocks.models.part.Alarm
            import clocks.models.part.Clock

            model AlarmClock {
                output {
                    ZonedTime currentTime
                }

                parts {
                    Alarm alarm
                    Clock clock
                }

                links {
                    link currentTime -> clock.inputTime
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
	def void testDoesNotParseModelWithLink_From_OutputField_To_PartOutputField() {
		var source = '''
            package clocks.models

            import clocks.datatypes.ZonedTime
            import clocks.models.part.Alarm
            import clocks.models.part.Clock

            model AlarmClock {
                 input {
                      ZonedTime timeOverride
                }

                output {
                    ZonedTime currentTime
                }

                parts {
                    Alarm alarm
                    Clock clock
                }

                links {
                    link currentTime -> clock.currentTime
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
