/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
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
