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
			checkForNonRefinedModelUsingRefinedRequires(refinedRequirementPart);
			checkForRefinementOfARequirementFieldThatsNotInModelBeingRefined(refinedRequirementPart);
		}
	}
	
	protected void checkForNonRefinedModelUsingRefinedRequires(RefinedRequireDeclaration requirement) {
		//Bring us up to the part model
		Model requirementModel = getModel(requirement);
		if (requirementModel !=null) {
			if (((Model)requirement.eContainer().eContainer()).getRefinedModel() == null) {
				String msg = String.format(
						"Cannot refine requirement '%s' if this model '%s' is not refining another model",
						requirement.getName(), requirementModel.getName());
				error(msg, requirement, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);
			}
		}
	}
	
	protected void checkForRefinementOfARequirementFieldThatsNotInModelBeingRefined(RefinedRequireDeclaration requirement) {
		Model requirementModel = getModel(requirement);
		if (requirementModel != null){
			if (!findRequirementName(requirementModel, requirement.getName())){
				String msg = String.format(
						"The requirement '%s' cannot be refined without being defined in Model '%s' thats being refined",
						requirement.getName(), requirementModel.getName());
				error(msg, requirement, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);	
			}
		}
	}
	
	
	static private Model getModel(RefinedRequireDeclaration requirement){
		if (requirement.eContainer().eContainer().eClass().equals(SystemDescriptorPackage.Literals.MODEL)) {
			return (Model)requirement.eContainer().eContainer();
		}
		return null;
	}
	
	static private boolean findRequirementName(Model model, String requirementName) {
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
