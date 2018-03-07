package com.ngc.seaside.systemdescriptor.tests.link

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
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
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.junit.Ignore

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
	}
	
	@Test
    @Ignore
    def void testDoesParseModelWithRefinedLink(){
        var source = '''
            package clocks.models
            
            import clocks.models.part.LinkedClock
            
            model AlarmClock refines LinkedClock {

                links {
                    refine currentTime -> clockA.inputTime
                }
            }
        '''

        var result = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(result)
        validationTester.assertNoIssues(result)
    }
    
    @Test
    @Ignore
    def void testDoesNotParseNonRefinedModelWithRefinedLink() {

        var source = '''
            package clocks.models
            
            model AlarmClock {

                links {
                    refine currentTime -> clockA.inputTime
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
