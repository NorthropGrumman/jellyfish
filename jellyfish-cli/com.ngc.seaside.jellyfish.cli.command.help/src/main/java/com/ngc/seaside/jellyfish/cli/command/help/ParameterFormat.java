/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.cli.command.help;

import com.ngc.seaside.jellyfish.api.IParameter;
import com.ngc.seaside.jellyfish.utilities.console.api.ITableFormat;

/**
 * Formatter for parameters with two columns: the name and the description.
 */
class ParameterFormat implements ITableFormat<IParameter<?>> {

   private final int lineWidth;
   private final int maxNameWidth;
   private final int columnWidth;

   /**
    * Constructor.
    *
    * @param lineWidth    max line width for the entire table
    * @param columnWidth  width of column separator
    * @param maxNameWidth max length of command name
    */
   public ParameterFormat(int lineWidth, int columnWidth, int maxNameWidth) {
      super();
      this.lineWidth = lineWidth;
      this.maxNameWidth = maxNameWidth;
      this.columnWidth = columnWidth;
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
            throw new IllegalArgumentException("Invalid column: " + column);
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
            throw new IllegalArgumentException("Invalid column: " + column);
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
   public Object getColumnValue(IParameter<?> object, int column) {
      switch (column) {
         case 0:
            return object.getName();
         case 1:
            return object.getDescription();
         default:
            throw new IndexOutOfBoundsException("Invalid column: " + column);
      }
   }

}
