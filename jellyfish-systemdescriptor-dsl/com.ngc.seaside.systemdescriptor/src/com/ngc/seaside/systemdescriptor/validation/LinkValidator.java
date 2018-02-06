package com.ngc.seaside.systemdescriptor.validation;

import org.eclipse.xtext.validation.Check;

import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldReference;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableExpression;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableReference;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Links;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

public class LinkValidator extends AbstractUnregisteredSystemDescriptorValidator {
	@Check
	public void checkLinkDeclaration(LinkDeclaration link) {
		checkForValidLinks(link);
		checkForTypeSafeLinks(link);
		checkForDuplicateLinks(link);
		checkForDuplicateLinkNames(link);
	}

	protected void checkForValidLinks(LinkDeclaration link) {
		LinkableReference source = link.getSource();
		LinkableReference target = link.getTarget();

		FieldDeclaration sourceField = resolveField(source);
		FieldDeclaration targetField = resolveField(target);

		// Make sure the field does not link to itself.
		if (sourceField.equals(targetField)) {
			String msg = String.format(
					"The field '%s' cannot be linked to itself.",
					sourceField.getName());
			error(msg, link, SystemDescriptorPackage.Literals.LINK_DECLARATION__TARGET);
		}

		// Make sure the fields are not both inputs, outputs, parts, or
		// requirements of the same model.
		if (sourceField.eContainer().equals(targetField.eContainer())) {
			String msg = String.format(
					"Cannot link %s '%s' to another %s of the same model.",
					sourceField.eContainer().eClass().getName().toLowerCase(),
					sourceField.getName(),
					targetField.eContainer().eClass().getName().toLowerCase());
			error(msg, link, SystemDescriptorPackage.Literals.LINK_DECLARATION__TARGET);
		}

//		if (!sourceField.eContainer().eClass().equals(targetField.eContainer().eClass())) {
//			String msg = String.format(
//					"Cannot link %s '%s' to %s '%s' of model '%s' because %s can't be linked to %s.",
//					sourceField.eContainer().eClass().getName().toLowerCase(),
//					sourceField.getName(),
//					targetField.eContainer().eClass().getName().toLowerCase(),
//					targetField.getName(),
//					((Model) targetField.eContainer().eContainer()).getName(),
//					sourceField.eContainer().eClass().getName().toLowerCase(),
//					targetField.eContainer().eClass().getName().toLowerCase());
//			error(msg, link, SystemDescriptorPackage.Literals.LINK_DECLARATION__TARGET);
//		}

		// Need a lot more checks here.
	}

	protected void checkForTypeSafeLinks(LinkDeclaration link) {
		// TODO TH: validate the data types for inputs and outputs
		// and model types for parts and requirements match up.
	}

    protected void checkForDuplicateLinks(LinkDeclaration link) {
        // TODO TH: declare duplicate links as a warning, not an error.
    }

    protected void checkForDuplicateLinkNames(LinkDeclaration link) {
        Links links = (Links) link.eContainer();
        Model model = (Model) links.eContainer();

        if (getNumberOfLinksNamed(model, link.getName()) > 1) {
            String msg = String.format(
                    "A link named '%s' is already defined for the model '%s'.",
                    link.getName(),
                    model.getName());
            error(msg, link, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);
        }
    }

	private static FieldDeclaration resolveField(LinkableReference ref) {
		FieldDeclaration field;
		if (ref.eClass().equals(SystemDescriptorPackage.Literals.FIELD_REFERENCE)) {
			field = ((FieldReference) ref).getFieldDeclaration();
		} else {
			// Must be an expression.
			LinkableExpression casted = (LinkableExpression) ref;
			field = casted.getTail();
		}
		return field;
	}

    private static int getNumberOfLinksNamed(
            Model model,
            String linkName) {
        Links links = model.getLinks();
        return links == null
                ? 0
                : (int) links.getDeclarations()
                        .stream()
                        .filter(d -> d.getName().equals(linkName))
                        .count();
    }
}
