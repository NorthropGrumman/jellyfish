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
package com.ngc.seaside.systemdescriptor.model.impl.basic.model;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;

import java.util.Objects;

/**
 * Implements an IDataReferenceField interface.
 *
 * @author psnell
 */
public class DataReferenceField implements IDataReferenceField {

   protected final String name;
   protected IMetadata metadata;
   protected IData type;
   protected FieldCardinality cardinality = FieldCardinality.SINGLE;
   protected IModel parent;

   /**
    * Creates a new field.
    */
   public DataReferenceField(String name) {
      Preconditions.checkNotNull(name, "name may not be null!");
      Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");
      this.name = name;
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public IModel getParent() {
      return parent;
   }

   @Override
   public IData getType() {
      return type;
   }

   @Override
   public IDataReferenceField setType(IData type) {
      this.type = type;
      return this;
   }

   @Override
   public FieldCardinality getCardinality() {
      return cardinality;
   }

   @Override
   public IDataReferenceField setCardinality(FieldCardinality cardinality) {
      Preconditions.checkNotNull(cardinality, "cardinality may not be null!");
      this.cardinality = cardinality;
      return this;
   }

   @Override
   public IMetadata getMetadata() {
      return metadata;
   }

   @Override
   public IReferenceField setMetadata(IMetadata metadata) {
      this.metadata = metadata;
      return this;
   }

   public DataReferenceField setParent(IModel model) {
      this.parent = model;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof DataReferenceField)) {
         return false;
      }
      DataReferenceField that = (DataReferenceField) o;
      return Objects.equals(name, that.name)
             && Objects.equals(metadata, that.metadata)
             && Objects.equals(type, that.type)
             && cardinality == that.cardinality
             && parent == that.parent;
   }

   @Override
   public int hashCode() {
      return Objects.hash(name, metadata, type, cardinality, System.identityHashCode(parent));
   }

   @Override
   public String toString() {
      return "DataReferenceField["
             + "name='" + name + '\''
             + ", metadata=" + metadata
             + ", type=" + type
             + ", cardinality=" + cardinality
             + ", parent=" + (parent == null ? "null" : parent.getName())
             + ']';
   }

   /**
    * Creates an immutable view of the given field.
    */
   public static IDataReferenceField immutable(IDataReferenceField field) {
      Preconditions.checkNotNull(field, "field may not be null!");

      // TODO TH: fix this.
      throw new UnsupportedOperationException("not implemented");
   }
}
