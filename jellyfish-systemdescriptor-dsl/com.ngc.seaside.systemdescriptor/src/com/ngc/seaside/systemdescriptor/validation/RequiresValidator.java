package com.ngc.seaside.systemdescriptor.validation;

import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.validation.Check;

import com.google.inject.Inject;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedRequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

public class RequiresValidator extends AbstractUnregisteredSystemDescriptorValidator {
	
	@Inject
	private IQualifiedNameProvider nameProvider;

	@Check
	public void checkLinkDeclaration(RequireDeclaration requirement) {

		if (requirement.eClass().equals(SystemDescriptorPackage.Literals.REFINED_REQUIRE_DECLARATION)) {
			RefinedRequireDeclaration refinedRequirementPart = ((RefinedRequireDeclaration)requirement);
			//Don't change order unless you have thought about what 
			// should be checked first
			checkForNonRefinedModelUsingRefinedParts(refinedRequirementPart);
			checkForRefinementOfANameNotInRefinedModel(refinedRequirementPart);
		}
	}
	
	protected void checkForNonRefinedModelUsingRefinedParts(RefinedRequireDeclaration requirement) {
		//Bring us up to the part model
		Model partModel = getModel(requirement);
		if (partModel !=null) {
			if (((Model)requirement.eContainer().eContainer()).getRefinedModel() == null) {
				error("Refining a Parts model without the Model being refined", requirement,
						SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);
			}
		}
	}
	
	protected void checkForRefinementOfANameNotInRefinedModel(RefinedRequireDeclaration requirement) {
		Model RequirementModel = getModel(requirement);
		if (RequirementModel != null){
			if (!findRequirementName(RequirementModel, requirement.getName())){
				error("Refining a part that is not defined in the Refined Model", requirement,
						SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);	
			}
		}
	}
	
	
	private Model getModel(RefinedRequireDeclaration requirement){
		if (requirement.eContainer().eContainer().eClass().equals(SystemDescriptorPackage.Literals.MODEL)) {
			return (Model)requirement.eContainer().eContainer();
		}
		return null;
	}
	
	private boolean findRequirementName(Model model, String requirementName) {
		boolean found = false;
		Model parentModel = model.getRefinedModel();
		
		while (parentModel != null) {
			if (parentModel.getParts() != null && 
					parentModel.getParts().getDeclarations() != null)
			{
				for(RequireDeclaration requiresment : parentModel.getRequires().getDeclarations()) {
					if (requiresment.getName().equals(requirementName)) {
						found = true;
						break;
					}
				}
			}
			parentModel = parentModel.getRefinedModel();
		}
		
		return found;
	}
}
