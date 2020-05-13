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
