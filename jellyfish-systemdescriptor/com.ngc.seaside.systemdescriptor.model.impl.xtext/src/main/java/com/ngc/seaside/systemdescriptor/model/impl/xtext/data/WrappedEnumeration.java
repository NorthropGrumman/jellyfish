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
