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
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValues;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.util.ConversionUtil;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedPropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

public class WrappedDataProperty extends AbstractWrappedProperty<ReferencedPropertyFieldDeclaration> {

   private final IPropertyValues<IPropertyDataValue> values;

   /**
    * Creates a new wrapped data property.
    */
   public WrappedDataProperty(IWrapperResolver resolver,
                              ReferencedPropertyFieldDeclaration wrapped,
                              IPropertyValues<IPropertyDataValue> values) {
      super(resolver, wrapped);
      if (!(wrapped.getDataModel() instanceof Data)) {
         throw new IllegalArgumentException("Expected reference property field declaration to be an enumeration");
      }
      this.values = Preconditions.checkNotNull(values, "values may not be null!");
   }

   @Override
   public IData getReferencedDataType() {
      return resolver.getWrapperFor((Data) wrapped.getDataModel());
   }

   @Override
   public IPropertyDataValue getData() {
      return firstOrDefault(getDatas(), UnsetProperties.UNSET_DATA_VALUE);
   }

   @Override
   public IPropertyValues<IPropertyDataValue> getDatas() {
      return values;
   }

   @Override
   public DataTypes getType() {
      return DataTypes.DATA;
   }

   /**
    * Converts the given property into an XText object.
    */
   public static ReferencedPropertyFieldDeclaration toXtextReferencedPropertyFieldDeclaration(IWrapperResolver resolver,
                                                                                              IProperty property) {
      Preconditions.checkNotNull(resolver, "resolver must not be null!");
      Preconditions.checkNotNull(property, "property must not be null!");
      if (property.getType() != DataTypes.DATA) {
         throw new IllegalArgumentException("property is not of type data");
      }
      ReferencedPropertyFieldDeclaration declaration =
            SystemDescriptorFactory.eINSTANCE.createReferencedPropertyFieldDeclaration();
      declaration.setName(property.getName());
      declaration.setCardinality(ConversionUtil.convertCardinalityToXtext(property.getCardinality()));
      IData data = property.getReferencedDataType();
      declaration.setDataModel(resolver.findXTextData(data.getName(), data.getParent().getName()).get());
      return declaration;
   }


}
