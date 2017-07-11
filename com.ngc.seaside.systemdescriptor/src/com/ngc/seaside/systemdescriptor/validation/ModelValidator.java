package com.ngc.seaside.systemdescriptor.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.validation.Check;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.GivenDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.GivenStep;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Import;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Input;
import com.ngc.seaside.systemdescriptor.systemDescriptor.InputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableReference;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Output;
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Parts;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Requires;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Step;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ThenDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ThenStep;
import com.ngc.seaside.systemdescriptor.systemDescriptor.WhenDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.WhenStep;

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
		HashMap<String, String> objectDeclarations = new HashMap<String, String>(); //<Objectname, Classname>
		List<String> superclasses = new ArrayList<String>();
		
		Resource eResource = (Resource) model.eResource();
		if(eResource != null) {
			//if eResource is null, no point in continuing since we need it to "see" all the classes.
			EList<Resource> resourceSetList = eResource.getResourceSet().getResources();
			
			//Iterate through all of our resources to identify any Data superclasses
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
			
			if(model.getInput() != null) {
				EList<InputDeclaration> inputDecs = model.getInput().getDeclarations();
				
				for(int i = 0; i < inputDecs.size(); i++){
					InputDeclaration decInp = (InputDeclaration) inputDecs.get(i);
					String classname = decInp.getType().getName();
					objectDeclarations.put(decInp.getName(), classname); //Keep track for later
					if(superclasses.contains(classname)){
						//if this classname is already identified as a superclass.
						String msg = String.format(
								"You are using class '%s', a superclass, in your  input declaration. Try using a class that inherits from '%s' instead.",
								classname, classname);
						warning(msg, decInp, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);
					}
				}		
			}
			
			if(model.getOutput() != null) {	
				EList<OutputDeclaration> outputDecs = model.getOutput().getDeclarations();
				
				for(int i = 0; i < outputDecs.size(); i++){
					OutputDeclaration decOut = (OutputDeclaration) outputDecs.get(i);
					String classname = decOut.getType().getName();
					objectDeclarations.put(decOut.getName(), classname); //Keep track for later
					if(superclasses.contains(classname)){
						//if this classname is already identified as a superclass.
						String msg = String.format(
								"You are using class '%s', a superclass, in your  output declaration. Try using a class that inherits from '%s' instead.",
								classname, classname);
						warning(msg, decOut, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);
					}
				}	
			}
			
			if(model.getLinks() != null) {
				//Only need to check input and output at the moment since parts and requires use models.
				//if target or source has an object name that corresponds to a class name that is in the superclasses list, send a warning
				EList<LinkDeclaration> linkDecs = model.getLinks().getDeclarations();
				
				//Sources
				for(int i = 0; i < linkDecs.size(); i++){
					//For link declarations
					List<LinkableReference> linkList = new ArrayList<LinkableReference>();
					//compile linkable references from sources and targets.
					linkList.add(linkDecs.get(i).getSource());
					linkList.add(linkDecs.get(i).getTarget());
						
					for(int j = 0; j < linkList.size(); j++){
						//For linkable references
						LinkableReference linkRef = (LinkableReference) linkList.get(j);
						EList<EObject> crossLinks = linkRef.eCrossReferences();
						for(int l = 0; l < crossLinks.size(); l++){ 
							EObject crossLink = crossLinks.get(l); //FieldDeclaration
							EClass crossClass = crossLink.eClass();
							if(crossClass == SystemDescriptorPackage.Literals.INPUT_DECLARATION || crossClass == SystemDescriptorPackage.Literals.OUTPUT_DECLARATION ){
								FieldDeclaration crossLinkData = (FieldDeclaration) crossLink;
								String objectname = crossLinkData.getName();
								String classname = objectDeclarations.get(objectname);
								if(classname != null && superclasses.contains(classname)){
										
									EStructuralFeature eFeature = null;
									if(linkRef.eClass() == SystemDescriptorPackage.Literals.FIELD_REFERENCE){
										eFeature = SystemDescriptorPackage.Literals.FIELD_REFERENCE__FIELD_DECLARATION;
									} else if (linkRef.eClass() == SystemDescriptorPackage.Literals.LINKABLE_REFERENCE) {
										eFeature = SystemDescriptorPackage.Literals.LINKABLE_EXPRESSION__REF;
									}
									
									//if this classname is already identified as a superclass.
									String msg = String.format(
											"You are using class '%s', a superclass, in your  link declaration. Try using a class that inherits from '%s' instead.",
											classname, classname);
									warning(msg, linkRef, eFeature);
								}
							}			
						}
					} 
				}			
			}
			
			if(model.getScenarios() != null) {
				//Only need to check input and output at the moment since parts and requires use models.
				
				EList<Scenario> scenarios = model.getScenarios();
				
				//Collect together all the steps.
				for(int i = 0; i < scenarios.size(); i++){
					List<Step> steps = 	new ArrayList<Step>();
					Scenario scenario = scenarios.get(i);
					
					GivenDeclaration given = scenario.getGiven();
					if(given != null){
						EList<GivenStep> stepsGiven = given.getSteps();
						for(int j = 0; j < stepsGiven.size(); j++){
							//Since Java won't let us add a <GivenStep> list to a <Step> list
							steps.add((Step) stepsGiven.get(j));
						}	
					}
					
					WhenDeclaration when = scenario.getWhen();
					if(when != null){
						EList<WhenStep> stepsWhen = when.getSteps();
						for(int j = 0; j < stepsWhen.size(); j++){
							//Since Java won't let us add a <GivenStep> list to a <Step> list
							steps.add((Step) stepsWhen.get(j));
						}
					}
					
					ThenDeclaration then = scenario.getThen();
					if(then != null){
						EList<ThenStep> stepsThen = then.getSteps();
						for(int j = 0; j < stepsThen.size(); j++){
							//Since Java won't let us add a <GivenStep> list to a <Step> list
							steps.add((Step) stepsThen.get(j));
						}
					}
					
					for(int j = 0; j < steps.size(); j++){
						Step step = steps.get(j);
						EList<String> params = step.getParameters();
						
						for(int l = 0; l < params.size(); l++){
							String objectname = params.get(l);
							String classname = objectDeclarations.get(objectname);
							if(superclasses.contains(classname))
							{
								EStructuralFeature eFeature = null;
								if(step.eClass() == SystemDescriptorPackage.Literals.GIVEN_STEP){
									eFeature = SystemDescriptorPackage.Literals.SCENARIO__GIVEN;
								} else if (step.eClass() == SystemDescriptorPackage.Literals.WHEN_STEP) {
									eFeature = SystemDescriptorPackage.Literals.SCENARIO__WHEN;
								} else if (step.eClass() == SystemDescriptorPackage.Literals.THEN_STEP) {
									eFeature = SystemDescriptorPackage.Literals.SCENARIO__THEN;
								}
								
								String msg = String.format(
										"You are using class '%s', a superclass, in your scenario declaration. Try using a class that inherits from '%s' instead.",
										classname, classname);
								warning(msg, scenario, eFeature);	
							}
						}
					}
				}			
			}
					
	//		for(int i = 0; i < scenarios.size(); i++){
			//if target or source has an object name that corresponds to a class name that is in the superclasses list, send a warning
	//			Scenario scenario = scenarios.get(i);
	//			scenario
	//			String decClassName = decInp.getType().getName();
	//			if(!declarationClasses.contains(decClassName)){
	//				System.out.println("Adding class to dec classes");
	//				declarationClasses.add(decInp);	
	//			}
	//		}
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
