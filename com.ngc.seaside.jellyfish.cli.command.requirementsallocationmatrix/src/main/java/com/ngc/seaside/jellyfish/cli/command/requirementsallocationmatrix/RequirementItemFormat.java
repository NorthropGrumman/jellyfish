package com.ngc.seaside.jellyfish.cli.command.requirementsallocationmatrix;

import com.ngc.seaside.bootstrap.utilities.console.api.ITableFormat;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

public class RequirementItemFormat implements ITableFormat<Requirement> {
   private Collection<IModel> models = new TreeSet<>(Collections.reverseOrder());
   private final int reqColWidth;

   public RequirementItemFormat(Collection<IModel> models, int reqColWidth) {
      this.models = models;
      this.reqColWidth = reqColWidth;
   }

   @Override
   public int getColumnCount() {
      return models.size() + 1;
   }

   @Override
   public String getColumnName(int column) {
      if (column == 0) {
         return " Req ";
      } else if (column < getColumnCount()) {
         return " ".concat(getModelAt(column - 1).getName());
      }
      return "";
   }

   @Override
   public ColumnSizePolicy getColumnSizePolicy(int column) {
      if (column == 0) {
         return ColumnSizePolicy.FIXED;
      } else if (column < getColumnCount()) {
         return ColumnSizePolicy.FIXED;
      } else {
         return ColumnSizePolicy.FIXED;
      }
   }

   @Override
   public int getColumnWidth(int column) {
      final int columnWidth;

      if (column == 0) {
         columnWidth = this.reqColWidth + 2;
      } else if (column < getColumnCount()) {
         columnWidth = getModelAt(column - 1).getName().length() + 2;
      } else {
         columnWidth = -1;
      }

      return columnWidth;
   }

   private IModel getModelAt(int index) {
      return (IModel) models.toArray()[index];
   }
   @Override
   public Object getColumnValue(Requirement object, int column) {
      if (column == 0) {
         return " " + object.getID();
      } else if (column < getColumnCount()) {
         if (object.getModels().contains(getModelAt(column - 1))) {
            int colWidth = getColumnWidth(column);
            int leftPad = (colWidth+1)/2;
            
            return StringUtils.leftPad("X", leftPad);
         }
      }
      return "";
   }
}
