package com.ngc.seaside.systemdescriptor.tests.imports

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import com.ngc.seaside.systemdescriptor.tests.resources.Datas
import com.ngc.seaside.systemdescriptor.tests.resources.Models
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.diagnostics.Diagnostic
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.util.ResourceHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.eclipse.xtext.xbase.validation.IssueCodes
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class ImportParsingTest {

	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ResourceHelper resourceHelper

	@Inject
	ValidationTestHelper validationTester

	Resource requiredResources

	@Before
	def void setup() {
		requiredResources = Datas.allOf(
			resourceHelper,
			Datas.DATE_TIME.requiredResources,
			Models.ALARM.requiredResources,
			Models.EMPTY_MODEL
		)
	}

	@Test
	def void testDoesParseImportedData() {
		val result = parseHelper.parse(Datas.DATE_TIME.source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)	

		var resultData = result.element as Data
		assertEquals(
			"wrong number of fields!",
			2,
			resultData.fields.size
		)
	}
	
	@Test
	def void testDoesParseModelWithImportedData() {
		val result = parseHelper.parse(Models.ALARM.source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoErrors(result)
	}
	
	@Test
	def void testDoesNotParseModelWithMissingOutputDataType() {
		val source = '''
			package clocks.models
			
			model Timer {
				
				output {
					Time currentTime
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.OUTPUT_DECLARATION,
			Diagnostic.LINKING_DIAGNOSTIC
		)
	}

	@Test
	def void testDoesNotParseModelWithMissingInputDataType() {
		val source = '''
			package clocks.models
			
			model Timer {
				
				input {
					Time currentTime
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.INPUT_DECLARATION,
			Diagnostic.LINKING_DIAGNOSTIC
		)
	}
	
	@Test
	def void testDoesNotParseWithImportsThatCannotBeResolved() {
		val source = '''
			package foo
			
			import foo.DoesNotExist
			
			model FooBar {
			}
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.IMPORT,
			IssueCodes.IMPORT_UNRESOLVED
		)
	}
}
