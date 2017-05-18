package com.ngc.seaside.jellyfish.cli.utilities.console.impl.stringtable;

import com.ngc.seaside.jellyfish.cli.utilities.console.api.AbstractTable;
import com.ngc.seaside.jellyfish.cli.utilities.console.api.ITableFormat;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Prints a representation of data in a table format. The values in the model must provide a
 * meaningful toString method implementation. The Table allows for the ability to restrict the
 * width of the table and use the vertical space to populate the cells. Each row and cell will have
 * 1 to many number of lines some of which may be empty. The empty lines in the cell provide the
 * ability to have some columns be taller than others while maintaining an easily readable table.
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
    return getRows(getColumnWidths());
  }

  /**
   * Get the header for the table. This method builds the header on demand.
   *
   * @return the header.
   */
  public MultiLineRow getHeader() {
    return getHeader(getColumnWidths());
  }


  /**
   * get a String representation of the table.
   *
   * @return The String representation of the table.
   */
  @Override
  public String toString() {
    Map<Integer, Integer> map = getColumnWidths();
    return toString(getRows(map), map);
  }

  /**
   * Get the rows given the column widths.
   *
   * @param columnWidths the column widths.
   * @return the rows formatted for multiline use based on the format.
   */
  protected List<MultiLineRow> getRows(Map<Integer, Integer> columnWidths) {
    List<MultiLineRow> rows = new ArrayList<>();
    int rowNumber = 0;
    for (T item : getModel().getItems()) {
      MultiLineRow row = new MultiLineRow(rowNumber);

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
   * Get the header given the column widths.
   *
   * @param columnWidths the column widths for each column in the table.
   * @return the header of the table.
   */
  protected MultiLineRow getHeader(Map<Integer, Integer> columnWidths) {
    MultiLineRow row = new MultiLineRow(-1);
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
            //ensure that the column width is updated if the
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
  protected String toString(List<MultiLineRow> rows, Map<Integer, Integer> columnWidths) {
    if (rows.isEmpty()) {
      return "";
    }
    StringBuilder builder = new StringBuilder();
    final int ROW_WIDTH = getTableWidth(columnWidths);

    if (showHeader) {
      MultiLineRow header = getHeader();
      builder.append(buildRowSeparator(ROW_WIDTH + 1));
      builder.append(rowToString(header, columnWidths));
    }

    builder.append(buildRowSeparator(ROW_WIDTH + 1));

    for (MultiLineRow row : rows) {
      builder.append(rowToString(row, columnWidths)).append(buildRowSeparator(ROW_WIDTH + 1));
    }
    return builder.toString();
  }

  /**
   * Get the table's width. This includes the spacers on the ends of the table.
   *
   * @param columnWidths the column widths.
   * @return the table's width in characters.
   */
  protected int getTableWidth(Map<Integer, Integer> columnWidths) {
    int width = 0;
    for (Integer columnWidth : columnWidths.values()) {
      width += columnWidth;
    }
    if (showRowNumber) {
      width += getRowColumnWidth(getModel().getItems().size()) + columnSpacer.length();
    }
    width += columnSpacer.length() * (columnWidths.size() - 1) - 1;

    return width;
  }

  /**
   * Convert the row to a String. The reason the row doesn't convert itself to a string
   * mostly is because of separation of concerns. This this method needs to know of attributes of
   * the table such as:
   * <pre>
   *   the column widths
   *   the column spacers
   *   total number of rows
   * </pre>
   * These attributes don't make sense to be passed to the row only so it can convert to a String.
   *
   * @param row             the row to convert.
   * @param columnWidthMap  the column widths.
   * @return the row as a String formatted for the display of this table.
   */
  protected String rowToString(MultiLineRow row, Map<Integer, Integer> columnWidthMap) {
    final StringBuilder builder = new StringBuilder();
    final String NEW_LINE = String.format("%n");
    final int ROW_COLUMN_WIDTH = getRowColumnWidth(getModel().getItems().size());

    for (int i = 0; i < row.getNumberOfLines(); i++) {
      if (showRowNumber) {
        if (i == 0) {
          builder.append(columnSpacer);
          if (row.getRowNumber() < 0) {
            builder.append(StringUtils.rightPad("", ROW_COLUMN_WIDTH));
          } else {
            builder.append(getRowColumn(row.getRowNumber(), ROW_COLUMN_WIDTH));
          }
        } else {
          builder.append(columnSpacer);
          builder.append(" ");
        }
      }

      List<MultiLineCell> cells = row.getCells();
      for (int j = 0; j < cells.size(); j++) {
        MultiLineCell cell = cells.get(j);
        builder.append(columnSpacer);
        builder.append(formatLine(cell.getLine(i), columnWidthMap.get(j)));
      }
      builder.append(columnSpacer);
      builder.append(NEW_LINE);
    }
    return builder.toString();
  }

  /**
   * Get the separator for the row of a table. The width should encompass the width of the table.
   *
   * @param tableWidth the width of the table.
   * @return the row separator.
   */
  protected String buildRowSeparator(int tableWidth) {
    StringBuilder builder = new StringBuilder();
    final String ROW_SEP = StringUtils.rightPad("", tableWidth, rowSpacerCharacter);

    builder.append(columnSpacer)
        .append(String.format("%s", ROW_SEP))
        .append(columnSpacer)
        .append(String.format("%n"));

    return builder.toString();
  }

  /**
   * Get the column max width given the total number of rows. This method is very important
   * when it comes to converting the row number to a String. This method should handle instances
   * where the table has more than 10 rows, 100 rows, 1000 rows and provide the correct number of
   * characters needed for the column in each case.
   *
   * totalNumberOfRows = 8 return 1
   * totalNumberOfRows = 10 return 2
   * totalNumberOfRows = 150 return 3
   *
   * @param totalNumberOfRows the total number of rows.
   * @return the total characters required to display that number of rows.
   */
  protected int getRowColumnWidth(int totalNumberOfRows) {
    return Integer.toString(totalNumberOfRows).length();
  }

  /**
   * Get the formatted row number given the width. See getRowColumnWidth.
   *
   * @param rowNumber the row's number.
   * @param width     the number of characters required to display the row.
   * @return the String representation of the row, padded to fit the width.
   */
  protected String getRowColumn(int rowNumber, int width) {
    return StringUtils.rightPad(String.format("%s", rowNumber), width);
  }

  /**
   * Format the line given the width that the line should be padded to.
   *
   * @param value the value.
   * @param width the width of the string.
   * @return the formatted string padded with spaces for any remaining width.
   */
  protected String formatLine(String value, int width) {
    return String.format("%-" + width + "s", value);
  }


}
