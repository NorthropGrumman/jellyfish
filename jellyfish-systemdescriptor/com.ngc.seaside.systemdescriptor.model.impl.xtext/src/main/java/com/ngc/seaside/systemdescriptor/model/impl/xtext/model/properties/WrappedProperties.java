package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.collection.WrappingNamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Properties;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

import org.eclipse.emf.common.util.EList;

import java.util.function.Function;

public class WrappedProperties extends WrappingNamedChildCollection<PropertyFieldDeclaration, IProperties, IProperty>
      implements IProperties {

   public WrappedProperties(EList<PropertyFieldDeclaration> wrapped,
                            Function<PropertyFieldDeclaration, IProperty> wrapperFunction,
                            Function<IProperty, PropertyFieldDeclaration> unwrapperFunction,
                            Function<PropertyFieldDeclaration, String> namingFunction) {
      super(wrapped, wrapperFunction, unwrapperFunction, namingFunction);
   }

   public static Properties toXtext(IWrapperResolver resolver, IProperties properties) {
      Properties xtextProperties = SystemDescriptorFactory.eINSTANCE.createProperties();

      for (IProperty property : properties) {
         xtextProperties.getDeclarations().add(AbstractWrappedProperty.toXTextPropertyFieldDeclaration(resolver,
                                                                                                       property));
      }

      return xtextProperties;
   }

   public static IProperties fromXtext(IWrapperResolver resolver, Properties properties) {
      return new WrappedProperties(
            properties.getDeclarations(),
            property -> AbstractWrappedProperty.getWrappedPropertiesFieldReference(resolver, property),
            property -> AbstractWrappedProperty.toXTextPropertyFieldDeclaration(resolver, property),
            PropertyFieldDeclaration::getName);
   }
}
