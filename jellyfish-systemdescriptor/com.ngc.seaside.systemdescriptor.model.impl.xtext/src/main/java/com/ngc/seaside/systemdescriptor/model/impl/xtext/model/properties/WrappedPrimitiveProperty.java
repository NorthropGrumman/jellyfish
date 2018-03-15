package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import java.util.Collection;
import java.util.Optional;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyPrimitiveValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValues;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.util.ConversionUtil;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataType;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitivePropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

public class WrappedPrimitiveProperty extends AbstractWrappedProperty<PrimitivePropertyFieldDeclaration> {

   public WrappedPrimitiveProperty(IWrapperResolver resolver, PrimitivePropertyFieldDeclaration wrapped) {
      super(resolver, wrapped);
   }

   @Override
   public IPropertyPrimitiveValue getPrimitive() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public IPropertyValues<IPropertyPrimitiveValue> getPrimitives() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public DataTypes getType() {
      return DataTypes.valueOf(wrapped.getType().name());
   }

   public static PrimitivePropertyFieldDeclaration toXtextPrimitivePropertyFieldDeclaration(IWrapperResolver resolver,
            IProperty property) {
      Preconditions.checkNotNull(resolver, "resolver must not be null!");
      Preconditions.checkNotNull(property, "property must not be null!");
      if (property.getType() == DataTypes.DATA || property.getType() == DataTypes.ENUM) {
         throw new IllegalArgumentException("property is not a primitive type");
      }
      PrimitivePropertyFieldDeclaration declaration =
            SystemDescriptorFactory.eINSTANCE.createPrimitivePropertyFieldDeclaration();
      declaration.setName(property.getName());
      declaration.setCardinality(ConversionUtil.convertCardinalityToXtext(property.getCardinality()));
      declaration.setType(PrimitiveDataType.valueOf(property.getType().toString()));
      return declaration;
   }


}
