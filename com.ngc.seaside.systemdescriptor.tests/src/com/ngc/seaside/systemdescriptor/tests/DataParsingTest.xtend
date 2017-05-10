package com.ngc.seaside.systemdescriptor.tests

import com.google.inject.Inject
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.junit.Test
import org.junit.runner.RunWith
import org.eclipse.xtext.junit4.validation.ValidationTestHelper

import static org.junit.Assert.*
import com.ngc.seaside.systemdescriptor.systemDescriptor.Descriptor

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class DataParsingTest {
	
	@Inject
	ParseHelper<Descriptor> parseHelper
	
	@Inject
    ValidationTestHelper validationTester
	
	@Test 
	def void testDoesParseData() {
		val source = '''
		  package my.foo.test
		  
		  data HelloWorld {
		  }
		''';
		
		val result = parseHelper.parse(source)
		assertNotNull(result)
		validationTester.assertNoIssues(result);
	}
}