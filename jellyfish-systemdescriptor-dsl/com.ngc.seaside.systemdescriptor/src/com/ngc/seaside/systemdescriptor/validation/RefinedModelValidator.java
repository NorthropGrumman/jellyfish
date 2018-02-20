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
        checkDoesNotParseModelThatRedeclaresRequires(model);
        checkDoesNotParseModelThatRedeclaresScenarios(model);
    }

    private void checkDoesNotParseModelThatRefinesUnloadableItem(Model model) {
        // Caveat: Since we can't directly check for refinement of data types, circular dependencies,
        // etc just prevent the user from refining anything that couldn't be loaded as a model.
        if (model.getRefinedModel() != null && model.getRefinedModel().getName() == null) {
            String msg = "Invalid model refinement due to model-load error!";
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
            String msg = "A refined model cannot add inputs!";
            error(msg, model, SystemDescriptorPackage.Literals.MODEL__REFINED_MODEL);
        }
    }

    private void checkDoesNotParseModelThatRedeclaresOutputs(Model model) {
        if (model.getOutput() != null && model.getOutput().getDeclarations() != null) {
            String msg = "A refined model cannot add outputs!";
            error(msg, model, SystemDescriptorPackage.Literals.MODEL__REFINED_MODEL);
        }
    }

    private void checkDoesNotParseModelThatRedeclaresRequires(Model model) {
        if (model.getRequires() != null && model.getRequires().getDeclarations() != null) {
            String msg = "A refined model cannot add requirements!";
            error(msg, model, SystemDescriptorPackage.Literals.MODEL__REFINED_MODEL);
        }
    }

    private void checkDoesNotParseModelThatRedeclaresScenarios(Model model) {
        if (!model.getScenarios().isEmpty()) {
            String msg = "A refined model cannot add scenarios!";
            error(msg, model, SystemDescriptorPackage.Literals.MODEL__REFINED_MODEL);
        }
    }
}
