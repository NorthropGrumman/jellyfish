package com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyEnumerationValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyPrimitiveValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValues;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PropertyDataValue extends PropertyValue implements IPropertyDataValue {

   private final IData data;
   private final Map<String, List<? extends IPropertyValue>> fieldValues;

   /**
    * Constructs a PropertyDataValue.
    *
    * @param data        IData type representing this value
    * @param fieldValues values of the type's fields, collections can be null for unset properties
    * @throws IllegalArgumentException if the fields values don't match the data's fields
    */
   public PropertyDataValue(IData data, Map<String, Collection<? extends IPropertyValue>> fieldValues) {
      super(DataTypes.DATA, PropertyDataValue.allSet(data, fieldValues));
      this.data = data;
      this.fieldValues = new LinkedHashMap<>(fieldValues.size());
      for (Map.Entry<String, Collection<? extends IPropertyValue>> entry : fieldValues.entrySet()) {
         this.fieldValues.put(entry.getKey(), new ArrayList<>(entry.getValue()));
      }
   }

   @Override
   public IData getReferencedDataType() {
      return data;
   }

   @Override
   public IPropertyPrimitiveValue getPrimitive(IDataField field) {
      checkField(field, FieldCardinality.SINGLE, Property.PRIMITIVES);
      return (IPropertyPrimitiveValue) fieldValues.get(field.getName()).get(0);
   }

   @Override
   public IPropertyEnumerationValue getEnumeration(IDataField field) {
      checkField(field, FieldCardinality.SINGLE, DataTypes.ENUM);
      return (IPropertyEnumerationValue) fieldValues.get(field.getName()).get(0);
   }

   @Override
   public IPropertyDataValue getData(IDataField field) {
      checkField(field, FieldCardinality.SINGLE, DataTypes.DATA);
      return (IPropertyDataValue) fieldValues.get(field.getName()).get(0);
   }

   @Override
   public IPropertyValues<IPropertyPrimitiveValue> getPrimitives(IDataField field) {
      checkField(field, FieldCardinality.MANY, Property.PRIMITIVES);
      Collection<IPropertyPrimitiveValue> values = fieldValues.get(field.getName())
            .stream()
            .map(p -> (IPropertyPrimitiveValue) p)
            .collect(Collectors.toList());
      return IPropertyValues.of(values);
   }

   @Override
   public IPropertyValues<IPropertyEnumerationValue> getEnumerations(IDataField field) {
      checkField(field, FieldCardinality.MANY, DataTypes.ENUM);
      Collection<IPropertyEnumerationValue> values = fieldValues.get(field.getName())
            .stream()
            .map(p -> (IPropertyEnumerationValue) p)
            .collect(Collectors.toList());
      return IPropertyValues.of(values);
   }

   @Override
   public IPropertyValues<IPropertyDataValue> getDatas(IDataField field) {
      checkField(field, FieldCardinality.MANY, DataTypes.DATA);
      Collection<IPropertyDataValue> values = fieldValues.get(field.getName())
            .stream()
            .map(p -> (IPropertyDataValue) p)
            .collect(Collectors.toList());
      return IPropertyValues.of(values);
   }

   private void checkField(IDataField field, FieldCardinality expectedCardinality, DataTypes... expectedDataTypes) {
      Preconditions.checkNotNull(field, "field may not be null!");
      if (!data.getFields().contains(field)) {
         throw new IllegalArgumentException(field.getName() + " is not a field in " + data.getFullyQualifiedName());
      }
      if (field.getCardinality() != expectedCardinality) {
         throw new IllegalStateException("Cannot get value" + (expectedCardinality == FieldCardinality.MANY ? "s" : "")
                                         + " for field " + field.getName() + ": the cardinality of the field is "
                                         + field.getCardinality());
      }
      if (!Arrays.asList(expectedDataTypes).contains(field.getType())) {
         throw new IllegalStateException("Cannot get value" + (expectedCardinality == FieldCardinality.MANY ? "s" : "")
                                         + " for field " + field.getName() + ": the data type of the field is " + field
                                               .getType());
      }
   }

   private static boolean allSet(IData data, Map<String, Collection<? extends IPropertyValue>> fieldValuesMap) {
      Preconditions.checkNotNull(data, "data may not be null!");
      Preconditions.checkNotNull(fieldValuesMap, "field values may not be null!");
      INamedChildCollection<?, IDataField> dataFields = data.getFields();
      boolean allSet = true;
      if (dataFields.size() != fieldValuesMap.size()) {
         throw new IllegalArgumentException(
               "there need to be exactly 1 field value corresponding to each field in " + data.getFullyQualifiedName());
      }
      for (Map.Entry<String, Collection<? extends IPropertyValue>> entry : fieldValuesMap.entrySet()) {
         String fieldName = entry.getKey();
         if (!dataFields.getByName(fieldName).isPresent()) {
            throw new IllegalArgumentException(fieldName + " is not a field in " + data.getFullyQualifiedName());
         }
         IDataField field = dataFields.getByName(fieldName).get();
         Collection<? extends IPropertyValue> fieldValues = entry.getValue();
         if (field.getCardinality() == FieldCardinality.SINGLE && (fieldValues == null || fieldValues.size() != 1)) {
            throw new IllegalArgumentException("field " + field.getName() + " must have exactly 1 property value");
         }
         if (fieldValues == null) {
            allSet = false;
         } else {
            for (IPropertyValue fieldValue : fieldValues) {
               Preconditions.checkNotNull(fieldValue, "field values may not contain a null value!");
               if (field.getType() != fieldValue.getType()) {
                  throw new IllegalArgumentException("field " + fieldName + " has a type of " + field.getType()
                                                     + ", but the property value was of type " + fieldValue.getType());
               }
               allSet &= fieldValue.isSet();
            }
         }
      }
      return allSet;
   }

   @Override
   public int hashCode() {
      return Objects.hash(System.identityHashCode(data), fieldValues);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (!(obj instanceof PropertyDataValue)) {
         return false;
      }
      PropertyDataValue that = (PropertyDataValue) obj;
      return Objects.equals(fieldValues, that.fieldValues)
             && data == that.data;
   }

   @Override
   public String toString() {
      return "PropertyDataValue[data=" + data.getFullyQualifiedName() + ", fieldValues=" + fieldValues + "]";
   }

}
