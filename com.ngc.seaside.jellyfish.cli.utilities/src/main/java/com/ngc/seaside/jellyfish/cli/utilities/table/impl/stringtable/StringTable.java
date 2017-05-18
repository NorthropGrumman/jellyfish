package com.ngc.seaside.jellyfish.cli.utilities.table.impl.stringtable;

import com.ngc.seaside.jellyfish.cli.utilities.table.api.AbstractTable;
import com.ngc.seaside.jellyfish.cli.utilities.table.api.ITableFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Prints a table with representations of multi-line cells with the ability to set the amount of
 * space between the columns.
 *
 * @author justan.provence@ngc.com
 */
public class StringTable<T> extends AbstractTable<T> {

  private char rowSpacerCharacter = ' ';
  private String columnSpacer = "   ";
  private boolean showHeader = true;

  /**
   * Constructor.
   *
   * @param format the format.
   */
  public StringTable(ITableFormat<T> format) {
    super(format);
  }

  /**
   * Set the space between the column.
   *
   * @param spacer the columnSpacer value.
   */
  public void setColumnSpacer(String spacer) {
    this.columnSpacer = spacer;
  }

  /**
   * Get the amount of space between the columns.
   *
   * @return the amount of space.
   */
  public String getColumnSpacer() {
    return this.columnSpacer;
  }

  /**
   * Set the character to use for the space between rows. The default is set to an empty character.
   *
   * @param value the value.
   */
  public void setRowSpacerCharacter(char value) {
    rowSpacerCharacter = value;
  }

  /**
   * Show the header.
   *
   * @return true if the header will be displayed.
   */
  public boolean isShowHeader() {
    return showHeader;
  }

  /**
   * Should the header be displayed.
   *
   * @param showHeader true if displayed.
   */
  public void setShowHeader(boolean showHeader) {
    this.showHeader = showHeader;
  }

  /**
   * Get the rows of the table. This method builds the rows on demand.
   *
   * @return the rows.
   */
  public List<MultiLineRow> getRows() {
    Map<Integer, Integer> columnWidths = new HashMap<>();
    //set the initial column widths
    for (int column = 0; column < getFormat().getColumnCount(); column++) {
      columnWidths.put(column, getFormat().getColumnWidth(column));
    }

    List<MultiLineRow> rows = new ArrayList<>();
    int rowNumber = 0;

    for (T item : getModel().getItems()) {
      MultiLineRow row = new MultiLineRow(rowNumber, columnWidths);
      row.setColumnSpacer(columnSpacer);
      int maxLines = 1;

      for (int column = 0; column < getFormat().getColumnCount(); column++) {
        MultiLineCell cell = new MultiLineCell(column);

        String value = getFormat().getColumnValue(item, column).toString();
        int length = value.length();
        int width = columnWidths.get(column);

        switch (getFormat().getColumnSizePolicy(column)) {
          case FIXED:
            if (length > width) {
              List<String> lines = StringWrap.wrap(value, width, true);
              if (maxLines < lines.size()) {
                maxLines = lines.size();
              }
              cell.addLines(lines);
            } else {
              cell.addLine(value);
            }
            break;
          case MAX:
            //ensure the column width is set correctly
            if (columnWidths.get(column) < length) {
              columnWidths.put(column, length);
            }
            cell.addLine(value);
            break;
          default:
            throw new IllegalArgumentException(
                String.format("Unable to determine size policy for column %s", column));
        }

        row.addCell(cell);
      }

      row.setNumberOfLines(maxLines);
      rows.add(row);
      rowNumber++;
    }

    return rows;
  }

  /**
   * get a String representation of the table.
   *
   * @return The String representation of the table.
   */
  @Override
  public String toString() {
    return toString(getRows());
  }

  /**
   * Build a String representation of the table.
   *
   * @param rows the rows.
   * @return The String representation of the table.
   */
  protected String toString(List<MultiLineRow> rows) {
    if(rows.isEmpty()) {
      return "";
    }

    StringBuilder builder = new StringBuilder();
    final String NEW_LINE = String.format("%n");

    MultiLineRow firstRow = rows.get(0);

    final String ROW_SEP = StringUtils.rightPad(
        "",
        firstRow.getWidth() + 1,
        String.valueOf(rowSpacerCharacter));

    final String spacerStr = String.valueOf(columnSpacer);

    String header = headerToString(firstRow.getColumnWidthMap());

    if(showHeader) {
      builder.append(spacerStr)
          .append(String.format("%s", ROW_SEP))
          .append(spacerStr)
          .append(NEW_LINE);

      builder.append(header);
    }

    builder.append(spacerStr)
        .append(String.format("%s", ROW_SEP))
        .append(spacerStr)
        .append(NEW_LINE);

    for (MultiLineRow row : rows) {
      builder.append(row.toString())
          .append(spacerStr)
          .append(String.format("%s", ROW_SEP))
          .append(spacerStr)
          .append(NEW_LINE);
    }
    return builder.toString();
  }

  /**
   * Print the header.
   *
   * @param columnWidths the table column widths.
   * @return
   */
  protected String headerToString(Map<Integer, Integer> columnWidths) {
    MultiLineRow row = new MultiLineRow(-1, columnWidths);
    row.setColumnSpacer(columnSpacer);
    int maxLines = 0;
    for (int column = 0; column < getFormat().getColumnCount(); column++) {
      MultiLineCell cell = new MultiLineCell(column);
      String value = getFormat().getColumnName(column);

      if(value.length() > columnWidths.get(column)) {
        List<String> lines = StringWrap.wrap(value, columnWidths.get(column), true);
        if (maxLines < lines.size()) {
          maxLines = lines.size();
        }
        cell.addLines(lines);
      } else {
        cell.addLine(value);
      }

      row.addCell(cell);
    }
    row.setNumberOfLines(maxLines);

    return row.toString();
  }

}
