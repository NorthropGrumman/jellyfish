package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Properties;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueAssignment;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

import java.util.HashSet;
import java.util.Set;

public class WrappedProperties extends NamedChildCollection<IProperties, IProperty>
      implements IProperties {

   public WrappedProperties(IWrapperResolver resolver, Properties properties) {
      // TODO: Create wrappers first using assignments, then default to declarations.
      // TODO TH: FIX

      WrapperPropertyFactory factory = new WrapperPropertyFactory(resolver);
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

   //   public WrappedProperties(EList<PropertyFieldDeclaration> wrapped,
//                            Function<PropertyFieldDeclaration, IProperty> wrapperFunction,
//                            Function<IProperty, PropertyFieldDeclaration> unwrapperFunction,
//                            Function<PropertyFieldDeclaration, String> namingFunction) {
//      super(wrapped, wrapperFunction, unwrapperFunction, namingFunction);
//   }
//
//   public static IProperties fromXtext(IWrapperResolver resolver, Properties properties) {
//      return new WrappedProperties(
//            properties.getDeclarations(),
//            property -> AbstractWrappedProperty.getWrappedPropertiesFieldReference(resolver, property),
//            property -> AbstractWrappedProperty.toXTextPropertyFieldDeclaration(resolver, property),
//            PropertyFieldDeclaration::getName);
//   }
//
   public static Properties toXtext(IWrapperResolver resolver, IProperties properties) {
      Properties xtextProperties = SystemDescriptorFactory.eINSTANCE.createProperties();

      for (IProperty property : properties) {
         xtextProperties.getDeclarations().add(AbstractWrappedProperty.toXTextPropertyFieldDeclaration(resolver,
                                                                                                       property));
      }

      return xtextProperties;
   }
}
