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
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataType;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

/**
 * Adapts a {@link DataFieldDeclaration} instance to an {@link IDataField}.
 *
 * This class is not threadsafe.
 */
public class WrappedDataField extends AbstractWrappedXtext<DataFieldDeclaration> implements IDataField {

   private IMetadata metadata;

   public WrappedDataField(IWrapperResolver resolver, DataFieldDeclaration wrapped) {
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
      wrapped.setType(DataType.valueOf(type.name()));
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
   public String getName() {
      return wrapped.getName();
   }

   @Override
   public IData getParent() {
      return resolver.getWrapperFor((Data) wrapped.eContainer());
   }

   /**
    * Creates a new {@code DataFieldDeclaration} that is equivalent to the given field.  Changes to the {@code
    * IDataField} are not reflected in the returned {@code DataFieldDeclaration} after construction.
    */
   public static DataFieldDeclaration toXtext(IDataField field) {
      Preconditions.checkNotNull(field, "field may not be null!");
      DataFieldDeclaration x = SystemDescriptorFactory.eINSTANCE.createDataFieldDeclaration();
      x.setMetadata(WrappedMetadata.toXtextJson(field.getMetadata()));
      x.setName(field.getName());
      x.setType(DataType.valueOf(field.getType().name()));
      return x;
   }
}
