package com.ngc.seaside.systemdescriptor.tests.part

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
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
import com.ngc.seaside.systemdescriptor.tests.resources.Models
import com.ngc.seaside.systemdescriptor.tests.resources.Datas
import org.junit.Ignore

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
			Models.SPEAKER,
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
		assertEquals(
			"part type not correct!",
			"Alarm",
			part.type.name
		)
	}
	
	@Test
	def void testDoesParseModelWithRefinedParts() {
		val source = '''
			package clocks.models
			
			import clocks.models.part.Clock
			import foo.AnEmptyModel
			
			model BigClock refines Clock{
				
				parts {
					refine AnEmptyModel emptyModel {
						metadata {
						  			"name" : "My Part",
						  			"description" : "EmptyModel Part",
						  			"stereotypes" : ["part", "example"]
						  		}
						}
				}
			}
		'''

		val result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

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
	@Ignore
    def void testDoesNotParseANonRefinedModelThatRefinedAPart() {
        val source = '''
			package clocks.models
			
			import foo.AnEmptyModel

			model BigClock {
			
				parts {
					refine AnEmptyModel emptyModel {
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

        validationTester.assertError(
            invalidResult,
            SystemDescriptorPackage.Literals.PART_DECLARATION,
            null)
    }
    
    @Test
    @Ignore
    def void testDoesNotParseRefinedModelOfAPartThatWasntInTheRefinedModel() {
        val source = '''
			package clocks.models
			
			import foo.AnEmptyModel
			import clocks.models.part.Alarm

			model BigClock refines Alarm{
			
				parts {
					refine AnEmptyModel emptyModel {
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

        validationTester.assertError(
            invalidResult,
            SystemDescriptorPackage.Literals.PART_DECLARATION,
            null)
    }
}
