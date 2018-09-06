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
package com.ngc.seaside.systemdescriptor.validation;

import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Properties;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;
import com.ngc.seaside.systemdescriptor.utils.SdUtils;
import com.ngc.seaside.systemdescriptor.validation.util.ValidatorUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.validation.Check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Validates the refine Require Declaration portion of Requires
 */
public class RequiresValidator extends AbstractUnregisteredSystemDescriptorValidator {

   /**
    * Entry into this validator for xtext
    *
    * @param requirement thats being validated
    */
   @Check
   public void checkLinkDeclaration(RequireDeclaration requirement) {

      if (requirement.eClass().equals(SystemDescriptorPackage.Literals.REFINED_REQUIRE_DECLARATION)) {
         //Don't change order unless you have thought about what
         // should be checked first
         setFieldDeclarationError(ValidatorUtil.checkForNonRefinedModelUsingRefinedfields(requirement),
                                  requirement);

         setFieldDeclarationError(
               ValidatorUtil.checkForRefinementOfAFieldThatsNotInModelBeingRefined(requirement),
               requirement);
      }
   }

   /**
    * Checks that the properties are not being redefined on Requirements.
    *
    * @param properties that are being checked
    */
   @Check
   public void checkRedefiningRequiresProperties(Properties properties) {
      EList<PropertyFieldDeclaration> fieldDecs = properties.getDeclarations();

      if (!fieldDecs.isEmpty()) {
         Model parentModel = SdUtils.getContainingModel(fieldDecs.get(0)).getRefinedModel();
         Map<String, PropertyFieldDeclaration> hierarchyLinkProps = getRequiresProperties(parentModel);

         for (PropertyFieldDeclaration propFieldDec : fieldDecs) {
            if (hierarchyLinkProps.containsKey(propFieldDec.getName())) {
               PropertyFieldDeclaration parentFieldDec = hierarchyLinkProps.get(propFieldDec.getName());
               Model parentFieldDecModel = SdUtils.getContainingModel(parentFieldDec);
               String msg = String.format(
                     "Cannot redefine property '%s' because '%s' model has that property already defined.",
                     propFieldDec.eClass().getName(),
                     parentFieldDecModel.getName());
               error(msg, properties, null);
               break;
            }
         }
      }
   }

   /**
    * Goes through the Model hierarchy and retrieves all the Requirements properties
    *
    * @param model thats the starting point in the hierarchy
    * @return A collection of all the properties
    */
   private static Map<String, PropertyFieldDeclaration> getRequiresProperties(Model model) {
      Collection<RequireDeclaration> requirements = new ArrayList<>();
      Map<String, PropertyFieldDeclaration> fields = new HashMap<>();
      do {
         if (model.getLinks() != null) {
            requirements.addAll(model.getRequires().getDeclarations());
         }
         model = model.getRefinedModel();
      } while (model != null);

      for (RequireDeclaration requireDec : requirements) {
         if (requireDec instanceof RequireDeclaration) {
            if (requireDec.getDefinition() != null) {
               if (requireDec.getDefinition().getProperties().getDeclarations() != null) {
                  for (PropertyFieldDeclaration fieldDec : requireDec.getDefinition().getProperties()
                        .getDeclarations()) {
                     fields.put(fieldDec.getName(), fieldDec);
                  }
               }
            }

         }
      }
      return fields;
   }

   private void setFieldDeclarationError(String error, FieldDeclaration requirement) {
      if (!error.isEmpty()) {
         error(error, requirement, SystemDescriptorPackage.Literals.FIELD_DECLARATION__NAME);
      }
   }

}
