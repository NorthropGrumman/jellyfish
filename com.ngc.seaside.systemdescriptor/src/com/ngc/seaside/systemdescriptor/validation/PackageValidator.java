package com.ngc.seaside.systemdescriptor.validation;

import org.eclipse.xtext.validation.Check;


import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

public class PackageValidator extends AbstractSystemDescriptorValidator {

	/**
	 * Validates that the user did not try to escape a keyword with ^ in the
	 * name of the package.
	 * 
	 * @param model is the model to evaluate
	 */	
	@Check
	public void checkUsageOfEscapeHatCharacter(Package p) {
		// Verify the data name doesn't not have the escape hat	
		if (p.getName().indexOf('^') >= 0) {
			String msg = String.format(
					"Cannot use '^' to escape the package name %s.",
					p.getName());
			error(msg, p, SystemDescriptorPackage.Literals.PACKAGE__ELEMENT);
		}
		
	}
}
