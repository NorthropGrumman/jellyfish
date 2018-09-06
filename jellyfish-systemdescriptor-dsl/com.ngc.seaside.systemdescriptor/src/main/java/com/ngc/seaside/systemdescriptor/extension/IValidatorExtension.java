/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.systemdescriptor.extension;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * An plugin validation interface to add validation logic at runtime.
 */
public interface IValidatorExtension {

   /**
    * Invoked to validate the given object
    *
    * @param source the object to validate
    * @param helper used to add validation errors
    */
   void validate(EObject source, ValidationHelper helper);

   /**
    * Helper interface provided to {@code IValidatorExtension}s to make it easy
    * to declare errors.
    */
   interface ValidationHelper {

      /**
       * Declares an error.
       *
       * @param message the error message
       * @param source  the offending object
       * @param feature the feature of the offending object that is invalid
       */
      void error(String message, EObject source, EStructuralFeature feature);

      /**
       * Declares an warning.
       *
       * @param message the error message
       * @param source  the offending object
       * @param feature the feature of the offending object that is invalid
       */
      void warning(String message, EObject source, EStructuralFeature feature);

      /**
       * Declares in informational message.
       *
       * @param message the error message
       * @param source  the offending object
       * @param feature the feature of the offending object that is invalid
       */
      void info(String message, EObject source, EStructuralFeature feature);
   }
}
