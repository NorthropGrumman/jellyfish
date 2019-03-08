/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;

import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of the interface IData. Stores IDataField objects and
 * metadata.
 *
 * @author psnell
 */
public class Data implements IData {

   protected IPackage parent;
   protected final String name;
   protected IMetadata metadata;
   protected final INamedChildCollection<IData, IDataField> fields;
   protected IData superDataType;


   private Data(String name, INamedChildCollection<IData, IDataField> fields) {
      this.name = name;
      this.fields = fields;
   }

   /**
    * Creates a new data object with the given name.
    */
   public Data(String name) {
      Preconditions.checkNotNull(name, "name may not be null!");
      Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");
      this.name = name;
      this.fields = new NamedChildCollection<IData, IDataField>().setOnChildAdded(field -> {
         if (field instanceof DataField) {
            DataField casted = (DataField) field;
            casted.setParent(Data.this);
         }
      }).setOnChildRemoved(field -> {
         if (field instanceof DataField) {
            DataField casted = (DataField) field;
            casted.setParent(null);
         }
      });
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public IPackage getParent() {
      return parent;
   }

   @Override
   public IMetadata getMetadata() {
      return metadata;
   }

   @Override
   public IData setMetadata(IMetadata metadata) {
      this.metadata = metadata;
      return this;
   }

   @Override
   public Optional<IData> getExtendedDataType() {
      return Optional.ofNullable(superDataType);
   }

   @Override
   public IData setExtendedDataType(IData superDataType) {
      this.superDataType = superDataType;
      return this;
   }

   @Override
   public INamedChildCollection<IData, IDataField> getFields() {
      return fields;
   }

   @Override
   public String getFullyQualifiedName() {
      return String.format("%s%s%s", parent == null ? "" : parent.getName(), parent == null ? "" : ".", name);
   }

   public Data setParent(IPackage parent) {
      this.parent = parent;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof Data)) {
         return false;
      }
      Data data = (Data) o;
      return Objects.equals(fields, data.fields)
             && Objects.equals(name, data.name)
             && Objects.equals(superDataType, data.superDataType)
             && parent == data.parent
             && Objects.equals(metadata, data.metadata);
   }

   @Override
   public int hashCode() {
      return Objects.hash(fields,
                          name,
                          superDataType,
                          System.identityHashCode(parent), metadata);
   }

   @Override
   public String toString() {
      return "Data[" + "fields=" + fields
             + ", name='" + name + '\''
             + ", superDataType='" + (superDataType == null ? "null" : superDataType.getFullyQualifiedName()) + '\''
             + ", parent=" + (parent == null ? "null" : parent.getName())
             + ", metadata=" + metadata + ']';
   }
}
