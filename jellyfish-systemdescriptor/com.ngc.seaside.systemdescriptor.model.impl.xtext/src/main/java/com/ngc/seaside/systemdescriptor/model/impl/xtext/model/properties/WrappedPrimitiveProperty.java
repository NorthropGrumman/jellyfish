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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyPrimitiveValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValues;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.util.ConversionUtil;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataType;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitivePropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

public class WrappedPrimitiveProperty extends AbstractWrappedProperty<PrimitivePropertyFieldDeclaration> {

   private final IPropertyValues<IPropertyPrimitiveValue> values;

   /**
    * Creates a new property.
    */
   public WrappedPrimitiveProperty(IWrapperResolver resolver,
                                   PrimitivePropertyFieldDeclaration wrapped,
                                   IPropertyValues<IPropertyPrimitiveValue> values) {
      super(resolver, wrapped);
      this.values = Preconditions.checkNotNull(values, "values may not be null!");
   }

   @Override
   public IPropertyPrimitiveValue getPrimitive() {
      return firstOrDefault(getPrimitives(), UnsetProperties.UNSET_PRIMITIVE_VALUE);
   }

   @Override
   public IPropertyValues<IPropertyPrimitiveValue> getPrimitives() {
      return values;
   }

   @Override
   public DataTypes getType() {
      return DataTypes.valueOf(wrapped.getType().name());
   }

   /**
    * Converts the given property into an XText object.
    */
   public static PrimitivePropertyFieldDeclaration toXtextPrimitivePropertyFieldDeclaration(IWrapperResolver resolver,
                                                                                            IProperty property) {
      Preconditions.checkNotNull(resolver, "resolver must not be null!");
      Preconditions.checkNotNull(property, "property must not be null!");
      if (property.getType() == DataTypes.DATA || property.getType() == DataTypes.ENUM) {
         throw new IllegalArgumentException("property is not a primitive type");
      }
      PrimitivePropertyFieldDeclaration declaration =
            SystemDescriptorFactory.eINSTANCE.createPrimitivePropertyFieldDeclaration();
      declaration.setName(property.getName());
      declaration.setCardinality(ConversionUtil.convertCardinalityToXtext(property.getCardinality()));
      declaration.setType(PrimitiveDataType.valueOf(property.getType().toString()));
      return declaration;
   }
}
