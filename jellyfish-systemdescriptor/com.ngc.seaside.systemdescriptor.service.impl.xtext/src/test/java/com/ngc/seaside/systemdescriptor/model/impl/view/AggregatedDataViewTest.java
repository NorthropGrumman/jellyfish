/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
