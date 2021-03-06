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
