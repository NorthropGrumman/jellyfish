package com.ngc.seaside.systemdescriptor.service.impl.xtext.validation;

import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import java.lang.reflect.Method;

public class ValidationBridgeUtil {

   public static <T> EStructuralFeature getFeature(T object, EObject xtext, Method method) {
      return SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION__NAME;
   }
}
