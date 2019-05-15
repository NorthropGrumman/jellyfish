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

import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinRow;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinTable;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.GherkinTable;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.CollectionChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.OptionalChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.UnknownSourceLocationException;

import gherkin.ast.Node;

import java.util.List;
import java.util.Optional;

public class GherkinTableChainedMethodCall
         extends AbstractGherkinChainedMethodCall<GherkinTable, Node> {

   /**
    * @param table table
    * @param context context
    */
   public GherkinTableChainedMethodCall(GherkinTable table, ChainedMethodCallContext context) {
      super(table, context);
      try {
         register(IGherkinTable.class.getMethod("getHeader"), this::thenGetHeader);
         register(IGherkinTable.class.getMethod("getRows"), this::thenGetRows);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<Optional<IGherkinRow>> thenGetHeader() {
      IGherkinRow row = element.getHeader()
               .orElseThrow(() -> new UnknownSourceLocationException("Table does not have a header row"));
      IChainedMethodCall<IGherkinRow> methodCall = context.getChainedMethodCallForElement(row);
      return new OptionalChainedMethodCall<>(methodCall, context);
   }

   private IChainedMethodCall<List<IGherkinRow>> thenGetRows() {
      List<IGherkinRow> rows = element.getRows();
      return new CollectionChainedMethodCall<>(getLocation(), rows, context::getChainedMethodCallForElement, context);
   }

}
