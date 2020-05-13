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

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Objects;
import java.util.Optional;

/**
 * Implements an ISystemDescriptor.  Maintains a collection of IPackage objects.
 *
 * @author psnell
 */
public class SystemDescriptor implements ISystemDescriptor {

   private final INamedChildCollection<ISystemDescriptor, IPackage> packages;

   SystemDescriptor() {
      packages = new NamedChildCollection<>();
   }

   @Override
   public INamedChildCollection<ISystemDescriptor, IPackage> getPackages() {
      return packages;
   }

   @Override
   public Optional<IModel> findModel(String fullyQualifiedName) {

      int i = fullyQualifiedName.lastIndexOf(".");
      if (i == -1) {
         return Optional.empty();
      }

      String packageName = fullyQualifiedName.substring(0, i);
      String modelName = fullyQualifiedName.substring(i + 1, fullyQualifiedName.length());
      return findModel(packageName, modelName);

   }

   @Override
   public Optional<IModel> findModel(String packageName, String name) {
      Optional<IPackage> p = packages.getByName(packageName);
      if (p.isPresent()) {
         return p.get().getModels().getByName(name);
      } else {
         return Optional.empty();
      }
   }

   @Override
   public Optional<IData> findData(String fullyQualifiedName) {
      int i = fullyQualifiedName.lastIndexOf(".");
      if (i == -1) {
         return Optional.empty();
      }

      String packageName = fullyQualifiedName.substring(0, i);
      String dataName = fullyQualifiedName.substring(i + 1, fullyQualifiedName.length());
      return findData(packageName, dataName);
   }

   @Override
   public Optional<IData> findData(String packageName, String name) {
      Optional<IPackage> p = packages.getByName(packageName);
      if (p.isPresent()) {
         return p.get().getData().getByName(name);
      } else {
         return Optional.empty();
      }
   }

   @Override
   public Optional<IEnumeration> findEnumeration(String fullyQualifiedName) {
      int i = fullyQualifiedName.lastIndexOf(".");
      if (i == -1) {
         return Optional.empty();
      }

      String packageName = fullyQualifiedName.substring(0, i);
      String enumName = fullyQualifiedName.substring(i + 1, fullyQualifiedName.length());
      return findEnumeration(packageName, enumName);
   }

   @Override
   public Optional<IEnumeration> findEnumeration(String packageName, String name) {
      Optional<IPackage> p = packages.getByName(packageName);
      if (p.isPresent()) {
         return p.get().getEnumerations().getByName(name);
      } else {
         return Optional.empty();
      }
   }

   /**
    * This method adds a IPackage object to the packages Container.
    *
    * @param pkg Package to add to the SystemDescriptor object
    * @return this object
    */
   public SystemDescriptor addPackage(IPackage pkg) {
      packages.add(pkg);
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof SystemDescriptor)) {
         return false;
      }

      SystemDescriptor s = (SystemDescriptor) o;
      return Objects.equals(packages, s.packages);
   }

   @Override
   public int hashCode() {
      return Objects.hash(packages);
   }

   @Override
   public String toString() {
      return "SystemDescriptor["
             + "packages=" + packages
             + ']';
   }
}
