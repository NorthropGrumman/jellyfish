/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.systemdescriptor.tests.model

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import com.ngc.seaside.systemdescriptor.tests.resources.Models
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import com.ngc.seaside.systemdescriptor.tests.resources.Datas
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.diagnostics.Diagnostic
import org.eclipse.xtext.testing.XtextRunner
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.util.ResourceHelper
import com.ngc.seaside.systemdescriptor.tests.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*
import org.junit.Ignore

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class RefinedModelParsingTest {
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
			Models.TIMER,
			Models.EMPTY_MODEL,
			Datas.TIME
		)
		validationTester.assertNoIssues(requiredResources)
	}

	@Test
	def void testDoesParseRefinedModelUsingFullyQualifiedName() {
		val source = '''
			package clocks.models
			model MyModel refines clocks.models.part.Alarm {
			}
		'''

		var result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
	}

	@Test
	def void testDoesParseRefinedModelUsingImport() {
		val source = '''
			package clocks.models
			import clocks.models.part.Alarm
			model MyModel refines Alarm {
			}
		'''

		var result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
	}

	@Test
	@Ignore("This test broke with an updated version of XText.")
	def void testDoesNotParseModelThatRefinesItself() {
		val source = '''
			package clocks.models
			model MyModel refines clocks.models.MyModel {
			}
		'''

		var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)

		validationTester.assertError(invalidResult, SystemDescriptorPackage.Literals.MODEL, null)
	}

	@Test
	def void testDoesNotParseModelThatRefinesData() {
		val source = '''
			package clocks.models
			model MyModel refines clocks.datatypes.Time {
			}
		'''

		var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)

		validationTester.assertError(invalidResult, SystemDescriptorPackage.Literals.MODEL,
			Diagnostic.LINKING_DIAGNOSTIC)
	}

	@Test
	@Ignore("This test broke with an upgraded version of XText")
	def void testDoesNotParseModelsThatCircularlyRefineEachOther() {
		val refinedModel = Models.allOf(
			resourceHelper,
			Models.INVALID_REFINED_MODEL
		)
		val source = '''
			package refinement.test
			import refinement.test.InvalidRefinedModel
			model AnotherInvalidRefinedModel refines InvalidRefinedModel {
			}
		'''

		var invalidResult = parseHelper.parse(source, refinedModel.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertNoIssues(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.MODEL,
			null
		)
	}

	@Test
	def void testDoesNotParseModelRedeclaring_Inputs() {
		val source = '''
			package clocks.models
			import clocks.models.part.Alarm
			import clocks.datatypes.Time
			model RefinedModel refines Alarm {
			    input {
			        Time time
			    }
			}
		'''

		var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)

		validationTester.assertError(invalidResult, SystemDescriptorPackage.Literals.INPUT_DECLARATION, null)
	}

	@Test
	def void testDoesNotParseModelRedeclaring_Outputs() {
		val source = '''
			package clocks.models
			import clocks.models.part.Alarm
			import clocks.datatypes.Time
			model RefinedModel refines Alarm {
			    output {
			        Time time
			    }
			}
		'''

		var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)

		validationTester.assertError(invalidResult, SystemDescriptorPackage.Literals.OUTPUT_DECLARATION, null)
	}

	@Test
	def void testDoesNotParseModelAdding_Requires() {
		val source = '''
			package clocks.models
			import clocks.models.part.Alarm
			import clocks.models.part.Timer
			import foo.AnEmptyModel
			model RefinedModel refines Alarm {
			    requires {
			        AnEmptyModel emptyModel
			        Timer timer
			    }
			}
		'''

		var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)

		validationTester.assertError(invalidResult, SystemDescriptorPackage.Literals.REQUIRE_DECLARATION, null)
	}

	@Test
	def void testDoesNotParseModelRedeclaring_Scenarios() {
		val source = '''
			package clocks.models
			import clocks.models.part.Alarm
			model RefinedModel refines Alarm {
			    scenario whatever {
			    }
			}
		'''

		var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)

		validationTester.assertError(invalidResult, SystemDescriptorPackage.Literals.SCENARIO, null)
	}
}
