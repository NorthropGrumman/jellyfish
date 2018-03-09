package com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

public class Property implements IProperty {

   static final DataTypes[] PRIMITIVES = { DataTypes.INT, DataTypes.FLOAT, DataTypes.BOOLEAN,
            DataTypes.STRING };

   private final String name;
   private IProperties parent;
   private final DataTypes type;
   private final FieldCardinality cardinality;
   private final List<? extends IPropertyValue> value;
   private final INamedChild<? extends IPackage> referencedType;

   /**
    * Constructs a property.
    *
    * @param name property name
    * @param type property type
    * @param cardinality property cardinality
    * @param values values of the property
    * @throws IllegalArgumentException if the number or type of values does not match the type or cardinality of the property
    */
   public Property(String name, DataTypes type, FieldCardinality cardinality,
                   Collection<? extends IPropertyValue> values) {
      Preconditions.checkNotNull(name, "name may not be null!");
      Preconditions.checkNotNull(type, "type may not be null!");
      Preconditions.checkNotNull(cardinality, "cardinality may not be null!");
      Preconditions.checkNotNull(values, "values may not be null!");
      if (cardinality == FieldCardinality.SINGLE && values.size() != 1) {
         throw new IllegalArgumentException("Expected a single property value");
      }
      if (type == DataTypes.DATA && !values.stream().allMatch(IPropertyDataValue.class::isInstance)) {
         throw new IllegalArgumentException("Expected property values to be of type IPropertyDataValue");
      }
      if (type == DataTypes.ENUM && !values.stream().allMatch(IPropertyEnumerationValue.class::isInstance)) {
         throw new IllegalArgumentException("Expected property values to be of type IPropertyEnumerationValue");
      }
      if ((type != DataTypes.DATA && type != DataTypes.ENUM)
         && !values.stream().allMatch(IPropertyPrimitiveValue.class::isInstance)) {
         throw new IllegalArgumentException("Expected property values to be of type IPropertyPrimitiveValue");
      }
      if (values.isEmpty()) {
         throw new IllegalArgumentException("Cannot determine referenced type when the values collection is empty");
      }
      this.name = name;
      this.type = type;
      this.cardinality = cardinality;
      this.value = Collections.unmodifiableList(new ArrayList<>(values));
      if (type == DataTypes.DATA) {
         this.referencedType = ((IPropertyDataValue) this.value.get(0)).getReferencedDataType();
      } else if (type == DataTypes.ENUM) {
         this.referencedType = ((IPropertyEnumerationValue) this.value.get(0)).getReferencedEnumeration();
      } else {
         this.referencedType = null;
      }
   }

   /**
    * Constructs a property with many cardinality, data/enumeration type and with the given referenced type. This can be used in the case that the values are empty.
    *
    * @param name property name
    * @param values values of the property
    * @param referencedType the type of data or enumeration
    * @throws IllegalArgumentException if the type of values does not match the type of the property
    */
   public Property(String name, Collection<? extends IPropertyValue> values,
                   INamedChild<? extends IPackage> referencedType) {
      Preconditions.checkNotNull(name, "name may not be null!");
      Preconditions.checkNotNull(values, "values may not be null!");
      Preconditions.checkNotNull(referencedType, "referenced type may not be null!");
      if (referencedType instanceof IData) {
         type = DataTypes.DATA;
      } else if (referencedType instanceof IEnumeration) {
         type = DataTypes.ENUM;
      } else {
         throw new IllegalArgumentException("Invalid referenced type: " + referencedType);
      }
      if (type == DataTypes.DATA && !values.stream().allMatch(IPropertyDataValue.class::isInstance)) {
         throw new IllegalArgumentException("Expected property values to be of type IPropertyDataValue");
      }
      if (type == DataTypes.ENUM && !values.stream().allMatch(IPropertyEnumerationValue.class::isInstance)) {
         throw new IllegalArgumentException("Expected property values to be of type IPropertyEnumerationValue");
      }
      if ((type != DataTypes.DATA && type != DataTypes.ENUM)
         && !values.stream().allMatch(IPropertyPrimitiveValue.class::isInstance)) {
         throw new IllegalArgumentException("Expected property values to be of type IPropertyPrimitiveValue");
      }
      this.name = name;
      this.value = Collections.unmodifiableList(new ArrayList<>(values));
      this.cardinality = FieldCardinality.MANY;
      this.referencedType = referencedType;
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
      return (PropertyDataValue) value.get(0);
   }

   @Override
   public IPropertyEnumerationValue getEnumeration() {
      checkTypeAndCardinality("enumeration", FieldCardinality.SINGLE, DataTypes.ENUM);
      return (PropertyEnumerationValue) value.get(0);
   }

   @Override
   public IPropertyPrimitiveValue getPrimitive() {
      checkTypeAndCardinality("primitive", FieldCardinality.SINGLE, PRIMITIVES);
      return (PropertyPrimitiveValue) value.get(0);
   }

   @SuppressWarnings("unchecked")
   @Override
   public Collection<IPropertyDataValue> getDatas() {
      checkTypeAndCardinality("data", FieldCardinality.MANY, DataTypes.DATA);
      return (Collection<IPropertyDataValue>) value;
   }

   @SuppressWarnings("unchecked")
   @Override
   public Collection<IPropertyEnumerationValue> getEnumerations() {
      checkTypeAndCardinality("enumeration", FieldCardinality.MANY, DataTypes.ENUM);
      return (Collection<IPropertyEnumerationValue>) value;
   }

   @SuppressWarnings("unchecked")
   @Override
   public Collection<IPropertyPrimitiveValue> getPrimitives() {
      checkTypeAndCardinality("primitive", FieldCardinality.MANY, PRIMITIVES);
      return (Collection<IPropertyPrimitiveValue>) value;
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

   @Override
   public int hashCode() {
      return Objects.hash(name, cardinality, System.identityHashCode(parent), type, value);
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
      return Objects.equals(name, that.name) &&
         Objects.equals(type, that.type) &&
         parent == that.parent &&
         Objects.equals(cardinality, that.cardinality) &&
         Objects.equals(value, that.value);
   }

   @Override
   public String toString() {
      return "Property[name=" + name +
         ", parent=" + parent +
         ", type=" + type +
         ", cardinality=" + cardinality +
         ", value=" + value +
         "]";
   }

}
