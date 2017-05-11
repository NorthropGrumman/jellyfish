package com.ngc.seaside.systemdescriptor.tests

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*
import org.eclipse.xtext.junit4.util.ResourceHelper
import org.junit.Before
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.diagnostics.Diagnostic
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class ModelParsingTest {

	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ResourceHelper resourceHelper

	@Inject
	ValidationTestHelper validationTester
	
	Resource dataResource

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
	}

	@Test
	def void testDoesParseEmptyModel() {
		val source = '''
			package clocks.models
			
			model Timer {
			}
		'''

		val result = parseHelper.parse(source)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		assertEquals(
			"package name not correct",
			"clocks.models",
			result.name
		)

		var model = result.element
		assertTrue(
			"did not parse model!",
			model instanceof Model
		)
	}

	@Test
	def void testDoesParseModelWithImportedData() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			
			model Timer {
			}
		'''

		val result = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
	}
	
	
	@Test
	def void testDoesNotParseWithMissingImports() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.Foo
			
			model Timer {
			}
		'''

		val invalidResult = parseHelper.parse(source, dataResource.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.IMPORT,
			Diagnostic.LINKING_DIAGNOSTIC
		)
	}

}
