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
package com.ngc.seaside.systemdescriptor.validation;

import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Properties;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyFieldDeclaration;
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
 * Validates the refine Part Declaration portion of Parts
 */
public class PartsValidator extends AbstractUnregisteredSystemDescriptorValidator {

   /**
    * Entry into this validator for xtext
    *
    * @param part thats being validated
    */
   @Check
   public void checkLinkDeclaration(PartDeclaration part) {

      if (part.eClass().equals(SystemDescriptorPackage.Literals.REFINED_PART_DECLARATION)) {
         //Don't change order unless you have thought about what
         // should be checked first
         setFieldDeclarationError(ValidatorUtil.checkForNonRefinedModelUsingRefinedfields(part),
                                  part);
         setFieldDeclarationError(ValidatorUtil.checkForRefinementOfAFieldThatsNotInModelBeingRefined(part),
                                  part);
      }

   }

   /**
    * Checks that the properties are not being redefined on Parts.
    *
    * @param properties that are being checked
    */
   @Check
   public void checkRedefiningPartsProperties(Properties properties) {
      EList<PropertyFieldDeclaration> fieldDecs = properties.getDeclarations();

      if (!fieldDecs.isEmpty()) {
         Model parentModel = SdUtils.getContainingModel(fieldDecs.get(0)).getRefinedModel();
         Map<String, PropertyFieldDeclaration> hierarchyLinkProps = getPartsProperties(parentModel);

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
    * Goes through the Model hierarchy and retrieves all the Parts properties
    *
    * @param model thats the starting point in the hierarchy
    * @return A collection of all the properties
    */
   private static Map<String, PropertyFieldDeclaration> getPartsProperties(Model model) {
      Collection<PartDeclaration> parts = new ArrayList<>();
      Map<String, PropertyFieldDeclaration> fields = new HashMap<>();
      do {
         if (model.getLinks() != null) {
            parts.addAll(model.getParts().getDeclarations());
         }
         model = model.getRefinedModel();
      } while (model != null);

      for (PartDeclaration partDec : parts) {
         if (partDec instanceof PartDeclaration) {
            if (partDec.getDefinition() != null) {
               if (partDec.getDefinition().getProperties().getDeclarations() != null) {
                  for (PropertyFieldDeclaration fieldDec : partDec.getDefinition().getProperties().getDeclarations()) {
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

