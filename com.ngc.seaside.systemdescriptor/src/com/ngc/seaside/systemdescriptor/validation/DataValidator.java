package com.ngc.seaside.systemdescriptor.validation;

import org.eclipse.xtext.validation.Check;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldReference;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableExpression;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableReference;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

public class DataValidator extends AbstractSystemDescriptorValidator {

	@Check
	public void checkHierarchy(Data data) {
		 
	}

	protected void checkNotInheritSelf(Data data) {

	}
	
	protected void compileSuperClassFields(Data data){
		
	}
}
