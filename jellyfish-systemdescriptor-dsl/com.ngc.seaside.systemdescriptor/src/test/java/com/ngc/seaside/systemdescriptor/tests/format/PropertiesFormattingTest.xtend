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
package com.ngc.seaside.systemdescriptor.tests.format

import com.google.inject.Inject
import com.ngc.seaside.systemdescriptor.tests.SystemDescriptorInjectorProvider
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(SystemDescriptorInjectorProvider))
class PropertiesFormattingTest {
	
	@Inject
	FormattingTestUtils formattingUtils
	
	var formattedModel = '''
		package com.ngc.test
		
		import com.ngc.imports.Model1
		import com.ngc.imports.Model2
		import com.ngc.imports.MyEnum

		model Model {
			properties {
				int a
				float b
				string c
				boolean d
				MyEnum status
				com.ngc.imports.Config config
				many int lots
				many MyEnum ofEnums
			}
		}
		'''
	
	@Test
	def void testModelPropertiesFormatting() {
		formattingUtils.testFormatter(formattedModel)
	}
	
	var formattedModelWithFields = '''
		package com.ngc.test
		
		import com.ngc.imports.Model1
		import com.ngc.imports.Model2
		import com.ngc.imports.MyEnum

		model Model {
			parts {
				Model1 part1 {
						properties {
								int a
								float b
								string c
								boolean d
								MyEnum status
								com.ngc.imports.Config config
								many int lots
								many MyEnum ofEnums
						}
				}
			}
		
			requires {
				Model2 require1 {
						properties {
								int a
						}
				}
			}
		
		}
		'''
	
	@Test
	def void testModelFieldPropertiesFormatting() {
		formattingUtils.testFormatter(formattedModelWithFields)
	}
	
	var formattedModelWithLinks = '''
		package com.ngc.test
		
		import com.ngc.imports.Model1
		import com.ngc.imports.Model2
		import com.ngc.imports.MyEnum

		model Model {
			links {
				link foo -> bar {
					properties {
						int a
						float b
						string c
						boolean d
						MyEnum status
						com.ngc.imports.Config config
						many int lots
						many MyEnum ofEnums
					}
				}
			}
		
		}
		'''
	
	@Test
	def void testModelLinkPropertiesFormatting() {
		formattingUtils.testFormatter(formattedModelWithLinks)
	}
}