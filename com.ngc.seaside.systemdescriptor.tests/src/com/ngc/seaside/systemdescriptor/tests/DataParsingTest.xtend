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
	def void testDoesParseEmptyData() {
		val source = '''
		  package my.foo.test
		  
		  data Time {
		  }
		''';
		
		val result = parseHelper.parse(source)
		assertNotNull(result)
		validationTester.assertNoIssues(result);
	}
	
	@Test 
	def void testDoesParseData() {
		val source = '''
		  package my.foo.test
		  
		  data Time {
		  	int hour
		  	int minute
		  	int second
		  }
		''';
		
		val result = parseHelper.parse(source)
		assertNotNull(result)
		validationTester.assertNoIssues(result);
	}
	
	@Test 
	def void testDoesParseDataWithMetadataValidation() {
		val source = '''
		  package my.foo.test
		  
		  data Time {
		  	metadata {
		  	    "name": "Time",
		  	    "description": "Represents a local time (does not account for timezones)."
		  	}
		  	
		  	int hour {
		  		"validation": {
		  			"min": "0",
		  			"max": "23"
		  		}
		  	}
		  	
		  	int minute {
		  		"validation": {
		  			"min": "0",
		  			"max": "59"
		  		}
		  	}
		  		
		  	int second {
		  		"validation": {
		  			"min": "0",
		  			"max": "59"
		  		}
		  	}
		  }
		''';
		
		val result = parseHelper.parse(source)
		assertNotNull(result)
		validationTester.assertNoIssues(result);
	}
}