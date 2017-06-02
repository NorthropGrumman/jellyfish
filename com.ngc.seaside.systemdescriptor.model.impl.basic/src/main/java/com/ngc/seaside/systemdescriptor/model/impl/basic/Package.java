package com.ngc.seaside.systemdescriptor.model.impl.basic;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.basic.data.Data;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;

import java.util.Objects;

/**
 * Implements the IPackage interface. Maintains data and models for the Package.
 * @author psnell
 *
 */
public class Package implements IPackage {

   private final String name;
   private ISystemDescriptor parent;
   private final INamedChildCollection<IPackage, IData> data;
   private final INamedChildCollection<IPackage, IModel> models;

   public Package(String name, 
            INamedChildCollection<IPackage, IData> data,
            INamedChildCollection<IPackage, IModel> models) {
      this.name = name;
      this.data = data;
      this.models = models;
   }
   
   public Package(String name) {
      Preconditions.checkNotNull(name, "name may not be null!");
      Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");
      this.name = name;
      this.data = new NamedChildCollection<>();
      this.models = new NamedChildCollection<>();
    }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public ISystemDescriptor getParent() {
      return parent;
   }

   @Override
   public INamedChildCollection<IPackage, IData> getData() {
      return data;
   }

   @Override
   public INamedChildCollection<IPackage, IModel> getModels() {
      return models;
   }
   
   public Package addModel(IModel model) {
      Model casted = (Model) model;
      casted.setParent(this);
      models.add(casted);
      return this;
   }

   public Package addData(IData data) {
      Data casted = (Data) data;
      casted.setParent(this);
      this.data.add(casted);
      return this;
   }

   public Package setParent(ISystemDescriptor parent) {
     this.parent = parent;
     return this;
   }
   

   @Override
   public boolean equals(Object o) {
     if (this == o) {
       return true;
     }
     if (!(o instanceof Package)) {
       return false;
     }
     Package p = (Package) o;
     return Objects.equals(name, p.name) &&
            parent == p.parent &&
            Objects.equals(data, p.data) &&
            Objects.equals(models, p.models);
   }

   @Override
   public int hashCode() {
     return Objects.hash(name, System.identityHashCode(parent), data, models);
   }

   @Override
   public String toString() {
     return "Package[" +
            "name='" + name + '\'' +
            //", parent=" + (parent == null ? "null" : parent.getName()) +
            ", data=" + data +
            ", models=" + models +
            ']';
   }

}
