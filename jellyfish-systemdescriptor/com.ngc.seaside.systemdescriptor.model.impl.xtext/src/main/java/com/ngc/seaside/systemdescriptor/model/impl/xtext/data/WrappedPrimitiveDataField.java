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

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.declaration.WrappedDeclarationDefinition;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.util.ConversionUtil;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataType;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

/**
 * Adapts a {@link PrimitiveDataFieldDeclaration} instance to an {@link IDataField}.
 * This class is not threadsafe.
 */
public class WrappedPrimitiveDataField extends AbstractWrappedDataField<PrimitiveDataFieldDeclaration>
      implements IDataField {

   // Note the "implements IDataField" is redundant since the base class implements the interface as well.  But this
   // avoid an issue in ProxyingValidationContext when that class tries to create a dynamic proxy of this class.
   // It uses dataField.getClass().getInterfaces() to determine which interfaces the proxy should implement but
   // getInterfaces() only returns the interfaces declared by the class directly and not the interfaces of the sub-
   // class.

   /**
    * Creates a new data field.
    */
   public WrappedPrimitiveDataField(IWrapperResolver resolver, PrimitiveDataFieldDeclaration wrapped) {
      super(resolver, wrapped);
   }

   @Override
   public DataTypes getType() {
      return DataTypes.valueOf(wrapped.getType().name());
   }

   @Override
   public IDataField setType(DataTypes type) {
      Preconditions.checkNotNull(type, "type may not be null!");
      Preconditions.checkArgument(type != DataTypes.DATA,
                                  "the type of this field must be a primitive, it cannot be changed to reference other"
                                        + " data types!");
      wrapped.setType(PrimitiveDataType.valueOf(type.name()));
      return this;
   }

   @Override
   public IData getReferencedDataType() {
      return null; // This is a primitive data type, it can never reference other data.
   }

   @Override
   public IDataField setReferencedDataType(IData dataType) {
      throw new IllegalStateException("the type of this field must be a primitive, it cannot be changed to reference"
                                            + " other data types!");
   }

   @Override
   public IEnumeration getReferencedEnumeration() {
      return null; // This is a primitive data type, it can never reference other data.
   }

   @Override
   public IDataField setReferencedEnumeration(IEnumeration enumeration) {
      throw new IllegalStateException("the type of this field must be a primitive, it cannot be changed to reference"
                                            + " enumerations!");
   }

   /**
    * Creates a new {@code PrimitiveDataFieldDeclaration} that is equivalent to the given field.  Changes to the {@code
    * IDataField} are not reflected in the returned {@code PrimitiveDataFieldDeclaration} after construction.
    */
   public static PrimitiveDataFieldDeclaration toXtext(IWrapperResolver resolver, IDataField field) {
      Preconditions.checkNotNull(field, "field may not be null!");
      Preconditions.checkArgument(
            field.getType() != DataTypes.DATA && field.getType() != DataTypes.ENUM,
            "cannot create a PrimitiveDataFieldDeclaration for an IDataField that references other data or enums!");
      PrimitiveDataFieldDeclaration x = SystemDescriptorFactory.eINSTANCE.createPrimitiveDataFieldDeclaration();
      x.setDefinition(WrappedDeclarationDefinition.toXtext(resolver, field.getMetadata(), null));
      x.setName(field.getName());
      x.setType(PrimitiveDataType.valueOf(field.getType().name()));
      x.setCardinality(ConversionUtil.convertCardinalityToXtext(field.getCardinality()));
      return x;
   }
}
