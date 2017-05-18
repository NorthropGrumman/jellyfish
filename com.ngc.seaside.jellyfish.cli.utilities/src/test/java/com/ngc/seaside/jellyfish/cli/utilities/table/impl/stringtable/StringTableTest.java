package com.ngc.seaside.jellyfish.cli.utilities.table.impl.stringtable;

import com.ngc.seaside.jellyfish.cli.utilities.TestItem;
import com.ngc.seaside.jellyfish.cli.utilities.table.api.ITableFormat;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author justan.provence@ngc.com
 */
public class StringTableTest {

  private StringTable<TestItem> fixture;

  @Before
  public void setup() {
    fixture = new StringTable<>(new ItemItemFormat());
  }

  @Test
  public void doesPrint() {
    List<TestItem> items = new ArrayList<>(
        Arrays.asList(new TestItem[] {
            new TestItem("property",
                         "this is the description of a property",
                         "This is the last column for row 1"),
            new TestItem("another property",
                         "this is a very very large description of the property. "
                         + "This should create a few lines within the table.",
                         "This is the last column for the 2nd row"),
            new TestItem("this is the property",
                         "this is a description of a property as well.",
                         "This is the last column for the last row")
        }));
    fixture.getModel().addItems(items);
    fixture.setRowSpacerCharacter('-');
    fixture.setColumnSpacer(" | ");

    fixture.setShowHeader(true);

    System.out.println(fixture.toString());

  }

  private class ItemItemFormat implements ITableFormat<TestItem> {

    @Override
    public int getColumnCount() {
      return 3;
    }

    @Override
    public String getColumnName(int column) {
      switch (column) {
        case 0: return "A very very very very very very large header";
        case 1: return "Header";
        case 2: return "A Column Header that is somewhat large";
        default: return "";
      }
    }

    @Override
    public ColumnSizePolicy getColumnSizePolicy(int column) {
      switch (column) {
        case 0: return ColumnSizePolicy.MAX;
        case 1: return ColumnSizePolicy.FIXED;
        case 2: return ColumnSizePolicy.FIXED;
        default: return ColumnSizePolicy.MAX;
      }
    }

    @Override
    public int getColumnWidth(int column) {
      switch (column) {
        case 1: return 15;
        case 2: return 25;
        default: return -1; //the other columns are set to max, it will have no meaning
      }
    }

    @Override
    public Object getColumnValue(TestItem object, int column) {
      switch (column) {
        case 0: return object.getParam1();
        case 1: return object.getParam2();
        case 2: return object.getParam3();
        default: return "";
      }
    }
  }

}
