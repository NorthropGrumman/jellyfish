package com.ngc.seaside.systemdescriptor.validation;

import org.eclipse.xtext.validation.Check;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

public class ScenarioValidator extends AbstractSystemDescriptorValidator {


	/**
	 * Validates that the user did not try to escape a keyword with ^ in the
	 * name of the model.
	 * 
	 * @param model is the model to evaluate
	 */	
	@Check
	public void checkUsageOfEscapeHatCharacter(Scenario scenario) {
		// Verify the data name doesn't not have the escape hat
		if (scenario.getName().indexOf('^') >= 0) {
			String msg = String.format(
					"Cannot use '^' to escape the scenario name %s.",
					scenario.getName());
			error(msg, scenario, SystemDescriptorPackage.Literals.SCENARIO__NAME);
		}
		
	}
	
}
