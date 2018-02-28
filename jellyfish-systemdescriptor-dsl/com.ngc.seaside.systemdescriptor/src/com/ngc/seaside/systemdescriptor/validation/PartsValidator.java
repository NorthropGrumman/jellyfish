package com.ngc.seaside.systemdescriptor.validation;


import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.validation.Check;

import com.google.inject.Inject;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedPartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;


/**
 * @author ceacide
 *
 *  Validates the refine Part Declaration portion of Parts  
 */
public class PartsValidator extends AbstractUnregisteredSystemDescriptorValidator {

	@Inject
	private IQualifiedNameProvider nameProvider;

	@Check
	/**
	 * Entry into this validator for xtext 
	 * 
	 * @param part thats being validated
	 */
	public void checkLinkDeclaration(PartDeclaration part) {

		if (part.eClass().equals(SystemDescriptorPackage.Literals.REFINED_PART_DECLARATION)) {
			RefinedPartDeclaration refinedPart = ((RefinedPartDeclaration)part);
			//Don't change order unless you have thought about what 
			// should be checked first
			checkForNonRefinedModelUsingRefinedParts(refinedPart);
			checkForRefinementOfAPartFieldThatsNotInModelBeingRefined(refinedPart);
		}

	}

	/**
	 * Validates that the Model thats not refining another Model can't then do a refine on
	 *  a partDeclaration 
	 *  
	 * @param part thats being validated 
	 */
	protected void checkForNonRefinedModelUsingRefinedParts(RefinedPartDeclaration part) {
		//Bring us up to the part model
		Model partModel = getModel(part);
		if (partModel !=null) {
			if (((Model)part.eContainer().eContainer()).getRefinedModel() == null) {
				String msg = String.format(
						"Cannot refine part '%s' if this model '%s' is not refining another model",
						part.getName(), partModel.getName());
				error(msg, part, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);
			}
		}
	}
	
	/**
	 * Validates that the Model or somewhere in the hierarchy that this part declaration has already been 
	 *  defined 
	 *  
	 * @param part thats being validated 
	 */
	protected void checkForRefinementOfAPartFieldThatsNotInModelBeingRefined(RefinedPartDeclaration part) {
		Model partModel = getModel(part);
		if (partModel != null){
			if (!findPartName(partModel, part.getName())){
				String msg = String.format(
						"The part '%s' cannot be refined without first being defined in Model"
						+ " '%s' thats being refined or its hierarchy",
						part.getName(), partModel.getRefinedModel().getName());
				error(msg, part, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);	
			}
		}
	}
		
	/**
	 * 
	 * Grabs the container that contains the parts declaration for this requirement 
	 * 
	 * @param part that we want its Model container for 
	 * @return Model that contains the refined part
	 */
	private Model getModel(PartDeclaration part){
		if (part.eContainer().eContainer().eClass().equals(SystemDescriptorPackage.Literals.MODEL)) {
			return (Model)part.eContainer().eContainer();
		}
		return null;
	}
	
	/**
	 * 
	 * Looks for the part thats being refined in the Model hierarchy
	 * 
	 * @param model used as a starting point for the Model hierarchy 
	 * @param partName the Name of the variable we are looking for 
	 * @return boolean as to whether we have found the variable in the Model
	 *    hierarchy 
	 */
	private boolean findPartName(Model model, String partName) {
		boolean found = false;
		Model parentModel = model.getRefinedModel();
		
		while (parentModel != null) {
			if (parentModel.getParts() != null && 
					parentModel.getParts().getDeclarations() != null)
			{
				for(PartDeclaration part : parentModel.getParts().getDeclarations()) {
					if (part.getName().equals(partName)) {
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

