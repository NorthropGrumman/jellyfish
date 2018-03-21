package com.ngc.seaside.systemdescriptor.tests.properties

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
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
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import com.ngc.seaside.systemdescriptor.tests.resources.Models
import com.ngc.seaside.systemdescriptor.tests.resources.Datas

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class ModelPropertiesParsingTest {

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
			Datas.DATE,
			Datas.TIME,
			Datas.TIME_ZONE,
			Datas.TIME_CONVENTION
		)
		validationTester.assertNoIssues(requiredResources)
	}

	@Test
	def void testDoesParseModelWithPropertiesDeclaration() {
		val source = '''
			package clocks.models

			import clocks.datatypes.Time
			import clocks.datatypes.TimeZone

			model BigClock {
				properties {
					int intField
					float floatField
					boolean booleanField
					string stringField
					TimeZone enumField
					clocks.datatypes.TimeConvention qualifiedEnumField
					Time dataField
					clocks.datatypes.Date qualifiedDataField
				}
			}
		'''

		val result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		val properties = model.properties
		assertNotNull(
			"did not parse properties",
			properties
		)

		val declaration = properties.declarations.get(0)
		assertEquals(
			"property name not correct",
			"intField",
			declaration.name
		)
	}
	
	@Test
	def void testDoesNotParseModelWithDuplicatePropertyNames() {
		val source = '''
			package clocks.models

			model BigClock {
				properties {
					int intField
					boolean intField
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(invalidResult, SystemDescriptorPackage.Literals.PROPERTY_FIELD_DECLARATION, null)
	}
}
