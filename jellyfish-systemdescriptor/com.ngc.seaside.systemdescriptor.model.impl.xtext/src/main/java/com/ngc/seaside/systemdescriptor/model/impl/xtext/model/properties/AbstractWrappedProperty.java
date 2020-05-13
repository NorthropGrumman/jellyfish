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

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyEnumerationValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyPrimitiveValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValues;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.util.ConversionUtil;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Properties;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyFieldDeclaration;

public abstract class AbstractWrappedProperty<T extends PropertyFieldDeclaration>
      extends AbstractWrappedXtext<T>
      implements IProperty {

   /**
    * Creates a new wrapped property.
    */
   public AbstractWrappedProperty(IWrapperResolver resolver, T wrapped) {
      super(resolver, wrapped);
   }

   @Override
   public String getName() {
      return wrapped.getName();
   }

   @Override
   public IProperties getParent() {
      return resolver.getWrapperFor((Properties) wrapped.eContainer());
   }

   @Override
   public FieldCardinality getCardinality() {
      return ConversionUtil.convertCardinalityFromXtext(wrapped.getCardinality());
   }

   @Override
   public IData getReferencedDataType() {
      throw new IllegalStateException("property is not a data type");
   }

   @Override
   public IEnumeration getReferencedEnumeration() {
      throw new IllegalStateException("property is not an enumeration type");
   }


   @Override
   public IPropertyDataValue getData() {
      throw new IllegalStateException("property is not a data type");
   }

   @Override
   public IPropertyEnumerationValue getEnumeration() {
      throw new IllegalStateException("property is not an enumeration type");
   }

   @Override
   public IPropertyPrimitiveValue getPrimitive() {
      throw new IllegalStateException("property is not a primitive type");
   }

   @Override
   public IPropertyValues<IPropertyDataValue> getDatas() {
      throw new IllegalStateException("property is not a data type");
   }

   @Override
   public IPropertyValues<IPropertyEnumerationValue> getEnumerations() {
      throw new IllegalStateException("property is not an enumeration type");
   }

   @Override
   public IPropertyValues<IPropertyPrimitiveValue> getPrimitives() {
      throw new IllegalStateException("property is not a primitive type");
   }

   /**
    * Converts the given property to an XText object.
    */
   public static PropertyFieldDeclaration toXTextPropertyFieldDeclaration(IWrapperResolver resolver,
                                                                          IProperty property) {
      Preconditions.checkNotNull(resolver, "resolver must not be null!");
      Preconditions.checkNotNull(property, "property must not be null!");
      switch (property.getType()) {
         case DATA:
            return WrappedDataProperty.toXtextReferencedPropertyFieldDeclaration(resolver, property);
         case ENUM:
            return WrappedEnumerationProperty.toXtextReferencedPropertyFieldDeclaration(resolver, property);
         default:
            return WrappedPrimitiveProperty.toXtextPrimitivePropertyFieldDeclaration(resolver, property);
      }
   }

   static <T extends IPropertyValue> T firstOrDefault(IPropertyValues<T> values, T defaultValue) {
      return values.isSet() && !values.isEmpty() ? values.iterator().next() : defaultValue;
   }
}
