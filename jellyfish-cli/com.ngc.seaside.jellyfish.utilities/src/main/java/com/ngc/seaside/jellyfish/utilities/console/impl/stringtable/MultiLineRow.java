/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.jellyfish.utilities.console.impl.stringtable;

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
