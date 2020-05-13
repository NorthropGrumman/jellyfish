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

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class ModelFieldPropertiesParsingTest {

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
			Models.WIRED_SPEAKER,
			Datas.ZONED_TIME
		)
		validationTester.assertNoIssues(requiredResources)
	}

	@Test
	def void testDoesParseModelWithPartProperties() {
		val source = '''
			package clocks.models

			import clocks.models.part.Clock
			import clocks.models.part.LinkedClock

			model BigClock refines LinkedClock {
				parts {
					Clock part {
						properties {
							int intField
						}
					}
					refine clock {
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
		var properties = model.parts.declarations.get(0).definition.properties
		assertNotNull("did not parse properties", properties)

		val declaration = properties.declarations.get(0)
		assertEquals("property name not correct", "intField", declaration.name)

		val refinedProperties = model.parts.declarations.get(1).definition.properties
		assertNotNull("did not parse properties", refinedProperties)

		val refinedDeclaration = refinedProperties.declarations.get(0)
		assertEquals("property name not correct", "stringField", refinedDeclaration.name)
	}

	@Test
	def void testDoesParseModelWithRequiresProperties() {
		val source = '''
			package clocks.models

			import clocks.models.part.Clock

			model BigClock {
				requires {
					Clock clock {
						properties {
							int intField 
							intField = 20
						}
					}
				}
			}
		'''

		val result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		var properties = model.requires.declarations.get(0).definition.properties
		assertNotNull("did not parse properties", properties)

		val declaration = properties.declarations.get(0)
		assertEquals("property name not correct", "intField", declaration.name)
	}

    @Test
    def void testDoesParseModelWithRequiresPropertiesBeingDeclaredAndAssigned() {
        val source = '''
            package clocks.models
            
            import clocks.models.part.LinkedClock
            
            model BigClock refines LinkedClock{
                requires {
                   refine reqClock {
                        properties {
                            int intRequiresField
                            intRequiresField = 100
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
    def void testDoesParseModelWithPartsPropertiesBeingDeclaredAndAssigned() {
        val source = '''
            package clocks.models
            
            import clocks.models.part.LinkedClock
            
            model BigClock refines LinkedClock{
                parts {
                   refine clockD {
                        properties {
                        int intMyPartsField
                        intMyPartsField = 100
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
	def void testDoesParseModelWithRefinedRequiresProperties() {
		val source = '''
			package clocks.models

			import clocks.models.part.LinkedClock

			model BigClock refines LinkedClock {
				requires {
					refine requiresEmptyModel {
						properties {
							int intField
						}
					}
				}
			}
		'''

		val result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)

		val model = result.element as Model
		var properties = model.requires.declarations.get(0).definition.properties
		assertNotNull("did not parse properties", properties)

		val declaration = properties.declarations.get(0)
		assertEquals("property name not correct", "intField", declaration.name)
	}
	
	@Test
    def void testDoesParseModelWithRefinedRequiresPropertiesSet() {
        val source = '''
            package clocks.models
            
            import clocks.models.part.LinkedClock
            
            model BigClock refines LinkedClock {
                requires {
                    refine reqClock {
                        properties {
                            intRequiredField = 1
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
    def void testDoesParseModelWithRefinedPartsPropertiesSet() {
        val source = '''
            package clocks.models
            
            import clocks.models.part.LinkedClock
            
            model BigClock refines LinkedClock {
                parts {
                    refine clockD {
                        properties {
                            intPartsField = 100
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
    def void testDoesParseModelWithPartPropertiesDirectlyFromModel() {
    	val source = '''
            package clocks.models
            
            import clocks.models.part.WiredSpeaker
            
            model MyModel {
                parts {
                    WiredSpeaker speaker {
                    	properties {
                    		wireGauge = 19
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
    def void testDoesParseModelWithRequirementPropertiesDirectlyFromModel() {
    	val source = '''
            package clocks.models
            
            import clocks.models.part.WiredSpeaker
            
            model MyModel {
                requires {
                    WiredSpeaker speaker {
                    	properties {
                    		wireGauge = 19
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
	def void testDoesNotParseModelWithInputProperties() {
		val source = '''
			package clocks.models

			import clocks.datatypes.ZonedTime

			model BigClock {
				input {
					ZonedTime input1 {
						properties {}
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

	@Test
	def void testDoesNotParseModelWithOutputProperties() {
		val source = '''
			package clocks.models

			import clocks.datatypes.ZonedTime

			model BigClock {
				output {
					ZonedTime output1 {
						properties {}
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

	@Test
	def void testDoesNotParseDataWithProperties() {
		val source = '''
			package clocks.models

			data Data {
				int field {
					properties {}
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
	
	@Test
    def void testDoesNotParsePartsWithRedifnedPropety() {
        val source = '''
            package clocks.models
                        
            import clocks.models.part.LinkedClock
            
            model BigClock refines LinkedClock {
                parts {
                    refine clockD {
                        properties {
                            int intPartsField
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
    
    @Test
    def void testDoesNotParseRequiresWithRedifnedPropety() {
        val source = '''
            package clocks.models
                        
            import clocks.models.part.LinkedClock
            
            model BigClock refines LinkedClock {
                requires {
                    refine reqClock {
                        properties {
                           int intRequiredField
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
