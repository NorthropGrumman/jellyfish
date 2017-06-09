package com.ngc.seaside.bootstrap.utilities.console.api;

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
   *         different for each column in the table.
   */
  Object getColumnValue(T object, int column);

}
