package com.ngc.seaside.systemdescriptor.extension;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public interface IValidatorExtension {

	void validate(EObject source, ValidationHelper helper);
	
	interface ValidationHelper {
		void error(String message, EObject source, EStructuralFeature feature);
		
		void warning(String message, EObject source, EStructuralFeature feature);
		
		void info(String message, EObject source, EStructuralFeature feature);
	}
}
