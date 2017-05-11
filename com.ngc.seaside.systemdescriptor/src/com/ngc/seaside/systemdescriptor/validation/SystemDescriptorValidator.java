package com.ngc.seaside.systemdescriptor.validation;

import org.eclipse.xtext.validation.ComposedChecks;

/**
 * This class contains custom validation rules. 
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
@ComposedChecks(validators = {
		DuplicateNameValidator.class})
public class SystemDescriptorValidator extends AbstractSystemDescriptorValidator {

//	public static val INVALID_NAME = 'invalidName'
//
//	@Check
//	def checkGreetingStartsWithCapital(Greeting greeting) {
//		if (!Character.isUpperCase(greeting.name.charAt(0))) {
//			warning('Name should start with a capital', 
//					SystemDescriptorPackage.Literals.GREETING__NAME,
//					INVALID_NAME)
//		}
//	}
	
}
