package com.ngc.seaside.jellyfish.cli.utilities.table.impl.stringtable;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class uses the {@link MultiLineCell} to provide a row of cells within a table that can
 * to organise String data that can be printed to a console in an easy to read format.
 *
 * @author justan.provence@ngc.com
 */
public class MultiLineRow {

  private int rowNumber = 0;
  private List<MultiLineCell> cells = new ArrayList<>();
  private int numberOfLines = 1;
  private Map<Integer, Integer> columnWidthMap;
  private String columnSpacer = " ";
  private boolean showRowNumber = false;
  private int totalNumberOfRows = -1;

  /**
   * Constructor. This class does not provide a default constructor. The rowNumber and columnWidths
   * are required in order for this class to "render" correctly.
   *
   * @param rowNumber      the row number.
   * @param totalNumberOfRows the total number of rows in the table. This is used so that when the
   *                          toString is called it can print the row number with the necessary
   *                          number of spaces.
   * @param columnWidthMap the width of the columns. This map may be mutated after being passed in
   *                       and should therefore not be copied.
   */
  public MultiLineRow(int rowNumber, int totalNumberOfRows, Map<Integer, Integer> columnWidthMap) {
    this.rowNumber = rowNumber;
    this.totalNumberOfRows = totalNumberOfRows;
    this.columnWidthMap = columnWidthMap;
  }

  /**
   * Get the width of the entire row.
   *
   * @return the width.
   */
  public int getWidth() {
    int width = 0;
    for (Integer w : columnWidthMap.values()) {
      width += w;
    }
    if(showRowNumber) {
      width += getRowColumnWidth(totalNumberOfRows) + columnSpacer.length();
    }

    return width + columnSpacer.length() * (cells.size() - 1) - 1;
  }

  /**
   * Set the column spacer. This is so we can calculate the width and print the row correctly.
   *
   * @param spacer the spacer between columns.
   */
  public void setColumnSpacer(String spacer) {
    this.columnSpacer = spacer;
  }

  /**
   * Set the number of lines that the row requires. This is affected by the multiline cells.
   * This will augment the existing cells to ensure each cell is the same size. This is due mainly
   * because we need to render (print) line by line, not cell by cell.
   *
   * @param numberOfLines the number of lines.
   */
  public void setNumberOfLines(int numberOfLines) {
    this.numberOfLines = numberOfLines;
    fillLines(numberOfLines);
  }

  /**
   * The number of lines for this row.
   *
   * @return the number of lines.
   */
  public int getNumberOfLines() {
    return numberOfLines;
  }

  /**
   * Add the cell. This assumes the cell is added in order.
   *
   * @param cell the cell.
   */
  public void addCell(MultiLineCell cell) {
    cells.add(cell);
  }

  /**
   * Fill the lines for the cells.
   *
   * @param totalNumberOfLines the number of lines.
   */
  public void fillLines(int totalNumberOfLines) {
    cells.forEach(cell -> cell.fillLines(totalNumberOfLines));
  }

  /**
   * Get the cells for this row.
   *
   * @return the cells.
   */
  public List<MultiLineCell> getCells() {
    return cells;
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


  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    final String NEW_LINE = String.format("%n");
    final int ROW_COLUMN_WIDTH = getRowColumnWidth(totalNumberOfRows);

    for (int i = 0; i < getNumberOfLines(); i++) {
      if(showRowNumber) {
        if (i == 0) {
          builder.append(columnSpacer);
          builder.append(getRowColumn(rowNumber, ROW_COLUMN_WIDTH));
        } else {
          builder.append(columnSpacer);
          builder.append(" ");
        }
      }

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

  protected int getRowColumnWidth(int totalNumberOfRows) {
    return Integer.toString(totalNumberOfRows).length();
  }

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
