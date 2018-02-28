package com.ngc.seaside.systemdescriptor.validation;

import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.validation.Check;

import com.google.inject.Inject;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedRequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;
import com.ngc.seaside.systemdescriptor.validation.util.ValidatorUtil;
/**
 * 
 * @author ceacide
 *
 **  Validates the refine Require Declaration portion of Requires
 */
public class RequiresValidator extends AbstractUnregisteredSystemDescriptorValidator {
	
	@Inject
	private IQualifiedNameProvider nameProvider;

	@Check
	/**
	 * Entry into this validator for xtext
	 * 
	 * @param requirement thats being validated
	 */
	public void checkLinkDeclaration(RequireDeclaration requirement) {

		if (requirement.eClass().equals(SystemDescriptorPackage.Literals.REFINED_REQUIRE_DECLARATION)) {
			//Don't change order unless you have thought about what 
			// should be checked first
			setFieldDeclarationError(ValidatorUtil.checkForNonRefinedModelUsingRefinedfields(requirement),
					requirement);
			
			setFieldDeclarationError(
					ValidatorUtil.checkForRefinementOfAFieldThatsNotInModelBeingRefined(requirement), 
					requirement);
		}
	}
	
	private void setFieldDeclarationError(String error, FieldDeclaration requirement){
		if (!error.isEmpty())
		{
			error(error, requirement, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);
		}
	}
	
}
