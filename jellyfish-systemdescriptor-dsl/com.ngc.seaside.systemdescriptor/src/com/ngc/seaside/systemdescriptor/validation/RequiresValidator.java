package com.ngc.seaside.systemdescriptor.validation;

import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.validation.Check;

import com.google.inject.Inject;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedRequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;
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
			RefinedRequireDeclaration refinedRequirementPart = ((RefinedRequireDeclaration)requirement);
			//Don't change order unless you have thought about what 
			// should be checked first
			checkForNonRefinedModelUsingRefinedRequires(refinedRequirementPart);
			checkForRefinementOfARequirementFieldThatsNotInModelBeingRefined(refinedRequirementPart);
		}
	}
	
	/**
	 * Validates that the Model thats not refining another Model can't then do a refine on
	 *  a requireDeclaration 
	 *  
	 * @param requirement thats being validated 
	 */
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
	
	/**
	 * Validates that the Model or somewhere in the hierarchy that this requirement declaration 
	 * has already been defined 
	 *  
	 * @param requirement thats being validated 
	 */
	protected void checkForRefinementOfARequirementFieldThatsNotInModelBeingRefined(RefinedRequireDeclaration requirement) {
		Model requirementModel = getModel(requirement);
		if (requirementModel != null){
			if (!findRequirementName(requirementModel, requirement.getName())){
				String msg = String.format(
						"The requirement '%s' cannot be refined without first being defined in Model"
						+ " '%s' thats being refined or its hierarchy",
						requirement.getName(), requirementModel.getName());
				error(msg, requirement, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);	
			}
		}
	}
	
	/**
	 * 
	 * Grabs the container that contains the requirement declaration for this requirement 
	 * 
	 * @param requirement that we want its Model container for 
	 * @return Model that contains the refined requirement
	 */
	static private Model getModel(RefinedRequireDeclaration requirement){
		if (requirement.eContainer().eContainer().eClass().equals(SystemDescriptorPackage.Literals.MODEL)) {
			return (Model)requirement.eContainer().eContainer();
		}
		return null;
	}
	
	/**
	 * 
	 * Looks for the requirement thats being refined in the Model hierarchy
	 * 
	 * @param model used as a starting point for the Model hierarchy 
	 * @param requirementName the Name of the variable we are looking for 
	 * @return boolean as to whether we have found the variable in the Model
	 *    hierarchy 
	 */
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
