/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.systemdescriptor.model.impl.basic.data;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
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
   protected IEnumeration referencedEnumeration;
   protected DataTypes type;
   protected FieldCardinality cardinality;

   /**
    * Creates a new data field with the given name.
    */
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
   public FieldCardinality getCardinality() {
      return cardinality;
   }

   @Override
   public IDataField setCardinality(FieldCardinality cardinality) {
      this.cardinality = Preconditions.checkNotNull(cardinality, "cardinality may not be null!");
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

   @Override
   public IEnumeration getReferencedEnumeration() {
      return referencedEnumeration;
   }

   @Override
   public IDataField setReferencedEnumeration(IEnumeration enumeration) {
      this.referencedEnumeration = enumeration;
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
             && Objects.equals(referencedDataType, dataField.referencedDataType)
             && Objects.equals(referencedEnumeration, dataField.referencedEnumeration)
             && cardinality == dataField.cardinality;
   }

   @Override
   public int hashCode() {
      return Objects.hash(name,
                          System.identityHashCode(parent),
                          metadata,
                          type,
                          referencedDataType,
                          referencedEnumeration,
                          cardinality);
   }

   @Override
   public String toString() {
      return "DataField[" + "name='" + name + '\'' + ", parent=" + (parent == null ? "null" : parent.getName())
             + ", metadata=" + metadata + ", type=" + type + ", referencedDataType=" + referencedDataType
             + ", referencedEnumeration=" + referencedEnumeration
             + ", cardinality='" + cardinality + ']';
   }
}
