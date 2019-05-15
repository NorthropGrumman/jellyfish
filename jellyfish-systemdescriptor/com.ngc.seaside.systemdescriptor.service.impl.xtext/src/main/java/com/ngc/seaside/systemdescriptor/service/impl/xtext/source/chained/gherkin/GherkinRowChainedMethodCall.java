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
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinRow;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.GherkinRow;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.CollectionChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.OptionalChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.UnknownSourceLocationException;

import gherkin.ast.TableRow;

import java.util.List;
import java.util.Optional;

public class GherkinRowChainedMethodCall
         extends AbstractGherkinChainedMethodCall<GherkinRow, TableRow> {

   /**
    * @param row row
    * @param context context
    */
   public GherkinRowChainedMethodCall(GherkinRow row, ChainedMethodCallContext context) {
      super(row, context);
      try {
         register(IGherkinRow.class.getMethod("getCell", String.class), this::thenGetCell);
         register(IGherkinRow.class.getMethod("getCells"), this::thenGetCells);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<Optional<IGherkinCell>> thenGetCell(String headerName) {
      IGherkinCell cell = element.getCell(headerName).orElseThrow(
               () -> new UnknownSourceLocationException("Table does not have a header with name " + headerName));
      IChainedMethodCall<IGherkinCell> methodCall = context.getChainedMethodCallForElement(cell, context);
      return new OptionalChainedMethodCall<>(methodCall, context);
   }

   private IChainedMethodCall<List<IGherkinCell>> thenGetCells() {
      List<IGherkinCell> cells = element.getCells();
      return new CollectionChainedMethodCall<>(getLocation(), cells, context::getChainedMethodCallForElement, context);
   }

   @Override
   protected int getLength() {
      String line = getLine(element.getLineNumber());
      return line.lastIndexOf('|') - line.indexOf('|') + 1;
   }
}
