package com.ngc.seaside.systemdescriptor.tests.link

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import com.ngc.seaside.systemdescriptor.tests.resources.Models
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.util.ResourceHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedLinkDeclaration

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class RefinedLinkParsingTest {

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
			Models.ALARM,
			Models.CLOCK,
			Models.LINKED_CLOCK
		)
		validationTester.assertNoIssues(requiredResources)
	}
	
	@Test
	def void testDoesParseModelWithRefinedNameLink(){
		var source = '''
			package clocks.models
			
			import clocks.models.part.LinkedClock
			
			model AlarmClock refines LinkedClock {

			    links {
			    	refine namedLink {
			             metadata {
			                 "name" : "My Link"
			             }
			        }
			    }
			}
		'''

		var result = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(result)
		validationTester.assertNoIssues(result)
		
		var model = result.element as Model
		var refinedLink = model.links.declarations.get(0)
        assertEquals(
            "refinedLink not correct!",
            "namedLink",
            refinedLink.name
        )
	}
	
	@Test
    def void testDoesParseModelWithRefinedLink(){
        var source = '''
            package clocks.models
            
            import clocks.models.part.LinkedClock
            
            model AlarmClock refines LinkedClock {

                links {
                    refine link currentTime -> clockA.inputTime
                }
            }
        '''

        var result = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(result)
        validationTester.assertNoIssues(result)
        
        var model = result.element as Model
		var refinedLink = model.links.declarations.get(0) as RefinedLinkDeclaration
        assertNotNull(
            "refinedLink not correct!",
            refinedLink.source
        )
        assertNotNull(
            "refinedLink not correct!",
            refinedLink.target
        )
    }
    
    @Test
    def void testDoesNotParseModelWithRefinedNamedLinkIfLinkNotDeclared() {
    	var source = '''
			package clocks.models
			
			import clocks.models.part.LinkedClock
			
			model AlarmClock refines LinkedClock {

			    links {
			    	refine foo {
			             metadata {
			                 "name" : "My Link"
			             }
			        }
			    }
			}
		'''

		var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
		assertNotNull(invalidResult)
		validationTester.assertError(
            invalidResult,
            SystemDescriptorPackage.Literals.REFINED_LINK_NAME_DECLARATION,
            null
        )
    }
    
    @Test
    def void testDoesNotParseNonRefinedModelWithRefinedLink() {

        var source = '''
			package clocks.models
			
			import clocks.datatypes.ZonedTime
			import clocks.models.part.Clock

			model AlarmClock {
				input {
					ZonedTime currentTime
				}
			
				parts {
					Clock clock
				}

				links {
					refine link currentTime -> clock.inputTime
				}
			}
        '''

        var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(invalidResult)
        validationTester.assertError(
            invalidResult,
            SystemDescriptorPackage.Literals.LINKS,
            null
        )
    }
    
    @Test
    def void testDoesNotParseRefinedModelWithRefinedLinkThatIsNotInTheModelBeginRefined() {
        var source = '''
            package clocks.models
            
            import clocks.models.part.LinkedClock
            
            model AlarmClock refines LinkedClock {

                links {
                    refine link currentTime -> clockB.inputTime
                }
            }
        '''

        var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(invalidResult)
        validationTester.assertError(
            invalidResult,
            SystemDescriptorPackage.Literals.LINKS,
            null
        )
    }
    
    @Test
    def void testDoesNotParseModelIfRefinedLinkedTriesToChangeNames() {
    	var source = '''
            package clocks.models
            
            import clocks.models.part.LinkedClock
            
            model AlarmClock refines LinkedClock {

                links {
                    refine link newName currentTime -> clock.inputTime
                }
            }
        '''
        
         var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(invalidResult)
        validationTester.assertError(
            invalidResult,
            SystemDescriptorPackage.Literals.LINK_DECLARATION,
            null
        )
    } 
}
