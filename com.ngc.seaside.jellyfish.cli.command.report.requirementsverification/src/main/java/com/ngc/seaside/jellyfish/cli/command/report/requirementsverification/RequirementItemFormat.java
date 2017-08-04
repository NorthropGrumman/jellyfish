package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification;

import com.ngc.seaside.bootstrap.utilities.console.api.ITableFormat;

import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

public class RequirementItemFormat implements ITableFormat<Requirement> {
   private Collection<String> features = new TreeSet<>(Collections.reverseOrder());

   public RequirementItemFormat(Collection<String> features) {
      this.features = features;
   }

   @Override
   public int getColumnCount() {
      return features.size();
   }

   @Override
   public String getColumnName(int column) {
      if (column == 0) {
         return "Req";
      } else if (column < getColumnCount()) {
         return " ".concat((String) features.toArray()[column - 1]);
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
         String feature = (String) features.toArray()[column - 1];
         if (object.getFeatures().contains(feature)) {
            return "X";
         }
      }
      return "";
   }
}
