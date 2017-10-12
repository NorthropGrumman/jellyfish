package com.ngc.seaside.systemdescriptor.validation;

import org.eclipse.xtext.validation.Check;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

public class DataValidator extends AbstractSystemDescriptorValidator {

	@Check
	public void checkHierarchy(Data data) {
	
	}
	
	@Check
	public void checkBaseDataObject(Data data) {
		// Ensure that the model does not already have a part with the same
		// name.
//		System.out.println("Psssst! data");
//		System.out.println(data);
//		System.out.println(data.eContainingFeature().getClass());
	}
	
	/**
	 * Validates that the user did not try to escape a keyword with ^ in any of 
	 * the data fields or the data name.
	 * 
	 * @param data is the Data object to evaluate
	 */	
	@Check
	public void checkUsageOfEscapeHatCharacter(Data data) {
		// Verify the data name doesn't not have the escape hat
		if (data.getName().charAt(0) == '^') {
			String msg = String.format(
					"Cannot use '^' to escape the data name %s.",
					data.getName());
			error(msg, data, SystemDescriptorPackage.Literals.ELEMENT__NAME);
		}
		
		// then loop through the fields and check the typename and the field name
		for (DataFieldDeclaration f : data.getFields()) {
			if (f.getName().charAt(0) == '^') {
				// Report error
				String msg = String.format(
						"Cannot use '^' to escape the field named %s in data %s.",
						f.getName(),
						data.getName());
				error(msg, f, SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION__NAME);
			}
			
		}
	}
}
