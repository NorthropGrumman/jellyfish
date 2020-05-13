/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
