package com.ngc.seaside.systemdescriptor.tests.scenario

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.StringValue
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.diagnostics.Diagnostic
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.util.ResourceHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class ScenarioMetadataParsingTest {

	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ResourceHelper resourceHelper

	@Inject
	ValidationTestHelper validationTester
	
	Resource dataResource

	Resource modelResource

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
		
		modelResource = resourceHelper.resource(
			'''
				package clocks.models.sub
							
				model Wire {
				}
			''',
			URI.createURI("part.sd")
		)
		validationTester.assertNoIssues(modelResource)
	}
	
	@Test
	def void testDoesParseScenarioWithMetadata() {
		val source = '''
			package clocks.models
			 
			model Speaker {
			  scenario buzz { 
			  	metadata {
			  		"name": "someName",
			  		"description": "someDescription"
			  	}
			  	when receiving alarmTime
			  	then doSomething withThis
			  }
			}
		'''

		val result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		assertFalse(
			"did not parse scenario!",
			model.scenarios.empty
		)
		
		val scenario = model.scenarios.get(0)	
		var metadata = scenario.metadata
		val firstmember = metadata.json.members.get(0)

		assertEquals(
			"metadata did not parse!",
			"name",
			firstmember.key
		)
		assertEquals(
			"metadata did not parse!",
			"someName",
			(firstmember.value as StringValue).value
		)
	}
	
	@Test
	def void testDoesNotParseScenarioWithMisplacedMetadata() {
		val source = '''
			package clocks.models
			 
			import clocks.datatypes.Time
			 
			model Speaker {
			  scenario buzz { 
			  	when receiving alarmTime
			  	then doSomething withThis
			  	metadata {
			  		"name": "someName",
			  		"description": "someDescription"
			  	}
			  }
			}
		'''
		
		val invalidResult = parseHelper.parse(source)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PACKAGE,
			Diagnostic.SYNTAX_DIAGNOSTIC
		)
	}
}
