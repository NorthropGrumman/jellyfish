package com.ngc.seaside.systemdescriptor.tests

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
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
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.junit.Ignore

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class PartsParsingTest {

	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ResourceHelper resourceHelper

	@Inject
	ValidationTestHelper validationTester

	Resource subPartResource

	@Before
	def void setup() {
		subPartResource = resourceHelper.resource(
			'''
				package clocks.models.sub
							
				model Gear {
				}
			''',
			URI.createURI("subpart.sd")
		)
		validationTester.assertNoIssues(subPartResource)
	}

	@Test
	def void testDoesParseModelWithParts() {
		val source = '''
			package clocks.models
			
			import clocks.models.sub.Gear
			
			model BigClock {
				
				parts {
					Gear gear
				}
			}
		'''

		val result = parseHelper.parse(source, subPartResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		val parts = model.parts
		assertNotNull(
			"did not parse parts",
			parts
		)
		
		val part = model.parts.parts.get(0)
		assertEquals(
			"part name not correct",
			"gear",
			part.name
		)
		assertEquals(
			"part type not correct!",
			"Gear",
			part.type.name
		)
	}
	
	@Test
	@Ignore("not passing yet")
	def void testDoesNotParseModelWithDuplicateParts() {
		val source = '''
			package clocks.models
			
			import clocks.models.sub.Gear
			
			model BigClock {
				
				parts {
					Gear gear
					Gear gear
				}
			}
		'''
		
		val invalidResult = parseHelper.parse(source, subPartResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertNoIssues(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PART_DECLARATION,
			null
		)
	}

}
