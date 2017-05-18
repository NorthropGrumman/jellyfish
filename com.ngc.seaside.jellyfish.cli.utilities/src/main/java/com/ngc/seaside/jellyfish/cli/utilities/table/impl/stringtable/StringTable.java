package com.ngc.seaside.jellyfish.cli.utilities.table.impl.stringtable;

import com.ngc.seaside.jellyfish.cli.utilities.table.api.AbstractTable;
import com.ngc.seaside.jellyfish.cli.utilities.table.api.ITableFormat;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  private boolean showRowNumber = false;

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
  public boolean getShowHeader() {
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
   * Determine if the row number is displayed. This value defaults to false.
   *
   * @return boolean if the row number will be displayed in the toString value.
   */
  public boolean getShowRowNumber() {
    return showRowNumber;
  }

  /**
   * Set this value to true if you want the row number to be displayed.
   * This value defaults to false.
   *
   * @param showRowNumber the row number.
   */
  public void setShowRowNumber(boolean showRowNumber) {
    this.showRowNumber = showRowNumber;
  }

  /**
   * Get the rows of the table. This method builds the rows on demand.
   *
   * @return the rows.
   */
  public List<MultiLineRow> getRows() {
    Map<Integer, Integer> columnWidths = getColumnWidths();

    List<MultiLineRow> rows = new ArrayList<>();
    int totalItems = getModel().getItems().size();
    int rowNumber = 0;

    for (T item : getModel().getItems()) {
      MultiLineRow row = new MultiLineRow(rowNumber, totalItems, columnWidths);
      row.setColumnSpacer(columnSpacer);
      row.setShowRowNumber(showRowNumber);

      int maxLines = 1;

      for (int column = 0; column < getFormat().getColumnCount(); column++) {
        MultiLineCell cell = new MultiLineCell(column);

        if (getFormat().getColumnValue(item, column) == null) {
          throw new IllegalArgumentException(String.format(
              "getRows() Column '%s' Row: '%s' value is null", column, rowNumber));
        }

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
   * Get the header for the table. This method builds the header on demand.
   *
   * @return the header.
   */
  public MultiLineRow getHeader() {
    Map<Integer, Integer> columnWidths = getColumnWidths();
    MultiLineRow row = new MultiLineRow(0, getModel().getItems().size(), columnWidths);
    row.setShowRowNumber(showRowNumber);
    row.setColumnSpacer(columnSpacer);

    int maxLines = 0;
    for (int column = 0; column < getFormat().getColumnCount(); column++) {
      MultiLineCell cell = new MultiLineCell(column);
      String value = getFormat().getColumnName(column);

      if (value.length() > columnWidths.get(column)) {
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

    return row;
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
   * Determine the column widths for the table based on the format.
   *
   * @return the column widths.
   */
  protected Map<Integer, Integer> getColumnWidths() {
    Map<Integer, Integer> columnWidths = new HashMap<>();
    for (int column = 0; column < getFormat().getColumnCount(); column++) {
      columnWidths.put(column, getFormat().getColumnWidth(column));
    }

    int rowNumber = 0;
    for (T item : getModel().getItems()) {
      for (int column = 0; column < getFormat().getColumnCount(); column++) {

        if (getFormat().getColumnValue(item, column) == null) {
          throw new IllegalArgumentException(String.format(
              "getColumnWidths() Column '%s' Row: '%s' value is null", column, rowNumber));
        }

        int length = getFormat().getColumnValue(item, column).toString().length();

        switch (getFormat().getColumnSizePolicy(column)) {
          case FIXED:
            break;
          case MAX:
            //ensure the column width is set correctly
            if (columnWidths.get(column) < length) {
              columnWidths.put(column, length);
            }
            break;
          default:
            throw new IllegalArgumentException(
                String.format("Unable to determine size policy for column %s", column));
        }
        rowNumber++;
      }
    }

    return columnWidths;
  }

  /**
   * Build a String representation of the table.
   *
   * @param rows the rows.
   * @return The String representation of the table.
   */
  protected String toString(List<MultiLineRow> rows) {
    if (rows.isEmpty()) {
      return "";
    }

    StringBuilder builder = new StringBuilder();
    MultiLineRow firstRow = rows.get(0);
    final int ROW_WIDTH = firstRow.getWidth();


    /**
     * Print the header, wrapping
     */
    if (showHeader) {
      MultiLineRow header = getHeader();
      builder.append(getRowSeparator(ROW_WIDTH + 1));
      builder.append(header.toString());
    }

    builder.append(getRowSeparator(ROW_WIDTH + 1));

    for (MultiLineRow row : rows) {
      builder.append(row.toString())
          .append(getRowSeparator(ROW_WIDTH + 1));
    }
    return builder.toString();
  }

  protected String getRowSeparator(int width) {
    StringBuilder builder = new StringBuilder();
    final String ROW_SEP = StringUtils.rightPad("", width, rowSpacerCharacter);

    if(showRowNumber) {

    }

    builder.append(columnSpacer)
        .append(String.format("%s", ROW_SEP))
        .append(columnSpacer)
        .append(String.format("%n"));

    return builder.toString();
  }

  protected int getRowColumnWidth(int totalNumberOfRows) {
    return Integer.toString(totalNumberOfRows).length();
  }

  protected String getRowColumn(int rowNumber, int width) {
    return StringUtils.rightPad(String.format("%s", rowNumber), width);
  }

}
