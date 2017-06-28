package com.ngc.seaside.jellyfish.cli.command.help;

import com.ngc.seaside.bootstrap.utilities.console.api.ITableFormat;
import com.ngc.seaside.command.api.IParameter;

class IParameterFormat implements ITableFormat<IParameter> {
   private final int lineWidth;
   private final int maxNameWidth;
   private final int columnWidth;

   public IParameterFormat(int lineWidth, int columnWidth, int maxNameWidth) {
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
   public Object getColumnValue(IParameter object, int column) {
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
