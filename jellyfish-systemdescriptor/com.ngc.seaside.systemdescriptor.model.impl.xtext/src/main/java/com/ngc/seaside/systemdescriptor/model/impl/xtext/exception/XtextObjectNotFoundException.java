package com.ngc.seaside.systemdescriptor.model.impl.xtext.exception;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.emf.ecore.EClass;

/**
 * A type of exception that can be used to indicate a particular XText object was not found.
 */
public class XtextObjectNotFoundException extends RuntimeException {

   /**
    * Creates a new exception.
    */
   public XtextObjectNotFoundException(EClass eclass, String fullyQualifiedName) {
      super(String.format("could not find XText %s type with name %s.",
                          eclass.getName().toLowerCase(),
                          fullyQualifiedName));
   }

   /**
    * Creates a new exception for the given model.
    */
   public static XtextObjectNotFoundException forModel(IModel model) {
      return new XtextObjectNotFoundException(SystemDescriptorPackage.Literals.MODEL, model.getFullyQualifiedName());
   }

   /**
    * Creates a new exception for the given data object.
    */
   public static XtextObjectNotFoundException forData(IData data) {
      return new XtextObjectNotFoundException(SystemDescriptorPackage.Literals.DATA, data.getFullyQualifiedName());
   }

   /**
    * Creates a new exception for the given enum object.
    */
   public static XtextObjectNotFoundException forEnum(IData enumeration) {
      return new XtextObjectNotFoundException(SystemDescriptorPackage.Literals.ENUMERATION,
                                              enumeration.getFullyQualifiedName());
   }
}
