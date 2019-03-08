/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
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
