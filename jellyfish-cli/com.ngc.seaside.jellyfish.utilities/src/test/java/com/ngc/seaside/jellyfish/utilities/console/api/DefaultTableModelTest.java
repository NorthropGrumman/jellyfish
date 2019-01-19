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
