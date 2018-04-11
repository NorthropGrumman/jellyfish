package com.ngc.seaside.systemdescriptor.model.impl.xtext.data;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.declaration.WrappedDeclarationDefinition;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.util.ConversionUtil;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataModelFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

/**
 * Adapts a {@link ReferencedDataModelFieldDeclaration} that references other data types instance to an {@link
 * IDataField}.
 * This class is not threadsafe.
 */
public class WrappedReferencedDataField extends AbstractWrappedDataField<ReferencedDataModelFieldDeclaration>
      implements IDataField {

   // Note the "implements IDataField" is redundant since the base class implements the interface as well.  But this
   // avoid an issue in ProxyingValidationContext when that class tries to create a dynamic proxy of this class.
   // It uses dataField.getClass().getInterfaces() to determine which interfaces the proxy should implement but
   // getInterfaces() only returns the interfaces declared by the class directly and not the interfaces of the sub-
   // class.

   /**
    * Creates a new field.
    */
   public WrappedReferencedDataField(IWrapperResolver resolver, ReferencedDataModelFieldDeclaration wrapped) {
      super(resolver, wrapped);
      Preconditions.checkArgument(wrapped.getDataModel().eClass().getClassifierID() == SystemDescriptorPackage.DATA,
                                  "can only wrap a field with a data type!");
   }

   @Override
   public DataTypes getType() {
      return DataTypes.DATA; // Only other data can be referenced, not primitive types or enums.
   }

   @Override
   public IDataField setType(DataTypes type) {
      Preconditions.checkNotNull(type, "type may not be null!");
      Preconditions.checkArgument(type == DataTypes.DATA,
                                  "the type of this field must be another data type, it cannot be changed to reference"
                                        + " primitives or enums!");
      // We don't actually have to do anything here.
      return this;
   }

   @Override
   public IData getReferencedDataType() {
      // Cast is safe because we only allow construction of this type for wrapping data.
      return resolver.getWrapperFor((Data) wrapped.getDataModel());
   }

   @Override
   public IDataField setReferencedDataType(IData data) {
      Preconditions.checkNotNull(data, "data may not be null!");
      wrapped.setDataModel(resolver.findXTextData(data.getName(), data.getParent().getName()).get());
      return this;
   }

   @Override
   public IEnumeration getReferencedEnumeration() {
      return null; // This type can only reference data.
   }

   @Override
   public IDataField setReferencedEnumeration(IEnumeration enumeration) {
      throw new IllegalStateException("the type of this field must be another data type, it cannot be changed to"
                                            + " reference enumerations!");
   }

   /**
    * Creates a new {@code ReferencedDataModelFieldDeclaration} that is equivalent
    * to the given data ref which references a data type. Changes to the {@code IReferencedDataField} are
    * not reflected in the returned {@code ReferencedDataModelFieldDeclaration}
    * after construction.
    */
   public static ReferencedDataModelFieldDeclaration toXtext(IWrapperResolver resolver,
                                                             IDataField dataRef) {
      Preconditions.checkNotNull(dataRef, "dataRef may not be null!");
      Preconditions.checkArgument(
            dataRef.getType() == DataTypes.DATA,
            "cannot create a ReferencedDataModelFieldDeclaration for an IDataField that references a primitive type"
                  + " or enum!");
      ReferencedDataModelFieldDeclaration x =
            SystemDescriptorFactory.eINSTANCE.createReferencedDataModelFieldDeclaration();
      x.setName(dataRef.getName());
      x.setDefinition(WrappedDeclarationDefinition.toXtext(resolver, dataRef.getMetadata(), null));
      x.setDataModel(resolver.findXTextData(dataRef.getReferencedDataType().getName(),
                                            dataRef.getReferencedDataType().getParent().getName()).get());
      x.setCardinality(ConversionUtil.convertCardinalityToXtext(dataRef.getCardinality()));
      return x;
   }
}
