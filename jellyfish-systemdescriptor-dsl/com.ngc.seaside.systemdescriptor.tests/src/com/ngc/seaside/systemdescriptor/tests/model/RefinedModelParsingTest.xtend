package com.ngc.seaside.systemdescriptor.tests.model

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
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
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import com.ngc.seaside.systemdescriptor.tests.resources.Models
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import com.ngc.seaside.systemdescriptor.tests.resources.Datas
import org.junit.Ignore

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class RefinedModelParsingTest {
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
            Models.TIMER,
            Models.EMPTY_MODEL,
            Datas.TIME
        )
        validationTester.assertNoIssues(requiredResources)
    }

    @Test
    def void testDoesParseRefinedModelUsingFullyQualifiedName() {
        val source = '''
            package clocks.models

            model MyModel refines clocks.models.part.Alarm {
            }
        '''

        var result = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(result)
        validationTester.assertNoIssues(result)
    }

    @Test
    def void testDoesParseRefinedModelUsingImport() {
        val source = '''
            package clocks.models

            import clocks.models.part.Alarm

            model MyModel refines Alarm {
            }
        '''

        var result = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(result)
        validationTester.assertNoIssues(result)
    }

    //DON'T Parse test methods

    @Test
    def void testDoesNotParseModelThatRefinesItself() {
        val source = '''
            package clocks.models

            model MyModel refines clocks.models.MyModel {
            }
        '''

        var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(invalidResult)

        validationTester.assertError(
            invalidResult,
            SystemDescriptorPackage.Literals.MODEL,
            null)
    }

    @Test
    def void testDoesNotParseModelThatRefinesData() {
        val source = '''
            package clocks.models

            model MyModel refines clocks.datatypes.Time {
            }
        '''

        var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(invalidResult)

        validationTester.assertError(
            invalidResult,
            SystemDescriptorPackage.Literals.MODEL,
            null)
    }

    @Test
    def void testDoesNotParseModelsThatCircularlyRefineEachOther() {
        val refinedModel = Models.allOf(
            resourceHelper,
            Models.INVALID_REFINED_MODEL
        )
        val source = '''
            package refines.test

            import refines.test.InvalidRefinedModel

            model AnotherInvalidRefinedModel refines InvalidRefinedModel {
            }
        '''

        var invalidResult = parseHelper.parse(source, refinedModel.resourceSet)
        assertNotNull(invalidResult)

        validationTester.assertError(
            invalidResult,
            SystemDescriptorPackage.Literals.MODEL,
            null
        )
    }

    @Test
    def void testDoesNotParseModelRedeclaring_Inputs() {
        val source = '''
            package clocks.models

            import clocks.models.part.Alarm
            import clocks.datatypes.Time

            model RefinedModel refines Alarm {
                input {
                    Time time
                }
            }
        '''

        var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(invalidResult)

        validationTester.assertError(
            invalidResult,
            SystemDescriptorPackage.Literals.MODEL,
            null)
    }
    
    @Test
    @Ignore
    def void testDoesNotParseModelRefining_Inputs() {
        val source = '''
            package clocks.models

            import clocks.models.part.Alarm
            import clocks.datatypes.Time

            model RefinedModel refines Alarm {
                Input {
                    refine Time time
                }
            }
        '''

        var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(invalidResult)

        validationTester.assertError(
            invalidResult,
            SystemDescriptorPackage.Literals.MODEL,
            null)
    }

    @Test
    def void testDoesNotParseModelRedeclaring_Outputs() {
        val source = '''
            package clocks.models

            import clocks.models.part.Alarm
            import clocks.datatypes.Time

            model RefinedModel refines Alarm {
                output {
                    Time time
                }
            }
        '''

        var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(invalidResult)

        validationTester.assertError(
            invalidResult,
            SystemDescriptorPackage.Literals.MODEL,
            null)
    }
    
    @Test
    def void testDoesNotParseModelAdding_Requires() {
        val source = '''
            package clocks.models

            import clocks.models.part.Alarm
            import clocks.models.part.Timer
            import foo.AnEmptyModel

            model RefinedModel refines Alarm {
                requires {
                    AnEmptyModel emptyModel
                    Timer timer
                }
            }
        '''

        var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(invalidResult)

        validationTester.assertError(
            invalidResult,
            SystemDescriptorPackage.Literals.MODEL,
            null)
    }

    @Test
    def void testDoesNotParseModelRedeclaring_Scenarios() {
        val source = '''
            package clocks.models

            import clocks.models.part.Alarm

            model RefinedModel refines Alarm {
                scenario whatever {
                }
            }
        '''

        var invalidResult = parseHelper.parse(source, requiredResources.resourceSet)
        assertNotNull(invalidResult)

        validationTester.assertError(
            invalidResult,
            SystemDescriptorPackage.Literals.MODEL,
            null)
    }
}
