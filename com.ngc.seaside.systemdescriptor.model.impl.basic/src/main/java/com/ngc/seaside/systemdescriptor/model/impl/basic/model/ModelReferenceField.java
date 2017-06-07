package com.ngc.seaside.systemdescriptor.model.impl.basic.model;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;

import java.util.Objects;

/**
 * Implements the IModelReferenceField interface.
 *
 * @author psnell
 */
public class ModelReferenceField implements IModelReferenceField {

   protected final String name;
   protected IMetadata metadata;
   protected IModel type;
   protected IModel parent;

   public ModelReferenceField(String name) {
      Preconditions.checkNotNull(name, "name may not be null!");
      Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");
      this.name = name;
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
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof ModelReferenceField)) {
         return false;
      }
      ModelReferenceField that = (ModelReferenceField) o;
      return Objects.equals(name, that.name) &&
             Objects.equals(metadata, that.metadata) &&
             Objects.equals(type, that.type) &&
             parent == that.parent;
   }

   @Override
   public int hashCode() {
      return Objects.hash(name, metadata, type, System.identityHashCode(parent));
   }

   @Override
   public String toString() {
      return "ModelReferenceField[" +
             "name='" + name + '\'' +
             ", metadata=" + metadata +
             ", type=" + type +
             ", parent=" + (parent == null ? "null" : parent.getName()) +
             ']';
   }
}
