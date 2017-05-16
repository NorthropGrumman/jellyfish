package com.ngc.seaside.systemdescriptor.tests

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.systemDescriptor.Cardinality
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.diagnostics.Diagnostic
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.util.ResourceHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*
import com.ngc.seaside.systemdescriptor.systemDescriptor.StringValue
import org.eclipse.xtext.naming.IQualifiedNameProvider

@RunWith(XtextRunner)
@InjectWith(SystemDescriptorInjectorProvider)
class LinkParsingTest {
	
	@Inject
	ParseHelper<Package> parseHelper

	@Inject
	ResourceHelper resourceHelper

	@Inject
	ValidationTestHelper validationTester

	@Inject
	IQualifiedNameProvider nameProvider;

	Resource dataResource

	Resource partResource1
	
	Resource requirementResource1
	
	@Before
	def void setup() {
		dataResource = resourceHelper.resource(
			'''
				package clocks.datatypes
							
				data Time {
				}
			''',
			URI.createURI("datatypes.sd")
		)
		validationTester.assertNoIssues(dataResource)

		partResource1 = resourceHelper.resource(
			'''
				package clocks.models.part
				
				import clocks.datatypes.Time
							
				model Alarm {
					input {
						Time myTime
					}
				}
			''',
			dataResource.resourceSet
		)
		validationTester.assertNoIssues(partResource1)
	}
	
	@Test
	def void testDoesParseModelWithLinkFromInputToPartInput() {
		val source = '''
			package clocks.models
			
			import clocks.datatypes.Time
			import clocks.models.part.Alarm
			
			model AlarmClock {
				input {
					Time currentTime
				}
				
				output {
					Time alarmTime
				}
				
				parts {
					Alarm alarm
				}
				
				links {
					link currentTime to alarm.myTime
				}
			}
		'''

		val result = parseHelper.parse(source, dataResource.resourceSet)
		val model = result.element as Model
		val part = model.parts.declarations.get(0)
		
		var i = result.imports.get(1)
		var partModel = i.importedNamespace as Model
		
//		System.out.println(nameProvider.getFullyQualifiedName(model.input.declarations.get(0)).toString)
//		System.out.println(nameProvider.getFullyQualifiedName(part).toString)
//		System.out.println(nameProvider.getFullyQualifiedName(partModel.input.declarations.get(0)).toString)
		
		assertNotNull(result)
		validationTester.assertNoIssues(result)
	}
}