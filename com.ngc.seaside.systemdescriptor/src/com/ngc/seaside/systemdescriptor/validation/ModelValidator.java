package com.ngc.seaside.systemdescriptor.validation;

import org.eclipse.xtext.validation.Check;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Output;
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

public class ModelValidator extends AbstractSystemDescriptorValidator {

	@Check
	public void checkForDuplicateOutputFields(OutputDeclaration declaration) {
		// Ensure that the element does not already have a declared output data
		// field with the same name.
		Output output = (Output) declaration.eContainer();
		Model model = (Model) output.eContainer();
		
		if (output.getDeclarations()
				.stream()
				.filter(d -> d.getName().equals(declaration.getName()))
				.count() > 1) {
			String msg = String.format(
					"An output named '%s' is already defined for the model '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.OUTPUT_DECLARATION__NAME);
		}
	}

}
