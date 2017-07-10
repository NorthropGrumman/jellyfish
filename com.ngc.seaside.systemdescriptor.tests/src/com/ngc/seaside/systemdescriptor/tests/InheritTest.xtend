package com.ngc.seaside.systemdescriptor.tests

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataType
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.eclipse.emf.common.util.URI
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.util.ResourceHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataFieldDeclaration
import org.eclipse.emf.ecore.resource.ResourceSet
import org.junit.Before
import com.google.inject.Provider
import org.eclipse.xtext.resource.XtextResourceSet

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class InheritTest {

	@Inject
	Provider<XtextResourceSet> resourceSetProvider;

	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ResourceHelper resourceHelper

	@Inject
	ValidationTestHelper validationTester

	ResourceSet resources

	@Before
	def void setup() {
		resources = resourceSetProvider.get()
	}
	
//	def void equalityFieldsTest(PrimitiveDataFieldDeclaration A, PrimitiveDataFieldDeclaration B) {
//		assertEquals(
//			"Data types do not match!",
//			A.type,
//			B.type
//		)
//		assertEquals(
//			"field names do not match!",
//			A.name,
//			B.name
//		)
//		assertEquals(
//			"Metadata does not match!",
//			A.metadata,
//			B.metadata
//		)	
//		assertEquals(
//			"Cardinality does not match!",
//			A.cardinality,
//			B.cardinality
//		)
//		
//	}

	@Test
	def void testInheritPrimitiveField() {
		
		val dataASource = '''
				package datathing.datatypes
				
				data A {
					int value
				}
			''';
		
		val dataAResult = parseHelper.parse(dataASource)
		assertNotNull(dataAResult)
		validationTester.assertNoIssues(dataAResult)
		
		var resourceA = resourceHelper.resource(dataASource, URI.createURI("A.sd"), resources);
		validationTester.assertNoIssues(resourceA)

		val dataBSource = '''
			package datathing.otherdatatypes
			
			import datathing.datatypes.A

			data B : A {
			}
		''';
		
		val dataBResult = parseHelper.parse(dataBSource, resources)
		assertNotNull(dataBResult)
		validationTester.assertNoIssues(dataBResult)
		
		//Test that b.value exists
		
		var dataA = dataAResult.element as Data
		var dataB = dataBResult.element as Data
		assertEquals(
			"data superclass not correct!",
			dataA.name,
			dataB.superclass.name// .superclass.name
		)
		
		var rawField = dataB.superclass.fields.get(0)		
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
//	
//	@Test
//	def void testInheritInstanceOf() {
//		
//		val dataASource = '''
//				package datathing.datatypes
//				
//				data A {
//					int value
//				}
//			''';
//		
//		val dataAResult = parseHelper.parse(dataASource)
//		assertNotNull(dataAResult)
//		validationTester.assertNoIssues(dataAResult)
//		
//		var resourceA = resourceHelper.resource(dataASource, URI.createURI("A.sd"), resources);
//		validationTester.assertNoIssues(resourceA)
//
//		val dataBSource = '''
//			package datathing.otherdatatypes
//			
//			import datathing.datatypes.A
//
//			data B : A {
//			}
//		''';
//		
//		val dataBResult = parseHelper.parse(dataBSource, resources)
//		assertNotNull(dataBResult)
//		validationTester.assertNoIssues(dataBResult)
//		
//		//Test that B.value == A.value
//		var dataA = dataAResult.element as Data
//		var dataB = dataBResult.element as Data
//		var dataAFieldValue = dataA.fields.get(0) as PrimitiveDataFieldDeclaration
//		var dataBFieldValue = dataB.superClass.data.fields.get(0) as PrimitiveDataFieldDeclaration
//		equalityFieldsTest(dataAFieldValue, dataBFieldValue); //A.val == B.val
//	}
//	
//	
//	@Test
//	def void testInheritFieldsAreEquivalent() {
//		val dataASource = '''
//				package datathing.datatypes
//				
//				data A {
//					int value {"some metadata" : "some value"}
//				}
//			''';
//		
//		val dataAResult = parseHelper.parse(dataASource)
//		assertNotNull(dataAResult)
//		validationTester.assertNoIssues(dataAResult)
//		
//		var resourceA = resourceHelper.resource(dataASource, URI.createURI("A.sd"), resources);
//		validationTester.assertNoIssues(resourceA)
//
//		val dataBSource = '''
//			package datathing.otherdatatypes
//			
//			import datathing.datatypes.A
//
//			data B : A {
//			}
//		''';
//		
//		val dataBResult = parseHelper.parse(dataBSource, resources)
//		assertNotNull(dataBResult)
//		validationTester.assertNoIssues(dataBResult)
//		
//		var dataA = dataAResult.element as Data
//		var dataB = dataBResult.element as Data
//		
//		var dataAFieldValue = dataA.fields.get(0) as PrimitiveDataFieldDeclaration
//		var dataBFieldValue = dataB.superClass.data.fields.get(0) as PrimitiveDataFieldDeclaration	
//
//		//Test that B.value == A.value
//		equalityFieldsTest(dataAFieldValue, dataBFieldValue);
//	}
//	
//	@Test
//	def void testInheritSuperSuper() {
//		
//		val dataASource = '''
//				package datathing.datatypes
//				
//				data A {
//					int value
//				}
//			''';
//		
//		val dataAResult = parseHelper.parse(dataASource, resources)
//		assertNotNull(dataAResult)
//		validationTester.assertNoIssues(dataAResult)
//		
//		var resourceA = resourceHelper.resource(dataASource, URI.createURI("A.sd"), resources);
//		validationTester.assertNoIssues(resourceA)
//
//		val dataBSource = '''
//			package datathing.otherdatatypes
//			
//			import datathing.datatypes.A
//
//			data B : A {
//			}
//		''';
//		
//		val dataBResult = parseHelper.parse(dataBSource)
//		assertNotNull(dataBResult)
//		validationTester.assertNoIssues(dataBResult)
//		
//		val dataCSource = '''
//			package datathing.otherdatatypes
//			
//			import datathing.datatypes.B
//
//			data C : B {
//			}
//		''';
//		
//		val dataCResult = parseHelper.parse(dataCSource, resources)
//		assertNotNull(dataCResult)
//		validationTester.assertNoIssues(dataCResult)
//		
//		//test that B.value == A.value, C.value == A.value, B.value == C.value, B == A, C == A, B =\= C
//		var dataA = dataAResult.element as Data
//		var dataB = dataBResult.element as Data
//		var dataC = dataCResult.element as Data
//		
//		var dataAFieldValue = dataA.fields.get(0) as PrimitiveDataFieldDeclaration
//		var dataBFieldValue = dataB.superClass.data.fields.get(0) as PrimitiveDataFieldDeclaration
//		var dataCAFieldValue = dataC.superClass.data.superClass.data.fields.get(0) as PrimitiveDataFieldDeclaration
//		var dataCBFieldValue = dataC.superClass.data.fields.get(0) as PrimitiveDataFieldDeclaration
//		
//		equalityFieldsTest(dataAFieldValue, dataBFieldValue); //A.val == B.val
//		equalityFieldsTest(dataBFieldValue, dataCBFieldValue); //B.val == C.val
//		equalityFieldsTest(dataAFieldValue, dataCAFieldValue); //A.val == C.val
//	}
//	
//	@Test
//	def void testInheritDataobjectFields() {
//		
//		val dateDateSource = '''
//			package datathing.datetypes
//			
//			data Date {
//				int day
//				int month
//				int year
//			}
//		''';
//		
//		val dataResource = resourceHelper.resource(dateDateSource, URI.createURI("datetypes.sd"))
//		validationTester.assertNoIssues(dataResource)
//		
//		val dataASource = '''
//			package datathing.datatypes
//			
//			import datathing.datetypes.Date
//			
//			data A {
//				Date date
//			}
//			''';
//		
//		val dataAResult = parseHelper.parse(dataASource, resources)
//		assertNotNull(dataAResult)
//		validationTester.assertNoIssues(dataAResult)
//		
//		var resourceA = resourceHelper.resource(dataASource, URI.createURI("A.sd"), resources);
//		validationTester.assertNoIssues(resourceA)
//
//		val dataBSource = '''
//			package datathing.otherdatatypes
//			
//			import datathing.datatypes.A
//
//			data B : A {
//			}
//		''';
//		
//		val dataBResult = parseHelper.parse(dataBSource, resources)
//		assertNotNull(dataBResult)
//		validationTester.assertNoIssues(dataBResult)
//		
//		var dataA = dataAResult.element as Data
//		var dataB = dataBResult.element as Data
//		
//		var dataAFieldValue = dataA.fields.get(0) as ReferencedDataFieldDeclaration
//		var dataBFieldValue = dataB.fields.get(0) as ReferencedDataFieldDeclaration	
//
//		//Test that B.value == A.value
//		assertEquals(
//			"Data does not match!",
//			dataAFieldValue.data,
//			dataBFieldValue.data
//		)
//		assertEquals(
//			"field names do not match!",
//			dataAFieldValue.name,
//			dataBFieldValue.name
//		)
//		assertEquals(
//			"Data names do not match!",
//			dataAFieldValue.data.name,
//			dataBFieldValue.data.name
//		)
//		assertTrue(
//			"Fields are not equal!",
//			dataAFieldValue.equals(dataBFieldValue)
//		)		
//		//Asserts and testing...
//		//Test that data composed of other data objects is inherited.	
//	}
//	
//	@Test
//	def void testWarnBaseInheritedClassInput() {
//
//		val dataASource = '''
//			package datathing.datatypes
//			
//			import datathing.datetypes.Date
//			
//			data A {
//				Date date
//			}
//			''';
//		
//		val dataAResult = parseHelper.parse(dataASource)
//		assertNotNull(dataAResult)
//		validationTester.assertNoIssues(dataAResult)
//		
//		var resourceA = resourceHelper.resource(dataASource, URI.createURI("A.sd"), resources);
//		validationTester.assertNoIssues(resourceA)
//		
//		val dataBSource = '''
//				package datathing.otherdatatypes
//				
//				import datathing.datatypes.A
//				
//				data B : A {
//				}
//			''';
//		
//		val dataBResult = parseHelper.parse(dataBSource, resources)
//		assertNotNull(dataBResult)
//		validationTester.assertNoIssues(dataBResult)
//		
//		var resourceB = resourceHelper.resource(dataASource, URI.createURI("B.sd"), resources);
//		validationTester.assertNoIssues(resourceB)
//		
//		val modelSource = '''
//			package data.models
//			
//			import data.datatypes
//			
//			model ModelA {
//			  input {
//			    A a {}
//			  }
//		'''
//
//		val result = parseHelper.parse(modelSource, resources)
//		assertNotNull(result)
//		
//		validationTester.assertWarning(
//			result,
//			SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION,
//			null);		
//	}
//	
//	@Test
//	def void testWarnBaseInheritedClassOutput() {
//	var resourceA = resourceHelper.resource(
//			'''
//				package data.datatypes
//				
//				data A {
//					int value
//				}
//			''',
//			URI.createURI("A.sd"),
//			resources
//		)
//		validationTester.assertNoIssues(resourceA)
//		
//		var resourceB = resourceHelper.resource(
//			'''
//				package data.otherdatatypes
//				
//				import data.datatypes.A
//				
//				data B : A {
//				}
//			''',
//			URI.createURI("B.sd"),
//			resources
//		)
//		validationTester.assertNoIssues(resourceB)
//		
//		val modelSource = '''
//			package data.models
//			
//			import data.datatypes
//			
//			model ModelA {
//			  output {
//			    A a {}
//			  }
//		'''
//
//		val result = parseHelper.parse(modelSource, resources)
//		assertNotNull(result)
//		
//		validationTester.assertWarning(
//			result,
//			SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION,
//			null);	
//		//Asserts and testing...
//		//Test a warning is given when a base class is used in the input, output, scenario, etc.
//		
//	}
//	
//	@Test
//	def void testWarnBaseInheritedClassLink() {
//	var resourceA = resourceHelper.resource(
//			'''
//				package data.datatypes
//				
//				data A {
//					int value
//				}
//			''',
//			URI.createURI("A.sd"),
//			resources
//		)
//		validationTester.assertNoIssues(resourceA)
//		
//		var resourceB = resourceHelper.resource(
//			'''
//				package data.otherdatatypes
//				
//				import data.datatypes.A
//				
//				data B : A {
//				}
//			''',
//			URI.createURI("B.sd"),
//			resources
//		)
//		validationTester.assertNoIssues(resourceB)
//		
//		val modelSource = '''
//			package data.models
//			
//			import data.datatypes.A
//			
//			model ModelA {
//			  links {
//			    A a {}
//			  }
//		'''
//
//		val result = parseHelper.parse(modelSource, resources)
//		assertNotNull(result)
//		
//		validationTester.assertWarning(
//			result,
//			SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION,
//			null);	
//		//Asserts and testing...
//		//Test a warning is given when a base class is used in the input, output, scenario, etc.
//		
//	}
//	
//	@Test
//	def void testWarnBaseInheritedClassScenario() {
//	var resourceA = resourceHelper.resource(
//			'''
//				package data.datatypes
//				
//				data A {
//					int value
//				}
//			''',
//			URI.createURI("A.sd"),
//			resources
//		)
//		validationTester.assertNoIssues(resourceA)
//		
//		var resourceB = resourceHelper.resource(
//			'''
//				package data.otherdatatypes
//				
//				import data.datatypes.A
//				
//				data B : A {
//				}
//			''',
//			URI.createURI("B.sd"),
//			resources
//		)
//		validationTester.assertNoIssues(resourceB)
//		
//		val modelSource = '''
//			package data.models
//			
//			import data.datatypes
//			
//			model ModelA {
//			scenario A a {}
//		'''
//
//		val result = parseHelper.parse(modelSource, resources)
//		assertNotNull(result)
//		
//		validationTester.assertWarning(
//			result,
//			SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION,
//			null);	
//		//Asserts and testing...
//		//Test a warning is given when a base class is used in the input, output, scenario, etc.	
//	}
}
