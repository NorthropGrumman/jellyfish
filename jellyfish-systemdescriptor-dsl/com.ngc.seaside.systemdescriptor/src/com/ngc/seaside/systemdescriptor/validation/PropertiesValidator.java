package com.ngc.seaside.systemdescriptor.validation;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.validation.Check;

import com.google.common.base.Objects;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Properties;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

public class PropertiesValidator extends AbstractUnregisteredSystemDescriptorValidator {

   @Check
   public void checkNames(Properties properties) {
      Set<String> propertyNames = new HashSet<>();
      for (PropertyFieldDeclaration property : properties.getDeclarations()) {
         String name = property.getName();
         if (name.indexOf('^') >= 0) {
            String msg = String.format(
               "Cannot use '^' to escape the field named %s.",
               name);
            error(msg, property, SystemDescriptorPackage.Literals.PROPERTY_FIELD_DECLARATION__NAME);
         }
         if (!propertyNames.add(name)) {
            String msg = String.format(
               "There is already another property declared with the name %s.",
               name);
            error(msg, property, SystemDescriptorPackage.Literals.PROPERTY_FIELD_DECLARATION__NAME);
         }
      }
   }

   /**
    * Checks that the properties are valid for the given parent. Properties are only allowed for models, parts, requirements and links.
    * 
    * @param properties
    */
   @Check
   public void checkParent(Properties properties) {
      if (Objects.equal(properties.eContainer().eClass(), SystemDescriptorPackage.Literals.MODEL)) {
         return;
      }
      Collection<EClass> validPropertiesContainers = Arrays.asList(
         SystemDescriptorPackage.Literals.PART_DECLARATION,
         SystemDescriptorPackage.Literals.REQUIRE_DECLARATION,
         SystemDescriptorPackage.Literals.LINK_DECLARATION);

      EObject parent = properties;
      EClass cls = null;
      while ((parent = parent.eContainer()) != null) {
         EClass parentClass = parent.eClass();
         for (EClass validClass : validPropertiesContainers) {
            if (validClass.isSuperTypeOf(parentClass)) {
               return;
            }
         }
         if (cls == null && !Objects.equal(parentClass, SystemDescriptorPackage.Literals.DECLARATION_DEFINITION)) {
            cls = parentClass;
         }
      }

      error("Properties are not allowed for " + cls.getName(), properties, null);
   }

}
