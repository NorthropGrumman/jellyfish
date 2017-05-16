package com.ngc.seaside.systemdescriptor.tests

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldReference
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableExpression
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.eclipse.emf.common.util.URI
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
import org.junit.Ignore

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class LinkParsingTest {

	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ResourceHelper resourceHelper

	@Inject
	ValidationTestHelper validationTester

	Resource dataResource

	Resource partResource1

	Resource requirementResource1

	@Before
	def void setup() {
		dataResource = resourceHelper.resource(
			'''
				package clocks.datatypes
							
				data Time {
				}
			''',
			URI.createURI("datatypes.sd")
		)
		validationTester.assertNoIssues(dataResource)

		requirementResource1 = resourceHelper.resource(
			'''
				package clocks.models.part
				
				import clocks.datatypes.Time
							
				model Speaker {
					input {
						Time speakTime
					}
					
					output {
						Time doneTime
					}
				}
			''',
			dataResource.resourceSet
		)
		validationTester.assertNoIssues(requirementResource1)

		partResource1 = resourceHelper.resource(
			'''
				package clocks.models.part
				
				import clocks.datatypes.Time
				import clocks.models.part.Speaker
							
				model Alarm {
					requires {
						Speaker speaker
					}
					
					input {
						Time myTime
					}
					
					output {
						Time fooTime
					}
				}
			''',
			dataResource.resourceSet
		)
		validationTester.assertNoIssues(partResource1)
	}

	@Test
	def void testDoesParseModelWithLinkFromInputToPartInput() {
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
					link currentTime to alarm.myTime
				}
			}
		'''

		var result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		var model = result.element as Model;
		var link = model.links.declarations.get(0)
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
			"myTime",
			linkTarget.tail.name
		)

		// Test the reverse.
		source = '''
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
					link alarm.myTime to currentTime
				}
			}
		'''

		result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
	}

	@Test
	def void testDoesParseModelWithLinkFromInputToRequirementInput() {
		var source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			import clocks.models.part.Alarm
			
			model AlarmClock {
				requires {
					Alarm alarm
				}
				
				input {
					Time currentTime
				}
				
				output {
					Time alarmTime
				}
				
				links {
					link currentTime to alarm.myTime
				}
			}
		'''

		var result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		var model = result.element as Model;
		var link = model.links.declarations.get(0)
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
			"myTime",
			linkTarget.tail.name
		)

		// Test the reverse.
		source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			import clocks.models.part.Alarm
			
			model AlarmClock {
				requires {
					Alarm alarm
				}
				
				input {
					Time currentTime
				}
				
				output {
					Time alarmTime
				}
				
				links {
					link alarm.myTime to currentTime
				}
			}
		'''

		result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
	}

	@Test
	def void testDoesParseModelWithLinkFromOutputToPartOutput() {
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
					link alarmTime to alarm.fooTime
				}
			}
		'''

		var result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		// Test the reverse.
		source = '''
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
					link alarm.fooTime to alarmTime
				}
			}
		'''

		result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
	}

	@Test
	def void testDoesParseModelWithLinkFromOutputToRequirementOutput() {
		var source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			import clocks.models.part.Alarm
			
			model AlarmClock {
				requires {
					Alarm alarm
				}
				
				input {
					Time currentTime
				}
				
				output {
					Time alarmTime
				}
				
				links {
					link alarmTime to alarm.fooTime
				}
			}
		'''

		var result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		// Test the reverse.
		source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			import clocks.models.part.Alarm
			
			model AlarmClock {
				requires {
					Alarm alarm
				}
				
				input {
					Time currentTime
				}
				
				output {
					Time alarmTime
				}
				
				links {
					link alarm.fooTime to alarmTime
				}
			}
		'''

		result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
	}

	@Test
	def void testDoesParseModelWithLinkFromRequirementToPartRequirement() {
		var source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			import clocks.models.part.Alarm
			import clocks.models.part.Speaker
			
			model AlarmClock {
				requires {
					Speaker speaker
				}
				
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
					link speaker to alarm.speaker
				}
			}
		'''

		var result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		// Test the reverse.
		source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			import clocks.models.part.Alarm
			import clocks.models.part.Speaker
			
			model AlarmClock {
				requires {
					Speaker speaker
				}
				
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
					link alarm.speaker to speaker
				}
			}
		'''

		result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
	}

	@Test
	def void testDoesNotParseModelWithLinkToSelf() {
		var source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			import clocks.models.part.Alarm
			
			model AlarmClock {
				input {
					Time currentTime
				}
				
				links {
					link currentTime to currentTime
				}
			}
		'''

		var invalidResult = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.LINK_DECLARATION,
			null
		)
	}

	@Test
	def void testDoesNotParseModelWithLinkFromInputToInput() {
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
					link currentTime to otherTime
				}
			}
		'''

		var invalidResult = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.LINK_DECLARATION,
			null
		)
	}

	@Test
	def void testDoesNotParseModelWithLinkFromOutputToOutput() {
		var source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			import clocks.models.part.Alarm
			
			model AlarmClock {
				output {
					Time currentTime
					Time otherTime
				}
				
				links {
					link currentTime to otherTime
				}
			}
		'''

		var invalidResult = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.LINK_DECLARATION,
			null
		)
	}

	@Test
	def void testDoesNotParseModelWithLinkFromPartToPart() {
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
				
				links {
					link a1 to a2
				}
			}
		'''

		var invalidResult = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.LINK_DECLARATION,
			null
		)
	}

	@Test
	@Ignore("not yet passing")
	def void testDoesNotParseModelWithLinkFromInputToPartOutput() {
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
					link currentTime to alarm.fooTime
				}
			}
		'''

		var invalidResult = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.LINK_DECLARATION,
			null
		)
	}
}
