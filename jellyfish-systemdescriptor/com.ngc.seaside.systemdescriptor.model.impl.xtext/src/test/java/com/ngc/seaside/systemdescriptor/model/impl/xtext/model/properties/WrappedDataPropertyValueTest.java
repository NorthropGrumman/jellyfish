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

import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyEnumerationValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyPrimitiveValue;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Cardinality;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.EnumPropertyValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Enumeration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.IntValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Properties;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueAssignment;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueExpression;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueExpressionPathSegment;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataModelFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedPropertyFieldDeclaration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WrappedDataPropertyValueTest extends AbstractWrappedXtextTest {

   private WrappedDataPropertyValue value;

   private ReferencedPropertyFieldDeclaration propertyDeclaration;

   private Properties properties;

   private Data data;

   private Enumeration enumeration;

   private Data nestedData;

   @Mock
   private IData wrappedData;

   @Before
   public void setup() throws Throwable {
      PrimitiveDataFieldDeclaration intField = factory().createPrimitiveDataFieldDeclaration();
      intField.setName("x");
      intField.setCardinality(Cardinality.DEFAULT);

      enumeration = factory().createEnumeration();
      ReferencedDataModelFieldDeclaration enumField = factory().createReferencedDataModelFieldDeclaration();
      enumField.setName("y");
      enumField.setCardinality(Cardinality.DEFAULT);
      enumField.setDataModel(enumeration);

      PrimitiveDataFieldDeclaration nestedIntField = factory().createPrimitiveDataFieldDeclaration();
      nestedIntField.setName("foo");
      nestedIntField.setCardinality(Cardinality.DEFAULT);

      nestedData = factory().createData();
      nestedData.getFields().add(nestedIntField);

      ReferencedDataModelFieldDeclaration dataField = factory().createReferencedDataModelFieldDeclaration();
      dataField.setName("z");
      dataField.setCardinality(Cardinality.DEFAULT);
      dataField.setDataModel(nestedData);

      data = factory().createData();
      data.getFields().add(intField);
      data.getFields().add(enumField);
      data.getFields().add(dataField);

      propertyDeclaration = factory().createReferencedPropertyFieldDeclaration();
      propertyDeclaration.setDataModel(data);

      properties = factory().createProperties();
      properties.getDeclarations().add(propertyDeclaration);

      when(resolver().getWrapperFor(data)).thenReturn(wrappedData);
   }

   @Test
   public void testDoesWrapUnsetData1() {
      value = new TestableWrappedDataPropertyValue(resolver(), propertyDeclaration, properties);
      assertEquals("value type not correct!",
                   DataTypes.DATA,
                   value.getType());
      assertEquals("referenced data type not correct!",
                   wrappedData,
                   value.getReferencedDataType());
      assertFalse("should not be set!",
                  value.isSet());
   }

   @Test
   public void testDoesWrapUnsetData2() {
      setupForPrimitiveProperty();
      value = new TestableWrappedDataPropertyValue(resolver(), propertyDeclaration, properties);
      assertFalse("should not be set!",
                  value.isSet());
   }

   @Test
   public void testDoesWrapUnsetData3() {
      setupForPrimitiveProperty();
      setupForEnumProperty();
      value = new TestableWrappedDataPropertyValue(resolver(), propertyDeclaration, properties);
      assertFalse("should not be set!",
                  value.isSet());

      setupForDataProperty();
      value = new TestableWrappedDataPropertyValue(resolver(), propertyDeclaration, properties);
      assertTrue("should be set!",
                 value.isSet());
   }

   @Test
   public void testDoesWrapUnsetData4() {
      setupForPrimitiveProperty();
      setupForEnumProperty();
      setupForDataProperty();
      value = new TestableWrappedDataPropertyValue(resolver(), propertyDeclaration, properties);
      assertTrue("should be set!",
                 value.isSet());
   }

   @Test
   public void testDoesWrapSetDataPrimitive() {
      setupForPrimitiveProperty();
      setupForEnumProperty();
      setupForDataProperty();

      value = new TestableWrappedDataPropertyValue(resolver(), propertyDeclaration, properties);
      assertTrue("should be set!",
                 value.isSet());

      IPropertyPrimitiveValue intValue = value.getPrimitive(fieldWithName("x", DataTypes.INT));
      assertTrue("intValue should be set!",
                 intValue.isSet());
      assertEquals("intValue not correct!",
                   BigInteger.ONE,
                   intValue.getInteger());
   }

   @Test
   public void testDoesWrapSetDataEnum() {
      setupForPrimitiveProperty();
      setupForEnumProperty();
      setupForDataProperty();

      value = new TestableWrappedDataPropertyValue(resolver(), propertyDeclaration, properties);
      assertTrue("should be set!",
                 value.isSet());

      IPropertyEnumerationValue enumValue = value.getEnumeration(fieldWithName("y", DataTypes.ENUM));
      assertTrue("enumValue should be set!",
                 enumValue.isSet());
      assertEquals("enumValue not correct!",
                   "HELLO_WORLD",
                   enumValue.getValue());
   }

   @Test
   public void testDoesWrapSetDataWithNestedData() {
      setupForPrimitiveProperty();
      setupForEnumProperty();
      setupForDataProperty();

      value = new TestableWrappedDataPropertyValue(resolver(), propertyDeclaration, properties);
      assertTrue("should be set!",
                 value.isSet());

      IPropertyDataValue dataValue = value.getData(fieldWithName("z", DataTypes.DATA));
      assertTrue("dataValue should be set!",
                 dataValue.isSet());
      assertEquals("dataValue not correct!",
                   new BigInteger("100"),
                   dataValue.getPrimitive(fieldWithName("foo", DataTypes.INT)).getInteger());
   }

   private void setupForPrimitiveProperty() {
      IntValue value = factory().createIntValue();
      value.setValue(1);
      setupAssignmentFor("x", value);
   }

   private void setupForEnumProperty() {
      EnumPropertyValue value = factory().createEnumPropertyValue();
      value.setEnumeration(enumeration);
      value.setValue("HELLO_WORLD");
      setupAssignmentFor("y", value);
   }

   private void setupForDataProperty() {
      IntValue value = factory().createIntValue();
      value.setValue(100);

      PropertyValueExpressionPathSegment segment1 = factory().createPropertyValueExpressionPathSegment();
      segment1.setFieldDeclaration(data.getFields().stream()
                                         .filter(f -> f.getName().equals("z"))
                                         .findFirst()
                                         .get());
      PropertyValueExpressionPathSegment segment2 = factory().createPropertyValueExpressionPathSegment();
      segment2.setFieldDeclaration(nestedData.getFields().iterator().next());

      PropertyValueExpression exp = factory().createPropertyValueExpression();
      exp.setDeclaration(propertyDeclaration);
      exp.getPathSegments().add(segment1);
      exp.getPathSegments().add(segment2);

      PropertyValueAssignment assignment = factory().createPropertyValueAssignment();
      assignment.setExpression(exp);
      assignment.setValue(value);

      properties.getAssignments().add(assignment);
   }

   private void setupAssignmentFor(String propertyName, PropertyValue propertyValue) {
      PropertyValueExpressionPathSegment segment1 = factory().createPropertyValueExpressionPathSegment();
      segment1.setFieldDeclaration(data.getFields().stream()
                                         .filter(f -> f.getName().equals(propertyName))
                                         .findFirst()
                                         .get());

      PropertyValueExpression exp = factory().createPropertyValueExpression();
      exp.setDeclaration(propertyDeclaration);
      exp.getPathSegments().add(segment1);

      PropertyValueAssignment assignment = factory().createPropertyValueAssignment();
      assignment.setExpression(exp);
      assignment.setValue(propertyValue);

      properties.getAssignments().add(assignment);
   }

   /**
    * Mocks a field with the given name.
    */
   public static IDataField fieldWithName(String name, DataTypes type) {
      IDataField field = mock(IDataField.class);
      when(field.getName()).thenReturn(name);
      when(field.getType()).thenReturn(type);
      return field;
   }

   private static class TestableWrappedDataPropertyValue extends WrappedDataPropertyValue {

      public TestableWrappedDataPropertyValue(
            IWrapperResolver resolver,
            ReferencedPropertyFieldDeclaration propertyDeclaration,
            Properties propertiesContainer) {
         super(resolver, propertyDeclaration, propertiesContainer);
      }

      @Override
      protected NestedPropertyValueResolver createValueResolver(ReferencedPropertyFieldDeclaration propertyDeclaration,
                                                                Properties propertiesContainer) {
         return new NonTraversingPropertyValueResolver(propertyDeclaration, propertiesContainer);
      }
   }

   private static class NonTraversingPropertyValueResolver extends NestedPropertyValueResolver {

      public NonTraversingPropertyValueResolver(
            ReferencedPropertyFieldDeclaration declaration,
            Properties properties) {
         super(declaration, properties);
      }

      @Override
      protected void handleUnrecognizedPropertiesContainer(Properties properties) {
         propertiesToSearch.add(properties);
      }
   }
}
