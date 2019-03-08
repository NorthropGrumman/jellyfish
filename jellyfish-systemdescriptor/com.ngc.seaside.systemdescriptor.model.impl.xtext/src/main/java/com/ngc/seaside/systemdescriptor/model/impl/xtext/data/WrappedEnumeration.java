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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.data;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.collection.AutoWrappingCollection;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Enumeration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.EnumerationValueDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

import java.util.Collection;
import java.util.HashSet;

public class WrappedEnumeration extends AbstractWrappedXtext<Enumeration> implements IEnumeration {

   private AutoWrappingCollection<EnumerationValueDeclaration, String> values;
   private IMetadata metadata;

   /**
    * Creates a wrapped enumeration.
    */
   public WrappedEnumeration(IWrapperResolver resolver, Enumeration wrapped) {
      super(resolver, wrapped);
      this.metadata = WrappedMetadata.fromXtext(wrapped.getMetadata());
      this.values = new AutoWrappingCollection<EnumerationValueDeclaration, String>(
            wrapped.getValues(),
            EnumerationValueDeclaration::getValue,
            WrappedEnumeration::newEnumValue) {
         @Override
         public boolean contains(Object o) {
            String value = o == null ? null : o.toString();
            return new HashSet<>(getValues()).contains(value);
         }
      };
   }

   @Override
   public IMetadata getMetadata() {
      return metadata;
   }

   @Override
   public IEnumeration setMetadata(IMetadata metadata) {
      Preconditions.checkNotNull(metadata, "metadata may not be null!");
      this.metadata = metadata;
      wrapped.setMetadata(WrappedMetadata.toXtext(metadata));
      return this;
   }

   @Override
   public Collection<String> getValues() {
      return values;
   }

   @Override
   public String getFullyQualifiedName() {
      Package p = (Package) wrapped.eContainer();
      return String.format("%s%s%s",
                           p == null ? "" : p.getName(),
                           p == null ? "" : ".",
                           wrapped.getName());
   }

   @Override
   public String getName() {
      return wrapped.getName();
   }

   @Override
   public IPackage getParent() {
      return resolver.getWrapperFor((Package) wrapped.eContainer());
   }

   /**
    * Coverts the given object to an XText enumeration.
    */
   public static Enumeration toXTextEnumeration(IWrapperResolver wrapperResolver, IEnumeration enumeration) {
      Preconditions.checkNotNull(enumeration, "enumeration may not be null!");
      Preconditions.checkNotNull(wrapperResolver, "wrapperResolver may not be null!");
      Enumeration e = SystemDescriptorFactory.eINSTANCE.createEnumeration();
      e.setName(enumeration.getName());
      e.setMetadata(WrappedMetadata.toXtext(enumeration.getMetadata()));
      enumeration.getValues().forEach(v -> e.getValues().add(newEnumValue(v)));
      return e;
   }

   /**
    * Coverts the given object to an XText enumeration value.
    */
   private static EnumerationValueDeclaration newEnumValue(String value) {
      EnumerationValueDeclaration declaration = SystemDescriptorFactory.eINSTANCE.createEnumerationValueDeclaration();
      declaration.setValue(value);
      return declaration;
   }
}
