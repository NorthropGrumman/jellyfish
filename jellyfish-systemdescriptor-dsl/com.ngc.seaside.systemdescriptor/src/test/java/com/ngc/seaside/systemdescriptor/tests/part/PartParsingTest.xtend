package com.ngc.seaside.systemdescriptor.tests.part

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.diagnostics.Diagnostic
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.util.ResourceHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import com.ngc.seaside.systemdescriptor.tests.resources.Models
import com.ngc.seaside.systemdescriptor.tests.resources.Datas
import com.ngc.seaside.systemdescriptor.systemDescriptor.BasePartDeclaration

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class PartParsingTest {

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

	@Test
	def void testDoesParseModelWithParts() {
		val source = '''
			package clocks.models
			
			import clocks.models.part.Alarm
			
			model BigClock {
				
				parts {
					Alarm alarm
				}
			}
		'''

		val result = parseHelper.parse(source, requiredResources.resourceSet)
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
			"alarm",
			part.name
		)
		if (part.eClass().equals(SystemDescriptorPackage.Literals.BASE_PART_DECLARATION)) {
			assertEquals(
				"part type not correct!",
				"Alarm",
				(part as BasePartDeclaration).type.name
			)
		}
	}

	@Test
	def void testDoesParseModelWithRefinedParts() {
		val source = '''
			package clocks.models
			
			import clocks.models.part.Clock
			
			model BigClock refines Clock{
				
				parts {
					refine emptyModel
				}
			}
		'''

		val result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

	}
	
	@Test
	def void testDoesNotParseModelWithRefinedPartsWithTypeDelcared() {
		val source = '''
			package clocks.models
			
			import clocks.models.part.Clock
			
			model BigClock refines Clock{
				
				parts {
					refine AnEmptyModel emptyModel 
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PART_DECLARATION,
			null
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

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
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
			
			import clocks.models.part.Alarm
			
			model BigClock {
				
				parts {
					Alarm alarm
					Alarm alarm
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
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
			
			import clocks.models.part.Alarm
			import clocks.datatypes.Time
			
			model BigClock {
				
				input {
					Time alarm
				}
				
				parts {
					Alarm alarm
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
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
			
			import clocks.models.part.Alarm
			import clocks.datatypes.Time
			
			model BigClock {
				
				output {
					Time alarm
				}
				
				parts {
					Alarm alarm
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
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
			
			import clocks.models.part.Alarm
			
			model BigClock {
				
				parts {
					Alarm ^int
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PART_DECLARATION,
			null
		)
	}

	@Test
	def void testDoesNotParseANonRefinedModelThatRefinedAPart() {
		val source = '''
			package clocks.models
			
			model BigClock {
			
				parts {
					refine emptyModel {
						metadata {
									"name" : "My Part",
									"description" : "EmptyModel Part",
									"stereotypes" : ["part", "example"]
								}
					}
				}
			}
		     '''

		var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)

		validationTester.assertError(invalidResult, SystemDescriptorPackage.Literals.PART_DECLARATION, null)
	}

	@Test
	def void testDoesNotParseRefinedModelOfAPartThatWasntInTheRefinedModel() {
		val source = '''
			package clocks.models
			
			import clocks.models.part.Alarm
			
			model BigClock refines Alarm{
			
				parts {
					refine superModel
					
				}
			}
		     '''

		var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)

		validationTester.assertError(invalidResult, SystemDescriptorPackage.Literals.PART_DECLARATION, null)
	}
}
