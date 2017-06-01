package com.ngc.seaside.systemdescriptor.model.impl.basic;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Optional;
import java.util.Iterator;

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

      for (Iterator<IPackage> iterator = packages.iterator(); iterator.hasNext();) {
         IPackage pkg = iterator.next();
         if (fullyQualifiedName.matches(pkg.getName() + ".*")) {
            String modelName = fullyQualifiedName.substring(pkg.getName().length() + 1);
            return pkg.getModels().getByName(modelName);
         }
      }

      return Optional.empty();
   }

   @Override
   public Optional<IModel> findModel(String packageName, String name) {
      Optional<IPackage> p = packages.getByName(packageName);
      return p.get().getModels().getByName(name);
   }

   @Override
   public Optional<IData> findData(String fullyQualifiedName) {
      for (Iterator<IPackage> iterator = packages.iterator(); iterator.hasNext();) {
         IPackage pkg = iterator.next();
         if (fullyQualifiedName.matches(pkg.getName() + ".*")) {
            String dataName = fullyQualifiedName.substring(pkg.getName().length() + 1);
            return pkg.getData().getByName(dataName);
         }
      }
      
      return Optional.empty();
   }

   @Override
   public Optional<IData> findData(String packageName, String name) {
      Optional<IPackage> p = packages.getByName(packageName);
      return p.get().getData().getByName(name);
   }
   
   public SystemDescriptor addPackage(IPackage pkg) {
      packages.add(pkg);
      return this;
   }

}
