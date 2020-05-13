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
package com.ngc.seaside.systemdescriptor.tests.data

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Cardinality
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data
import com.ngc.seaside.systemdescriptor.systemDescriptor.IntValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataType
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import com.ngc.seaside.systemdescriptor.tests.resources.Datas
import com.ngc.seaside.systemdescriptor.tests.resources.Models
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.util.ResourceHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class DataExtensionParseingTest {

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
			Datas.HAMBURGER.requiredResources,
			Datas.SLIDERS_MEAL.requiredResources
		)
	}

	@Test
	def void testDoesEnableDataTypesToBeExtended() {
		val result = parseHelper.parse(Datas.HAMBURGER.source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		var hamburger = result.element as Data
		assertEquals(
			"data superclass not correct!",
			"Food",
			hamburger.extendedDataType.name
		)

		var caloriesField = hamburger.extendedDataType.fields.get(0) as PrimitiveDataFieldDeclaration
		assertEquals(
			"inherited field name not correct!",
			"calories",
			caloriesField.name
		)
		assertEquals(
			"inherited field type not correct!",
			PrimitiveDataType.INT,
			caloriesField.type
		)
		assertEquals(
			"inherited field metadata not correct!",
			"maxPerMeal",
			caloriesField.definition.metadata.json.members.get(0).key
		)
		assertEquals(
			"inherited field metadata not correct!",
			500,
			(caloriesField.definition.metadata.json.members.get(0).value as IntValue).value
		)
	}

	@Test
	def void testDoesEnableDataTypesWithManyFieldsToBeExtendedWithMultipleLevelsOfExtension() {
		val result = parseHelper.parse(Datas.SLIDERS_MEAL.source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		var sliders = result.element as Data
		assertEquals(
			"data superclass not correct!",
			"Hamburger",
			sliders.extendedDataType.name
		)

		assertEquals(
			"data superclass's superclass not correct!",
			"Food",
			sliders.extendedDataType.extendedDataType.name
		)

		var burgersField = sliders.fields.get(0)
		assertEquals(
			"inherited field cardinality name not correct!",
			Cardinality.MANY,
			burgersField.cardinality
		)
	}

	@Test
	def void testDoesWarnIfExtendedClassIsUsedAsInput() {
		val source = '''
			package food
			
			import food.Food
			
			model FastFoodPlace {
				input {
					Food food
				}
			}
		'''

		val result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoErrors(result)

		validationTester.assertWarning(result, SystemDescriptorPackage.Literals.FIELD_DECLARATION, null)
	}

	@Test
	def void testDoesWarnIfExtendedClassIsUsedAsOutput() {
		val source = '''
			package food
			
			import food.Food
			
			model FastFoodPlace {
				output {
					Food food
				}
			}
		'''

		val result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoErrors(result)

		validationTester.assertWarning(result, SystemDescriptorPackage.Literals.FIELD_DECLARATION, null)
	}
}
