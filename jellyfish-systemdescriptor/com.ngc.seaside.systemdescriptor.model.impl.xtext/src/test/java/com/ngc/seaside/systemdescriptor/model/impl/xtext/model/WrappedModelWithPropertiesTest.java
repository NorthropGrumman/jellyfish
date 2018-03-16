package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyEnumerationValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyPrimitiveValue;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValue;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BasePartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BaseRequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Cardinality;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.EnumPropertyValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Enumeration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.InputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.IntValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataType;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitivePropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Properties;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueAssignment;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueExpression;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataModelFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedPropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

// Note we run this test with MockitoJUnitRunner.Silent to avoid UnnecessaryStubbingExceptions.  This happens because
// we are cheating and reusing the setup code for the data and model link tests.
@RunWith(MockitoJUnitRunner.Silent.class)
public class WrappedModelWithPropertiesTest extends AbstractWrappedXtextTest {

   private WrappedModel wrapped;

   private Model model;

   private Enumeration enumeration;

   @Mock
   private IPackage parent;

   @Mock
   private IEnumeration wrappedEnum;

   @Before
   public void setup() throws Throwable {
      model = factory().createModel();
      model.setName("MyModel");

      Data data = factory().createData();
      data.setName("Foo");

      Model modelType = factory().createModel();
      modelType.setName("Bar");

      InputDeclaration input = factory().createInputDeclaration();
      input.setName("input");
      input.setType(data);
      model.setInput(factory().createInput());
      model.getInput().getDeclarations().add(input);

      OutputDeclaration output = factory().createOutputDeclaration();
      output.setName("output");
      output.setType(data);
      model.setOutput(factory().createOutput());
      model.getOutput().getDeclarations().add(output);

      BaseRequireDeclaration require = factory().createBaseRequireDeclaration();
      require.setName("require");
      require.setType(modelType);
      model.setRequires(factory().createRequires());
      model.getRequires().getDeclarations().add(require);

      BasePartDeclaration part = factory().createBasePartDeclaration();
      part.setName("part");
      part.setType(modelType);
      model.setParts(factory().createParts());
      model.getParts().getDeclarations().add(part);

      Scenario scenario = factory().createScenario();
      scenario.setName("myScenario");
      model.getScenarios().add(scenario);

      enumeration = factory().createEnumeration();
      enumeration.setName("MyEnum");

      Package p = factory().createPackage();
      p.setName("my.package");
      p.setElement(model);
      when(resolver().getWrapperFor(p)).thenReturn(parent);
      when(resolver().getWrapperFor(enumeration)).thenReturn(wrappedEnum);
   }

   @Test
   public void testDoesWrapModelWithUnsetPrimitiveProperties() {
      PrimitivePropertyFieldDeclaration primitiveProperty = factory().createPrimitivePropertyFieldDeclaration();
      primitiveProperty.setType(PrimitiveDataType.INT);
      primitiveProperty.setCardinality(Cardinality.DEFAULT);
      primitiveProperty.setName("x");

      Properties properties = factory().createProperties();
      properties.getDeclarations().add(primitiveProperty);
      model.setProperties(properties);

      wrapped = new WrappedModel(resolver(), model);
      String propertyName = model.getProperties().getDeclarations().get(0).getName();
      Optional<IProperty> property = wrapped.getProperties().getByName(propertyName);
      assertTrue("property not present!",
                 property.isPresent());
      assertEquals("property type not correct!",
                   DataTypes.INT,
                   property.get().getType());
      assertEquals("cardinality not correct!",
                   FieldCardinality.SINGLE,
                   property.get().getCardinality());

      IPropertyValue wrappedValue = property.get().getValue();
      assertFalse("value should not be set",
                  wrappedValue.isSet());
   }

