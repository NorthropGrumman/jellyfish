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

import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.GherkinStepKeyword;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinStep;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinTable;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.GherkinStep;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.DetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.CollectionChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.EnumChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.OptionalChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.StringChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.UnknownSourceLocationException;

import gherkin.ast.DocString;
import gherkin.ast.Location;
import gherkin.ast.Node;
import gherkin.ast.Step;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GherkinStepChainedMethodCall
         extends AbstractGherkinChainedMethodCall<GherkinStep, Step> {

   private static final Pattern SCENARIO_NAME_PATTERN =
            Pattern.compile("^\\s*(?<keyword>Given|When|Then|And|But)\\s+(?<content>.*?)\\s*$");

   /**
    * @param step step
    * @param context context
    */
   public GherkinStepChainedMethodCall(GherkinStep step, ChainedMethodCallContext context) {
      super(step, context);
      try {
         register(IGherkinStep.class.getMethod("getContent"), this::thenGetContent);
         register(IGherkinStep.class.getMethod("getKeyword"), this::thenGetKeyword);
         register(IGherkinStep.class.getMethod("getTableArgument"), this::thenGetTableArgument);
         register(IGherkinStep.class.getMethod("getDocStringArgument"), this::thenGetDocStringArgument);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<String> thenGetContent() {
      String line = getLine(element.getLineNumber());
      Matcher m = SCENARIO_NAME_PATTERN.matcher(line);
      if (m.matches()) {
         String content = m.group("content");
         int column = m.start("content") + 1;
         IDetailedSourceLocation location = DetailedSourceLocation.of(element.getPath(), element.getLineNumber(),
                  column, content.length());
         return new StringChainedMethodCall(location, context);
      }
      throw new IllegalStateException(
               "Cannot find feature on line " + element.getLineNumber() + " in " + element.getPath());
   }

   private IChainedMethodCall<GherkinStepKeyword> thenGetKeyword() {
      String line = getLine(element.getLineNumber());
      Matcher m = SCENARIO_NAME_PATTERN.matcher(line);
      if (m.matches()) {
         String keyword = m.group("keyword");
         int column = m.start("keyword") + 1;
         IDetailedSourceLocation location = DetailedSourceLocation.of(element.getPath(), element.getLineNumber(),
                  column, keyword.length());
         return new EnumChainedMethodCall<>(location, context);
      }
      throw new IllegalStateException(
               "Cannot find feature on line " + element.getLineNumber() + " in " + element.getPath());
   }

   private IChainedMethodCall<Optional<IGherkinTable>> thenGetTableArgument() {
      IGherkinTable table = element.getTableArgument().orElseThrow(() -> new UnknownSourceLocationException(
               "Scenario step <" + element.getContent() + "> does not have a table argument"));
      IChainedMethodCall<IGherkinTable> methodCall = context.getChainedMethodCallForElement(table);
      return new OptionalChainedMethodCall<>(methodCall, methodCall.getLocation(), context);
   }

   private IChainedMethodCall<List<String>> thenGetDocStringArgument() {
      Node argument = element.unwrap().getArgument();
      if (!(argument instanceof DocString)) {
         throw new UnknownSourceLocationException(
                  "Step " + element.getContent() + " does not have a doc string argument");
      }
      DocString docString = (DocString) argument;
      Location docStringLocation = docString.getLocation();
      int column = docStringLocation.getColumn();
      List<Entry<String, IChainedMethodCall<String>>> list = new ArrayList<>();
      int lineNumber = docStringLocation.getLine() + 1;
      for (String line : element.getDocStringArgument()) {
         IDetailedSourceLocation location =
                  DetailedSourceLocation.of(element.getPath(), lineNumber, column, line.length());
         IChainedMethodCall<String> methodCall = new StringChainedMethodCall(location, context);
         list.add(new SimpleImmutableEntry<>(line, methodCall));
         lineNumber++;
      }
      DetailedSourceLocation startLocation = DetailedSourceLocation.of(element.getPath(), docStringLocation.getLine(),
               docStringLocation.getColumn(), 1);
      DetailedSourceLocation endLocation = DetailedSourceLocation.of(element.getPath(),
               docStringLocation.getLine() + element.getDocStringArgument().size() + 1, docStringLocation.getColumn(),
               3);
      return new CollectionChainedMethodCall<>(startLocation.merge(endLocation), element.getDocStringArgument(), list,
               context);
   }
}
