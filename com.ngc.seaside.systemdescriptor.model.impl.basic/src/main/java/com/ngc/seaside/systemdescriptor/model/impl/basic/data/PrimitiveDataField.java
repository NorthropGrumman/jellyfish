package com.ngc.seaside.systemdescriptor.model.impl.basic.data;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IPrimitiveDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.basic.metadata.Metadata;

import java.util.Objects;

/**
 * Implements an IDataField.
 *
 * @author psnell
 */
public class PrimitiveDataField implements IPrimitiveDataField {

   protected final String name;
   protected IData parent;
   protected DataTypes type;
   protected IMetadata metadata;

   public PrimitiveDataField(String name) {
      Preconditions.checkNotNull(name, "name may not be null!");
      Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");
      this.name = name;
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public IData getParent() {
      return parent;
   }

   @Override
   public DataTypes getType() {
      return type;
   }

   @Override
   public IMetadata getMetadata() {
      return metadata;
   }

   @Override
   public PrimitiveDataField setType(DataTypes type) {
      this.type = type;
      return this;
   }

   @Override
   public PrimitiveDataField setMetadata(IMetadata metadata) {
      this.metadata = metadata;
      return this;
   }

   public IPrimitiveDataField setParent(IData parent) {
      this.parent = parent;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof PrimitiveDataField)) {
         return false;
      }
      PrimitiveDataField dataField = (PrimitiveDataField) o;
      return Objects.equals(name, dataField.name) &&
             parent == dataField.parent &&
             type == dataField.type &&
             Objects.equals(metadata, dataField.metadata);
   }

   @Override
   public int hashCode() {
      return Objects.hash(name, System.identityHashCode(parent), type, metadata);
   }

   @Override
   public String toString() {
      return "PrimitiveDataField[" +
             "name='" + name + '\'' +
             ", parent=" + (parent == null ? "null" : parent.getName()) +
             ", type=" + type +
             ", metadata=" + metadata +
             ']';
   }

   public static IPrimitiveDataField immutable(IPrimitiveDataField dataField) {
      Preconditions.checkNotNull(dataField, "dataField may not be null!");
      ImmutablePrimitiveDataField immutable = new ImmutablePrimitiveDataField(dataField.getName());
      immutable.parent = dataField.getParent();
      immutable.type = dataField.getType();
      immutable.metadata = Metadata.immutable(dataField.getMetadata());
      return immutable;
   }

   static class ImmutablePrimitiveDataField extends PrimitiveDataField {

      private ImmutablePrimitiveDataField(String name) {
         super(name);
      }

      @Override
      public PrimitiveDataField setType(DataTypes type) {
         throw new UnsupportedOperationException("object is not modifiable!");
      }

      @Override
      public PrimitiveDataField setMetadata(IMetadata metadata) {
         throw new UnsupportedOperationException("object is not modifiable!");
      }

      @Override
      public PrimitiveDataField setParent(IData parent) {
         throw new UnsupportedOperationException("object is not modifiable!");
      }
   }
}
