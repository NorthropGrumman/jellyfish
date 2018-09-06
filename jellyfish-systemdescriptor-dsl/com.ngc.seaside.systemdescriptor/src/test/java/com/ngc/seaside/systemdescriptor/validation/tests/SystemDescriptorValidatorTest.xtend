/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
