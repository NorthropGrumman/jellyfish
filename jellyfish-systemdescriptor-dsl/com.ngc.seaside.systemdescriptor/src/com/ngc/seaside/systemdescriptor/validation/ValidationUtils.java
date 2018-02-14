package com.ngc.seaside.systemdescriptor.validation;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

public class ValidationUtils {

	private ValidationUtils() {
	}
	
	public static String getFullyQualifiedName(Data data) {
		return ((Package) data.eContainer()).getName() + "." + data.getName();
	}
	
	public static String getFullyQualifiedName(Model model) {
		return ((Package) model.eContainer()).getName() + "." + model.getName();
	}
	
}
