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
