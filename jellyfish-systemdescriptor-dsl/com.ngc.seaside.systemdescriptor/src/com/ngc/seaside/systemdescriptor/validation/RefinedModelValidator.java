package com.ngc.seaside.systemdescriptor.validation;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.xtext.validation.Check;

public class RefinedModelValidator extends AbstractUnregisteredSystemDescriptorValidator {
    @Check
    public void checkRefinedModel(Model model) {
        if (model == null || model.getRefinedModel() == null)
            return;

        checkDoesNotParseModelThatRefinesUnloadableItem(model);
        checkDoesNotParseModelThatRefinesItself(model);
        checkDoesNotParseModelThatRedeclaresInputs(model);
        checkDoesNotParseModelThatRedeclaresOutputs(model);
        checkDoesNotParseModelThatRedeclaresScenarios(model);
        checkDoesNotParseModelThatDeclaresNewRequires(model);
    }

    private void checkDoesNotParseModelThatRefinesUnloadableItem(Model model) {
        // Caveat: Since we can't directly check for refinement of data types, circular dependencies,
        // etc just prevent the user from refining anything that couldn't be loaded as a model.
        if (model.getRefinedModel() != null && model.getRefinedModel().getName() == null) {
            String msg = "A model cannot refine data!";
            error(msg, model, SystemDescriptorPackage.Literals.MODEL__REFINED_MODEL);
        } else if (model.getRefinedModel() != null && model.getRefinedModel().getRefinedModel() != null && model.getRefinedModel().getRefinedModel().getName().equals(model.getName())) {
            String msg = "A model cannot refine a model that refines the current model!";
            error(msg, model, SystemDescriptorPackage.Literals.MODEL__REFINED_MODEL);
        }
    }

    private void checkDoesNotParseModelThatRefinesItself(Model model) {
        if (model.getRefinedModel().equals(model)) {
            String msg = "A model cannot refine itself!";
            error(msg, model, SystemDescriptorPackage.Literals.MODEL__REFINED_MODEL);
        }
    }

    private void checkDoesNotParseModelThatRedeclaresInputs(Model model) {
        if (model.getInput() != null && model.getInput().getDeclarations() != null) {
            causeUnpermittedAdditionErrorRegarding("inputs", model);
        }
    }

    private void checkDoesNotParseModelThatRedeclaresOutputs(Model model) {
        if (model.getOutput() != null && model.getOutput().getDeclarations() != null) {
            causeUnpermittedAdditionErrorRegarding("outputs", model);
        }
    }

    private void checkDoesNotParseModelThatDeclaresNewRequires(Model model) {
//        if (!refinedModelHasValidRequiresBlock(model, model.getRefinedModel())) {
//            causeUnpermittedAdditionErrorRegarding("requirements", model);
//        }
    	if (refinedModelHasNewRequiresDeclarations(model)) {
    		causeUnpermittedAdditionErrorRegarding("requirements", model);
    	}
    }

    private void checkDoesNotParseModelThatRedeclaresScenarios(Model model) {
        if (!model.getScenarios().isEmpty()) {
            causeUnpermittedAdditionErrorRegarding("scenarios", model);
        }
    }

    private void causeUnpermittedAdditionErrorRegarding(String typeOfRedefinition, Model model) {
        String msg = String.format("A refined model cannot add %s!", typeOfRedefinition);
        error(msg, model, SystemDescriptorPackage.Literals.MODEL__REFINED_MODEL);
    }
    
    private boolean refinedModelHasNewRequiresDeclarations(Model model) {
    	return model.getRequires() != null
    			&& model.getRequires().getDeclarations()
    				.stream()
    				.anyMatch(r -> r.eClass().equals(SystemDescriptorPackage.Literals.BASE_REQUIRE_DECLARATION));

    }
}
