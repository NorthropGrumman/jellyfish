/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
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
