package com.ngc.seaside.systemdescriptor.validation;

import com.google.common.base.Objects;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Properties;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.validation.Check;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

   /**
    * Checks that the properties are not being redefined on links.
    * 
    * @param properties
    */
   @Check
   public void checkRedefiningLinkProperties(Properties properties) {
      EList<PropertyFieldDeclaration> fieldDecs = properties.getDeclarations();

      if (!fieldDecs.isEmpty()) {
         Model parentModel = getModelForProps(fieldDecs.get(0)).getRefinedModel();
         Map<String, PropertyFieldDeclaration> hierarchyLinkProps = getLinkProperties(parentModel);

         for (PropertyFieldDeclaration propFieldDec : fieldDecs) {
            if (hierarchyLinkProps.containsKey(propFieldDec.getName())) {
               PropertyFieldDeclaration parentFieldDec = hierarchyLinkProps.get(propFieldDec.getName());
               Model parentFieldDecModel = getModelForProps(parentFieldDec);
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
    * 
    * Grabs the container that contains the field declaration for this requirement
    * 
    * @param proFieldDec that we want its Model container for
    * @return Model that contains the refined part
    */
   private static Model getModelForProps(PropertyFieldDeclaration proFieldDec) {
      EObject currentObject = proFieldDec;
      boolean modelFound = false;
      Model model = null;
      do {
         if (currentObject.eContainer().eClass().equals(SystemDescriptorPackage.Literals.MODEL)) {
            model = (Model) currentObject.eContainer();
            modelFound = true;
         } else {
            currentObject = currentObject.eContainer();
         }
      } while (!modelFound && currentObject != null);

      return model;
   }

   /**
    * Goes through the Model hierarchy and retrieves all the links properties
    * 
    * @param model thats the starting point in the hierarchy
    * @return A collection of all the properties
    */
   private static Map<String, PropertyFieldDeclaration> getLinkProperties(Model model) {
      Collection<LinkDeclaration> links = new ArrayList<>();
      Map<String, PropertyFieldDeclaration> fields = new HashMap<>();
      do {
         if (model.getLinks() != null) {
            links.addAll(model.getLinks().getDeclarations());
         }
         model = model.getRefinedModel();
      } while (model != null);
      for (LinkDeclaration linkDec : links) {
         if (linkDec instanceof LinkDeclaration) {
            if (linkDec.getDefinition() != null) {
               if (linkDec.getDefinition().getProperties().getDeclarations() != null) {
                  for (PropertyFieldDeclaration fieldDec : linkDec.getDefinition().getProperties().getDeclarations()) {
                     fields.put(fieldDec.getName(), fieldDec);
                  }
               }
            }

         }
      }
      return fields;
   }

}
