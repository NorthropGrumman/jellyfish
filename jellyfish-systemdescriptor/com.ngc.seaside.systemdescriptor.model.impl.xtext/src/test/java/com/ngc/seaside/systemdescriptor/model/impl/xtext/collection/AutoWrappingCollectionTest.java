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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.collection;

import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.data.WrappedPrimitiveDataField;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataType;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AutoWrappingCollectionTest extends AbstractWrappedXtextTest {

   private AutoWrappingCollection<DataFieldDeclaration, IDataField> wrapped;

   private Data data;

   @Before
   public void setup() throws Throwable {
      data = factory().createData();

      wrapped = new AutoWrappingCollection<>(
            data.getFields(),
            f -> new WrappedPrimitiveDataField(resolver(), (PrimitiveDataFieldDeclaration) f),
            AutoWrappingCollection.defaultUnwrapper());

      PrimitiveDataFieldDeclaration field = factory().createPrimitiveDataFieldDeclaration();
      field.setName("field1");
      field.setType(PrimitiveDataType.STRING);
      data.getFields().add(field);
   }

   @Test
   public void testDoesWrapXtextList() throws Throwable {
      assertEquals("size not correct!",
                   1,
                   wrapped.size());
      IDataField field = wrapped.iterator().next();

      assertFalse("isEmpty not correct!",
                  wrapped.isEmpty());
      assertTrue("contains not correct!",
                 wrapped.contains(field));

      Iterator<IDataField> i = wrapped.iterator();
      assertTrue("iterator.hasNext() not correct!",
                 i.hasNext());
      assertEquals("iterator.next() not correct!",
                   field.getName(),
                   i.next().getName());
      i.remove();
      assertFalse("iterator.remove() not correct!",
                  i.hasNext());

      assertTrue("did not return true if added!",
                 wrapped.add(field));
      assertFalse("add not correct!",
                  wrapped.isEmpty());
      assertTrue("did not return true if removed!",
                 wrapped.remove(field));
      assertTrue("remove not correct!",
                 wrapped.isEmpty());
   }
}
