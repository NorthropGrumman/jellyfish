package com.ngc.seaside.systemdescriptor.model.impl.xtext.data;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.util.ConversionUtil;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

/**
 * Adapts a {@link ReferencedDataFieldDeclaration} instance to an {@link IDataField}.
 *
 * This class is not threadsafe.
 */
public class WrappedReferencedDataField extends AbstractWrappedDataField<ReferencedDataFieldDeclaration> {

   public WrappedReferencedDataField(IWrapperResolver resolver, ReferencedDataFieldDeclaration wrapped) {
      super(resolver, wrapped);
   }

   @Override
   public DataTypes getType() {
      return DataTypes.DATA; // Only other data can be referenced, not primitive data types.
   }

   @Override
   public IDataField setType(DataTypes type) {
      Preconditions.checkNotNull(type, "type may not be null!");
      Preconditions.checkArgument(type == DataTypes.DATA,
                                  "the type of this field must be another data type, it cannot be changed to reference"
                                  + " primitives!");
      // We don't actually have to do anything here.
      return this;
   }

   @Override
   public IData getReferencedDataType() {
      return resolver.getWrapperFor(wrapped.getData());
   }

   @Override
   public IDataField setReferencedDataType(IData data) {
      Preconditions.checkNotNull(data, "data may not be null!");
      wrapped.setData(resolver.findXTextData(data.getName(), data.getParent().getName()).get());
      return this;
   }

   /**
    * Creates a new {@code ReferencedDataFieldDeclaration} that is equivalent
    * to the given data ref. Changes to the {@code IReferencedDataField} are
    * not reflected in the returned {@code ReferencedDataFieldDeclaration}
    * after construction.
    */
   public static ReferencedDataFieldDeclaration toXtext(IWrapperResolver resolver,
                                                        IDataField dataRef) {
      Preconditions.checkNotNull(dataRef, "dataRef may not be null!");
      Preconditions.checkArgument(
            dataRef.getType() == DataTypes.DATA,
            "cannot create a ReferencedDataFieldDeclaration for an IDataField that references a primitive type!");
      ReferencedDataFieldDeclaration x = SystemDescriptorFactory.eINSTANCE.createReferencedDataFieldDeclaration();
      x.setName(dataRef.getName());
      x.setData(resolver.findXTextData(dataRef.getReferencedDataType().getName(),
                                       dataRef.getReferencedDataType().getParent().getName()).get());
      x.setCardinality(ConversionUtil.convertCardinalityToXtext(dataRef.getCardinality()));
      return x;
   }
}
