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
