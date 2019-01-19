/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.cli.command.report.requirementsallocation;

import com.ngc.seaside.jellyfish.utilities.console.api.ITableFormat;
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
         return " " + object.getId();
      } else if (column < getColumnCount()) {
         if (object.getModels().contains(getModelAt(column - 1))) {
            int colWidth = getColumnWidth(column);
            int leftPad = (colWidth + 1) / 2;

            return StringUtils.leftPad("X", leftPad);
         }
      }
      return "";
   }
}
