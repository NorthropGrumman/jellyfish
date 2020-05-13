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
package com.ngc.seaside.jellyfish.utilities.console.api;

/**
 * Think of this class as the Controller in a model-view-controller pattern.
 * It is responsible for accessing providing the contents of the table when queried.
 *
 * @author justan.provence@ngc.com
 */
public interface ITableFormat<T> {

   /**
    * The column currently has 2 different size policy settings. The renderer can use these settings
    * in order to create and size the cells in the table.
    */
   enum ColumnSizePolicy {
      /**
       * The fixed size policy ensures that the column never has a width greater than a specified
       * value. @see {@link #getColumnWidth(int)}
       */
      FIXED,
      /**
       * Use the max value in the column. This will ensure that all items in this column are visible
       * and should only require 1 line. The {@link #getColumnWidth(int)} value should be ignored
       * for any columns with this size policy.
       */
      MAX
   }

   /**
    * The number of columns.
    *
    * @return the number of columns being displayed.
    */
   int getColumnCount();

   /**
    * Gets the name of the specified column.
    *
    * @param column the column number. should be 0 indexed.
    * @return the column's header text.
    */
   String getColumnName(int column);

   /**
    * Get the size policy for the given column.
    *
    * @param column the column number.
    * @return the size policy.
    */
   ColumnSizePolicy getColumnSizePolicy(int column);

   /**
    * Get the column width. If the {@link ColumnSizePolicy} is fixed, use this method to provide the
    * column width.
    *
    * @param column the column number.
    */
   int getColumnWidth(int column);

   /**
    * Gets the value of the specified field for the specified object.
    *
    * @param object The object in the table.
    * @param column the column number. must not be greater than the column count.
    * @return the Object represented at that column. We return an Object because that type could be
    * different for each column in the table.
    */
   Object getColumnValue(T object, int column);

}
