package com.ngc.seaside.systemdescriptor.validation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.validation.Check;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Import;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Input;
import com.ngc.seaside.systemdescriptor.systemDescriptor.InputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Output;
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Parts;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Requires;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

/**
 * Validates a {@code Model} is correct. This validator mostly handles checking
 * for duplicate declarations of fields within a model.
 */
public class ModelValidator extends AbstractSystemDescriptorValidator {

	// The grammar allows the various blocks to be declared in the following
	// order:
	//
	// 1) requires
	// 2) input
	// 3) output
	// 4) scenarios
	// 5) parts
	//
	// Therefore, we check for duplicate declarations in the manner shown
	// below. The requires block has the fewest checks and the parts block
	// has the most. This is because we consider a requirement declared in
	// the requires block to have a "higher precedence" than a part with the
	// same name in the parts block because requires is listed first. Thus,
	// the part declaration is the duplicate (not the requires declaration).

	/**
	 * Validates that a require declaration is correct. Requires the containing
	 * model not contain another requirement with the same name.
	 * 
	 * @param declaration
	 */
	@Check
	public void checkForDuplicateRequirements(RequireDeclaration declaration) {
		// Ensure that the model does not already have a requirement with the
		// same
		// name.
		Requires requires = (Requires) declaration.eContainer();
		Model model = (Model) requires.eContainer();

		if (getNumberOfRequirementsNamed(model, declaration.getName()) > 1) {
			String msg = String.format(
					"A requirement named '%s' is already defined for the model '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);
		}
	}

	/**
	 * Validates that an input declaration is correct. Requires the containing
	 * model not contain another input field with the same name and requires the
	 * model have no requirement with the same name.
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
			error(msg, declaration, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);

			// Ensure that the model does not already have a declared
			// requirement with the same name.
		} else if (getNumberOfRequirementsNamed(model, declaration.getName()) > 0) {
			String msg = String.format(
					"A requirement named '%s' is already defined for the element '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);
		}
	}

	/**
	 * Validates that an output declaration is correct. Requires the containing
	 * model not contain another output field with the same name, requires the
	 * model have no requirement with the same name, and requires the model have
	 * no input field with the same name.
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
			error(msg, declaration, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);

			// Ensure that the model does not already have a declared
			// requirement with the same name.
		} else if (getNumberOfRequirementsNamed(model, declaration.getName()) > 0) {
			String msg = String.format(
					"A requirement named '%s' is already defined for the element '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);

			// Ensure that the model does not already have a declared input
			// data field with the same name.
		} else if (getNumberOfInputFieldsNamed(model, declaration.getName()) > 0) {
			String msg = String.format(
					"An input named '%s' is already defined for the element '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);
		}
	}

	/**
	 * Validates that a scenario is correct. Requires the containing model not
	 * contain another scenario with the same name, requires the model not
	 * contain an output field with the same name, requires the model have no
	 * requirement with the same name, and requires the model have no input
	 * field with the same name.
	 */
	@Check
	public void checkForDuplicateScenarios(Scenario scenario) {
		// Ensure that the model does not already have a scenario with the same
		// name.
		Model model = (Model) scenario.eContainer();

		if (getNumberOfScenariosNamed(model, scenario.getName()) > 1) {
			String msg = String.format(
					"A scenario named '%s' is already defined for the model '%s'.",
					scenario.getName(),
					model.getName());
			error(msg, scenario, SystemDescriptorPackage.Literals.SCENARIO__NAME);

			// Ensure that the model does not already have a declared
			// requirement with the same name.
		} else if (getNumberOfRequirementsNamed(model, scenario.getName()) > 0) {
			String msg = String.format(
					"A requirement named '%s' is already defined for the element '%s'.",
					scenario.getName(),
					model.getName());
			error(msg, scenario, SystemDescriptorPackage.Literals.SCENARIO__NAME);

			// Ensure that the model does not already have a declared input
			// data field with the same name.
		} else if (getNumberOfInputFieldsNamed(model, scenario.getName()) > 0) {
			String msg = String.format(
					"An input named '%s' is already defined for the element '%s'.",
					scenario.getName(),
					model.getName());
			error(msg, scenario, SystemDescriptorPackage.Literals.SCENARIO__NAME);

			// Ensure that the model does not already have a declared output
			// data field with the same name.
		} else if (getNumberOfOutputFieldsNamed(model, scenario.getName()) > 0) {
			String msg = String.format(
					"An output named '%s' is already defined for the element '%s'.",
					scenario.getName(),
					model.getName());
			error(msg, scenario, SystemDescriptorPackage.Literals.SCENARIO__NAME);
		}
	}

	/**
	 * Validates that the part declaration is correct. Requires the containing
	 * model not to contain another part declaration with the same name,
	 * requires the model have no requirement with the same name, requires the
	 * mode not to have another input field with the same name, requires the
	 * model not to have another output field with the same name, and requires
	 * the model not to have a scenario with the same name.
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
			error(msg, declaration, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);

			// Ensure that the model does not already have a declared
			// requirement with the same name.
		} else if (getNumberOfRequirementsNamed(model, declaration.getName()) > 0) {
			String msg = String.format(
					"A requirement named '%s' is already defined for the element '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);

			// Ensure that the model does not already have a declared input
			// data field with the same name.
		} else if (getNumberOfInputFieldsNamed(model, declaration.getName()) > 0) {
			String msg = String.format(
					"An input named '%s' is already defined for the element '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);

			// Ensure there is no output field with the same name.
		} else if (getNumberOfOutputFieldsNamed(model, declaration.getName()) > 0) {
			String msg = String.format(
					"An output named '%s' is already defined for the element '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);

			// Ensure there is no scenario with the same name.
		} else if (getNumberOfScenariosNamed(model, declaration.getName()) > 0) {
			String msg = String.format(
					"A scenario named '%s' is already defined for the element '%s'.",
					declaration.getName(),
					model.getName());
			error(msg, declaration, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);
		}
	}
	
	/**
	 * If the model is using a base class in the links, scenarios, output, or input fields
	 * it needs to warn the user.
	 * 
	 * @param declaration
	 */
	@Check
	public void checkBaseDataObject(Model model) {
		// Ensure that the model does not already have a part with the same
		// name.
		List<String> superclasses = new ArrayList<String>();
		List<InputDeclaration> declarationClasses = new ArrayList<InputDeclaration>();
		
		Resource eResource = (Resource) model.eResource();
		EList<Resource> resourceSetList = eResource.getResourceSet().getResources();
		Input input = model.getInput();
		EList<InputDeclaration> inputDecs = input.getDeclarations();

		for(int i = 0; i < inputDecs.size(); i++){
			InputDeclaration decInp = (InputDeclaration) inputDecs.get(i);
			System.out.println("Adding class to dec classes");
			String decClassName = decInp.getType().getName();
			if(!declarationClasses.contains(decClassName)){
				declarationClasses.add(decInp);	
			}
		}
		
		for(int i = 0; i < resourceSetList.size(); i++)
		{
			//Resources
			Resource tempResource = (Resource) resourceSetList.get(i);
			EList<EObject> eObjectsList = tempResource.getContents();
			//System.out.println("I " + tempResource.getURI());
			for(int j = 0; j < eObjectsList.size(); j++)
			{
				//System.out.println("J " + eObjectsList.get(j).getClass());
				//Packages
				EList<EObject> eObjectsInnerList = eObjectsList.get(j).eContents();
				for(int l = 0; l < eObjectsInnerList.size(); l++)
				{
					
					//Import/Data 
					EObject obj = (EObject) eObjectsInnerList.get(l);
					
					if(obj.eClass().equals(SystemDescriptorPackage.Literals.DATA)){
						Data data = (Data) obj;
						//System.out.println("Data name " + data.getName());
						//System.out.println("Data superclass " + data.getSuperclass());
						Data superclass = data.getSuperclass();
						if(superclass != null ){
							String superclassName = superclass.getName();
							if(!superclasses.contains(superclassName)){
								superclasses.add(superclassName);	
							}
						}
					}
				}
			}
		}
		
		System.out.println("Superclasses");
		for(int i = 0; i < superclasses.size(); i++){
			System.out.println(superclasses.get(i));
		}
		
		System.out.println("Declaration Classes");
		for(int i = 0; i < declarationClasses.size(); i++){
			System.out.println(declarationClasses.get(i).getType().getName());
		}
	
		for(int i = 0; i < declarationClasses.size(); i++){
			if(superclasses.contains(declarationClasses.get(i).getType().getName())){
				System.out.println("Warning!");
				
				String msg = String.format(
						"You are using superclass '%s' in your declaration. Try using a subclass instead.",
						declarationClasses.get(i).getType().getName());
				warning(msg, declarationClasses.get(i), SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);
			}
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

	private static int getNumberOfScenariosNamed(
			Model model,
			String scenarioName) {
		return (int) model.getScenarios()
				.stream()
				.filter(d -> d.getName().equals(scenarioName))
				.count();
	}
}
