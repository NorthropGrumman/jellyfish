package com.ngc.seaside.systemdescriptor.tests

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataType
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.eclipse.emf.common.util.URI
import org.eclipse.xtext.diagnostics.Diagnostic
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.util.ResourceHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*
import com.ngc.seaside.systemdescriptor.systemDescriptor.JsonValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.IntValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.StringValue
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataFieldDeclaration

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class InheritTest {

	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ResourceHelper resourceHelper

	@Inject
	ValidationTestHelper validationTester

	@Test
	def void testInheritPrimitiveField() {
		val dataASource = '''
			package data.datatypes
			
			data A {
				int value
			}
		''';
		
		val dataAResult = parseHelper.parse(dataASource)
		assertNotNull(dataAResult)
		validationTester.assertNoIssues(dataAResult)

		val dataBSource = '''
			package data.otherdatatypes
			
			import data.datatypes.A

			data B : A {
			}
		''';
		
		val dataBResult = parseHelper.parse(dataBSource)
		assertNotNull(dataBResult)
		validationTester.assertNoIssues(dataBResult)
		
		//Test that b.value exists
		
		var dataB = dataBResult.element as Data
		assertEquals(
			"wrong number of fields!",
			1,
			dataB.fields.size 
		)
		
		var rawField = dataB.fields.get(0)		
		assertTrue(rawField instanceof PrimitiveDataFieldDeclaration)
		var field = rawField as PrimitiveDataFieldDeclaration		
		
		assertEquals(
			"data type not correct!",
			PrimitiveDataType.INT,
			field.type
		)
		assertEquals(
			"field name not correct!",
			"value",
			field.name
		)	
	}
	
	@Test
	def void testInheritInstanceOf() {
		val dataASource = '''
			package data.datatypes
			
			data A {
				int value
			}
		''';
		
		val dataAResult = parseHelper.parse(dataASource)
		assertNotNull(dataAResult)
		validationTester.assertNoIssues(dataAResult)

		val dataBSource = '''
			package data.otherdatatypes
			
			import data.datatypes.A

			data B : A {
			}
		''';
		
		val dataBResult = parseHelper.parse(dataBSource)
		assertNotNull(dataBResult)
		validationTester.assertNoIssues(dataBResult)
		
		//Test that B.value == A.value
		assertTrue("B is not an instance of A", dataBResult.equals(dataAResult))
	}
	
	
	@Test
	def void testInheritFieldsAreEquivalent() {
				val dataASource = '''
			package data.datatypes
			
			data A {
				int value
			}
		''';
		
		val dataAResult = parseHelper.parse(dataASource)
		assertNotNull(dataAResult)
		validationTester.assertNoIssues(dataAResult)

		val dataBSource = '''
			package data.otherdatatypes
			
			import data.datatypes.A

			data B : A {
			}
		''';
		
		val dataBResult = parseHelper.parse(dataBSource)
		assertNotNull(dataBResult)
		validationTester.assertNoIssues(dataBResult)
		
		var dataA = dataAResult.element as Data
		var dataB = dataBResult.element as Data
		
		var dataAFieldValue = dataA.fields.get(0) as PrimitiveDataFieldDeclaration
		var dataBFieldValue = dataB.fields.get(0) as PrimitiveDataFieldDeclaration	

		//Test that B.value == A.value
		assertEquals(
			"Data types do not match!",
			dataAFieldValue.type,
			dataBFieldValue.type
		)
		assertEquals(
			"field names do not match!",
			dataAFieldValue.type,
			dataBFieldValue.type
		)
		assertTrue(
			"Fields are not equal!",
			dataAFieldValue.equals(dataBFieldValue)
		)		
	}
	
	@Test
	def void testInheritSubclassNotEquivalent() {
		
		val dataASource = '''
			package data.datatypes
			
			data A {
				int value
			}
		''';
		
		val dataAResult = parseHelper.parse(dataASource)
		assertNotNull(dataAResult)
		validationTester.assertNoIssues(dataAResult)

		val dataBSource = '''
			package data.otherdatatypes
			
			import data.datatypes.A

			data B : A {
			}
		''';
		
		val dataBResult = parseHelper.parse(dataBSource)
		assertNotNull(dataBResult)
		validationTester.assertNoIssues(dataBResult)
		
		val dataCSource = '''
			package data.otherdatatypes
			
			import data.datatypes.A

			data C : A {
			}
		''';
		
		val dataCResult = parseHelper.parse(dataCSource)
		assertNotNull(dataCResult)
		validationTester.assertNoIssues(dataCResult)
		
		//test that B.value == A.value, C.value == A.value, B.value == C.value, B == A, C == A, B =\= C
		assertTrue("B is not an instance of A", dataBResult.equals(dataAResult))
		assertTrue("C is not an instance of A", dataCResult.equals(dataAResult))
		assertTrue("B is an instance of C", dataBResult.equals(dataCResult))
		assertTrue("C is an instance of B", dataCResult.equals(dataBResult))	
	}
	
	@Test
	def void testInheritCorrectFields() {
		//TODO
		//Asserts and testing...
		//Test that data composed of other data objects is inherited.
	}
	
	@Test
	def void testWarnBaseClassInput() {
		//TODO
		//Asserts and testing...
		//Test a warning is given when a base class is used in the input, output, scenario, etc.
		
	}
	
	@Test
	def void testWarnBaseClassOutput() {
		//TODO
		//Asserts and testing...
		//Test a warning is given when a base class is used in the input, output, scenario, etc.
		
	}
	
	@Test
	def void testWarnBaseClassLink() {
		//TODO
		//Asserts and testing...
		//Test a warning is given when a base class is used in the input, output, scenario, etc.
		
	}
	
	@Test
	def void testWarnBaseClassScenario() {
		//TODO
		//Asserts and testing...
		//Test a warning is given when a base class is used in the input, output, scenario, etc.
		
	}
}
