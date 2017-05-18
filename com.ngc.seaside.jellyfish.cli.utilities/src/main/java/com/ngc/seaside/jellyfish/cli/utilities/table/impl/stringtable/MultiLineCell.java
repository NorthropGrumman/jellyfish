package com.ngc.seaside.jellyfish.cli.utilities.table.impl.stringtable;

import java.util.ArrayList;
import java.util.List;

/**
 * A cell in a table that can have 1 to many lines of text.
 *
 * @author justan.provence@ngc.com
 */
public class MultiLineCell {
  private List<String> lines = new ArrayList<>();

  private int column = -1;

  /**
   * The cell column.
   *
   * @param column the column.
   */
  public MultiLineCell(int column) {
    this.column = column;
  }

  /**
   * Get the column number.
   *
   * @return the column.
   */
  public int getColumn() {
    return column;
  }


  /**
   * The row will use this method to fill in the missing lines with an empty String
   * so that the cell will still be displayed correctly.
   *
   * @param totalNumberOfLines the number of lines that should be in this cell.
   */
  public void fillLines(int totalNumberOfLines) {
    while(lines.size() <= totalNumberOfLines) {
      addLine("");
    }
  }

  /**
   * Get the formatted line given the width.
   *
   * @param lineNumber the number of the line
   * @param width      the width of the line.
   * @return the formatted line.
   */
  public String getFormattedLine(int lineNumber, int width) {
    return String.format("%-" + width + "s", lines.get(lineNumber));
  }

  /**
   * Add the given line to the cell.
   *
   * @param line the line.
   */
  public void addLine(String line) {
    lines.add(line);
  }

  /**
   * Add all the given lines to the cell.
   *
   * @param lines the lines.
   */
  public void addLines(List<String> lines) {
    this.lines.addAll(lines);
  }

  /**
   * Get the lines in this cell.
   * See also {@link #getFormattedLine(int, int)}
   *
   * @return the lines.
   */
  public List<String> getLines() {
    return lines;
  }
}
