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
      while (lines.size() < totalNumberOfLines) {
         addLine("");
      }
   }

   /**
    * Get the line given the number of the line.
    *
    * @param lineNumber the number of the line
    * @return the line.
    */
   public String getLine(int lineNumber) {
      return lines.get(lineNumber);
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
    * See also {@link #getLine(int)}}
    *
    * @return the lines.
    */
   public List<String> getLines() {
      return lines;
   }
}
