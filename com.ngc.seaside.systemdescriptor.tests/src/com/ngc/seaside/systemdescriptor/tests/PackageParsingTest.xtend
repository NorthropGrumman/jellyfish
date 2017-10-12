package com.ngc.seaside.systemdescriptor.tests

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class PackageParsingTest {

	@Inject
	ParseHelper<Package> parseHelper


	@Inject
	ValidationTestHelper validationTester
	
	
	@Test
	def void testDoesNotAllowPackageNameKeywords() {
		val source = '''
			package ^foo.datatypes
			
			data Foo {
				int i
				float f
			}

		'''

		val invalidResult = parseHelper.parse(source)
		assertNotNull(invalidResult)
		
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PACKAGE,
			null
		)
	}	
}