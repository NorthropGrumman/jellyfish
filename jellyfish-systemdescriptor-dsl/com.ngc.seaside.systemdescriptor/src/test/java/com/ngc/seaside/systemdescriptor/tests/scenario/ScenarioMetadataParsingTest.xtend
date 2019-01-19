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
package com.ngc.seaside.systemdescriptor.tests.scenario

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.StringValue
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import com.ngc.seaside.systemdescriptor.tests.resources.Models
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.util.ResourceHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class ScenarioMetadataParsingTest {

	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ResourceHelper resourceHelper

	@Inject
	ValidationTestHelper validationTester
	
	Resource requiredResources

	@Before
	def void setup() {
		requiredResources = Models.allOf(
			resourceHelper,
			Models.SPEAKER.requiredResources
		)
	}
	
	@Test
	def void testDoesParseScenarioWithMetadata() {
		val result = parseHelper.parse(Models.SPEAKER.source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		assertFalse(
			"did not parse scenario!",
			model.scenarios.empty
		)
		
		val scenario = model.scenarios.get(0)	
		var metadata = scenario.metadata
		val firstmember = metadata.json.members.get(0)

		assertEquals(
			"metadata did not parse!",
			"name",
			firstmember.key
		)
		assertEquals(
			"metadata did not parse!",
			"someName",
			(firstmember.value as StringValue).value
		)
	}
}
