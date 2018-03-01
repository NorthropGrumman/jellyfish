package com.ngc.seaside.systemdescriptor.validation;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.validation.Check;

import com.google.inject.Inject;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BaseRequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

public class RefinedModelValidator extends AbstractUnregisteredSystemDescriptorValidator {

	@Inject
	private IQualifiedNameProvider nameProvider;
	
	@Check
	public void checkRefinedModel(Model model) {
		if (model == null || model.getRefinedModel() == null)
			return;

        checkDoesNotParseModelThatCircularlyRefinesAnotherModel(model);
        checkDoesNotParseModelThatRedeclaresInputs(model);
        checkDoesNotParseModelThatRedeclaresOutputs(model);
        checkDoesNotParseModelThatRedeclaresScenarios(model);
        checkDoesNotParseModelThatDeclaresNewRequires(model);
	}

	private void checkDoesNotParseModelThatCircularlyRefinesAnotherModel(Model model) {
		if (model.getRefinedModel().equals(model)) {
			String msg = "A model cannot refine itself!";
			error(msg, model, SystemDescriptorPackage.Literals.MODEL__REFINED_MODEL);
		} else {
			Set<QualifiedName> refinedModelNames = new LinkedHashSet<>();
			Model refinedModel = model;
			while (refinedModel != null) {
				QualifiedName name = nameProvider.getFullyQualifiedName(refinedModel);
				if (!refinedModelNames.add(name)) {
					StringBuilder msg = new StringBuilder("A cycle has been detected in the refinement hierarcy!  ");
					for(QualifiedName n : refinedModelNames) {
						msg.append(n).append(" refines ");
					}
					msg.append(name);
					error(msg.toString(), model, SystemDescriptorPackage.Literals.MODEL__REFINED_MODEL);
					refinedModel = null;
				}
				refinedModel = refinedModel == null ? null : refinedModel.getRefinedModel();
			}
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
