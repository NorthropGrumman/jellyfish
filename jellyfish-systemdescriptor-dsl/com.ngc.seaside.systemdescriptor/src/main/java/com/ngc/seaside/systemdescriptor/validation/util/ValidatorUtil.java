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
package com.ngc.seaside.systemdescriptor.validation.util;

import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;
import com.ngc.seaside.systemdescriptor.utils.SdUtils;

/**
 * Utility methods to help with validation
 */
public class ValidatorUtil {

   /**
    * Validates that the Model thats not refining another Model can't then do a refine on
    * a fieldDeclaration
    *
    * @param fieldDeclaration thats being validated
    */
   public static String checkForNonRefinedModelUsingRefinedfields(FieldDeclaration fieldDeclaration) {
      // Bring us up to the part model
      String msg = "";
      Model fieldDeclarationModel = SdUtils.getContainingModel(fieldDeclaration);
      if (fieldDeclarationModel != null) {
         if (fieldDeclarationModel.getRefinedModel() == null) {
            msg = String.format(
                  "Field '%s' cannot be refined because model '%s.%s.' does not refine another model.",
                  fieldDeclaration.getName(),
                  ((Package) fieldDeclarationModel.eContainer()).getName(),
                  fieldDeclarationModel.getName());
         }
      }
      return msg;
   }

   /**
    * Validates that the Model or somewhere in the hierarchy that this requirement declaration
    * has already been defined
    *
    * @param fieldDeclaration thats being validated
    */
   public static String checkForRefinementOfAFieldThatsNotInModelBeingRefined(FieldDeclaration fieldDeclaration) {
      String msg = "";
      Model fieldDeclarationModel = SdUtils.getContainingModel(fieldDeclaration);
      if (fieldDeclarationModel != null) {
         if (fieldDeclaration.eClass().equals(SystemDescriptorPackage.Literals.REFINED_PART_DECLARATION)) {
            if (SdUtils.findPartDeclaration(fieldDeclarationModel.getRefinedModel(), fieldDeclaration.getName())
                  == null) {
               msg = String.format(
                     "Cannot refine the part '%s' as no part with that name has been declared in the"
                           + " refinement hierarcy of '%s.%s'.",
                     fieldDeclaration.getName(),
                     fieldDeclarationModel.getName(),
                     ((Package) fieldDeclarationModel.eContainer()).getName());
            }
         } else if (fieldDeclaration.eClass().equals(SystemDescriptorPackage.Literals.REFINED_REQUIRE_DECLARATION)) {
            if (SdUtils.findRequireDeclaration(fieldDeclarationModel.getRefinedModel(), fieldDeclaration.getName())
                  == null) {
               msg = String.format(
                     "Cannot refine the requirement '%s' as no requirement with that name has been declared in the"
                           + " refinement hierarcy of '%s.%s'.",
                     fieldDeclaration.getName(),
                     fieldDeclarationModel.getName(),
                     ((Package) fieldDeclarationModel.eContainer()).getName());
            }
         }

      }
      return msg;
   }
}
