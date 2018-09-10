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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Cardinality;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Output;
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WrappedOutputDataReferenceFieldTest extends AbstractWrappedXtextTest {

   private WrappedOutputDataReferenceField wrapped;

   private OutputDeclaration outputDeclaration;

   private Data dataType;

   @Mock
   private IModel parent;

   @Mock
   private IData wrappedDataType;

   @Before
   public void setup() throws Throwable {
      dataType = factory().createData();

      outputDeclaration = factory().createOutputDeclaration();
      outputDeclaration.setName("output1");
      outputDeclaration.setCardinality(Cardinality.MANY);
      outputDeclaration.setType(dataType);

      Output output = factory().createOutput();
      output.getDeclarations().add(outputDeclaration);

      Model model = factory().createModel();
      model.setOutput(output);

      when(resolver().getWrapperFor(model)).thenReturn(parent);
      when(resolver().getWrapperFor(dataType)).thenReturn(wrappedDataType);
   }

   @Test
   public void testDoesWrapXtextObject() throws Throwable {
      wrapped = new WrappedOutputDataReferenceField(resolver(), outputDeclaration);
      assertEquals("name not correct!",
                   wrapped.getName(),
                   outputDeclaration.getName());
      assertEquals("parent not correct!",
                   parent,
                   wrapped.getParent());
      assertEquals("cardinality not correct!",
                   FieldCardinality.MANY,
                   wrapped.getCardinality());
      assertEquals("type not correct!",
                   wrappedDataType,
                   wrapped.getType());
   }

   @Test
   public void testDoesUpdateXtextObject() throws Throwable {
      Data newXtextType = factory().createData();
      IData newType = mock(IData.class);
      IPackage p = mock(IPackage.class);
      when(newType.getName()).thenReturn("NewData");
      when(newType.getParent()).thenReturn(p);
      when(p.getName()).thenReturn("some.package");
      when(resolver().findXTextData(newType.getName(), p.getName())).thenReturn(Optional.of(newXtextType));

      wrapped = new WrappedOutputDataReferenceField(resolver(), outputDeclaration);
      wrapped.setCardinality(FieldCardinality.SINGLE);
      assertEquals("cardinality not updated!",
                   Cardinality.DEFAULT,
                   outputDeclaration.getCardinality());

      wrapped.setType(newType);
      assertEquals("type not updated!",
                   newXtextType,
                   outputDeclaration.getType());
   }

   @Test(expected = IllegalStateException.class)
   public void testDoesNotAllowMissingType() throws Throwable {
      IData newType = mock(IData.class);
      IPackage p = mock(IPackage.class);
      when(newType.getName()).thenReturn("NewData");
      when(newType.getParent()).thenReturn(p);
      when(p.getName()).thenReturn("some.package");
      when(resolver().findXTextData(newType.getName(), p.getName())).thenReturn(Optional.empty());

      wrapped = new WrappedOutputDataReferenceField(resolver(), outputDeclaration);
      wrapped.setType(newType);
   }
}
