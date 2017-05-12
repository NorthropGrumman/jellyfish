package com.ngc.seaside.systemdescriptor.validation;

import org.eclipse.xtext.validation.Check;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Input;
import com.ngc.seaside.systemdescriptor.systemDescriptor.InputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Output;
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Parts;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Requires;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

/**
 * Validates a {@code Model} is correct.
 */
public class ModelValidator extends AbstractSystemDescriptorValidator {

	/**
	 * Validates that an input declaration is correct. Requires the containing
	 * model not contain another input field with the same name and requires the
	 * model have no output field with the same name.
	 */
	@Check
	public void checkForDuplicateInputFields(InputDeclaration declaration) {
		// Ensure that the model does not already have a declared input data
		// field with the same name.
		Input input = (Input) declaration.eContainer();
		Model model = (Model) input.eContainer();
		if (getNumberOfInputFieldsNamed(model, declaration.getName()) > 1) {
			String msg = String.format(
					"An input named '%s' is already defined for the model '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.INPUT_DECLARATION__NAME);

			// Ensure that the model does not already have a declared output
			// field with the same name.
		} else if (getNumberOfOutputFieldsNamed(model, declaration.getName()) > 0) {
			String msg = String.format(
					"An output named '%s' is already defined for the element '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.INPUT_DECLARATION__NAME);
		}
	}

	/**
	 * Validates that an output declaration is correct. Requires the containing
	 * model not contain another output field with the same name and requires
	 * the model have no input field with the same name.
	 */
	@Check
	public void checkForDuplicateOutputFields(OutputDeclaration declaration) {
		// Ensure that the model does not already have a declared output data
		// field with the same name.
		Output output = (Output) declaration.eContainer();
		Model model = (Model) output.eContainer();

		if (getNumberOfOutputFieldsNamed(model, declaration.getName()) > 1) {
			String msg = String.format(
					"An output named '%s' is already defined for the model '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.OUTPUT_DECLARATION__NAME);

			// Ensure that the model does not already have a declared input
			// data field with the same name.
		} else if (getNumberOfInputFieldsNamed(model, declaration.getName()) > 0) {
			String msg = String.format(
					"An input named '%s' is already defined for the element '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.OUTPUT_DECLARATION__NAME);
		}
	}

	/**
	 * Validates that the part declaration is correct. Requires the containing
	 * model not to contain another part declaration with the same name, not to
	 * contain another input field with the same name, and not to contain
	 * another output field with the same name.
	 * 
	 * @param declaration
	 */
	@Check
	public void checkForDuplicateParts(PartDeclaration declaration) {
		// Ensure that the model does not already have a part with the same
		// name.
		Parts parts = (Parts) declaration.eContainer();
		Model model = (Model) parts.eContainer();

		if (getNumberOfPartsNamed(model, declaration.getName()) > 1) {
			String msg = String.format(
					"A part named '%s' is already defined for the model '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.PART_DECLARATION__NAME);

			// Ensure there is no input field with the same name.
		} else if (getNumberOfInputFieldsNamed(model, declaration.getName()) > 0) {
			String msg = String.format(
					"An input named '%s' is already defined for the element '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.PART_DECLARATION__NAME);

			// Ensure there is no output field with the same name.
		} else if (getNumberOfOutputFieldsNamed(model, declaration.getName()) > 0) {
			String msg = String.format(
					"An output named '%s' is already defined for the element '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.PART_DECLARATION__NAME);
		}
	}
	
	@Check
	public void checkForDuplicateRequirements(RequireDeclaration declaration) {
		// Ensure that the model does not already have a requirement with the same
		// name.
		Requires requires = (Requires) declaration.eContainer();
		Model model = (Model) requires.eContainer();

		if (getNumberOfRequirementsNamed(model, declaration.getName()) > 1) {
			String msg = String.format(
					"A requirement named '%s' is already defined for the model '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.REQUIRE_DECLARATION__NAME);

			// Ensure there is no input field with the same name.
		} else if (getNumberOfInputFieldsNamed(model, declaration.getName()) > 0) {
			String msg = String.format(
					"An input named '%s' is already defined for the element '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.REQUIRE_DECLARATION__NAME);

			// Ensure there is no output field with the same name.
		} else if (getNumberOfOutputFieldsNamed(model, declaration.getName()) > 0) {
			String msg = String.format(
					"An output named '%s' is already defined for the element '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.REQUIRE_DECLARATION__NAME);
		}
	}

	private static int getNumberOfInputFieldsNamed(
			Model model,
			String fieldName) {
		Input input = model.getInput();
		return input == null
				? 0
				: (int) input.getDeclarations()
						.stream()
						.filter(d -> d.getName().equals(fieldName))
						.count();
	}

	private static int getNumberOfOutputFieldsNamed(
			Model model,
			String fieldName) {
		Output output = model.getOutput();
		return output == null
				? 0
				: (int) output.getDeclarations()
						.stream()
						.filter(d -> d.getName().equals(fieldName))
						.count();
	}

	private static int getNumberOfPartsNamed(
			Model model,
			String partName) {
		Parts parts = model.getParts();
		return parts == null
				? 0
				: (int) parts.getDeclarations()
						.stream()
						.filter(d -> d.getName().equals(partName))
						.count();
	}
	
	private static int getNumberOfRequirementsNamed(
			Model model,
			String requirementName) {
		Requires requires = model.getRequires();
		return requires == null
				? 0
				: (int) requires.getDeclarations()
						.stream()
						.filter(d -> d.getName().equals(requirementName))
						.count();
	}
}
