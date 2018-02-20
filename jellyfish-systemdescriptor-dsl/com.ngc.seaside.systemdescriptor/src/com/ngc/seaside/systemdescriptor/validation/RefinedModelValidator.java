package com.ngc.seaside.systemdescriptor.validation;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.xtext.validation.Check;

public class RefinedModelValidator extends AbstractUnregisteredSystemDescriptorValidator {
    @Check
    public void checkDoesNotParseModelThatRefinesItself(Model model) {
        if (model.getRefinedModel().equals(model)) {
            String msg = "A model cannot refine itself!";
            error(msg, model, SystemDescriptorPackage.Literals.MODEL__REFINED_MODEL);
        }
    }

    @Check
    public void checkDoesNotParseModelThatRefinesData(Model model) {
    }

    @Check
    public void checkDoesNotParseModelsThatCircularlyRefineEachOther(Model model) {
    }

    @Check
    public void checkDoesNotParseModelThatRedeclaresInputs(Model model) {
        if (!model.getRefinedModel().getInput().getDeclarations().equals(model.getInput().getDeclarations())) {
            String msg = "A refined model cannot re-define inputs!";
            error(msg, model, SystemDescriptorPackage.Literals.MODEL__REFINED_MODEL);
        }
    }

    @Check
    public void checkDoesNotParseModelThatRedeclaresOutputs(Model model) {
        if (!model.getRefinedModel().getOutput().getDeclarations().equals(model.getOutput().getDeclarations())) {
            String msg = "A refined model cannot re-define outputs!";
            error(msg, model, SystemDescriptorPackage.Literals.MODEL__REFINED_MODEL);
        }
    }

    @Check
    public void checkDoesNotParseModelThatRedeclaresRequires(Model model) {
        if (!model.getRefinedModel().getRequires().getDeclarations().equals(model.getRequires().getDeclarations())) {
            String msg = "A refined model cannot re-define requires!";
            error(msg, model, SystemDescriptorPackage.Literals.MODEL__REFINED_MODEL);
        }
    }

    @Check
    public void checkDoesNotParseModelThatRedeclaresScenarios(Model model) {
        if (!model.getRefinedModel().getScenarios().equals(model.getScenarios())) {
            String msg = "A refined model cannot add scenarios!";
            error(msg, model, SystemDescriptorPackage.Literals.MODEL__REFINED_MODEL);
        }
    }
}
