package com.ngc.seaside.systemdescriptor.validation;


import org.eclipse.xtext.validation.Check;

import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;
import com.ngc.seaside.systemdescriptor.validation.util.ValidatorUtil;


/**
 * @author ceacide
 *
 *  Validates the refine Part Declaration portion of Parts  
 */
public class PartsValidator extends AbstractUnregisteredSystemDescriptorValidator {

	@Check
	/**
	 * Entry into this validator for xtext 
	 * 
	 * @param part thats being validated
	 */
	public void checkLinkDeclaration(PartDeclaration part) {

		if (part.eClass().equals(SystemDescriptorPackage.Literals.REFINED_PART_DECLARATION)) {
			//Don't change order unless you have thought about what 
			// should be checked first
			setFieldDeclarationError(ValidatorUtil.checkForNonRefinedModelUsingRefinedfields(part),
					part);
			setFieldDeclarationError(ValidatorUtil.checkForRefinementOfAFieldThatsNotInModelBeingRefined(part),
					part);
		}

	}

	private void setFieldDeclarationError(String error, FieldDeclaration requirement){
		if (!error.isEmpty()) {
			error(error, requirement, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);
		}
	}
}

