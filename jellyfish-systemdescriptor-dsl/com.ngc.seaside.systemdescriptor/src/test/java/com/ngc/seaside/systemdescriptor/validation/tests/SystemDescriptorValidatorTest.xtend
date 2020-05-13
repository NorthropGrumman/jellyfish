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
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import org.eclipse.xtext.testing.validation.ValidationTestHelper
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
	def void setup() {
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
	def void teardown() {
		if(additionalValidator !== null) {
			validator.removeValidatorExtension(additionalValidator)
		}
	}

	private static class MockedValidator implements IValidatorExtension {

		final Collection<EObject> validatedObjects = new ArrayList()
		String errorWhenFieldNamed = null;
		String warnWhenFieldNamed = null;

		override validate(EObject source, ValidationHelper helper) {
			validatedObjects.add(source);
			if(source instanceof DataFieldDeclaration) {
				if(source.name.equals(errorWhenFieldNamed)) {
					helper.error(
					"trying mocked validator",
						source,
						SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION__NAME
					)
				}
				if(source.name.equals(warnWhenFieldNamed)) {
					helper.warning(
					"trying mocked validator",
						source,
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
