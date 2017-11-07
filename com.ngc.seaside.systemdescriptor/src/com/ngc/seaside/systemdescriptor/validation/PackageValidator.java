package com.ngc.seaside.systemdescriptor.validation;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
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
	
	
	/**
	 * 
	 */
	@Check
	public void validatePackageMatchesFilePath(Package p) {
		// p.name() is "com.ngc"
		List<String> resourceUriPath = p.eResource().getURI().segmentsList(); // [resource, Test, bin, com, ngc, TestModel.sd]
		List<String> packageElements = Arrays.asList(p.getName().split("\\.")); // [com, ngc]
				
		int indexLast = resourceUriPath.size() - 1;
		if (resourceUriPath.get(indexLast).indexOf(".sd") > 0 && p.eResource().getURI().scheme().equals("platform")) { // Confirm that the last element is an .sd file
			int numElements = packageElements.size();
			List<String> uriSublist = resourceUriPath.subList(indexLast - numElements, indexLast);
			
			if (!uriSublist.equals(packageElements)) {
				StringBuffer sb = new StringBuffer();
				sb.append("Package and File Path do not match:\n");
				sb.append("Package: " + p.getName() + "\n");
				sb.append("File URI: " + p.eResource().getURI());
				error(sb.toString(), p, SystemDescriptorPackage.Literals.PACKAGE__ELEMENT);
			}
		}
	}
}
