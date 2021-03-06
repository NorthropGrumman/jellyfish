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
package com.ngc.seaside.systemdescriptor.model.impl.basic;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Consumer;

import static com.ngc.seaside.systemdescriptor.model.impl.basic.TestUtils.demandImmutability;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NamedChildCollectionTest {

   private NamedChildCollection<IData, IDataField> collection;

   @Mock
   private IDataField field1;

   @Mock
   private IDataField field2;

   @Before
   public void setup() throws Throwable {
      when(field1.getName()).thenReturn("field1");
      when(field2.getName()).thenReturn("field2");

      collection = new NamedChildCollection<>();
   }


   @Test
   public void testDoesImplementCollection() throws Throwable {
      assertTrue("did not implement isEmpty correctly!",
                 collection.isEmpty());

      assertTrue("did not return true when adding!",
                 collection.add(field1));
      assertFalse("did not implement add correctly!",
                  collection.isEmpty());
      assertEquals("did not implement size correctly!",
                   1,
                   collection.size());
      assertTrue("did not contains correctly!",
                 collection.contains(field1));

      assertArrayEquals("did not implement toArray correctly!",
                        new Object[]{field1},
                        collection.toArray());
      assertArrayEquals("did not implement toArray correctly!",
                        new IDataField[]{field1},
                        collection.toArray(new IDataField[1]));

      assertTrue("did not return true when removing field!",
                 collection.remove(field1));
      assertTrue("did not remove field!",
                 collection.isEmpty());
      assertFalse("did not return false when removing non-existent field!",
                  collection.remove(field1));

      collection.add(field1);
      Iterator<IDataField> i = collection.iterator();
      assertTrue("iterator not correct!",
                 i.hasNext());
      assertEquals("iterator not correct!",
                   i.next(),
                   field1);
      i.remove();
      assertTrue("iterator not correct!",
                 collection.isEmpty());

      collection.addAll(Arrays.asList(field1, field2));
      assertEquals("did not implement addAll correctly!",
                   2,
                   collection.size());
      assertTrue("did not implement containsAll correctly!",
                 collection.containsAll(Arrays.asList(field1, field2)));
      assertTrue("did not return true if removing items",
                 collection.removeAll(Arrays.asList(field1, field2)));
      assertTrue("did not implement remove all!",
                 collection.isEmpty());
   }

   @Test
   public void testDoesImplementExtraOperations() throws Throwable {
      collection.add(field1);

      Optional<IDataField> field = collection.getByName(field1.getName());
      assertTrue("did not implement getByName correctly",
                 field.isPresent());
      assertEquals("did not implement getByName correctly!",
                   field1,
                   field.get());

      IDataField newField = mock(IDataField.class);
      when(newField.getName()).thenReturn("field1");
      assertTrue("did not replace existing field!",
                 collection.add(newField));
      assertSame("did not replace existing field!",
                 newField,
                 collection.getByName(field1.getName()).get());
   }

   @SuppressWarnings("unchecked")
   @Test
   public void testDoesIssueCallbacks() throws Throwable {
      Consumer<IDataField> addedConsumer = mock(Consumer.class);
      Consumer<IDataField> removedConsumer = mock(Consumer.class);
      collection.setOnChildAdded(addedConsumer);
      collection.setOnChildRemoved(removedConsumer);

      collection.add(field1);
      collection.remove(field1);

      verify(addedConsumer).accept(field1);
      verify(removedConsumer).accept(field1);
   }

   @Test
   public void testDoesMakeImmutableCollection() throws Throwable {
      collection.add(field1);

      INamedChildCollection<IData, IDataField> immutable = NamedChildCollection.immutable(collection);
      demandImmutability(() -> immutable.add(field2));
      demandImmutability(() -> immutable.addAll(Collections.singletonList(field2)));
      demandImmutability(() -> immutable.remove(field1));
      demandImmutability(() -> immutable.removeAll(Collections.singletonList(field1)));
      demandImmutability(() -> immutable.retainAll(Collections.singletonList(field1)));
      demandImmutability(() -> {
         Iterator<IDataField> i = immutable.iterator();
         i.next();
         i.remove();
      });
      demandImmutability(immutable::clear);
   }
}
