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
package com.ngc.seaside.systemdescriptor.model.impl.xtext;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.data.WrappedData;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.data.WrappedEnumeration;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.exception.UnrecognizedXtextTypeException;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.model.WrappedModel;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Element;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Enumeration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Adapts a {@link Package} to an {@link IPackage}.  Unlike the XText version of {@code Package}, this package may
 * contain multiple data and model types declarations.  Thus there is a 1 ({@link IPackage}) to many ({@link Package})
 * relationship between the two.
 */
public class WrappedPackage implements IPackage, IUnwrappableCollection<Package> {

   private final Collection<Package> wrapped = new ArrayList<>();
   private final NamedChildCollection<IPackage, IData> data = new NamedChildCollection<>();
   private final NamedChildCollection<IPackage, IModel> models = new NamedChildCollection<>();
   private final NamedChildCollection<IPackage, IEnumeration> enumerations = new NamedChildCollection<>();

   private final ISystemDescriptor descriptor;
   private final String packageName;
   private final IWrapperResolver resolver;

   /**
    * Creates a new wrapper for the given package.  Use {@link #wrap(Package)} to add an additional package with the
    * same name to wrap.
    */
   public WrappedPackage(IWrapperResolver resolver, ISystemDescriptor descriptor, Package p) {
      this.resolver = Preconditions.checkNotNull(resolver, "resolver may not be null!");
      this.descriptor = Preconditions.checkNotNull(descriptor, "descriptor may not be null!");
      Preconditions.checkNotNull(p, "p may not be null!");
      Preconditions.checkArgument(!p.getName().trim().isEmpty(), "package name may not be empty!");
      packageName = p.getName();
      wrap(p);
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

   @Override
   public String getName() {
      return packageName;
   }

   @Override
   public ISystemDescriptor getParent() {
      return descriptor;
   }

   @Override
   public Collection<? extends Package> unwrapAll() {
      return Collections.unmodifiableCollection(wrapped);
   }

   /**
    * Begins wrapping the given package.  The name of the package must be the same as this package.
    */
   public WrappedPackage wrap(Package p) {
      Preconditions.checkNotNull(p, "p may not be null!");
      Preconditions.checkArgument(
            packageName.equals(p.getName()),
            "can only wrap packages with the same name!  Currently wrapping %s but package name was %s.",
            packageName,
            p.getName());

      wrapped.add(p);
      Element element = p.getElement();
      // It is possible that the package is empty.
      if (element != null) {
         switch (element.eClass().getClassifierID()) {
            case SystemDescriptorPackage.DATA:
               doAddData((Data) element);
               break;
            case SystemDescriptorPackage.MODEL:
               doAddModel((Model) element);
               break;
            case SystemDescriptorPackage.ENUMERATION:
               doAddEnumeration((Enumeration) element);
               break;
            default:
               throw new UnrecognizedXtextTypeException(element);
         }
      }
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof WrappedPackage)) {
         return false;
      }
      WrappedPackage that = (WrappedPackage) o;
      return Objects.equals(wrapped, that.wrapped);
   }

   @Override
   public int hashCode() {
      return Objects.hash(wrapped);
   }

   @Override
   public String toString() {
      return wrapped.toString();
   }

   /**
    * Creates a new collection of {@code Package}s that contain teh models and data in the given package.
    */
   public static Collection<Package> toXTextPackages(IWrapperResolver resolver, IPackage p) {
      Preconditions.checkNotNull(p, "p may not be null!");
      Collection<Package> packages = new ArrayList<>(p.getData().size() + p.getModels().size());
      for (IData data : p.getData()) {
         Package newPackage = SystemDescriptorFactory.eINSTANCE.createPackage();
         newPackage.setName(p.getName());
         newPackage.setElement(WrappedData.toXTextData(resolver, data));
         packages.add(newPackage);
      }

      for (IModel model : p.getModels()) {
         Package newPackage = SystemDescriptorFactory.eINSTANCE.createPackage();
         newPackage.setName(p.getName());
         newPackage.setElement(WrappedModel.toXtextModel(resolver, model));
         packages.add(newPackage);
      }

      for (IEnumeration enumeration : p.getEnumerations()) {
         Package newPackage = SystemDescriptorFactory.eINSTANCE.createPackage();
         newPackage.setName(p.getName());
         newPackage.setElement(WrappedEnumeration.toXTextEnumeration(resolver, enumeration));
         packages.add(newPackage);
      }

      return packages;
   }

   private void doAddData(Data element) {
      data.add(new WrappedData(resolver, element));
   }

   private void doAddModel(Model element) {
      models.add(new WrappedModel(resolver, element));
   }

   private void doAddEnumeration(Enumeration enumeration) {
      enumerations.add(new WrappedEnumeration(resolver, enumeration));
   }
}
