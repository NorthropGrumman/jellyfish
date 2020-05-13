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
package com.ngc.seaside.jellyfish.utilities.console.api;

import com.ngc.seaside.jellyfish.utilities.TestItem;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author justan.provence@ngc.com
 */
public class DefaultTableModelTest {

   private DefaultTableModel<TestItem> fixture;
   private TestListener listener;

   @Before
   public void setup() {
      fixture = new DefaultTableModel<>();
      listener = new TestListener();

      fixture.addTableModelListener(listener);
   }

   @Test
   public void doesAdd() {
      fixture.addItem(new TestItem("p", "p2", "p3"));
      assertEquals(1, listener.getChangeCount());
      assertEquals(1, fixture.getItems().size());
   }

   @Test
   public void doesRemove() {
      TestItem item = new TestItem("p", "p2", "p3");
      fixture.addItem(item);
      fixture.removeItem(item);
      assertEquals(2, listener.getChangeCount());
      assertEquals(0, fixture.getItems().size());
   }

   @Test
   public void doesRemoveAll() {
      fixture.addItem(new TestItem("p", "p2", "p3"));
      fixture.addItem(new TestItem("p", "p2", "p3"));
      assertEquals(2, listener.getChangeCount());
      assertEquals(2, fixture.getItems().size());
      fixture.removeAll();
      assertEquals(3, listener.getChangeCount());
      assertEquals(0, fixture.getItems().size());
   }

   @Test
   public void doesAddItems() {
      List<TestItem> items = new ArrayList<>(
            Arrays.asList(new TestItem[]{new TestItem("p", "p2", "p3"),
                                         new TestItem("p", "p2", "p3")}));
      fixture.addItems(items);
      assertEquals(1, listener.getChangeCount());
      assertEquals(2, fixture.getItems().size());

   }

   private class TestListener implements ITableModelListener {

      private int changeCount = 0;

      int getChangeCount() {
         return changeCount;
      }

      @Override
      public void modelChanged() {
         changeCount++;
      }
   }


}
