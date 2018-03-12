package com.ngc.seaside.systemdescriptor.model.impl.xtext.exception;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.emf.ecore.EClass;

/**
 * A type of exception that can be used to indicate a particular XText object was not found.
 */
public class XtextObjectNotFoundException extends RuntimeException {

   public XtextObjectNotFoundException(EClass eclass, String fullyQualifiedName) {
      super(String.format("could not find XText %s type with name %s.", eclass.getName(), fullyQualifiedName));
   }

   public static XtextObjectNotFoundException forModel(IModel model) {
      return new XtextObjectNotFoundException(SystemDescriptorPackage.Literals.MODEL, model.getFullyQualifiedName());
   }

   public static XtextObjectNotFoundException forData(IData data) {
      return new XtextObjectNotFoundException(SystemDescriptorPackage.Literals.DATA, data.getFullyQualifiedName());
   }
}
