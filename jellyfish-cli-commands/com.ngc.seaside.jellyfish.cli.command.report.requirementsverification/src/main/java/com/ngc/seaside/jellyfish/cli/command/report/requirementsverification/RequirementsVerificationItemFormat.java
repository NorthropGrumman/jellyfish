package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification;

import com.ngc.seaside.jellyfish.utilities.console.api.ITableFormat;

import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

public class RequirementsVerificationItemFormat implements ITableFormat<Requirement> {
   private final int reqColWidth;
   private Collection<String> features = new TreeSet<>(Collections.reverseOrder());

   public RequirementsVerificationItemFormat(Collection<String> features, int reqColWidth) {
      this.features = features;
      this.reqColWidth = reqColWidth;
   }

   @Override
   public int getColumnCount() {
      return features.size() + 1;
   }

   @Override
   public String getColumnName(int column) {
      if (column == 0) {
         return "Req";
      } else if (column < getColumnCount()) {
         return " ".concat(getFeatureAt(column - 1));
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
         return this.reqColWidth + 2;
      } else if (column < getColumnCount()) {
         return getFeatureAt(column - 1).length() + 2;
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
            int colWidth = getColumnWidth(column);
            int leftPad = (colWidth + 1) / 2;

            return StringUtils.leftPad("X", leftPad);
         }
      }
      return "";
   }

   /**
    * Searches for a feature name at a location
    *
    * @param index index location to search
    * @return returns feature at index
    */
   private String getFeatureAt(int index) {
      return (String) features.toArray()[index];
   }
}
