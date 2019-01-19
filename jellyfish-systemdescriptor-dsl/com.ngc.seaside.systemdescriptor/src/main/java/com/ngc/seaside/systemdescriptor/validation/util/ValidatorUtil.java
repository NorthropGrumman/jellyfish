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
