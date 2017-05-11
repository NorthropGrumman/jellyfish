package com.ngc.seaside.systemdescriptor.validation;

import org.eclipse.xtext.validation.Check;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Input;
import com.ngc.seaside.systemdescriptor.systemDescriptor.InputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Output;
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

/**
 * Validates a {@code Model} is correct.
 */
public class ModelValidator extends AbstractSystemDescriptorValidator {

	/**
	 * Validates that an output declaration is correct.  Requires the
	 * containing model not contain another output field with the same name
	 * and requires the model have no input field with the same name.
	 */
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
		
		// Ensure that the element does not already have a declared input data
		// field with the same name.
		Input input = model.getInput();
		if(input != null && input.getDeclarations()
				.stream()
				.anyMatch(d -> d.getName().equals(declaration.getName()))) {
			String msg = String.format(
					"An input named '%s' is already defined for the element '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.OUTPUT_DECLARATION__NAME);
		}
	}
	
	/**
	 * Validates that an input declaration is correct.  Requires the
	 * containing model not contain another input field with the same name
	 * and requires the model have no output field with the same name.
	 */
	@Check
	public void checkForDuplicateInputFields(InputDeclaration declaration) {
		// Ensure that the element does not already have a declared input data
		// field with the same name.
		Input input = (Input) declaration.eContainer();
		Model model = (Model) input.eContainer();
		
		if (input.getDeclarations()
				.stream()
				.filter(d -> d.getName().equals(declaration.getName()))
				.count() > 1) {
			String msg = String.format(
					"An input named '%s' is already defined for the model '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.INPUT_DECLARATION__NAME);
		}
		
		// Ensure that the element does not already have a declared output data
		// field with the same name.
		// Note this check is really redundant because checkForDuplicateOutputFields will
		// detect this cause anyway, but we include it for consistency.
		Output output = model.getOutput();
		if(output != null && output.getDeclarations()
				.stream()
				.anyMatch(d -> d.getName().equals(declaration.getName()))) {
			String msg = String.format(
					"An output named '%s' is already defined for the element '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.INPUT_DECLARATION__NAME);
		}
	}
}
