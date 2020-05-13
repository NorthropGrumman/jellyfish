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
package com.ngc.seaside.jellyfish.cli.command.help;

import com.ngc.seaside.jellyfish.api.ICommand;
import com.ngc.seaside.jellyfish.utilities.console.api.ITableFormat;

/**
 * Formatter for JellyFish commands with two columns: the name and the description.
 */
class JellyFishCommandFormat implements ITableFormat<ICommand<?>> {

   private final int lineWidth;
   private final int columnWidth;
   private final int maxNameWidth;

   /**
    * Constructor.
    *
    * @param lineWidth    max line width for the entire table
    * @param columnWidth  width of column separator
    * @param maxNameWidth max length of command name
    */
   public JellyFishCommandFormat(int lineWidth, int columnWidth, int maxNameWidth) {
      this.lineWidth = lineWidth;
      this.columnWidth = columnWidth;
      this.maxNameWidth = maxNameWidth;
   }

   @Override
   public int getColumnCount() {
      return 2;
   }

   @Override
   public String getColumnName(int column) {
      switch (column) {
         case 0:
            return "Name";
         case 1:
            return "Description";
         default:
            throw new IndexOutOfBoundsException("Invalid column: " + column);
      }
   }

   @Override
   public ITableFormat.ColumnSizePolicy getColumnSizePolicy(int column) {
      switch (column) {
         case 0:
            return ITableFormat.ColumnSizePolicy.FIXED;
         case 1:
            return ITableFormat.ColumnSizePolicy.FIXED;
         default:
            throw new IndexOutOfBoundsException("Invalid column: " + column);
      }
   }

   @Override
   public int getColumnWidth(int column) {
      switch (column) {
         case 0:
            return maxNameWidth;
         case 1:
            return lineWidth - maxNameWidth - columnWidth * (getColumnCount() + 1);
         default:
            throw new IndexOutOfBoundsException("Invalid column: " + column);
      }
   }

   @Override
   public Object getColumnValue(ICommand<?> object, int column) {
      switch (column) {
         case 0:
            return object.getName();
         case 1:
            return object.getUsage().getDescription();
         default:
            throw new IndexOutOfBoundsException("Invalid column: " + column);
      }
   }

}
