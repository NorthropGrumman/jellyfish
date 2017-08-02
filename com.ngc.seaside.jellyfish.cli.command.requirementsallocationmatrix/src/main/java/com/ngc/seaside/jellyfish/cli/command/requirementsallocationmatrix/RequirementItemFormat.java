package com.ngc.seaside.jellyfish.cli.command.requirementsallocationmatrix;

import com.ngc.seaside.bootstrap.utilities.console.api.ITableFormat;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

public class RequirementItemFormat implements ITableFormat<Requirement> {
   private Collection<String> models = new TreeSet<>(Collections.reverseOrder());

   public RequirementItemFormat(Collection<String> models) {
      this.models = models;
   }

   @Override
   public int getColumnCount() {
      return models.size();
   }

   @Override
   public String getColumnName(int column) {
      if (column == 0) {
         return "Req";
      } else if (column < getColumnCount()) {
         return " ".concat((String) models.toArray()[column - 1]);
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
      if (column == 0) {
         return 15;
      } else if (column < getColumnCount()) {
         return 30;
      } else {
         return -1;
      }
   }

   @Override
   public Object getColumnValue(Requirement object, int column) {
      if (column == 0) {
         return object.getID();
      } else if (column < getColumnCount()) {
         String model = (String) models.toArray()[column - 1];
         if (object.getModels().contains(model)) {
            return "X";
         }
      }
      return "";
   }
}

