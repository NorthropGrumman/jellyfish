package com.ngc.seaside.systemdescriptor.validation;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.xtext.validation.Check;

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
   
}
