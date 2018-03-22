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

import static com.ngc.seaside.systemdescriptor.tests.resources.ParsingTestResource.resource
import static org.junit.Assert.*
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedLinkDeclaration
import com.ngc.seaside.systemdescriptor.tests.resources.Datas

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
            Models.LINKED_CLOCK,
            Datas.DATE_TIME
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
    def void testDoesParseModelWithRefinedIOLinks() {
        val model1 = '''
            package test

            import clocks.datatypes.DateTime

            model Model1 {
                input {
                    DateTime input1
                }
            }
        '''
        val model2 = '''
            package test

            import clocks.datatypes.DateTime

            model Model2 {
                output {
                    DateTime output1
                }
            }
        '''
        val model1Source = resource(model1, Datas.DATE_TIME)
        val model2Source = resource(model2, Datas.DATE_TIME)
        val baseModel = '''
            package test

            import test.Model1
            import test.Model2

            model BaseModel {
                parts {
                    Model1 part1
                    Model2 part2
                }

                links {
                    link part2.output1 -> part1.input1
                }
            }
        '''
        val refinedModel = '''
            package test

            import test.BaseModel

            model RefinedModel refines BaseModel {
                links {
                    refine link part2.output1 -> part1.input1
                }
            }
        '''
        val baseModelSource = resource(baseModel, model1Source, model2Source)
        val result = parseHelper.parse(refinedModel, Models.allOf(resourceHelper, baseModelSource).resourceSet)
        assertNotNull(result)
        validationTester.assertNoIssues(result)
    }

    @Test
    def void testDoesParseModelWithRefinedLink(){
        val source = '''
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
