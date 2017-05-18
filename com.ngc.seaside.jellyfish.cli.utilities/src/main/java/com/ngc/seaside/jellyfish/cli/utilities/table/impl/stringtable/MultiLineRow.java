package com.ngc.seaside.jellyfish.cli.utilities.table.impl.stringtable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class uses the {@link MultiLineCell} to provide a row of cells within a table that can
 * to organise String data that can be printed to a console in an easy to read format.
 *
 * @author justan.provence@ngc.com
 */
public class MultiLineRow {

  private int rowNumber = 0;
  private int numberOfLines = 1;
  private List<MultiLineCell> cells = new ArrayList<>();

  /**
   * Constructor. This class does not provide a default constructor. The rowNumber and columnWidths
   * are required in order for this class to "render" correctly.
   *
   * @param rowNumber the row number.
   */
  public MultiLineRow(int rowNumber) {
    this.rowNumber = rowNumber;
  }

  /**
   * Get the row number.
   *
   * @return the row number.
   */
  public int getRowNumber() {
    return rowNumber;
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

}
