package com.ngc.seaside.systemdescriptor.validation.tests

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.^extension.IValidatorExtension
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import com.ngc.seaside.systemdescriptor.tests.util.ParseHelper
import com.ngc.seaside.systemdescriptor.validation.SystemDescriptorValidator
import java.util.ArrayList
import java.util.Collection
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class SystemDescriptorValidatorTest {

	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ValidationTestHelper validationTester
	
	@Inject
	SystemDescriptorValidator validator
	
	MockedValidator additionalValidator

	@Before
	def public void setup() {
		additionalValidator = new MockedValidator()
		validator.addValidatorExtension(additionalValidator)
	}

	@Test
	def void testDoesInvokedAdditionalValidator() {
		val source = '''
			package clocks.datatypes
			
			data Time {
				int hour
				int minute
				int second
			}
		'''

		val result = parseHelper.parse(source)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
		var data = result.element as Data

		assertTrue(
			"did not invoke validator!",
			additionalValidator.wasValidated(data)
		)
	}
	
	@Test
	def void testDoesAllowAdditionalValidatorToDeclareErrors() {
		val source = '''
			package clocks.datatypes
			
			data Time {
				int hour
				int minute
				int second
			}
		'''

		additionalValidator.errorWhenFieldNamed("second")

		val invalidResult = parseHelper.parse(source)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION,
			null
		)
	}
	
	@Test
	def void testDoesAllowAdditionalValidatorToDeclareWarnings() {
		val source = '''
			package clocks.datatypes
			
			data Time {
				int hour
				int minute
				int second
			}
		'''

		additionalValidator.warnWhenFieldNamed("second")

		val invalidResult = parseHelper.parse(source)
		assertNotNull(invalidResult)
		validationTester.assertWarning(
			invalidResult,
			SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION,
			null
		)
	}
	
	@After
	def public void teardown() {
		if(additionalValidator != null) {
			validator.removeValidatorExtension(additionalValidator)
		}
	}

	private static class MockedValidator implements IValidatorExtension {

		private final Collection<EObject> validatedObjects = new ArrayList()
		private String errorWhenFieldNamed = null;
		private String warnWhenFieldNamed = null;

		override validate(EObject source, ValidationHelper helper) {
			validatedObjects.add(source);
			if(source instanceof DataFieldDeclaration) {
				val field = source as DataFieldDeclaration
				if(field.name.equals(errorWhenFieldNamed)) {
					helper.error(
					"trying mocked validator",
						field,
						SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION__NAME
					)
				}
				if(field.name.equals(warnWhenFieldNamed)) {
					helper.warning(
					"trying mocked validator",
						field,
						SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION__NAME
					)
				}
			}
		}

		def private boolean wasValidated(EObject object) {
			return validatedObjects.contains(object)
		}
		
		def private MockedValidator errorWhenFieldNamed(String name) {
			this.errorWhenFieldNamed = name;
			return this
		}
		
		def private MockedValidator warnWhenFieldNamed(String name) {
			this.warnWhenFieldNamed = name;
			return this
		}
	}

}
