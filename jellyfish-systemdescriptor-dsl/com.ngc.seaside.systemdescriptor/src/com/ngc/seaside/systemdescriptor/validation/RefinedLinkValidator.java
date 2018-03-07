package com.ngc.seaside.systemdescriptor.validation;

import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.validation.Check;

import com.google.inject.Inject;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BaseLinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldReference;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Input;
import com.ngc.seaside.systemdescriptor.systemDescriptor.InputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableExpression;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableReference;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Links;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Output;
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Parts;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Requires;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;
import com.ngc.seaside.systemdescriptor.validation.util.ValidatorUtil;

public class RefinedLinkValidator extends AbstractUnregisteredSystemDescriptorValidator {
	
	@Inject
	private IQualifiedNameProvider nameProvider;

	// TODO TH: I don't think this class is needed.
	
	/**
	 * Entry into this validator for xtext 
	 * 
	 * @param link thats being validated
	 */
	@Check
	public void checkLinkDeclaration(LinkDeclaration link) {

		if (link.eClass().equals(SystemDescriptorPackage.Literals.REFINED_LINK_DECLARATION)) {
			//Don't change order unless you have thought about what 
			// should be checked first
//			setFieldDeclarationError(ValidatorUtil.checkForNonRefinedModelUsingRefinedfields(link),
//					link);
//			setFieldDeclarationError(ValidatorUtil.checkForRefinementOfAFieldThatsNotInModelBeingRefined(link),
//					link);
		}

	}
	
	/**
	 * Validates that the Model thats not refining another Model can't then do a refine on
	 *  a fieldDeclaration 
	 *  
	 * @param fieldDeclaration thats being validated 
	 */
	static public String checkForNonRefinedModelUsingRefinedfields(FieldDeclaration fieldDeclaration) {
		//Bring us up to the part model
		String msg = "";
//		//Model fieldDeclarationModel = getModel(fieldDeclaration);
//		if (fieldDeclarationModel != null) {
//			if (fieldDeclarationModel.getRefinedModel() == null) {
//				msg = String.format(
//						"Field '%s' cannot be refined because model '%s.%s.' does not refine another model.",
//						fieldDeclaration.getName(),
//						((Package) fieldDeclarationModel.eContainer()).getName(),
//						fieldDeclarationModel.getName());
//			}
//		}
		return msg;
	}
	private void setFieldDeclarationError(String error, FieldDeclaration requirement){
		if (!error.isEmpty()) {
			error(error, requirement, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);
		}
	}
}
