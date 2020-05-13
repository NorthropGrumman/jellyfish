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
package com.ngc.seaside.systemdescriptor.tests.properties

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.EnumPropertyValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueAssignment
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import com.ngc.seaside.systemdescriptor.tests.resources.Datas
import com.ngc.seaside.systemdescriptor.tests.resources.Models
import org.eclipse.emf.ecore.EAttribute
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

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class ModelPropertyValuesParsingTest {

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
			Datas.ALL_DAY_APPOINTMENT,
			Datas.TIME,
			Datas.TIME_ZONE,
			Datas.TIME_CONVENTION,
			Datas.ZONED_TIME,
			Models.CLOCK
		)
		validationTester.assertNoIssues(requiredResources)
	}

	@Test
	def void testDoesParseModelWithPrimitivePropertyValues() {
		val source = '''
			package clocks.models
			
			model BigClock {
				properties {
					int intField
					float floatField
					boolean booleanField
					string stringField
					
					intField = 1
					floatField = 0.95
					booleanField = true
					stringField = "myString"
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

		var property = properties.assignments.get(0)
		assertPropertyValue(property, "intField", SystemDescriptorPackage.Literals.INT_VALUE__VALUE, 1)

		property = properties.assignments.get(1)
		assertPropertyValue(property, "floatField", SystemDescriptorPackage.Literals.DBL_VALUE__VALUE, 0.95)

		property = properties.assignments.get(2)
		assertPropertyValue(property, "booleanField", SystemDescriptorPackage.Literals.BOOLEAN_VALUE__VALUE, "true")

		property = properties.assignments.get(3)
		assertPropertyValue(property, "stringField", SystemDescriptorPackage.Literals.STRING_VALUE__VALUE, "myString")
	}

	@Test
	def void testDoesParseModelWithEnumPropertyValues() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.TimeZone
			
			model BigClock {
				properties {
					TimeZone userTimeZone
					
					userTimeZone = TimeZone.CST
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

		var property = properties.assignments.get(0)
		assertPropertyEnumValue(property, "userTimeZone", "TimeZone", "CST")
	}

	@Test
	def void testDoesParseModelWithComplexDataTypePropertyValues() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.TimeZone
			import clocks.datatypes.ZonedTime
			
			model BigClock {
				properties {
					ZonedTime complexProperty
					
					complexProperty.timeZone = TimeZone.CST
					complexProperty.dataTime.date.day = 1
					complexProperty.dataTime.date.month = 2
					complexProperty.dataTime.date.year = 3
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

		var property = properties.assignments.get(0)
		assertComplexPropertyEnumValue(property, "complexProperty.timeZone", "TimeZone", "CST")
		
		property = properties.assignments.get(1)
		assertComplexPropertyValue(
			property, 
			"complexProperty.dataTime.date.day", 
			SystemDescriptorPackage.Literals.INT_VALUE__VALUE, 
			1
		)
		
		property = properties.assignments.get(2)
		assertComplexPropertyValue(
			property, 
			"complexProperty.dataTime.date.month", 
			SystemDescriptorPackage.Literals.INT_VALUE__VALUE, 
			2
		)
		
		property = properties.assignments.get(3)
		assertComplexPropertyValue(
			property, 
			"complexProperty.dataTime.date.year", 
			SystemDescriptorPackage.Literals.INT_VALUE__VALUE, 
			3
		)
	}

	@Test
	def void testDoesParseModelWithPropertiesFromRefinedModel() {
		val source = '''
            package clocks.models.part

            import clocks.models.part.Clock
            import clocks.datatypes.TimeZone

            model BetaClock refines Clock {
            	
                properties {
                	releaseDate.timeZone = TimeZone.CST
                	releaseDate.dataTime.date.day = 1
                	releaseDate.dataTime.date.month = 1
                	releaseDate.dataTime.date.year = 1
                	releaseDate.dataTime.time.hour = 1
                	releaseDate.dataTime.time.minute = 1
                	releaseDate.dataTime.time.second = 1
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

		var property = properties.assignments.get(0)
		assertComplexPropertyEnumValue(property, "releaseDate.timeZone", "TimeZone", "CST")
		
		property = properties.assignments.get(1)
		assertComplexPropertyValue(
			property, 
			"releaseDate.dataTime.date.day", 
			SystemDescriptorPackage.Literals.INT_VALUE__VALUE, 
			1
		)
	}

	@Test
	def void testDoesAllowForOverridingProperties() {
		val source = '''
			package clocks.models
			
			model BigClock {
				properties {
					int intField
					
					intField = 1
					intField = 2
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

		var property = properties.assignments.get(1)
		assertPropertyValue(property, "intField", SystemDescriptorPackage.Literals.INT_VALUE__VALUE, 2)
	}
	
	@Test
	def void testDoesHandleInheritedFieldsFromComplexDataProperties() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.AllDayAppointment
			
			model BigClock {
				properties {
					AllDayAppointment appointment
					
					appointment.day = 3
					appointment.month = 1
					appointment.year = 2018
					appointment.name = "foo"
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
		
		var property = properties.assignments.get(0)
		assertComplexPropertyValue(
			property, 
			"appointment.day", 
			SystemDescriptorPackage.Literals.INT_VALUE__VALUE, 
			3
		)
	}

	@Test
	def void testDoesNotParseModelIfPropertyNotDeclared() {
		val source = '''
			package clocks.models
			
			model BigClock {
				properties {
					int intField
					
					fooField = 1
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PROPERTY_VALUE_EXPRESSION,
			Diagnostic.LINKING_DIAGNOSTIC
		)
	}

	@Test
	def void testDoesNotParseModelIfPrimitivePropertyValueTypesNotCorrect() {
		var source = '''
			package clocks.models
			
			model BigClock {
				properties {
					int intField
					
					intField = 0.95
				}
			}
		'''
		var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PROPERTY_VALUE,
			null
		)

		source = '''
			package clocks.models
			
			model BigClock {
				properties {
					float floatField
					
					floatField = 1
				}
			}
		'''
		invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PROPERTY_VALUE,
			null
		)

		source = '''
			package clocks.models
			
			model BigClock {
				properties {
					boolean booleanField
					
					booleanField = "hello"
				}
			}
		'''
		invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PROPERTY_VALUE,
			null
		)

		source = '''
			package clocks.models
			
			model BigClock {
				properties {
					string stringField
					
					stringField = false
				}
			}
		'''
		invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PROPERTY_VALUE,
			null
		)

		source = '''
			package clocks.models
			
			import clocks.datatypes.TimeZone
			
			model BigClock {
				properties {
					string stringField
					
					stringField = TimeZone.CST
				}
			}
		'''
		invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PROPERTY_VALUE,
			null
		)
	}

	@Test
	def void testDoesNotParseModelIfEnumPropertyValueTypeNotCorrect() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.TimeZone
			import clocks.datatypes.TimeConvention
			
			model BigClock {
				properties {
					TimeZone userTimeZone
					
					userTimeZone = TimeConvention.TWELVE_HOUR
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.ENUM_PROPERTY_VALUE,
			null
		)
	}

	@Test
	def void testDoesNotParseModelIfEnumPropertyValueIsNotAnEnumConstant() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.TimeZone
			
			model BigClock {
				properties {
					TimeZone userTimeZone
					
					userTimeZone = TimeZone.FOO
				}
			}
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.ENUM_PROPERTY_VALUE,
			null
		)
	}

	@Test
	def void testDoesNotParseModelIfComplexDataTypePropertyValueTypeIsNotCorrect() {
		var source = '''
			package clocks.models
			
			import clocks.datatypes.ZonedTime
			
			model BigClock {
				properties {
					ZonedTime complexProperty
					
					complexProperty.timeZone = 123
					complexProperty.dataTime.date.day = 1
					complexProperty.dataTime.date.month = 2
					complexProperty.dataTime.date.year = 3
				}
			}
		'''

		var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.INT_VALUE,
			null
		)
		
		source = '''
			package clocks.models
			
			import clocks.datatypes.TimeZone
			import clocks.datatypes.ZonedTime
			
			model BigClock {
				properties {
					ZonedTime complexProperty
					
					complexProperty.timeZone = TimeZone.CST
					complexProperty.dataTime.date.day = "foo"
				}
			}
		'''
		
		invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.STRING_VALUE,
			null
		)
	}

	@Test
	def void testDoesNotParseModelIfComplexDataTypePathIsNotCorrect() {
		var source = '''
			package clocks.models
			
			import clocks.datatypes.ZonedTime
			
			model BigClock {
				properties {
					ZonedTime complexProperty
					
					//complexProperty.dataTime.date.day = 1
					complexProperty.dataTime.this.is.invalid = 2
				}
			}
		'''
		
		var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PROPERTY_VALUE_EXPRESSION_PATH_SEGMENT,
			Diagnostic.LINKING_DIAGNOSTIC
		)
		
		source = '''
			package clocks.models
			
			import clocks.datatypes.ZonedTime
			
			model BigClock {
				properties {
					ZonedTime complexProperty
					
					complexProperty.dataTime.date.day = 1
					complexProperty.this.is.invalid = 2
				}
			}
		'''
		
		invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PROPERTY_VALUE_EXPRESSION_PATH_SEGMENT,
			Diagnostic.LINKING_DIAGNOSTIC
		)
		
		source = '''
			package clocks.models
			
			import clocks.datatypes.ZonedTime
			
			model BigClock {
				properties {
					ZonedTime complexProperty
					
					complexProperty.dataTime.date.day = 1
					this.is.invalid = 2
				}
			}
		'''
		
		invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PROPERTY_VALUE_EXPRESSION_PATH_SEGMENT,
			Diagnostic.LINKING_DIAGNOSTIC
		)
	}

	def private static void assertPropertyValue(PropertyValueAssignment property, String name, EAttribute attribute,
		Object expected) {
		assertEquals(
			"property name not correct!",
			property.expression.declaration.name,
			name
		)
		val value = property.value;
		assertTrue(
			"property type not correct!",
			value.eClass.isSuperTypeOf(attribute.EContainingClass)
		)
		assertEquals(
			"property value not correct!",
			expected,
			value.eGet(attribute)
		)
	}
	
	def private static void assertComplexPropertyValue(PropertyValueAssignment property, String path, EAttribute attribute,
		Object expected) {
		val value = property.value;
		assertTrue(
			"property type not correct!",
			value.eClass.isSuperTypeOf(attribute.EContainingClass)
		)
		assertEquals(
			"property value not correct!",
			expected,
			value.eGet(attribute)
		)
		val actualPath = new StringBuilder(property.expression.declaration.name)
		property.expression.pathSegments.forEach[s|actualPath.append(".").append(s.fieldDeclaration.name)]
		assertEquals(
			"property path not correct!",
			path,
			actualPath.toString()
		)
	}

	def private static void assertPropertyEnumValue(PropertyValueAssignment property, String name,
		String enumerationTypeName, String expected) {
		val value = property.value
		assertTrue(
			"value is not a enum property value!",
			value instanceof EnumPropertyValue
		)
		assertEquals(
			"property name not correct!",
			name,
			property.expression.declaration.name
		)
		assertEquals(
			"enumeration type not correct!",
			enumerationTypeName,
			(value as EnumPropertyValue).enumeration.name
		)
		assertEquals(
			"value not correct!",
			expected,
			(value as EnumPropertyValue).value
		)
	}

	def private static void assertComplexPropertyEnumValue(PropertyValueAssignment property, String path,
		String enumerationTypeName, String expected) {
		val value = property.value
		assertTrue(
			"value is not a enum property value!",
			value instanceof EnumPropertyValue
		)
		assertEquals(
			"enumeration type not correct!",
			enumerationTypeName,
			(value as EnumPropertyValue).enumeration.name
		)
		assertEquals(
			"value not correct!",
			expected,
			(value as EnumPropertyValue).value
		)

		val actualPath = new StringBuilder(property.expression.declaration.name)
		property.expression.pathSegments.forEach[s|actualPath.append(".").append(s.fieldDeclaration.name)]
		assertEquals(
			"property path not correct!",
			path,
			actualPath.toString()
		)
	}
}
