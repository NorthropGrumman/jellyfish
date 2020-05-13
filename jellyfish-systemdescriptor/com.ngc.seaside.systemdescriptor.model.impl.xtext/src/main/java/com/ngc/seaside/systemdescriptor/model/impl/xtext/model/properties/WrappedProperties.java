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
