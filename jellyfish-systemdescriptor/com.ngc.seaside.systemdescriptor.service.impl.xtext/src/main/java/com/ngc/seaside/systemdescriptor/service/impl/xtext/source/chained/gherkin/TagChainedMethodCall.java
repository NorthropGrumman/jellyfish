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

import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinTag;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.GherkinTag;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.DetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.StringChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;

import gherkin.ast.Tag;

public class TagChainedMethodCall extends AbstractGherkinChainedMethodCall<GherkinTag, Tag> {

   /**
    * @param tag tag
    * @param context context
    */
   public TagChainedMethodCall(GherkinTag tag, ChainedMethodCallContext context) {
      super(tag, context);
      try {
         register(IGherkinTag.class.getMethod("getName"), this::thenGetName);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   @Override
   protected int getLength() {
      return element.getName().length() + 1;
   }
   
   private IChainedMethodCall<String> thenGetName() {
      String line = getLine(element.getLineNumber());
      int column = line.indexOf("@" + element.getName()) + 2;
      DetailedSourceLocation location = DetailedSourceLocation.of(element.getPath(), element.getLineNumber(), column,
               element.getName().length());
      return new StringChainedMethodCall(location, context);
   }

}
