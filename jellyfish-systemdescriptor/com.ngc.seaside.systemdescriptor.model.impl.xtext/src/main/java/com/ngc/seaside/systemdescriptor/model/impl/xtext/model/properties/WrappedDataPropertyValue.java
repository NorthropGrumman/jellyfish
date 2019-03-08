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

import com.ngc.seaside.systemdescriptor.model.api.SystemDescriptors;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyEnumerationValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyPrimitiveValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValues;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.exception.UnrecognizedXtextTypeException;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataModel;
import com.ngc.seaside.systemdescriptor.systemDescriptor.EnumPropertyValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Properties;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueAssignment;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataModelFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedPropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class WrappedDataPropertyValue implements IPropertyDataValue {

   private final IWrapperResolver resolver;
   private final ReferencedPropertyFieldDeclaration propertyDeclaration;
   private final NestedPropertyValueResolver propertyValueResolver;

   /**
    * Creates a new property value.
    */
   public WrappedDataPropertyValue(IWrapperResolver resolver,
                                   ReferencedPropertyFieldDeclaration propertyDeclaration,
                                   Properties propertiesContainer) {
      this.resolver = Preconditions.checkNotNull(resolver, "resolver may not be null!");
      this.propertyDeclaration = Preconditions.checkNotNull(propertyDeclaration,
                                                            "propertyDeclaration may not be null!");
      Preconditions.checkNotNull(propertiesContainer,
                                 "propertiesContainer may not be null!");
      Preconditions.checkArgument(propertyDeclaration.getDataModel() instanceof Data,
                                  "propertyDeclaration must reference a Data object!");
      this.propertyValueResolver = createValueResolver(propertyDeclaration, propertiesContainer);
   }

   @Override
   public IData getReferencedDataType() {
      return resolver.getWrapperFor((Data) propertyDeclaration.getDataModel());
   }

   @Override
   public IPropertyPrimitiveValue getPrimitive(IDataField field) {
      Preconditions.checkNotNull(field, "field may not be null!");
      Preconditions.checkArgument(SystemDescriptors.isPrimitiveDataFieldDeclaration(field),
                                  "cannot get the primitive value of a field whose type is %s!",
                                  field.getType());
      Preconditions.checkState(isSet(), "this value is not set!");

      PropertyValueAssignment assignment = getAssignmentFor(Collections.singleton(field.getName()))
            .orElseThrow(() -> new IllegalStateException("no value for supposedly set property " + field));
      return new WrappedPrimitivePropertyValue(resolver, assignment.getValue());
   }

   @Override
   public IPropertyEnumerationValue getEnumeration(IDataField field) {
      Preconditions.checkNotNull(field, "field may not be null!");
      Preconditions.checkArgument(field.getType() == DataTypes.ENUM,
                                  "cannot get the enum value of a field whose type is %s!",
                                  field.getType());
      Preconditions.checkState(isSet(), "this value is not set!");

      PropertyValueAssignment assignment = getAssignmentFor(Collections.singleton(field.getName()))
            .orElseThrow(() -> new IllegalStateException("no value for supposedly set property " + field));
      return new WrappedEnumerationPropertyValue(resolver, (EnumPropertyValue) assignment.getValue());
   }

   @Override
   public IPropertyDataValue getData(IDataField field) {
      Preconditions.checkNotNull(field, "field may not be null!");
      Preconditions.checkArgument(field.getType() == DataTypes.DATA,
                                  "cannot get the data value of a field whose type is %s!",
                                  field.getType());
      Preconditions.checkState(isSet(), "this value is not set!");
      return new NestedDataPropertyValue(field.getReferencedDataType(), Collections.singleton(field.getName()));
   }

   @Override
   public IPropertyValues<IPropertyPrimitiveValue> getPrimitives(IDataField field) {
      throw new UnsupportedOperationException("cardinality of many not currently supported!");
   }

   @Override
   public IPropertyValues<IPropertyEnumerationValue> getEnumerations(IDataField field) {
      throw new UnsupportedOperationException("cardinality of many not currently supported!");
   }

   @Override
   public IPropertyValues<IPropertyDataValue> getDatas(IDataField field) {
      throw new UnsupportedOperationException("cardinality of many not currently supported!");
   }

   @Override
   public DataTypes getType() {
      return DataTypes.DATA;
   }

   @Override
   public boolean isSet() {
      return doIsSet(propertyDeclaration.getDataModel(), Collections.emptyList());
   }

   protected NestedPropertyValueResolver createValueResolver(ReferencedPropertyFieldDeclaration propertyDeclaration,
                                                             Properties propertiesContainer) {
      // This method is used to make testing easier.
      return new NestedPropertyValueResolver(propertyDeclaration, propertiesContainer);
   }

   private boolean doIsSet(DataModel dataModel, Collection<String> fieldNames) {
      boolean set;

      switch (dataModel.eClass().getClassifierID()) {
         case SystemDescriptorPackage.DATA:
            set = doIsSet((Data) dataModel, fieldNames);
            break;
         case SystemDescriptorPackage.ENUMERATION:
            set = getAssignmentFor(fieldNames).isPresent();
            break;
         default:
            throw new UnrecognizedXtextTypeException(dataModel);
      }

      return set;
   }

   private boolean doIsSet(Data data, Collection<String> fieldNames) {
      boolean set = true;

      for (DataFieldDeclaration field : data.getFields()) {
         switch (field.eClass().getClassifierID()) {
            case SystemDescriptorPackage.PRIMITIVE_DATA_FIELD_DECLARATION:
               set &= getAssignmentFor(appendTo(fieldNames, field.getName())).isPresent();
               break;
            case SystemDescriptorPackage.REFERENCED_DATA_MODEL_FIELD_DECLARATION:
               set &= doIsSet(((ReferencedDataModelFieldDeclaration) field).getDataModel(),
                              appendTo(fieldNames, field.getName()));
               break;
            default:
               throw new UnrecognizedXtextTypeException(field);
         }
      }

      return set;
   }

   private Optional<PropertyValueAssignment> getAssignmentFor(Collection<String> fieldNames) {
      return propertyValueResolver.resoleValue(fieldNames);
   }

   private static Collection<String> appendTo(Collection<String> collection, String value) {
      Collection<String> copy = new ArrayList<>(collection);
      copy.add(value);
      return copy;
   }

   private class NestedDataPropertyValue implements IPropertyDataValue {

      private final IData data;
      private final Collection<String> paths;

      private NestedDataPropertyValue(IData data, Collection<String> paths) {
         this.data = data;
         this.paths = paths;
      }

      @Override
      public IData getReferencedDataType() {
         return data;
      }

      @Override
      public IPropertyPrimitiveValue getPrimitive(IDataField field) {
         Preconditions.checkNotNull(field, "field may not be null!");
         Preconditions.checkArgument(SystemDescriptors.isPrimitiveDataFieldDeclaration(field),
                                     "cannot get the primitive value of a field whose type is %s!",
                                     field.getType());

         PropertyValueAssignment assignment = getAssignmentFor(appendTo(paths, field.getName()))
               .orElseThrow(() -> new IllegalStateException("no value for supposedly set property " + field));
         return new WrappedPrimitivePropertyValue(resolver, assignment.getValue());
      }

      @Override
      public IPropertyEnumerationValue getEnumeration(IDataField field) {
         Preconditions.checkNotNull(field, "field may not be null!");
         Preconditions.checkArgument(field.getType() == DataTypes.ENUM,
                                     "cannot get the enum value of a field whose type is %s!",
                                     field.getType());

         PropertyValueAssignment assignment = getAssignmentFor(appendTo(paths, field.getName()))
               .orElseThrow(() -> new IllegalStateException("no value for supposedly set property " + field));
         return new WrappedEnumerationPropertyValue(resolver, (EnumPropertyValue) assignment.getValue());
      }

      @Override
      public IPropertyDataValue getData(IDataField field) {
         Preconditions.checkNotNull(field, "field may not be null!");
         Preconditions.checkArgument(field.getType() == DataTypes.DATA,
                                     "cannot get the data value of a field whose type is %s!",
                                     field.getType());
         return new NestedDataPropertyValue(field.getReferencedDataType(), appendTo(paths, field.getName()));
      }

      @Override
      public IPropertyValues<IPropertyPrimitiveValue> getPrimitives(IDataField field) {
         throw new UnsupportedOperationException("cardinality of many not currently supported!");
      }

      @Override
      public IPropertyValues<IPropertyEnumerationValue> getEnumerations(IDataField field) {
         throw new UnsupportedOperationException("cardinality of many not currently supported!");
      }

      @Override
      public IPropertyValues<IPropertyDataValue> getDatas(IDataField field) {
         throw new UnsupportedOperationException("cardinality of many not currently supported!");
      }

      @Override
      public DataTypes getType() {
         return DataTypes.DATA;
      }

      @Override
      public boolean isSet() {
         return true;
      }
   }
}
