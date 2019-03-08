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

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties.Properties;

import java.util.Objects;
import java.util.Optional;

/**
 * Implements the IModelReferenceField interface for an unrefined reference.
 */
public class BaseModelReferenceField implements IModelReferenceField {

   protected final String name;
   protected IMetadata metadata;
   protected IModel type;
   protected IModel parent;
   protected IProperties properties;

   /**
    * Creates a new field.
    */
   public BaseModelReferenceField(String name) {
      Preconditions.checkNotNull(name, "name may not be null!");
      Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");
      this.name = name;
      this.properties = new Properties();
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

   @Override
   public String getName() {
      return name;
   }

   @Override
   public IModel getParent() {
      return parent;
   }

   @Override
   public IModel getType() {
      return type;
   }

   @Override
   public IModelReferenceField setType(IModel model) {
      this.type = model;
      return this;
   }

   public void setParent(Model model) {
      parent = model;

   }

   @Override
   public IProperties getProperties() {
      return properties;
   }

   @Override
   public IReferenceField setProperties(IProperties properties) {
      this.properties = properties;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof BaseModelReferenceField)) {
         return false;
      }
      BaseModelReferenceField that = (BaseModelReferenceField) o;
      return Objects.equals(name, that.name)
             && Objects.equals(metadata, that.metadata)
             && Objects.equals(type, that.type)
             && parent == that.parent
             && Objects.equals(properties, that.properties);
   }

   @Override
   public int hashCode() {
      return Objects.hash(name, metadata, type, System.identityHashCode(parent), properties);
   }

   @Override
   public String toString() {
      return "BaseModelReferenceField["
             + "name='" + name + '\''
             + ", metadata=" + metadata
             + ", type=" + type
             + ", parent=" + (parent == null ? "null" : parent.getName())
             + ", properties=" + properties
             + ']';
   }

   @Override
   public Optional<IModelReferenceField> getRefinedField() {
      return Optional.empty();
   }
}
