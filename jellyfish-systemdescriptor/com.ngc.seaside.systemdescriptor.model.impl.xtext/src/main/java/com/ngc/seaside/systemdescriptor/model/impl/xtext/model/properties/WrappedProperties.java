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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.collection.DeferredNamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Properties;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueAssignment;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

import java.util.HashSet;
import java.util.Set;

public class WrappedProperties extends DeferredNamedChildCollection<IProperties, IProperty>
      implements IProperties {

   private final WrapperPropertyFactory factory;
   private final Properties properties;

   /**
    * Creates new wrapped properties.
    */
   public WrappedProperties(IWrapperResolver resolver, Properties properties) {
      this.properties = Preconditions.checkNotNull(properties, "properties may not be null!");
      factory = new WrapperPropertyFactory(resolver);
   }

   @Override
   protected void initialize() {
      Set<String> propertyNamesAlreadyAdded = new HashSet<>();
      for (PropertyValueAssignment assignment : properties.getAssignments()) {
         if (propertyNamesAlreadyAdded.add(assignment.getExpression().getDeclaration().getName())) {
            add(factory.newProperty(assignment));
         }
      }

      for (PropertyFieldDeclaration declaration : properties.getDeclarations()) {
         if (propertyNamesAlreadyAdded.add(declaration.getName())) {
            add(factory.newProperty(declaration));
         }
      }
   }

   /**
    * Converts the given object to an XText object.
    */
   public static Properties toXtext(IWrapperResolver resolver, IProperties properties) {
      Properties xtextProperties = SystemDescriptorFactory.eINSTANCE.createProperties();

      for (IProperty property : properties) {
         xtextProperties.getDeclarations().add(AbstractWrappedProperty.toXTextPropertyFieldDeclaration(resolver,
                                                                                                       property));
      }

      return xtextProperties;
   }
}
