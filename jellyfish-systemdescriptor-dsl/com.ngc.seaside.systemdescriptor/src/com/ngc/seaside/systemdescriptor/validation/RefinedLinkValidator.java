package com.ngc.seaside.systemdescriptor.validation;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.validation.Check;

import com.google.inject.Inject;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Links;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedLinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedLinkNameDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

public class RefinedLinkValidator extends AbstractUnregisteredSystemDescriptorValidator {

	@Inject
	private IQualifiedNameProvider nameProvider;

	@Check
	public void checkRefinedLinkedDeclaration(RefinedLinkDeclaration link) {
		Model model = (Model) link
				.eContainer() // Links
				.eContainer(); // Model

		if(checkModelRefinesAnotherModel(link, model)) {
			// Only do these checks if a model is being refined.
		}
	}

	@Check
	public void checkRefinedLinkedDeclaration(RefinedLinkNameDeclaration link) {
		Model model = (Model) link
				.eContainer() // Links
				.eContainer(); // Model
		if (checkModelRefinesAnotherModel(link, model)) {
			// Only do these checks if a model is being refined.
			checkLinkIsDeclaredInRefinedModel(link, model);
		}
	}

	private boolean checkModelRefinesAnotherModel(LinkDeclaration link, Model model) {
		boolean refinesModel = model.getRefinedModel() != null;
		
		if (!refinesModel) {
			Links links = (Links) link.eContainer();
			String msg = String.format(
					"Cannot refine a link because the model '%s' does not refine another model.",
					nameProvider.getFullyQualifiedName(model));
			int index = links.getDeclarations().indexOf(link);
			error(msg, links, SystemDescriptorPackage.Literals.LINKS__DECLARATIONS, index);
		}
		
		return refinesModel;
	}

	private void checkLinkIsDeclaredInRefinedModel(RefinedLinkNameDeclaration link, Model model) {
		Set<String> linkNames = new HashSet<>();

		Model refinedModel = model.getRefinedModel();
		while (refinedModel != null) {
			if (refinedModel.getLinks() != null) {
				refinedModel.getLinks().getDeclarations()
						.stream()
						.filter(l -> l.getName() != null)
						.forEach(l -> linkNames.add(l.getName()));
			}
			refinedModel = refinedModel.getRefinedModel();
		}

		if (!linkNames.contains(link.getName())) {
			String msg = String.format(
					"No link named '%s' declared in the refinement hierarchy of '%s'.",
					link.getName(),
					nameProvider.getFullyQualifiedName(model));
			error(msg, link, SystemDescriptorPackage.Literals.LINK_DECLARATION__NAME);
		}
	}
}
