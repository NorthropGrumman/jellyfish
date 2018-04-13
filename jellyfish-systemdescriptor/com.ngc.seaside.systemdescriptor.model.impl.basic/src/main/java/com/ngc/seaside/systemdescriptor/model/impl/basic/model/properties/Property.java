package com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyEnumerationValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyPrimitiveValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValues;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Property implements IProperty {

   static final DataTypes[] PRIMITIVES = {DataTypes.INT, DataTypes.FLOAT, DataTypes.BOOLEAN,
                                          DataTypes.STRING};

   private final String name;
   private IProperties parent;
   private final DataTypes type;
   private final FieldCardinality cardinality;
   private final List<? extends IPropertyValue> values;
   private final INamedChild<? extends IPackage> referencedType;

   /**
    * Constructs a property.
    *
    * @param name           property name
    * @param type           property type
    * @param cardinality    property cardinality
    * @param values         values of the property, can be null for unset properties with many cardinality
    * @param referencedType data/enum that the property referenced, can be null
    * @throws IllegalArgumentException if the number or type of values does not match the type or cardinality of the
    *                                  property
    */
   public Property(String name, DataTypes type, FieldCardinality cardinality,
                   Collection<? extends IPropertyValue> values, INamedChild<? extends IPackage> referencedType) {
      Preconditions.checkNotNull(name, "name may not be null!");
      Preconditions.checkNotNull(type, "type may not be null!");
      Preconditions.checkNotNull(values, "values may not be null!");
      Preconditions.checkNotNull(cardinality, "cardinality may not be null!");
      if (cardinality == FieldCardinality.SINGLE && values.size() != 1) {
         throw new IllegalArgumentException("Expected a single property value");
      }
      if (type == DataTypes.DATA && !values.stream().allMatch(IPropertyDataValue.class::isInstance)) {
         throw new IllegalArgumentException("Expected property values to be of type IPropertyDataValue");
      }
      if (type == DataTypes.ENUM && !values.stream()
            .allMatch(IPropertyEnumerationValue.class::isInstance)) {
         throw new IllegalArgumentException("Expected property values to be of type IPropertyEnumerationValue");
      }
      if (type != DataTypes.DATA
            && type != DataTypes.ENUM
            && !values.stream().allMatch(IPropertyPrimitiveValue.class::isInstance)) {
         throw new IllegalArgumentException("Expected property values to be of type IPropertyPrimitiveValue");
      }
      if (referencedType == null && values.isEmpty()) {
         throw new IllegalArgumentException("Cannot determine referenced type when the values collection is empty");
      }

      this.name = name;
      this.type = type;
      this.cardinality = cardinality;
      this.values = Collections.unmodifiableList(new ArrayList<>(values));
      if (type == DataTypes.DATA) {
         Preconditions.checkArgument(referencedType == null || referencedType instanceof IData,
                                     "referenced type must be IData");
         this.referencedType =
               referencedType == null ? ((IPropertyDataValue) this.values.get(0)).getReferencedDataType()
                                      : referencedType;
      } else if (type == DataTypes.ENUM) {
         Preconditions.checkArgument(referencedType == null || referencedType instanceof IEnumeration,
                                     "referenced type must be IEnumeration");
         this.referencedType =
               referencedType == null ? ((IPropertyEnumerationValue) this.values.get(0)).getReferencedEnumeration()
                                      : referencedType;
      } else {
         Preconditions.checkArgument(referencedType == null, "cannot referenced type for primitive values");
         this.referencedType = null;
      }
   }

   @Override
   public String getName() {
      return name;
   }

   public Property setProperties(IProperties parent) {
      this.parent = parent;
      return this;
   }

   @Override
   public IProperties getParent() {
      return parent;
   }

   @Override
   public DataTypes getType() {
      return type;
   }

   @Override
   public FieldCardinality getCardinality() {
      return cardinality;
   }

   @Override
   public IData getReferencedDataType() {
      checkTypeAndCardinality("data", null, DataTypes.DATA);
      return (IData) referencedType;
   }

   @Override
   public IEnumeration getReferencedEnumeration() {
      checkTypeAndCardinality("enumeration", null, DataTypes.ENUM);
      return (IEnumeration) referencedType;
   }

   @Override
   public IPropertyDataValue getData() {
      checkTypeAndCardinality("data", FieldCardinality.SINGLE, DataTypes.DATA);
      return (PropertyDataValue) values.get(0);
   }

   @Override
   public IPropertyEnumerationValue getEnumeration() {
      checkTypeAndCardinality("enumeration", FieldCardinality.SINGLE, DataTypes.ENUM);
      return (PropertyEnumerationValue) values.get(0);
   }

   @Override
   public IPropertyPrimitiveValue getPrimitive() {
      checkTypeAndCardinality("primitive", FieldCardinality.SINGLE, PRIMITIVES);
      return (PropertyPrimitiveValue) values.get(0);
   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
   @Override
   public IPropertyValues<IPropertyDataValue> getDatas() {
      checkTypeAndCardinality("data", FieldCardinality.MANY, DataTypes.DATA);
      return IPropertyValues.of((Collection) values);
   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
   @Override
   public IPropertyValues<IPropertyEnumerationValue> getEnumerations() {
      checkTypeAndCardinality("enumeration", FieldCardinality.MANY, DataTypes.ENUM);
      return IPropertyValues.of((Collection) values);
   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
   @Override
   public IPropertyValues<IPropertyPrimitiveValue> getPrimitives() {
      checkTypeAndCardinality("primitive", FieldCardinality.MANY, PRIMITIVES);
      return IPropertyValues.of((Collection) values);
   }

   @Override
   public int hashCode() {
      return Objects.hash(name, cardinality, System.identityHashCode(parent), type, values);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (!(obj instanceof Property)) {
         return false;
      }
      Property that = (Property) obj;
      return Objects.equals(name, that.name)
            && Objects.equals(type, that.type)
            && parent == that.parent
            && Objects.equals(cardinality, that.cardinality)
            && Objects.equals(values, that.values);
   }

   @Override
   public String toString() {
      return "Property[name=" + name
            + ", parent=" + parent
            + ", type=" + type
            + ", cardinality=" + cardinality
            + ", values=" + values
            + "]";
   }

   private void checkTypeAndCardinality(String valueTypes, FieldCardinality cardinality, DataTypes... types) {
      if (cardinality != null && this.cardinality != cardinality) {
         throw new IllegalStateException(
               "cannot get " + valueTypes + " value" + (cardinality == FieldCardinality.SINGLE ? "" : "s")
                     + ": expected cardinality to be FieldCardinality." + cardinality);
      }
      if (!Arrays.asList(types).contains(this.type)) {
         throw new IllegalStateException(
               "cannot get " + valueTypes + " value" + (cardinality == FieldCardinality.SINGLE ? "" : "s")
                     + ": expected type to be " + (types.length > 1 ? "one of " : "") + Arrays.toString(types));
      }
   }
}
