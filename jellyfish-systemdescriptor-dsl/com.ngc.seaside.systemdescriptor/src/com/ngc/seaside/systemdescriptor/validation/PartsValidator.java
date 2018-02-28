package com.ngc.seaside.systemdescriptor.validation;


import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.validation.Check;

import com.google.inject.Inject;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedPartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

public class PartsValidator extends AbstractUnregisteredSystemDescriptorValidator {

	@Inject
	private IQualifiedNameProvider nameProvider;

	@Check
	public void checkLinkDeclaration(PartDeclaration part) {

		if (part.eClass().equals(SystemDescriptorPackage.Literals.REFINED_PART_DECLARATION)) {
			RefinedPartDeclaration refinedPart = ((RefinedPartDeclaration)part);
			//Don't change order unless you have thought about what 
			// should be checked first
			checkForNonRefinedModelUsingRefinedParts(refinedPart);
			checkForRefinementOfAPartFieldThatsNotInModelBeingRefined(refinedPart);
		}

	}

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
	
	protected void checkForRefinementOfAPartFieldThatsNotInModelBeingRefined(RefinedPartDeclaration part) {
		Model partModel = getModel(part);
		if (partModel != null){
			if (!findPartName(partModel, part.getName())){
				String msg = String.format(
						"The requirement '%s' cannot be refined without being defined in Model '%s' thats being refined",
						part.getName(), partModel.getName());
				error(msg, part, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);	
			}
		}
	}
	
	private Model getModel(PartDeclaration part){
		if (part.eContainer().eContainer().eClass().equals(SystemDescriptorPackage.Literals.MODEL)) {
			return (Model)part.eContainer().eContainer();
		}
		return null;
	}
	
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

