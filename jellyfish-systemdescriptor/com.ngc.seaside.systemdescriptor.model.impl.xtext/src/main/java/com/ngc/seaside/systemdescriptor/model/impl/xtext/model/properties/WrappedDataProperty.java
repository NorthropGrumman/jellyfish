package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import java.util.Collection;
import java.util.Optional;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.util.ConversionUtil;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedPropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

public class WrappedDataProperty extends AbstractWrappedProperty<ReferencedPropertyFieldDeclaration> {

   private final IData referencedType;

   public WrappedDataProperty(IWrapperResolver resolver, ReferencedPropertyFieldDeclaration wrapped) {
      super(resolver, wrapped);
      if (!(wrapped.getDataModel() instanceof Data)) {
         throw new IllegalArgumentException("Expected reference property field declaration to be an enumeration");
      }
      this.referencedType = resolver.getWrapperFor((Data) wrapped.getDataModel());
   }

   @Override
   public IData getReferencedDataType() {
      return referencedType;
   }

   @Override
   public IPropertyDataValue getData() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public Optional<Collection<IPropertyDataValue>> getDatas() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public DataTypes getType() {
      return DataTypes.DATA;
   }

   public static ReferencedPropertyFieldDeclaration toXtextReferencedPropertyFieldDeclaration(IWrapperResolver resolver,
            IProperty property) {
      Preconditions.checkNotNull(resolver, "resolver must not be null!");
      Preconditions.checkNotNull(property, "property must not be null!");
      if (property.getType() != DataTypes.DATA) {
         throw new IllegalArgumentException("property is not of type data");
      }
      ReferencedPropertyFieldDeclaration declaration = SystemDescriptorFactory.eINSTANCE.createReferencedPropertyFieldDeclaration();
      declaration.setName(property.getName());
      declaration.setCardinality(ConversionUtil.convertCardinalityToXtext(property.getCardinality()));
      IData data = property.getReferencedDataType();
      declaration.setDataModel(resolver.findXTextData(data.getName(), data.getParent().getName()).get());
      return declaration;
   }


}
