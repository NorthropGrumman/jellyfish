package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import java.util.Collection;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyEnumerationValue;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.data.WrappedEnumeration;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.util.ConversionUtil;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Enumeration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedPropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

public class WrappedEnumerationProperty extends AbstractWrappedProperty<ReferencedPropertyFieldDeclaration> {

   public WrappedEnumerationProperty(IWrapperResolver resolver, ReferencedPropertyFieldDeclaration wrapped) {
      super(resolver, wrapped);
      if (!(wrapped.getDataModel() instanceof Enumeration)) {
         throw new IllegalArgumentException("Expected reference property field declaration to be an enumeration");
      }
   }

   @Override
   public IPropertyEnumerationValue getEnumeration() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public Collection<IPropertyEnumerationValue> getEnumerations() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public DataTypes getType() {
      return DataTypes.ENUM;
   }

   public static PropertyFieldDeclaration toXtextReferencedPropertyFieldDeclaration(IWrapperResolver resolver,
            IProperty property) {
      Preconditions.checkNotNull(resolver, "resolver must not be null!");
      Preconditions.checkNotNull(property, "property must not be null!");
      if (property.getType() != DataTypes.ENUM) {
         throw new IllegalArgumentException("property is not of type enumeration");
      }
      ReferencedPropertyFieldDeclaration declaration = SystemDescriptorFactory.eINSTANCE.createReferencedPropertyFieldDeclaration();
      declaration.setName(property.getName());
      declaration.setCardinality(ConversionUtil.convertCardinalityToXtext(property.getCardinality()));
      declaration.setDataModel(WrappedEnumeration.toXTextEnumeration(resolver, property.getReferencedEnumeration()));
      return declaration;
   }

}
