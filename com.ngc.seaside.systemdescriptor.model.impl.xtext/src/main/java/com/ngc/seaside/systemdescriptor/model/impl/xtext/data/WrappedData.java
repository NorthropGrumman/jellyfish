package com.ngc.seaside.systemdescriptor.model.impl.xtext.data;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.collection.WrappingNamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.exception.UnrecognizedXtextTypeException;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

/**
 * Adapts a {@link Data} instance to {@link IData}.
 *
 * This class is not threadsafe.
 */
public class WrappedData extends AbstractWrappedXtext<Data> implements IData {

   private final WrappingNamedChildCollection<DataFieldDeclaration, IData, IDataField> fields;
   private IMetadata metadata;

   public WrappedData(IWrapperResolver resolver, Data wrapped) {
      super(resolver, wrapped);
      this.metadata = WrappedMetadata.fromXtext(wrapped.getMetadata());
      this.fields = new WrappingNamedChildCollection<>(wrapped.getFields(),
                                                       f -> toWrappedDataField(resolver, f),
                                                       f -> WrappedData.toXtextDataFieldDeclaration(resolver, f),
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

   public static Data toXTextData(IWrapperResolver wrapperResolver, IData data) {
      Preconditions.checkNotNull(data, "data may not be null!");
      Preconditions.checkNotNull(wrapperResolver, "wrapperResolver may not be null!");
      Data d = SystemDescriptorFactory.eINSTANCE.createData();
      d.setName(data.getName());
      d.setMetadata(WrappedMetadata.toXtext(data.getMetadata()));
      data.getFields()
            .stream()
            .map(f -> toXtextDataFieldDeclaration(wrapperResolver, f))
            .forEach(d.getFields()::add);
      return d;
   }

   private static DataFieldDeclaration toXtextDataFieldDeclaration(IWrapperResolver wrapperResolver,
                                                                   IDataField field) {
      return field.getType() == DataTypes.DATA ? WrappedReferencedDataField.toXtext(wrapperResolver, field)
                                               : WrappedPrimitiveDataField.toXtext(field);
   }

   private static IDataField toWrappedDataField(IWrapperResolver wrapperResolver, DataFieldDeclaration field) {
      switch (field.eClass().getClassifierID()) {
         case SystemDescriptorPackage.PRIMITIVE_DATA_FIELD_DECLARATION:
            return new WrappedPrimitiveDataField(wrapperResolver, (PrimitiveDataFieldDeclaration) field);
         case SystemDescriptorPackage.REFERENCED_DATA_FIELD_DECLARATION:
            return new WrappedReferencedDataField(wrapperResolver, (ReferencedDataFieldDeclaration) field);
         default:
            throw new UnrecognizedXtextTypeException(field);
      }
   }
}
