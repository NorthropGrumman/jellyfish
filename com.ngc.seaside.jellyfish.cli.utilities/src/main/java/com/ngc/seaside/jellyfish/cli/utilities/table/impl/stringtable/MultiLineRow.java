package com.ngc.seaside.jellyfish.cli.utilities.table.impl.stringtable;

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

  /**
   * Constructor. This class does not provide a default constructor. The rowNumber and columnWidths
   * are required in order for this class to "render" correctly.
   *
   * @param rowNumber      the row number.
   * @param columnWidthMap the width of the columns. This map may be mutated after being passed in
   *                       and should therefore not be copied.
   */
  public MultiLineRow(int rowNumber, Map<Integer, Integer> columnWidthMap) {
    this.rowNumber = rowNumber;
    this.columnWidthMap = columnWidthMap;
  }

  /**
   * Get the map.
   *
   * @return the column map.
   */
  public Map<Integer, Integer> getColumnWidthMap() {
    return columnWidthMap;
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
    cells.stream().forEach(cell -> cell.fillLines(totalNumberOfLines));
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    final String spacerStr = String.valueOf(columnSpacer);

    for (int i = 0; i < getNumberOfLines(); i++) {
      for (int j = 0; j < cells.size(); j++) {
        MultiLineCell cell = cells.get(j);
        builder.append(spacerStr);
        builder.append(cell.getFormattedLine(i, columnWidthMap.get(j)));
      }
      builder.append(spacerStr);
      builder.append(String.format("%n"));
    }
    return builder.toString();
  }

}
