package com.ngc.seaside.systemdescriptor.validation;

import com.ngc.seaside.systemdescriptor.systemDescriptor.BaseRequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
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
		// Caveat: Since we can't directly check for refinement of data types,
		// circular dependencies,
		// etc just prevent the user from refining anything that couldn't be
		// loaded as a model.
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
		if (model.getInput() != null && hasElement(model.getInput().getDeclarations())) {
			causeUnpermittedAdditionErrorRegarding(
					"inputs",
					model.getInput().getDeclarations().get(0),
					SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);
		}
	}

	private void checkDoesNotParseModelThatRedeclaresOutputs(Model model) {
		if (model.getOutput() != null && hasElement(model.getOutput().getDeclarations())) {
			causeUnpermittedAdditionErrorRegarding(
					"outputs",
					model.getOutput().getDeclarations().get(0),
					SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);
		}
	}

	private void checkDoesNotParseModelThatDeclaresNewRequires(Model model) {
		BaseRequireDeclaration declaration = getNewRequiredDeclaration(model);
		if (declaration != null) {
			causeUnpermittedAdditionErrorRegarding(
					"requirements",
					declaration,
					SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);
		}
	}

	private void checkDoesNotParseModelThatRedeclaresScenarios(Model model) {
		if (!model.getScenarios().isEmpty()) {
			causeUnpermittedAdditionErrorRegarding(
					"scenarios",
					model.getScenarios().get(0),
					SystemDescriptorPackage.Literals.SCENARIO__NAME);
		}
	}

	private void causeUnpermittedAdditionErrorRegarding(String typeOfRedefinition, EObject object, EStructuralFeature feature) {
		String msg = String.format("A refined model cannot add %s!", typeOfRedefinition);
		error(msg, object, feature);
	}

	private static BaseRequireDeclaration getNewRequiredDeclaration(Model model) {
		BaseRequireDeclaration d = null;
		if (model.getRequires() != null) {
			d = model.getRequires().getDeclarations()
					.stream()
					.filter(r -> r.eClass().equals(SystemDescriptorPackage.Literals.BASE_REQUIRE_DECLARATION))
					.map(r -> (BaseRequireDeclaration) r)
					.findFirst()
					.orElse(null);
		}
		return d;

	}

	private static boolean hasElement(Collection<?> collection) {
		return collection != null && !collection.isEmpty();
	}
}
