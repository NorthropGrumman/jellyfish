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
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyEnumerationValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValues;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.util.ConversionUtil;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Enumeration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedPropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

public class WrappedEnumerationProperty extends AbstractWrappedProperty<ReferencedPropertyFieldDeclaration> {

   private final IPropertyValues<IPropertyEnumerationValue> values;

   /**
    * Creates a new wrapped enumeration property.
    */
   public WrappedEnumerationProperty(IWrapperResolver resolver,
                                     ReferencedPropertyFieldDeclaration wrapped,
                                     IPropertyValues<IPropertyEnumerationValue> values) {
      super(resolver, wrapped);
      if (!(wrapped.getDataModel() instanceof Enumeration)) {
         throw new IllegalArgumentException("Expected reference property field declaration to be an enumeration");
      }
      this.values = Preconditions.checkNotNull(values, "values may not be null!");
   }

   @Override
   public IEnumeration getReferencedEnumeration() {
      return resolver.getWrapperFor((Enumeration) wrapped.getDataModel());
   }

   @Override
   public IPropertyEnumerationValue getEnumeration() {
      return firstOrDefault(getEnumerations(), UnsetProperties.UNSET_ENUMERATION_VALUE);
   }

   @Override
   public IPropertyValues<IPropertyEnumerationValue> getEnumerations() {
      return values;
   }

   @Override
   public DataTypes getType() {
      return DataTypes.ENUM;
   }

   /**
    * Converts the given object to an XText object.
    */
   public static ReferencedPropertyFieldDeclaration toXtextReferencedPropertyFieldDeclaration(IWrapperResolver resolver,
                                                                                              IProperty property) {
      Preconditions.checkNotNull(resolver, "resolver must not be null!");
      Preconditions.checkNotNull(property, "property must not be null!");
      if (property.getType() != DataTypes.ENUM) {
         throw new IllegalArgumentException("property is not of type enumeration");
      }
      ReferencedPropertyFieldDeclaration declaration =
            SystemDescriptorFactory.eINSTANCE.createReferencedPropertyFieldDeclaration();
      declaration.setName(property.getName());
      declaration.setCardinality(ConversionUtil.convertCardinalityToXtext(property.getCardinality()));
      IEnumeration enumeration = property.getReferencedEnumeration();
      declaration.setDataModel(resolver.findXTextEnum(enumeration.getName(), enumeration.getParent().getName()).get());
      return declaration;
   }

}
