/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.gherkin;

import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinCell;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.GherkinCell;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.DetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.StringChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;

import gherkin.ast.TableCell;

public class GherkinCellChainedMethodCall
         extends AbstractGherkinChainedMethodCall<GherkinCell, TableCell> {

   /**
    * @param cell cell
    * @param context context
    */
   public GherkinCellChainedMethodCall(GherkinCell cell, ChainedMethodCallContext context) {
      super(cell, context);
      try {
         register(IGherkinCell.class.getMethod("getValue"), this::thenGetValue);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<String> thenGetValue() {
      IDetailedSourceLocation location = context.getSourceLocation(element);
      location = new DetailedSourceLocation.Builder(location).setLength(element.getValue().length()).build();
      return new StringChainedMethodCall(location, context);
   }

   @Override
   protected int getColumn() {
      String line = getLine(element.getLineNumber());
      return line.lastIndexOf('|', element.getColumn()) + 1;
   }

   @Override
   protected int getLength() {
      String line = getLine(element.getLineNumber());
      int length = line.indexOf('|', element.getColumn()) - line.lastIndexOf('|', element.getColumn()) + 1;
      return length;
   }

}
