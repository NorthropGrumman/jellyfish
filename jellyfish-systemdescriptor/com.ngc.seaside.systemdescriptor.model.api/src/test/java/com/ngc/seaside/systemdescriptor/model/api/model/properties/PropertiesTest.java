package com.ngc.seaside.systemdescriptor.model.api.model.properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;

import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PropertiesTest {

   private static final String PROPERTY_NAME = "property";
   private static final String FIELD1_NAME = "field1";
   private static final String FIELD2_NAME = "field2";

   private IProperties properties = mock(IProperties.class, CALLS_REAL_METHODS);

   @Test
   public void testValidSingleCase() {
      /**
       * properties {
       *     A property
       *     property.field1.field2 = 10
       * }
       * data A {
       *     B field1
       * }
       * data B {
       *     int field2
       * }
       *
       * properties.resolveAsInteger("property", "field1", "field2")
       */
      final BigInteger value = BigInteger.valueOf(10);

      final IProperty property = mock(IProperty.class);
      final IPropertyDataValue propertyValue = mock(IPropertyDataValue.class);
      final IPropertyDataValue field1Value = mock(IPropertyDataValue.class);
      final IPropertyPrimitiveValue field2Value = mock(IPropertyPrimitiveValue.class);
      final IDataField field1 = mock(IDataField.class);
      final IDataField field2 = mock(IDataField.class);

      when(properties.getByName(PROPERTY_NAME)).thenReturn(Optional.of(property));
      when(property.getName()).thenReturn(PROPERTY_NAME);
      when(property.getCardinality()).thenReturn(FieldCardinality.SINGLE);
      when(property.getType()).thenReturn(DataTypes.DATA);
      when(property.getValue()).thenReturn(propertyValue);
      when(property.getData()).thenReturn(propertyValue);

      when(propertyValue.getType()).thenReturn(DataTypes.DATA);
      when(propertyValue.isData()).thenReturn(true);
      when(propertyValue.getFieldByName(FIELD1_NAME)).thenReturn(Optional.of(field1));
      when(propertyValue.getValue(field1)).thenReturn(field1Value);
      when(propertyValue.getData(field1)).thenReturn(field1Value);

      when(field1.getType()).thenReturn(DataTypes.DATA);
      when(field1.getCardinality()).thenReturn(FieldCardinality.SINGLE);

      when(field1Value.getType()).thenReturn(DataTypes.DATA);
      when(field1Value.isData()).thenReturn(true);
      when(field1Value.getFieldByName(FIELD2_NAME)).thenReturn(Optional.of(field2));
      when(field1Value.getValue(field2)).thenReturn(field2Value);
      when(field1Value.getPrimitive(field2)).thenReturn(field2Value);

      when(field2.getType()).thenReturn(DataTypes.INT);
      when(field2.getCardinality()).thenReturn(FieldCardinality.SINGLE);

      when(field2Value.getType()).thenReturn(DataTypes.INT);
      when(field2Value.isPrimitive()).thenReturn(true);
      when(field2Value.getInteger()).thenReturn(value);

      Optional<BigInteger> optional = properties.resolveAsInteger(PROPERTY_NAME, FIELD1_NAME, FIELD2_NAME);
      assertNotNull(optional);
      assertTrue(optional.isPresent());
      assertEquals(value, optional.get());
   }

   @SuppressWarnings({"unchecked"})
   @Test
   public void testValidManyCaseDirectlyFromProperty() {
      /**
       * properties {
       *     A property
       *     property = [10, 20]
       * }
       * data A {
       *     B field1
       * }
       * data B {
       *     many int field2
       * }
       *
       * properties.resolveAsIntegers("property")
       */
      final List<BigInteger> values = Collections.singletonList(BigInteger.valueOf(10));

      final IProperty property = mock(IProperty.class);
      final IPropertyPrimitiveValue propertyValue = mock(IPropertyPrimitiveValue.class);

      when(properties.getByName(PROPERTY_NAME)).thenReturn(Optional.of(property));
      when(property.getName()).thenReturn(PROPERTY_NAME);
      when(property.getCardinality()).thenReturn(FieldCardinality.MANY);
      when(property.getType()).thenReturn(DataTypes.INT);
      when(property.getValues()).thenReturn((IPropertyValues) IPropertyValues.of(Collections.singleton(propertyValue)));

      when(propertyValue.getType()).thenReturn(DataTypes.INT);
      when(propertyValue.isPrimitive()).thenReturn(true);
      when(propertyValue.getInteger()).thenReturn(values.get(0));

      IPropertyValues<BigInteger> intValues = properties.resolveAsIntegers(PROPERTY_NAME);
      assertNotNull(intValues);
      assertTrue(intValues.isSet());
      assertEquals(new ArrayList<>(values), new ArrayList<>(intValues));
   }

   @SuppressWarnings({"unchecked"})
   @Test
   public void testValidManyCase() {
      /**
       * properties {
       *     A property
       *     property.field1.field2 = [10, 20]
       * }
       * data A {
       *     B field1
       * }
       * data B {
       *     many int field2
       * }
       *
       * properties.resolveAsIntegers("property", "field1", "field2")
       */
      final List<BigInteger> values = Arrays.asList(BigInteger.valueOf(10), BigInteger.valueOf(20));

      final IProperty property = mock(IProperty.class);
      final IPropertyDataValue propertyValue = mock(IPropertyDataValue.class);
      final IPropertyDataValue field1Value = mock(IPropertyDataValue.class);
      final IPropertyPrimitiveValue field2Value1 = mock(IPropertyPrimitiveValue.class);
      final IPropertyPrimitiveValue field2Value2 = mock(IPropertyPrimitiveValue.class);
      final IDataField field1 = mock(IDataField.class);
      final IDataField field2 = mock(IDataField.class);

      when(properties.getByName(PROPERTY_NAME)).thenReturn(Optional.of(property));
      when(property.getName()).thenReturn(PROPERTY_NAME);
      when(property.getCardinality()).thenReturn(FieldCardinality.SINGLE);
      when(property.getType()).thenReturn(DataTypes.DATA);
      when(property.getValue()).thenReturn(propertyValue);
      when(property.getData()).thenReturn(propertyValue);

      when(propertyValue.getType()).thenReturn(DataTypes.DATA);
      when(propertyValue.isData()).thenReturn(true);
      when(propertyValue.getFieldByName(FIELD1_NAME)).thenReturn(Optional.of(field1));
      when(propertyValue.getValue(field1)).thenReturn(field1Value);
      when(propertyValue.getData(field1)).thenReturn(field1Value);

      when(field1.getType()).thenReturn(DataTypes.DATA);
      when(field1.getCardinality()).thenReturn(FieldCardinality.SINGLE);

      when(field1Value.getType()).thenReturn(DataTypes.DATA);
      when(field1Value.isData()).thenReturn(true);
      when(field1Value.getFieldByName(FIELD2_NAME)).thenReturn(Optional.of(field2));
      when(field1Value.getValues(field2)).thenReturn((IPropertyValues) IPropertyValues.of(Arrays.asList(field2Value1,
                                                                                                        field2Value2)));
      when(field1Value.getPrimitives(field2)).thenReturn(IPropertyValues.of(Arrays.asList(field2Value1,
                                                                                          field2Value2)));

      when(field2.getType()).thenReturn(DataTypes.INT);
      when(field2.getCardinality()).thenReturn(FieldCardinality.MANY);

      when(field2Value1.getType()).thenReturn(DataTypes.INT);
      when(field2Value1.isPrimitive()).thenReturn(true);
      when(field2Value1.getInteger()).thenReturn(values.get(0));

      when(field2Value2.getType()).thenReturn(DataTypes.INT);
      when(field2Value2.isPrimitive()).thenReturn(true);
      when(field2Value2.getInteger()).thenReturn(values.get(1));

      IPropertyValues<BigInteger> intValues = properties.resolveAsIntegers(PROPERTY_NAME, FIELD1_NAME, FIELD2_NAME);
      assertNotNull(intValues);
      assertTrue(intValues.isSet());
      assertEquals(new ArrayList<>(values), new ArrayList<>(intValues));
   }

   @Test
   public void testUndefinedProperty() {
      /**
       * properties {
       * }
       *
       * properties.resolveAsInteger("property", "field1", "field2")
       */
      when(properties.getByName(PROPERTY_NAME)).thenReturn(Optional.empty());
      Optional<BigInteger> optional = properties.resolveAsInteger(PROPERTY_NAME, FIELD1_NAME, FIELD2_NAME);
      assertNotNull(optional);
      assertFalse(optional.isPresent());
   }

   @Test
   public void testUndefinedField() {
      /**
       * properties {
       *     A property
       * }
       * data A {
       * }
       *
       * properties.resolveAsInteger("property", "field1", "field2")
       */
      final IProperty property = mock(IProperty.class);
      final IPropertyDataValue propertyValue = mock(IPropertyDataValue.class);

      when(properties.getByName(PROPERTY_NAME)).thenReturn(Optional.of(property));
      when(property.getName()).thenReturn(PROPERTY_NAME);
      when(property.getCardinality()).thenReturn(FieldCardinality.SINGLE);
      when(property.getType()).thenReturn(DataTypes.DATA);
      when(property.getValue()).thenReturn(propertyValue);
      when(property.getData()).thenReturn(propertyValue);

      when(propertyValue.getType()).thenReturn(DataTypes.DATA);
      when(propertyValue.isData()).thenReturn(true);
      when(propertyValue.getFieldByName(FIELD1_NAME)).thenReturn(Optional.empty());

      Optional<BigInteger> optional = properties.resolveAsInteger(PROPERTY_NAME, FIELD1_NAME, FIELD2_NAME);
      assertNotNull(optional);
      assertFalse(optional.isPresent());
   }

   @Test
   public void testInvalidTypeAndCardinality() {
      /**
       * properties {
       *     A property
       *     property.field1.field2 = 10
       * }
       * data A {
       *     B field1
       * }
       * data B {
       *     int field2
       * }
       *
       * properties.resolveAsBoolean("property", "field1", "field2")
       * properties.resolveAsIntegers("property", "field1", "field2")
       */
      final BigInteger value = BigInteger.valueOf(10);

      final IProperty property = mock(IProperty.class);
      final IPropertyDataValue propertyValue = mock(IPropertyDataValue.class);
      final IPropertyDataValue field1Value = mock(IPropertyDataValue.class);
      final IPropertyPrimitiveValue field2Value = mock(IPropertyPrimitiveValue.class);
      final IDataField field1 = mock(IDataField.class);
      final IDataField field2 = mock(IDataField.class);

      when(properties.getByName(PROPERTY_NAME)).thenReturn(Optional.of(property));
      when(property.getName()).thenReturn(PROPERTY_NAME);
      when(property.getCardinality()).thenReturn(FieldCardinality.SINGLE);
      when(property.getType()).thenReturn(DataTypes.DATA);
      when(property.getValue()).thenReturn(propertyValue);
      when(property.getData()).thenReturn(propertyValue);

      when(propertyValue.getType()).thenReturn(DataTypes.DATA);
      when(propertyValue.isData()).thenReturn(true);
      when(propertyValue.getFieldByName(FIELD1_NAME)).thenReturn(Optional.of(field1));
      when(propertyValue.getValue(field1)).thenReturn(field1Value);
      when(propertyValue.getData(field1)).thenReturn(field1Value);

      when(field1.getType()).thenReturn(DataTypes.DATA);
      when(field1.getCardinality()).thenReturn(FieldCardinality.SINGLE);

      when(field1Value.getType()).thenReturn(DataTypes.DATA);
      when(field1Value.isData()).thenReturn(true);
      when(field1Value.getFieldByName(FIELD2_NAME)).thenReturn(Optional.of(field2));
      when(field1Value.getValue(field2)).thenReturn(field2Value);
      when(field1Value.getPrimitive(field2)).thenReturn(field2Value);

      when(field2.getType()).thenReturn(DataTypes.INT);
      when(field2.getCardinality()).thenReturn(FieldCardinality.SINGLE);

      when(field2Value.getType()).thenReturn(DataTypes.INT);
      when(field2Value.isPrimitive()).thenReturn(true);
      when(field2Value.getInteger()).thenReturn(value);

      Optional<Boolean> optional1 = properties.resolveAsBoolean(PROPERTY_NAME, FIELD1_NAME, FIELD2_NAME);
      assertNotNull(optional1);
      assertFalse(optional1.isPresent());

      IPropertyValues<BigInteger> intValues =  properties.resolveAsIntegers(PROPERTY_NAME, FIELD1_NAME, FIELD2_NAME);
      assertNotNull(intValues);
      assertFalse("values that could not be resolved should not be set!",
                  intValues.isSet());
   }

   @Test
   public void testInvalidIntermediateManyField() {
      /**
       * properties {
       *     A property
       * }
       * data A {
       *     many B field1
       * }
       * data B {
       *     int field2
       * }
       *
       * properties.resolveAsInteger("property", "field1", "field2")
       */
      final BigInteger value = BigInteger.valueOf(10);

      final IProperty property = mock(IProperty.class);
      final IPropertyDataValue propertyValue = mock(IPropertyDataValue.class);
      final IPropertyDataValue field1Value = mock(IPropertyDataValue.class);
      final IPropertyPrimitiveValue field2Value = mock(IPropertyPrimitiveValue.class);
      final IDataField field1 = mock(IDataField.class);
      final IDataField field2 = mock(IDataField.class);

      when(properties.getByName(PROPERTY_NAME)).thenReturn(Optional.of(property));
      when(property.getName()).thenReturn(PROPERTY_NAME);
      when(property.getCardinality()).thenReturn(FieldCardinality.SINGLE);
      when(property.getType()).thenReturn(DataTypes.DATA);
      when(property.getValue()).thenReturn(propertyValue);
      when(property.getData()).thenReturn(propertyValue);

      when(propertyValue.getType()).thenReturn(DataTypes.DATA);
      when(propertyValue.isData()).thenReturn(true);
      when(propertyValue.getFieldByName(FIELD1_NAME)).thenReturn(Optional.of(field1));
      when(propertyValue.getValue(field1)).thenReturn(field1Value);
      when(propertyValue.getData(field1)).thenReturn(field1Value);

      when(field1.getType()).thenReturn(DataTypes.DATA);
      when(field1.getCardinality()).thenReturn(FieldCardinality.MANY);

      when(field1Value.getType()).thenReturn(DataTypes.DATA);
      when(field1Value.isData()).thenReturn(true);
      when(field1Value.getFieldByName(FIELD2_NAME)).thenReturn(Optional.of(field2));
      when(field1Value.getValue(field2)).thenReturn(field2Value);
      when(field1Value.getPrimitive(field2)).thenReturn(field2Value);

      when(field2.getType()).thenReturn(DataTypes.INT);
      when(field2.getCardinality()).thenReturn(FieldCardinality.SINGLE);

      when(field2Value.getType()).thenReturn(DataTypes.INT);
      when(field2Value.isPrimitive()).thenReturn(true);
      when(field2Value.getInteger()).thenReturn(value);

      Optional<BigInteger> optional1 = properties.resolveAsInteger(PROPERTY_NAME, FIELD1_NAME, FIELD2_NAME);
      assertNotNull(optional1);
      assertFalse(optional1.isPresent());
   }

}
