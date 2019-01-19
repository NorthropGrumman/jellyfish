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
package com.ngc.seaside.systemdescriptor.tests.properties

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
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

import static org.junit.Assert.*
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.junit.Ignore

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class ModelLinkPropertiesParsingTest {

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
			Models.CLOCK,
			Models.LINKED_CLOCK,
			Datas.ZONED_TIME
		)
		validationTester.assertNoIssues(requiredResources)
	}

	@Test
	def void testDoesParseModelWithLinkProperties() {
		val source = '''
			package clocks.models.part
			
			import clocks.datatypes.ZonedTime
			import clocks.models.part.Clock
			
			model LinkedClock {
			
				input {
					ZonedTime currentTime
				}
			
				parts {
				Clock clock
				Clock clockA
				}
			
				links {
				link namedLink currentTime -> clock.inputTime {
					properties {
						int intField
					}
				}
				link currentTime -> clockA.inputTime {
					properties {
						string stringField
					}
				}
				}
			}
		'''

		val result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		var properties = model.links.declarations.get(0).definition.properties
		assertNotNull("did not parse properties", properties)

		val declaration = properties.declarations.get(0)
		assertEquals("property name not correct", "intField", declaration.name)

		val refinedProperties = model.links.declarations.get(1).definition.properties
		assertNotNull("did not parse properties", refinedProperties)

		val refinedDeclaration = refinedProperties.declarations.get(0)
		assertEquals("property name not correct", "stringField", refinedDeclaration.name)
	}

	@Test
	def void testDoesParseModelWithRefinedLinkProperties() {
		val source = '''
			package clocks.models.part
			
			import clocks.models.part.LinkedClock
			
			model BigClock refines LinkedClock {
				
				parts {
					refine clockA {
					}
				}
			
				links {
					refine namedLink {
						properties {
							int intField
						}
					}
					refine link currentTime -> clockA.inputTime {
						properties {
							string stringField
						}
					}
				}
			}
		'''

		val result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		var properties = model.links.declarations.get(0).definition.properties
		assertNotNull("did not parse properties", properties)

		val declaration = properties.declarations.get(0)
		assertEquals("property name not correct", "intField", declaration.name)

		val refinedProperties = model.links.declarations.get(1).definition.properties
		assertNotNull("did not parse properties", refinedProperties)

		val refinedDeclaration = refinedProperties.declarations.get(0)
		assertEquals("property name not correct", "stringField", refinedDeclaration.name)
	}

	@Test
	def void testDoesParseModelWithRefinedLinkWithNamePropertiesWithValueSet() {
		val source = '''
			package clocks.models.part
			
			import clocks.models.part.LinkedClock
			
			model BigClock refines LinkedClock {
			
			    links {
			        refine propNamedLink {
			            properties {
			                 intLinkedClockField = 1
			            }
			        }
			    }
			}
		'''

		val result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
	}

	@Test
	@Ignore("Bug not fixed; see SystemDescriptorScopeProvider line 273")
	def void testDoesParseModelWithRefinedLinkWithNoNamePropertiesWithValueSet() {
		val source = '''
			package clocks.models.part
			
			import clocks.models.part.LinkedClock
			
			model BigClock refines LinkedClock {
			
			    links {
			        refine link currentTime -> clockA.inputTime {
			        	properties {
			        		anotherIntField = 2
			        	}
			        }
			    }
			}
		'''

		val result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
	}

	@Test
	def void testDoesParseModelWithLinkPropertiesWithValueSet() {
		val source = '''
			package clocks.models.part
			
			import clocks.datatypes.ZonedTime
			import clocks.models.part.Clock
			
			model BigClock {
			    input {
			        ZonedTime currentTime
			    }
			    
			    parts {
			        Clock clock
			    }
			
			    links {
			    link namedLink currentTime -> clock.inputTime {
			        properties {
			            int intField
			                intField = 100
			        }
			    }
			    }
			}
		'''

		val result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
	}

	@Test
	def void testDoesParseModelWithLinkPropertiesWithOverWritenValueSet() {
		val source = '''
			package clocks.models.part
			
			import clocks.models.part.LinkedClock
			
			model AnotherLinkedClock refines LinkedClock {
			
			    links {
			        refine valueNamedLink {
			            properties {
			                intValueClockField = 200 
			            }
			        }
			    }
			}
		'''

		val result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
	}

	@Test
	def void testDoesNotParseModelWithLinkPropertiesWithRedifinedProperty() {
		val source = '''
			package clocks.models.part
			
			import clocks.models.part.LinkedClock
			
			model AnotherLinkedClock refines LinkedClock {
			
			    links {
			        refine valueNamedLink {
			            properties {
			                int intValueClockField
			            }
			        }
			    }
			}
		'''

		val invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
			invalidResult,
			SystemDescriptorPackage.Literals.PROPERTIES,
			null
		)
	}

}
