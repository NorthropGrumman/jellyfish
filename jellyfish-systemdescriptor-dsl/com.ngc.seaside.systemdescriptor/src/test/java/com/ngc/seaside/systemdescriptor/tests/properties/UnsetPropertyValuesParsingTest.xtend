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
package com.ngc.seaside.systemdescriptor.tests.properties

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import com.ngc.seaside.systemdescriptor.tests.resources.Datas
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

import static com.ngc.seaside.systemdescriptor.tests.resources.ParsingTestResource.resource
import static org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class UnsetPropertyValuesParsingTest {

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
			Datas.DATE,
			Datas.TIME,
			Datas.TIME_ZONE,
			Datas.TIME_CONVENTION,
			Datas.ZONED_TIME,
			Models.CLOCK
		)
		validationTester.assertNoIssues(requiredResources)
	}

	@Test
	def void testDoesFailForUnsetModelProperties() {
		val base = '''
			package clocks.models
			
			import clocks.datatypes.Date
			
			model Base {
				properties {
					Date date
				}
			}
		'''
		val refined = '''
			package clocks.models
			
			import clocks.models.Base
			
			model Refined refines Base {
				properties {
					date.day = 1
				}
			}
		'''
		val baseSource = resource(base, Datas.DATE)

		var invalidResult = parseHelper.parse(refined, Models.allOf(resourceHelper, baseSource).resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.MODEL,
			null
		)
	}

	@Test
	def void testDoesFailForUnsetPartProperties() {
		val base = '''
			package clocks.models
			
			import clocks.models.part.Clock
			import clocks.datatypes.Date
			
			model Base {
				parts {
					Clock clock {
						properties {
							Date date
						}
					}
				}
			}
		'''
		val refined = '''
			package clocks.models
			
			import clocks.models.Base
			
			model Refined refines Base {
				parts {
					refine clock {
						properties {
							date.day = 1
						}
					}
				}
			}
		'''
		val baseSource = resource(base, Datas.DATE, Models.CLOCK)

		var invalidResult = parseHelper.parse(refined, Models.allOf(resourceHelper, baseSource).resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.MODEL,
			null
		)
	}

	@Test
	def void testDoesFailForUnsetRequiresProperties() {
		val base = '''
			package clocks.models
			
			import clocks.models.part.Clock
			import clocks.datatypes.Date
			
			model Base {
				requires {
					Clock clock {
						properties {
							Date date
						}
					}
				}
			}
		'''
		val refined = '''
			package clocks.models
			
			import clocks.models.Base
			
			model Refined refines Base {
				requires {
					refine clock {
						properties {
							date.day = 1
						}
					}
				}
			}
		'''
		val baseSource = resource(base, Datas.DATE, Models.CLOCK)

		var invalidResult = parseHelper.parse(refined, Models.allOf(resourceHelper, baseSource).resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.MODEL,
			null
		)
	}

	@Test
	def void testDoesFailForUnsetLinkProperties() {
		val base = '''
			package clocks.models
			
			import clocks.models.part.Clock
			import clocks.datatypes.Date
			import clocks.datatypes.ZonedTime
			
			model Base {
				input {
					ZonedTime time
				}
				requires {
					Clock clock
				}
				links {
					link name time -> clock.currentTime {
						properties {
							Date date
						}
					}
				}
			}
		'''
		val refined = '''
			package clocks.models
			
			import clocks.models.Base
			
			model Refined refines Base {
				links {
					refine name {
						properties {
							date.day = 1
						}
					}
				}
			}
		'''
		val baseSource = resource(base, Datas.DATE, Datas.ZONED_TIME, Models.CLOCK)

		var invalidResult = parseHelper.parse(refined, Models.allOf(resourceHelper, baseSource).resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.MODEL,
			null
		)
	}

}
