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

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.collection.AutoWrappingCollection;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.collection.WrappingNamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.exception.UnrecognizedXtextTypeException;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataModelFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import java.util.Optional;

/**
 * Adapts a {@link Data} instance to {@link IData}.
 * This class is not threadsafe.
 */
public class WrappedData extends AbstractWrappedXtext<Data> implements IData {

   private final WrappingNamedChildCollection<DataFieldDeclaration, IData, IDataField> fields;
   private IMetadata metadata;

   /**
    * Creates a new wrapped data.
    */
   public WrappedData(IWrapperResolver resolver, Data wrapped) {
      super(resolver, wrapped);
      this.metadata = WrappedMetadata.fromXtext(wrapped.getMetadata());
      this.fields = new WrappingNamedChildCollection<>(wrapped.getFields(),
                                                       f -> toWrappedDataField(resolver, f),
                                                       AutoWrappingCollection.defaultUnwrapper(),
                                                       DataFieldDeclaration::getName);
   }

   @Override
   public IMetadata getMetadata() {
      return metadata;
   }

   @Override
   public IData setMetadata(IMetadata metadata) {
      Preconditions.checkNotNull(metadata, "metadata may not be null!");
      this.metadata = metadata;
      wrapped.setMetadata(WrappedMetadata.toXtext(metadata));
      return this;
   }

   @Override
   public Optional<IData> getExtendedDataType() {
      Data superType = wrapped.getExtendedDataType();
      return superType == null ? Optional.empty() : Optional.of(resolver.getWrapperFor(superType));
   }

   @Override
   public IData setExtendedDataType(IData superDataType) {
      Preconditions.checkNotNull(superDataType, "superDataType may not be null!");
      Preconditions.checkArgument(superDataType.getParent() != null, "data must be contained within a package");
      wrapped.setExtendedDataType(findXtextData(superDataType.getName(), superDataType.getParent().getName()));
      return this;
   }

   @Override
   public INamedChildCollection<IData, IDataField> getFields() {
      return fields;
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
    * Converts the given data object to an XText object.
    */
   public static Data toXTextData(IWrapperResolver wrapperResolver, IData data) {
      Preconditions.checkNotNull(data, "data may not be null!");
      Preconditions.checkNotNull(wrapperResolver, "wrapperResolver may not be null!");
      IData superType = data.getExtendedDataType().orElse(null);
      Data d = SystemDescriptorFactory.eINSTANCE.createData();
      d.setName(data.getName());
      d.setMetadata(WrappedMetadata.toXtext(data.getMetadata()));
      d.setExtendedDataType(superType == null ? null : doFindXtextData(wrapperResolver,
                                                                       superType.getName(),
                                                                       superType.getParent().getName()));
      data.getFields()
            .stream()
            .map(f -> toXtextDataFieldDeclaration(wrapperResolver, f))
            .forEach(d.getFields()::add);
      return d;
   }

   /**
    * Finds the XText {@code Data} object with the given name and package.
    *
    * @param name        the name of the of type
    * @param packageName the name of the package that contains the type
    * @return the XText type
    * @throws IllegalStateException if the XText type could not be found
    */
   private Data findXtextData(String name, String packageName) {
      return doFindXtextData(resolver, name, packageName);
   }

   /**
    * Finds the XText {@code Data} object with the given name and package.
    *
    * @param resolver    the resolver that can locate XText data objects
    * @param name        the name of the of type
    * @param packageName the name of the package that contains the type
    * @return the XText type
    * @throws IllegalStateException if the XText type could not be found
    */
   private static Data doFindXtextData(IWrapperResolver resolver, String name, String packageName) {
      return resolver.findXTextData(name, packageName).orElseThrow(() -> new IllegalStateException(String.format(
            "Could not find XText type for data type '%s' in package '%s'!"
                  + "  Make sure the IData object is added to"
                  + " a package within the ISystemDescriptor before adding a reference to it!",
            name,
            packageName)));
   }

   private static DataFieldDeclaration toXtextDataFieldDeclaration(IWrapperResolver wrapperResolver,
                                                                   IDataField field) {
      switch (field.getType()) {
         case DATA:
            return WrappedReferencedDataField.toXtext(wrapperResolver, field);
         case ENUM:
            return WrappedReferencedEnumField.toXtext(wrapperResolver, field);
         default:
            return WrappedPrimitiveDataField.toXtext(wrapperResolver, field);
      }
   }

   private static IDataField toWrappedDataField(IWrapperResolver wrapperResolver, DataFieldDeclaration field) {
      switch (field.eClass().getClassifierID()) {
         case SystemDescriptorPackage.PRIMITIVE_DATA_FIELD_DECLARATION:
            return new WrappedPrimitiveDataField(wrapperResolver, (PrimitiveDataFieldDeclaration) field);
         case SystemDescriptorPackage.REFERENCED_DATA_MODEL_FIELD_DECLARATION:
            return toWrappedReferenceDataField(wrapperResolver, (ReferencedDataModelFieldDeclaration) field);
         default:
            throw new UnrecognizedXtextTypeException(field);
      }
   }

   private static IDataField toWrappedReferenceDataField(IWrapperResolver wrapperResolver,
                                                         ReferencedDataModelFieldDeclaration field) {
      switch (field.getDataModel().eClass().getClassifierID()) {
         case SystemDescriptorPackage.DATA:
            return new WrappedReferencedDataField(wrapperResolver, field);
         case SystemDescriptorPackage.ENUMERATION:
            return new WrappedReferencedEnumField(wrapperResolver, field);
         default:
            throw new UnrecognizedXtextTypeException(field);
      }
   }
}
