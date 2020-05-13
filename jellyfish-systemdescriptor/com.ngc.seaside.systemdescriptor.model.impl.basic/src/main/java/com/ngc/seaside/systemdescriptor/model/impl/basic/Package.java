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
package com.ngc.seaside.systemdescriptor.model.impl.basic;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.basic.data.Data;
import com.ngc.seaside.systemdescriptor.model.impl.basic.data.Enumeration;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;

import java.util.Objects;

/**
 * Implements the IPackage interface. Maintains data and models for the Package.
 *
 * @author psnell
 */
public class Package implements IPackage {

   private final String name;
   private ISystemDescriptor parent;
   private final INamedChildCollection<IPackage, IData> data;
   private final INamedChildCollection<IPackage, IModel> models;
   private final INamedChildCollection<IPackage, IEnumeration> enumerations;

   /**
    * Creates a new package.
    */
   public Package(String name,
                  INamedChildCollection<IPackage, IData> data,
                  INamedChildCollection<IPackage, IModel> models,
                  INamedChildCollection<IPackage, IEnumeration> enumerations) {
      this.name = name;
      this.data = data;
      this.models = models;
      this.enumerations = enumerations;
   }

   /**
    * Creates a new package.
    */
   public Package(String name) {
      Preconditions.checkNotNull(name, "name may not be null!");
      Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");
      this.name = name;
      this.data = new NamedChildCollection<>();
      this.models = new NamedChildCollection<>();
      this.enumerations = new NamedChildCollection<>();
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

   @Override
   public INamedChildCollection<IPackage, IEnumeration> getEnumerations() {
      return enumerations;
   }

   /**
    * Adds a model.
    */
   public Package addModel(IModel model) {
      if (model instanceof Model) {
         ((Model) model).setParent(this);
      }
      models.add(model);
      return this;
   }

   /**
    * Adds a data.
    */
   public Package addData(IData data) {
      if (data instanceof Data) {
         ((Data) data).setParent(this);
      }
      this.data.add(data);
      return this;
   }

   /**
    * Adds an enumeration.
    */
   public Package addEnumeration(IEnumeration enumeration) {
      if (enumeration instanceof Enumeration) {
         ((Enumeration) enumeration).setParent(this);
      }
      this.enumerations.add(enumeration);
      return this;
   }

   /**
    * Sets the parent descriptor.
    */
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
      return Objects.equals(name, p.name)
             && parent == p.parent
             && Objects.equals(data, p.data)
             && Objects.equals(models, p.models)
             && Objects.equals(enumerations, p.enumerations);
   }

   @Override
   public int hashCode() {
      return Objects.hash(name, System.identityHashCode(parent), data, models, enumerations);
   }

   @Override
   public String toString() {
      return "Package["
             + "name='" + name + '\''
             + ", data=" + data
             + ", models=" + models
             + ", enumerations=" + enumerations
             + ']';
   }

}
