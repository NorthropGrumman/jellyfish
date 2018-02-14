package com.ngc.seaside.systemdescriptor.tests.part

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
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
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class PartParsingTest {

	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ResourceHelper resourceHelper

	@Inject
	ValidationTestHelper validationTester

	Resource subPartResource

	Resource dataResource

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

		dataResource = resourceHelper.resource(
			'''
				package clocks.datatypes
							
				data Time {
				}
			''',
			subPartResource.resourceSet
		)
		validationTester.assertNoIssues(dataResource)
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

		val part = model.parts.declarations.get(0)
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
	def void testDoesNotParseModelWithPartOfTypeData() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			
			model BigClock {
				
				parts {
					Time time
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, subPartResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PART_DECLARATION,
			Diagnostic.LINKING_DIAGNOSTIC
		)
	}

	@Test
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
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PART_DECLARATION,
			null
		)
	}

	@Test
	def void testDoesNotParseModelWithDuplicatePartAndInput() {
		val source = '''
			package clocks.models
			
			import clocks.models.sub.Gear
			import clocks.datatypes.Time
			
			model BigClock {
				
				input {
					Time gear
				}
				
				parts {
					Gear gear
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, subPartResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PART_DECLARATION,
			null
		)
	}

	@Test
	def void testDoesNotParseModelWithDuplicatePartAndOutput() {
		val source = '''
			package clocks.models
			
			import clocks.models.sub.Gear
			import clocks.datatypes.Time
			
			model BigClock {
				
				output {
					Time gear
				}
				
				parts {
					Gear gear
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, subPartResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PART_DECLARATION,
			null
		)
	}
	
	
	@Test
	def void testDoesNotParseModelWithEscapedPartsFieldName() {
		val source = '''
			package clocks.models
			
			import clocks.models.sub.Gear
			
			model BigClock {
				
				parts {
					Gear ^int
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, subPartResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PART_DECLARATION,
			null
		)
	}
}
