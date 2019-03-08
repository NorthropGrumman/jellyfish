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

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyValues;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Cardinality;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Enumeration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Properties;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedPropertyFieldDeclaration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WrappedEnumerationPropertyTest extends AbstractWrappedXtextTest {

   private WrappedEnumerationProperty wrappedProperty;

   private ReferencedPropertyFieldDeclaration property;

   private Enumeration referencedEnum;

   @Mock
   private IEnumeration referenced;

   @Mock
   private IProperties parent;

   @Mock
   private IPackage pack;

   @Before
   public void setup() throws Throwable {
      Properties parentData = factory().createProperties();

      referencedEnum = factory().createEnumeration();
      referencedEnum.setName("ReferencedEnum");

      Package packageZ = factory().createPackage();
      packageZ.setName("my.foo.enums");
      packageZ.setElement(referencedEnum);

      property = factory().createReferencedPropertyFieldDeclaration();
      property.setName("property1");
      property.setDataModel(referencedEnum);
      property.setCardinality(Cardinality.DEFAULT);
      parentData.getDeclarations().add(property);

      when(referenced.getName()).thenReturn(referencedEnum.getName());
      when(referenced.getParent()).thenReturn(pack);
      when(pack.getName()).thenReturn(packageZ.getName());

      when(resolver().getWrapperFor(parentData)).thenReturn(parent);
      when(resolver().getWrapperFor(referencedEnum)).thenReturn(referenced);
      when(resolver().findXTextEnum(referenced.getName(), packageZ.getName())).thenReturn(Optional.of(referencedEnum));
   }

   @Test
   public void testDoesWrapXtextObject() throws Throwable {
      wrappedProperty = new WrappedEnumerationProperty(resolver(), property, IPropertyValues.emptyPropertyValues());

      assertEquals("name not correct!",
                   wrappedProperty.getName(),
                   property.getName());
      assertEquals("parent not correct!",
                   parent,
                   wrappedProperty.getParent());
      assertEquals("referenced enum not correct!",
                   referenced,
                   wrappedProperty.getReferencedEnumeration());
      assertEquals("cardinality not correct!",
                   FieldCardinality.SINGLE,
                   wrappedProperty.getCardinality());
   }

   @Test
   public void testDoesCreateXtextObject() throws Throwable {
      IProperty newProperty = mock(IProperty.class);
      when(newProperty.getName()).thenReturn("newProperty");
      when(newProperty.getType()).thenReturn(DataTypes.ENUM);
      when(newProperty.getReferencedEnumeration()).thenReturn(referenced);
      when(newProperty.getCardinality()).thenReturn(FieldCardinality.MANY);

      ReferencedPropertyFieldDeclaration xtext = WrappedEnumerationProperty.toXtextReferencedPropertyFieldDeclaration(
            resolver(),
            newProperty);
      assertEquals("name not correct!",
                   newProperty.getName(),
                   xtext.getName());
      assertEquals("referenced enum not correct!",
                   referencedEnum,
                   xtext.getDataModel());
      assertEquals("cardinality not correct!",
                   xtext.getCardinality(),
                   Cardinality.MANY);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testDoesNotCreateXtextObjectForPrimitiveType() throws Throwable {
      IProperty newProperty = mock(IProperty.class);
      when(newProperty.getType()).thenReturn(DataTypes.INT);
      WrappedEnumerationProperty.toXtextReferencedPropertyFieldDeclaration(resolver(), newProperty);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testDoesNotCreateXtextObjectForDataType() throws Throwable {
      IProperty newProperty = mock(IProperty.class);
      when(newProperty.getType()).thenReturn(DataTypes.DATA);
      WrappedEnumerationProperty.toXtextReferencedPropertyFieldDeclaration(resolver(), newProperty);
   }
}
