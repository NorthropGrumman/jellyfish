package com.ngc.seaside.systemdescriptor.model.impl.basic.data;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

import java.util.Objects;

/**
 * Implements an IDataField.
 *
 * @author psnell
 */
public class DataField implements IDataField {

   protected final String name;
   protected IData parent;
   protected IMetadata metadata;
   protected IData referencedDataType;
   protected DataTypes type;

   public DataField(String name) {
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
   public IMetadata getMetadata() {
      return metadata;
   }

   @Override
   public DataField setMetadata(IMetadata metadata) {
      this.metadata = metadata;
      return this;
   }

   @Override
   public DataTypes getType() {
      return type;
   }

   @Override
   public IDataField setType(DataTypes type) {
      this.type = Preconditions.checkNotNull(type, "type may not be null!");
      return this;
   }

   @Override
   public IData getReferencedDataType() {
      return referencedDataType;
   }

   @Override
   public IDataField setReferencedDataType(IData dataType) {
      this.referencedDataType = dataType;
      return this;
   }

   public DataField setParent(IData parent) {
      this.parent = parent;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof DataField)) {
         return false;
      }
      DataField dataField = (DataField) o;
      return Objects.equals(name, dataField.name) && parent == dataField.parent
             && Objects.equals(metadata, dataField.metadata)
             && type == dataField.type
             && Objects.equals(referencedDataType, dataField.referencedDataType);
   }

   @Override
   public int hashCode() {
      return Objects.hash(name, System.identityHashCode(parent), metadata, type, referencedDataType);
   }

   @Override
   public String toString() {
      return "DataField[" + "name='" + name + '\'' + ", parent=" + (parent == null ? "null" : parent.getName())
             + ", metadata=" + metadata + ", type=" + type + ", referencedDataType=" + referencedDataType + ']';
   }
}
