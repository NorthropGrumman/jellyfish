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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.gherkin;

import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinScenario;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinStep;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinTable;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinTag;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.GherkinScenario;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.GherkinStep;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.DetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.CollectionChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.OptionalChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.StringChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.UnknownSourceLocationException;

import gherkin.ast.ScenarioDefinition;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GherkinScenarioChainedMethodCall
         extends AbstractGherkinChainedMethodCall<GherkinScenario, ScenarioDefinition> {

   private static final Pattern SCENARIO_NAME_PATTERN =
            Pattern.compile("^\\s*(?:Scenario|Scenario Outline|Background):\\s*(?<name>.*?)\\s*$");

   /**
    * @param scenario scenario
    * @param context context
    */
   public GherkinScenarioChainedMethodCall(GherkinScenario scenario, ChainedMethodCallContext context) {
      super(scenario, context);
      try {
         register(IGherkinScenario.class.getMethod("getName"), this::thenGetName);
         register(IGherkinScenario.class.getMethod("getDescription"), this::thenGetDescription);
         register(IGherkinScenario.class.getMethod("getTags"), this::thenGetTags);
         register(IGherkinScenario.class.getMethod("getSteps"), this::thenGetSteps);
         register(IGherkinScenario.class.getMethod("getGivens"), this::thenGetGivenSteps);
         register(IGherkinScenario.class.getMethod("getWhens"), this::thenGetWhenSteps);
         register(IGherkinScenario.class.getMethod("getThens"), this::thenGetThenSteps);
         register(IGherkinScenario.class.getMethod("getBackground"), this::thenGetBackground);
         register(IGherkinScenario.class.getMethod("getExamples"), this::thenGetExamples);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<String> thenGetName() {
      String line = getLine(element.getLineNumber());
      Matcher m = SCENARIO_NAME_PATTERN.matcher(line);
      if (m.matches()) {
         String description = m.group("name");
         int column = m.start("name") + 1;
         IDetailedSourceLocation location = DetailedSourceLocation.of(element.getPath(), element.getLineNumber(),
                  column, description.length());
         return new StringChainedMethodCall(location, context);
      }
      throw new IllegalStateException(
               "Cannot find feature on line " + element.getLineNumber() + " in " + element.getPath());
   }

   private IChainedMethodCall<String> thenGetDescription() {
      if (element.getDescription().isEmpty()) {
         throw new IllegalStateException("Scenario does not have a description");
      }
      int endLineNumber = element.getSteps().stream().findFirst().map(GherkinStep.class::cast)
               .map(GherkinStep::getLineNumber).orElse(Integer.MAX_VALUE);
      int startLineNumber = element.getLineNumber() + 1;
      List<String> lines = getLines(startLineNumber, endLineNumber - 1);
      for (ListIterator<String> itr = lines.listIterator(); itr.hasNext();) {
         if (itr.next().trim().isEmpty()) {
            itr.remove();
            startLineNumber++;
         } else {
            break;
         }
      }
      if (lines.isEmpty()) {
         throw new IllegalStateException("Feature description is empty for " + element.getPath());
      }
      DetailedSourceLocation location =
               DetailedSourceLocation.of(element.getPath(), startLineNumber, 1, element.getDescription().length());
      return new StringChainedMethodCall(location, context);
   }

   private IChainedMethodCall<Collection<IGherkinTag>> thenGetTags() {
      List<Entry<IGherkinTag, IChainedMethodCall<IGherkinTag>>> list = new ArrayList<>();
      for (IGherkinTag tag : element.getTags()) {
         IChainedMethodCall<IGherkinTag> methodCall = context.getChainedMethodCallForElement(tag);
         list.add(new SimpleImmutableEntry<>(tag, methodCall));
      }
      IDetailedSourceLocation location = list.stream().map(Entry::getValue).map(IChainedMethodCall::getLocation)
               .map(DetailedSourceLocation.Builder::new).map(DetailedSourceLocation.Builder::build)
               .reduce(DetailedSourceLocation::merge)
               .orElseThrow(() -> new IllegalStateException("Scenario does not have tags"));
      return new CollectionChainedMethodCall<>(location, element.getTags(), list, context);
   }

   private IChainedMethodCall<List<IGherkinStep>> thenGetSteps() {
      return getStepMethodCall(element.getSteps());
   }

   private IChainedMethodCall<List<IGherkinStep>> thenGetGivenSteps() {
      return getStepMethodCall(element.getGivens());
   }

   private IChainedMethodCall<List<IGherkinStep>> thenGetWhenSteps() {
      return getStepMethodCall(element.getWhens());
   }

   private IChainedMethodCall<List<IGherkinStep>> thenGetThenSteps() {
      return getStepMethodCall(element.getThens());
   }

   private IChainedMethodCall<List<IGherkinStep>> getStepMethodCall(List<IGherkinStep> steps) {
      DetailedSourceLocation location = steps.stream().map(context::getChainedMethodCallForElement)
               .map(IChainedMethodCall::getLocation).map(DetailedSourceLocation.Builder::new)
               .map(DetailedSourceLocation.Builder::build).reduce(DetailedSourceLocation::merge)
               .orElseThrow(() -> new IllegalStateException("Feature does not have any steps"));
      List<Entry<IGherkinStep, IChainedMethodCall<IGherkinStep>>> list = new ArrayList<>();
      for (IGherkinStep step : steps) {
         IChainedMethodCall<IGherkinStep> methodCall = context.getChainedMethodCallForElement(step);
         list.add(new SimpleImmutableEntry<>(step, methodCall));
      }
      return new CollectionChainedMethodCall<>(location, steps, list, context);
   }

   private IChainedMethodCall<Optional<IGherkinScenario>> thenGetBackground() {
      IGherkinScenario background = element.getBackground().orElseThrow(() -> new IllegalArgumentException(
               "Feature " + element.getParent().getName() + " does not have a background"));
      IChainedMethodCall<IGherkinScenario> methodCall = context.getChainedMethodCallForElement(background);
      return new OptionalChainedMethodCall<>(methodCall, methodCall.getLocation(), context);
   }

   private IChainedMethodCall<Optional<IGherkinTable>> thenGetExamples() {
      IGherkinTable examples = element.getExamples().orElseThrow(
               () -> new UnknownSourceLocationException("Scenario " + element.getName() + " does not have examples"));
      IChainedMethodCall<IGherkinTable> methodCall = context.getChainedMethodCallForElement(examples);
      return new OptionalChainedMethodCall<>(methodCall, context);
   }
}
