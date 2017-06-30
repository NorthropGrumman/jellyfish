package com.ngc.seaside.systemdescriptor.model.impl.xtext.data;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataType;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

/**
 * Adapts a {@link PrimitiveDataFieldDeclaration} instance to an {@link IDataField}.
 *
 * This class is not threadsafe.
 */
public class WrappedPrimitiveDataField extends AbstractWrappedXtext<PrimitiveDataFieldDeclaration>
      implements IDataField {

   private IMetadata metadata;

   public WrappedPrimitiveDataField(IWrapperResolver resolver, PrimitiveDataFieldDeclaration wrapped) {
      super(resolver, wrapped);
      this.metadata = WrappedMetadata.fromXtextJson(wrapped.getMetadata());
   }

   @Override
   public DataTypes getType() {
      return DataTypes.valueOf(wrapped.getType().name());
   }

   @Override
   public IDataField setType(DataTypes type) {
      Preconditions.checkNotNull(type, "type may not be null!");
      Preconditions.checkArgument(type != DataTypes.DATA,
                                  "the type of this field must be a primitive, it cannot be changed to reference other"
                                  + " data types!");
      wrapped.setType(PrimitiveDataType.valueOf(type.name()));
      return this;
   }

   @Override
   public IMetadata getMetadata() {
      return metadata;
   }

   @Override
   public IDataField setMetadata(IMetadata metadata) {
      Preconditions.checkNotNull(metadata, "metadata may not be null!");
      this.metadata = metadata;
      wrapped.setMetadata(WrappedMetadata.toXtextJson(metadata));
      return this;
   }

   @Override
   public IData getReferencedDataType() {
      return null; // This is a primitive data type, it can never reference other data.
   }

   @Override
   public IDataField setReferencedDataType(IData dataType) {
      throw new IllegalStateException("the type of this field must be a primitive, it cannot be changed to reference"
                                      + " other data types!");
   }

   @Override
   public String getName() {
      return wrapped.getName();
   }

   @Override
   public IData getParent() {
      return resolver.getWrapperFor((Data) wrapped.eContainer());
   }

   /**
    * Creates a new {@code PrimitiveDataFieldDeclaration} that is equivalent to the given field.  Changes to the {@code
    * IDataField} are not reflected in the returned {@code PrimitiveDataFieldDeclaration} after construction.
    */
   public static PrimitiveDataFieldDeclaration toXtext(IDataField field) {
      Preconditions.checkNotNull(field, "field may not be null!");
      Preconditions.checkArgument(
            field.getType() != DataTypes.DATA,
            "cannot create a PrimitiveDataFieldDeclaration for an IDataField that references other data!");
      PrimitiveDataFieldDeclaration x = SystemDescriptorFactory.eINSTANCE.createPrimitiveDataFieldDeclaration();
      x.setMetadata(WrappedMetadata.toXtextJson(field.getMetadata()));
      x.setName(field.getName());
      x.setType(PrimitiveDataType.valueOf(field.getType().name()));
      return x;
   }
}
