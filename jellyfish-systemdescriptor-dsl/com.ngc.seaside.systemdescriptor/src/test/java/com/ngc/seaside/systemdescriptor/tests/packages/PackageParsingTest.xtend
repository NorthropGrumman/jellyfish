/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.systemdescriptor.tests.packages

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*
import org.eclipse.xtext.testing.util.ResourceHelper
import org.eclipse.emf.common.util.URI
import com.ngc.seaside.systemdescriptor.validation.SdIssueCodes
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class PackageParsingTest {

	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ResourceHelper resourceHelper

	@Inject
	ValidationTestHelper validationTester

	@Test
	def void testDoesNotAllowEscapingPackageNames() {
		var source = '''
			package ^foo.datatypes
			
			data Foo {
				int i
				float f
			}
			
		'''

		var invalidResult = parseHelper.parse(source)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PACKAGE,
			null
		)

		source = '''
			package foo.^datatypes
			
			data Foo {
				int i
				float f
			}
			
		'''

		invalidResult = parseHelper.parse(source)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PACKAGE,
			null
		)
	}

	@Test
	def void testDoesRequirePackageAndFilePathToMatch() {
		val dateSourceValid = '''
			package com.ngc.seaside.common
			
			data Date {
				int day
				int month
				int year
			}
		''';

		val validDataResource = resourceHelper.resource(
			dateSourceValid,
			URI.createPlatformResourceURI("src/main/sd/com/ngc/seaside/common/datatypes.sd", false)
		)
		validationTester.assertNoIssues(validDataResource)

		val dateSourceInvalid1 = '''
			package com.ngc.seaside
			
			data Date {
				int day
				int month
				int year
			}
		''';

		val invalidDataResource1 = resourceHelper.resource(
			dateSourceInvalid1,
			URI.createPlatformResourceURI("src/main/sd/com/ngc/seaside/common/datatypes.sd", false)
		)
		validationTester.assertError(
			invalidDataResource1,
			SystemDescriptorPackage.Literals.PACKAGE,
			SdIssueCodes.MISMATCHED_PACKAGE
		)

		val dateSourceInvalid2 = '''
			package foo
			
			data Date {
				int day
				int month
				int year
			}
		''';

		val invalidDataResource2 = resourceHelper.resource(
			dateSourceInvalid2,
			URI.createPlatformResourceURI("src/main/sd/Foo.sd", false)
		)
		validationTester.assertError(
			invalidDataResource2,
			SystemDescriptorPackage.Literals.PACKAGE,
			SdIssueCodes.MISMATCHED_PACKAGE
		)
	}
}
