package com.ngc.seaside.systemdescriptor.validation;

import org.eclipse.xtext.validation.Check;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldReference;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableExpression;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableReference;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
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
}
