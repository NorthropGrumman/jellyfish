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
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.util.ResourceHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
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
			hamburger.superclass.name
		)

		var caloriesField = hamburger.superclass.fields.get(0) as PrimitiveDataFieldDeclaration
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
			caloriesField.metadata.members.get(0).key
		)
		assertEquals(
			"inherited field metadata not correct!",
			500,
			(caloriesField.metadata.members.get(0).value as IntValue).value
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
			sliders.superclass.name
		)

		assertEquals(
			"data superclass's superclass not correct!",
			"Food",
			sliders.superclass.superclass.name
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
