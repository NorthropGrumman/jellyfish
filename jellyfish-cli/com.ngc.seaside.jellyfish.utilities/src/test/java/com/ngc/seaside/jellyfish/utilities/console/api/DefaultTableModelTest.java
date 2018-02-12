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
