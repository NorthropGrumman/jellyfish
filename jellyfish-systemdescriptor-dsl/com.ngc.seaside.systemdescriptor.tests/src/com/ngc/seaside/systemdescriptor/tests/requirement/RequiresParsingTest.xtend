package com.ngc.seaside.systemdescriptor.tests.requirement

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
import com.ngc.seaside.systemdescriptor.systemDescriptor.BaseRequireDeclaration

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class RequiresParsingTest {

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
	def void testDoesParseModelWithRequires() {
		val source = '''
			package clocks.models
			
			import clocks.models.part.Speaker
			
			model Alarm {
				
				requires {
				    Speaker speaker
				 }
			}
		'''

		val result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		val requirements = model.requires
		assertNotNull(
			"did not parse requires!",
			requirements
		)

		val require = requirements.declarations.get(0)
		assertEquals(
			"require name not correct!",
			"speaker",
			require.name
		)
		assertEquals(
			"require type not correct!",
			"Speaker",
			(require as BaseRequireDeclaration).type.name
		)
	}
	
	@Test
	def void testDoesParseModelWithRefinedRequires() {
		val source = '''
			package clocks.models
			
			import clocks.models.part.Clock
			
			model BigClock refines Clock{
				
				requires {
					refine requiresEmptyModel
				}
			}
		'''

		val result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

	}

	@Test
	def void testDoesNotParseModelWithRequiresOfTypeData() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			
			model Alarm {
				
				requires {
					Time time
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.REQUIRE_DECLARATION,
			Diagnostic.LINKING_DIAGNOSTIC
		)
	}

	@Test
	def void testDoesNotParseModelWithDuplicateRequires() {
		val source = '''
			package clocks.models
			
			import clocks.models.part.Speaker
			
			model Alarm {
				
				requires {
					Speaker speaker
					Speaker speaker
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.REQUIRE_DECLARATION,
			null
		)
	}

	@Test
	def void testDoesNotParseModelWithDuplicateRequireAndInput() {
		val source = '''
			package clocks.models
			
			import clocks.models.part.Speaker
			import clocks.datatypes.Time
			
			model Alarm {
				
				requires {
					Speaker speaker
				}
				
				input {
					Time speaker
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.INPUT_DECLARATION,
			null
		)
	}

	@Test
	def void testDoesNotParseModelWithDuplicateRequireAndOutput() {
		val source = '''
			package clocks.models
			
			import clocks.models.part.Speaker
			import clocks.datatypes.Time
			
			model Alarm {
				
				requires {
					Speaker speaker
				}
				
				output {
					Time speaker
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.OUTPUT_DECLARATION,
			null
		)
	}

	@Test
	def void testDoesNotParseModelWithDuplicateRequireAndPart() {
		val source = '''
			package clocks.models
			
			import clocks.models.part.Speaker
			import clocks.datatypes.Time
			
			model Alarm {
				
				requires {
					Speaker speaker
				}
				
				parts {
					Speaker speaker
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
	def void testDoesNotParseModelWithEscapedRequiresFieldName() {
		val source = '''
			package clocks.models
			
			import clocks.models.part.Speaker
			
			model Alarm {
				
				requires {
				    Speaker ^int
				 }
			}
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.REQUIRE_DECLARATION,
			null
		)
	}
	
	@Test
	def void testDoesNotParseANonRefinedModelThatRefinesARequirement() {
		val source = '''
			package clocks.models
			
			model BigClock {
			
				requires {
					refine emptyModel
				}
			}
		     '''

		var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)

		validationTester.assertError(invalidResult, SystemDescriptorPackage.Literals.REQUIRE_DECLARATION, null)
	}
	
	@Test
	def void testDoesNotParseRefinedModelOfARequireThatWasntInTheRefinedModel() {
		val source = '''
			package clocks.models
			
			import clocks.models.part.Clock
			
			model BigClock refines Clock{
			
				requires {
					refine superModel
				}
			}
		     '''

		var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)

		validationTester.assertError(invalidResult, SystemDescriptorPackage.Literals.REQUIRE_DECLARATION, null)
	}
	
	@Test
	def void testDoesNotParseRefinedModelOfARequireOnAModel() {
		val source = '''
			package clocks.models
			
			import clocks.models.part.Clock
			
			model BigClock refines Clock{
			
				requires {
					refine AnEmptyModel
				}
			}
		     '''

		var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)

		validationTester.assertError(invalidResult, SystemDescriptorPackage.Literals.REQUIRE_DECLARATION, null)
	}
	

}
