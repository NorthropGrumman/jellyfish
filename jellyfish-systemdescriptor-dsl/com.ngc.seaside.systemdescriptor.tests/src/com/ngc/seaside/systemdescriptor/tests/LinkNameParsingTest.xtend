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

//@RunWith(XtextRunner)
//@InjectWith(SystemDescriptorInjectorProvider)
class LinkNameParsingTest {
/*
	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ResourceHelper resourceHelper

	@Inject
	ValidationTestHelper validationTester

	Resource dataResource
	
	Resource dataResource1
	
	Resource dataResource2

	Resource partResource1

	Resource requirementResource1

	@Before
	def void setup() {
		dataResource = resourceHelper.resource(
			'''
				package clocks.datatypes
							
				data ZonedTime {
				}
			''',
			URI.createURI("datatypes.sd")
		)
		validationTester.assertNoIssues(dataResource)
		
		dataResource1 = resourceHelper.resource(
			'''
				package clocks.datatypes
				
				data AlarmAcknowledgement {
				}
			''',
			dataResource.resourceSet
		)
		validationTester.assertNoIssues(dataResource1)
		
		dataResource2 = resourceHelper.resource(
			'''
				package clocks.datatypes
				
				data AlarmStatus {
				}
			''',
			dataResource.resourceSet
		)
		validationTester.assertNoIssues(dataResource2)

		requirementResource1 = resourceHelper.resource(
			'''
				package clocks.models.part
				
				import clocks.datatypes.ZonedTime
							
				model Clock {
					
					output {
						ZonedTime currentTime {
						}
					}
				}
			''',
			dataResource.resourceSet
		)
		validationTester.assertNoIssues(requirementResource1)

		partResource1 = resourceHelper.resource(
			'''
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
			''',
			dataResource.resourceSet
		)
		validationTester.assertNoIssues(partResource1)
	}
	
	@Test
	def void testDoesParseModelWithLinkFromInputFieldToOutputField() {
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
					link clock.currentTime -> alarm.currentTime
				}
			}
		'''

		var result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		var model = result.element as Model;
		var link = model.links.declarations.get(0)
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
	def void testDoesParseModelWithLinkRHSInputToLHSOutput() {
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
					link clock.currentTime -> currentTime
				}
			}
		'''

		var result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		var model = result.element as Model;
		var link = model.links.declarations.get(0)
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
			linkTarget.fieldDeclaration
		)
	}
	
	@Test
	def void testDoesParseModelWithLinkRHSInputToDirectlyToLHSOutputOfClass() {
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

		var result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		var model = result.element as Model;
		var link = model.links.declarations.get(0)
		var linkSource = link.source as FieldReference
		var linkTarget = link.target as LinkableExpression
		assertEquals(
			"linkSource not correct!",
			"timeOverride",
			linkSource.fieldDeclaration.name
		)
		assertEquals(
			"linkSource not correct!",
			"clock",
			(linkTarget.ref as FieldReference).fieldDeclaration.name
		)
		assertEquals(
			"linkTarget not correct!",
			"currentTime",
			linkTarget.tail.name
		)
	}
	
	@Test
	def void testDoesNotParseModelWithLinkFromInputFieldToOutputField() {
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
					link clock.currentTime -> alarm.alarmAcknowledgement
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
	def void testDoesNotParseModelWithLinkLHSOutputToRHSInput() {
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

		var invalidResult = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.LINK_DECLARATION,
			null
		)
	}
	
	@Test
	def void testDoesNotParseModelWithLinkLHSInputToRHSOutput() {
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
					timeOverride -> clock.currentTime
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
*/
}
