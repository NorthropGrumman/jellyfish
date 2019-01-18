/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.data;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WrappedDataTest extends AbstractWrappedXtextTest {

   private WrappedData wrappedData;

   private Data data;

   private Data superType;

   @Mock
   private IPackage parent;

   @Mock
   private IPackage superTypePackage;

   @Mock
   private IData wrappedSuperType;

   @Before
   public void setup() throws Throwable {
      data = factory().createData();
      data.setName("Foo");

      superType = factory().createData();
      superType.setName("Super");

      PrimitiveDataFieldDeclaration field = factory().createPrimitiveDataFieldDeclaration();
      field.setName("field1");
      field.setType(PrimitiveDataType.STRING);
      data.getFields().add(field);

      Package p = factory().createPackage();
      p.setName("my.package");
      p.setElement(data);
      when(resolver().getWrapperFor(p)).thenReturn(parent);

      Package superP = factory().createPackage();
      superP.setName("my.super.package");
      superP.setElement(superType);

      when(resolver().getWrapperFor(superType)).thenReturn(wrappedSuperType);
   }

   @Test
   public void testDoesWrapXtextObject() throws Throwable {
      wrappedData = new WrappedData(resolver(), data);
      assertEquals("name not correct!",
                   wrappedData.getName(),
                   data.getName());
      assertEquals("fully qualified name not correct!",
                   "my.package.Foo",
                   wrappedData.getFullyQualifiedName());
      assertEquals("parent not correct!",
                   parent,
                   wrappedData.getParent());
      assertEquals("metadata not set!",
                   IMetadata.EMPTY_METADATA,
                   wrappedData.getMetadata());
      assertFalse("superType should not be set!",
                  wrappedData.getExtendedDataType().isPresent());

      String fieldName = data.getFields().get(0).getName();
      assertEquals("did not get fields!",
                   fieldName,
                   wrappedData.getFields().getByName(fieldName).get().getName());
   }

   @Test
   public void testDoesWrapXtextObjectWithSuperType() throws Throwable {
      data.setExtendedDataType(superType);
      wrappedData = new WrappedData(resolver(), data);
      assertEquals("did not return wrapper for superType!",
                   wrappedSuperType,
                   wrappedData.getExtendedDataType().get());
   }

   @Test
   public void testDoesUpdateXtextObject() throws Throwable {
      wrappedData = new WrappedData(resolver(), data);
      wrappedData.setMetadata(newMetadata("foo", "bar"));
      assertNotNull("metadata not set!",
                    data.getMetadata());
   }

   @Test
   public void testDoesUpdateXtextObjectWithSuperType() throws Throwable {
      String superTypePackageName = ((Package) superType.eContainer()).getName();
      when(resolver().findXTextData(superType.getName(), superTypePackageName))
            .thenReturn(Optional.of(superType));
      when(wrappedSuperType.getName()).thenReturn(superType.getName());
      when(wrappedSuperType.getParent()).thenReturn(superTypePackage);
      when(superTypePackage.getName()).thenReturn(superTypePackageName);

      data.setExtendedDataType(superType);
      wrappedData = new WrappedData(resolver(), data);
      wrappedData.setExtendedDataType(wrappedSuperType);
      assertEquals("did not update superType!",
                   superType.getName(),
                   wrappedData.getExtendedDataType().get().getName());
   }
}