   @Test
   public void testDoesWrapModelWithSetPrimitiveProperties() {
      PrimitivePropertyFieldDeclaration primitiveProperty = factory().createPrimitivePropertyFieldDeclaration();
      primitiveProperty.setType(PrimitiveDataType.INT);
      primitiveProperty.setCardinality(Cardinality.DEFAULT);
      primitiveProperty.setName("x");

      IntValue value = factory().createIntValue();
      value.setValue(1);

      PropertyValueExpression exp = factory().createPropertyValueExpression();
      exp.setDeclaration(primitiveProperty);

      PropertyValueAssignment assignment = factory().createPropertyValueAssignment();
      assignment.setExpression(exp);
      assignment.setValue(value);

      Properties properties = factory().createProperties();
      properties.getDeclarations().add(primitiveProperty);
      properties.getAssignments().add(assignment);
      model.setProperties(properties);

      wrapped = new WrappedModel(resolver(), model);
      String propertyName = model.getProperties().getDeclarations().get(0).getName();
      Optional<IProperty> property = wrapped.getProperties().getByName(propertyName);
      assertTrue("property not present!",
                 property.isPresent());
      assertEquals("property type not correct!",
                   DataTypes.INT,
                   property.get().getType());
      assertEquals("cardinality not correct!",
                   FieldCardinality.SINGLE,
                   property.get().getCardinality());

      IPropertyValue wrappedValue = property.get().getValue();
      assertTrue("value should be set!",
                 wrappedValue.isSet());
      assertTrue("should be a primitive value!",
                 wrappedValue.isPrimitive());
      assertEquals("value type not correct!",
                   DataTypes.INT,
                   wrappedValue.getType());
      assertEquals("value not correct!",
                   BigInteger.ONE,
                   ((IPropertyPrimitiveValue) wrappedValue).getInteger());
   }

   @Test
   public void testDoesWrapModelWithUnsetEnumProperties() {
      ReferencedPropertyFieldDeclaration enumProperty = factory().createReferencedPropertyFieldDeclaration();
      enumProperty.setDataModel(enumeration);
      enumProperty.setCardinality(Cardinality.DEFAULT);
      enumProperty.setName("x");

      Properties properties = factory().createProperties();
      properties.getDeclarations().add(enumProperty);
      model.setProperties(properties);

      wrapped = new WrappedModel(resolver(), model);
      String propertyName = model.getProperties().getDeclarations().get(0).getName();
      Optional<IProperty> property = wrapped.getProperties().getByName(propertyName);
      assertTrue("property not present!",
                 property.isPresent());
      assertEquals("property type not correct!",
                   DataTypes.ENUM,
                   property.get().getType());
      assertEquals("cardinality not correct!",
                   FieldCardinality.SINGLE,
                   property.get().getCardinality());
      assertEquals("enum type not correct on property!",
                   wrappedEnum,
                   property.get().getReferencedEnumeration());

      IPropertyValue wrappedValue = property.get().getValue();
      assertFalse("value should not be set",
                  wrappedValue.isSet());
   }

   @Test
   public void testDoesWrapModelWithSetEnumProperties() {
      ReferencedPropertyFieldDeclaration enumProperty = factory().createReferencedPropertyFieldDeclaration();
      enumProperty.setDataModel(enumeration);
      enumProperty.setCardinality(Cardinality.DEFAULT);
      enumProperty.setName("x");

      EnumPropertyValue value = factory().createEnumPropertyValue();
      value.setEnumeration(enumeration);
      value.setValue("HELLO_WORLD");

      PropertyValueExpression exp = factory().createPropertyValueExpression();
      exp.setDeclaration(enumProperty);

      PropertyValueAssignment assignment = factory().createPropertyValueAssignment();
      assignment.setExpression(exp);
      assignment.setValue(value);

      Properties properties = factory().createProperties();
      properties.getDeclarations().add(enumProperty);
      properties.getAssignments().add(assignment);
      model.setProperties(properties);

      wrapped = new WrappedModel(resolver(), model);
      String propertyName = model.getProperties().getDeclarations().get(0).getName();
      Optional<IProperty> property = wrapped.getProperties().getByName(propertyName);
      assertTrue("property not present!",
                 property.isPresent());
      assertEquals("property type not correct!",
                   DataTypes.ENUM,
                   property.get().getType());
      assertEquals("cardinality not correct!",
                   FieldCardinality.SINGLE,
                   property.get().getCardinality());

      IPropertyValue wrappedValue = property.get().getValue();
      assertTrue("value should be set!",
                 wrappedValue.isSet());
      assertTrue("should be an enum value!",
                 wrappedValue.isEnumeration());
      assertEquals("value type not correct!",
                   DataTypes.ENUM,
                   wrappedValue.getType());
      assertEquals("value not correct!",
                   value.getValue(),
                   ((IPropertyEnumerationValue) wrappedValue).getValue());
      assertEquals("reference enum from value not correct!",
                   wrappedEnum,
                   ((IPropertyEnumerationValue) wrappedValue).getReferencedEnumeration());
   }
}
