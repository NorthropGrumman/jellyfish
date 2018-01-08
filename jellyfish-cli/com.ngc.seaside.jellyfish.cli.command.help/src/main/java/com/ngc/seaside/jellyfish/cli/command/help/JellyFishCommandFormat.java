package com.ngc.seaside.jellyfish.cli.command.help;

import com.ngc.seaside.bootstrap.utilities.console.api.ITableFormat;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;

/**
 * Formatter for JellyFish commands with two columns: the name and the description.
 */
class JellyFishCommandFormat implements ITableFormat<IJellyFishCommand> {
   private final int lineWidth;
   private final int columnWidth;
   private final int maxNameWidth;

   /**
    * Constructor.
    * 
    * @param lineWidth max line width for the entire table
    * @param columnWidth width of column separator
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
   public Object getColumnValue(IJellyFishCommand object, int column) {
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
