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
