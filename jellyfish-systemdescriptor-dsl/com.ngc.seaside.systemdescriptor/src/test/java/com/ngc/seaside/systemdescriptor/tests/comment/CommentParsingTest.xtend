package com.ngc.seaside.systemdescriptor.tests.comment

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class CommentParsingTest {

	@Inject
	ParseHelper<Package> parseHelper
	
	@Inject
	ValidationTestHelper validationTester

	@Test
	def void testDoesParseModelWithComments() {
		val source = '''
			// This is a package comment
			/* this is a package comment */
			/* this
			 * is
			 * a 
			 * comment
			 */
			-- This is another package comment
			package clocks.models
			
			// This is a model comment
			/* this is a model comment */
			/* this
			 * a
			 * comment
			 */
			-- This is another model comment
			model Timer {
			}
		'''

		val result = parseHelper.parse(source)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
	}

}
