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
package com.ngc.seaside.systemdescriptor.model.impl.view;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AggregatedDataViewTest {

   private AggregatedDataView view;

   @Mock
   private IData data;

   @Mock
   private IData parent;

   @Mock
   private IData grandparent;

   @Before
   public void setup() {
      INamedChildCollection<IData, IDataField> children = fields("a");
      when(data.getFields()).thenReturn(children);
      when(data.getExtendedDataType()).thenReturn(Optional.of(parent));
      when(data.getMetadata()).thenReturn(IMetadata.EMPTY_METADATA);

      children = fields("b");
      when(parent.getFields()).thenReturn(children);
      when(parent.getExtendedDataType()).thenReturn(Optional.of(grandparent));
      when(parent.getMetadata()).thenReturn(IMetadata.EMPTY_METADATA);

      children = fields("c");
      when(grandparent.getFields()).thenReturn(children);
      when(grandparent.getExtendedDataType()).thenReturn(Optional.empty());
      when(grandparent.getMetadata()).thenReturn(IMetadata.EMPTY_METADATA);

      view = new AggregatedDataView(data);
   }

   @Test
   public void testDoesReturnFieldsFromExtendedDataTypes() {
      assertTrue("missing field on data object!",
                 view.getFields().getByName("a").isPresent());
      assertTrue("missing field on parent data object!",
                 view.getFields().getByName("b").isPresent());
      assertTrue("missing field on grandparent data object!",
                 view.getFields().getByName("c").isPresent());
   }

   /**
    * Creates a collection of data fields with the given names.
    */
   public static INamedChildCollection<IData, IDataField> fields(String... names) {
      NamedChildCollection<IData, IDataField> collection = new NamedChildCollection<>();
      if (names != null) {
         for (String name : names) {
            IDataField field = Mockito.mock(IDataField.class);
            when(field.getName()).thenReturn(name);
            collection.add(field);
         }
      }
      return collection;
   }
}
