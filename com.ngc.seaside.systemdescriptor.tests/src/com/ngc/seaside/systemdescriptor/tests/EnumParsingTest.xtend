package com.ngc.seaside.systemdescriptor.tests

import org.junit.runner.RunWith
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.InjectWith
import com.ngc.seaside.systemdescriptor.tests.util.ParseHelper
import com.google.inject.Inject
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test

import static org.junit.Assert.*
import com.ngc.seaside.systemdescriptor.systemDescriptor.Enumeration
import org.eclipse.xtext.junit4.util.ResourceHelper
import org.eclipse.emf.common.util.URI
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataModelFieldDeclaration

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class EnumParsingTest {

	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ValidationTestHelper validationTester
	
	@Inject
	ResourceHelper resourceHelper

    @Test
	def void testDoesParseEnumWithWhitespace() {
		val source = '''
			package clocks.datatypes
			
			enum TimeZone {
				CST EST MST PST
			}
		'''

		val result = parseHelper.parse(source)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		assertEquals(
			"package name not correct",
			"clocks.datatypes",
			result.name
		)

		var data = result.element
		assertTrue(
			"did not parse enum!",
			data instanceof Enumeration
		)
		
		var enumeration = result.element as Enumeration
		assertTrue(
			"missing enum value!",
			enumeration.values.stream().anyMatch(v | v.value == "CST" )
			
		)
		assertTrue(
			"missing enum value!",
			enumeration.values.stream().anyMatch(v | v.value == "EST" )
		)
		assertTrue(
			"missing enum value!",
			enumeration.values.stream().anyMatch(v | v.value == "MST" )
		)
		assertTrue(
			"missing enum value!",
			enumeration.values.stream().anyMatch(v | v.value == "PST" )
		)
	}

    @Test
	def void testDoesParseEnumWithNewlines() {
		val source = '''
			package clocks.datatypes
			
			enum TimeZone {
				CST
				EST
				MST
				PST
			}
		'''

		val result = parseHelper.parse(source)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		assertEquals(
			"package name not correct",
			"clocks.datatypes",
			result.name
		)

		var data = result.element
		assertTrue(
			"did not parse enum!",
			data instanceof Enumeration
		)
		
		var enumeration = result.element as Enumeration
		assertTrue(
			"missing enum value!",
			enumeration.values.stream().anyMatch(v | v.value == "CST" )
			
		)
		assertTrue(
			"missing enum value!",
			enumeration.values.stream().anyMatch(v | v.value == "EST" )
		)
		assertTrue(
			"missing enum value!",
			enumeration.values.stream().anyMatch(v | v.value == "MST" )
		)
		assertTrue(
			"missing enum value!",
			enumeration.values.stream().anyMatch(v | v.value == "PST" )
		)
	}

	@Test
	def void testDoesParseEnumWithComma() {
		val source = '''
			package clocks.datatypes
			
			enum TimeZone {
				CST,
				EST,
				MST,
				PST
			}
		'''

		val result = parseHelper.parse(source)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		assertEquals(
			"package name not correct",
			"clocks.datatypes",
			result.name
		)

		var data = result.element
		assertTrue(
			"did not parse enum!",
			data instanceof Enumeration
		)
		
		var enumeration = result.element as Enumeration
		assertTrue(
			"missing enum value!",
			enumeration.values.stream().anyMatch(v | v.value == "CST" )
			
		)
		assertTrue(
			"missing enum value!",
			enumeration.values.stream().anyMatch(v | v.value == "EST" )
		)
		assertTrue(
			"missing enum value!",
			enumeration.values.stream().anyMatch(v | v.value == "MST" )
		)
		assertTrue(
			"missing enum value!",
			enumeration.values.stream().anyMatch(v | v.value == "PST" )
		)
	}

	@Test
	def void testDoesParseEnumWithTrailingComma() {
		val source = '''
			package clocks.datatypes
			
			enum TimeZone {
				CST,
				EST,
				MST,
				PST,
			}
		'''

		val result = parseHelper.parse(source)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		assertEquals(
			"package name not correct",
			"clocks.datatypes",
			result.name
		)

		var data = result.element
		assertTrue(
			"did not parse enum!",
			data instanceof Enumeration
		)
		
		var enumeration = result.element as Enumeration
		assertTrue(
			"missing enum value!",
			enumeration.values.stream().anyMatch(v | v.value == "CST" )
			
		)
		assertTrue(
			"missing enum value!",
			enumeration.values.stream().anyMatch(v | v.value == "EST" )
		)
		assertTrue(
			"missing enum value!",
			enumeration.values.stream().anyMatch(v | v.value == "MST" )
		)
		assertTrue(
			"missing enum value!",
			enumeration.values.stream().anyMatch(v | v.value == "PST" )
		)
	}
	
	@Test
	def void testDoesAllowEnumTypesToBeReferencedInData() {
		val enumSource = '''
			package clocks.datatypes
			
			enum TimeZone {
				CST EST MST PST
			}
		''';
		
		val enumResource = resourceHelper.resource(enumSource, URI.createURI("datatypes.sd"))
		validationTester.assertNoIssues(enumResource)
		
		val dataSource = '''
			package clocks.otherdatatypes
			
			import clocks.datatypes.TimeZone
			
			data Time {
			  int hour
			  int min
			  int sec
			  TimeZone timeZone
			}
		'''
		
		val result = parseHelper.parse(dataSource, enumResource.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)	
		
		val data = result.element as Data
		assertEquals(
			"wrong number of fields!",
			4,
			data.fields.size
		)
		
		val field = data.fields.get(3)
		assertTrue(
			"enum field is not referencing a datamodel!",
			field instanceof ReferencedDataModelFieldDeclaration
		)
		
		val enumField = field as ReferencedDataModelFieldDeclaration
		assertTrue(
			"enumField dataModel should be an instance of an enumeration!",
			enumField.dataModel instanceof Enumeration
		)	
		assertEquals(
			"enum name not correct",
			"TimeZone",
			enumField.dataModel.name
		)	
	}
}
