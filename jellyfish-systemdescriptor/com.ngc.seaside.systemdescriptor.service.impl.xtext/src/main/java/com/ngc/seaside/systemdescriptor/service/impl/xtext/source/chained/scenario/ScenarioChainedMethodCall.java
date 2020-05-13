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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.scenario;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.CollectionChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.StringChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.sd.AbstractXtextChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Step;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.util.ITextRegion;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

public class ScenarioChainedMethodCall extends AbstractXtextChainedMethodCall<IScenario, Scenario> {

   /**
    * @param scenario scenario
    * @param xtextScenario xtext scenario
    * @param context context
    */
   public ScenarioChainedMethodCall(IScenario scenario, Scenario xtextScenario, ChainedMethodCallContext context) {
      super(scenario, xtextScenario, context);

      try {
         register(IScenario.class.getMethod("getName"), this::thenGetName);
         register(IScenario.class.getMethod("getParent"), this::thenGetParent);
         register(IScenario.class.getMethod("getMetadata"), this::thenGetMetadata);
         register(IScenario.class.getMethod("getGivens"), this::thenGetGivens);
         register(IScenario.class.getMethod("getWhens"), this::thenGetWhens);
         register(IScenario.class.getMethod("getThens"), this::thenGetThens);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<String> thenGetName() {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextElement,
               SystemDescriptorPackage.Literals.SCENARIO__NAME, 0);
      IDetailedSourceLocation location = context.getSourceLocation(xtextElement, region);
      return new StringChainedMethodCall(location, context);
   }

   private IChainedMethodCall<IModel> thenGetParent() {
      return context.getChainedMethodCallForElement(element.getParent());
   }

   private IChainedMethodCall<IMetadata> thenGetMetadata() {
      return context.getChainedMethodCallForElement(element.getMetadata(), element);
   }

   private IChainedMethodCall<Collection<IScenarioStep>> thenGetGivens() {
      return thenGetSteps(xtextElement.getGiven(), xtextElement.getGiven().getSteps(), element.getGivens());
   }

   private IChainedMethodCall<Collection<IScenarioStep>> thenGetWhens() {
      return thenGetSteps(xtextElement.getWhen(), xtextElement.getWhen().getSteps(), element.getWhens());
   }

   private IChainedMethodCall<Collection<IScenarioStep>> thenGetThens() {
      return thenGetSteps(xtextElement.getThen(), xtextElement.getThen().getSteps(), element.getThens());
   }

   private IChainedMethodCall<Collection<IScenarioStep>> thenGetSteps(EObject declaration,
            EList<? extends Step> xtextSteps, Collection<IScenarioStep> steps) {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(declaration);
      IDetailedSourceLocation location = context.getSourceLocation(declaration, region);
      List<Entry<IScenarioStep, IChainedMethodCall<IScenarioStep>>> list = new ArrayList<>(steps.size());
      int index = 0;
      for (IScenarioStep step : steps) {
         Step xtextStep = xtextSteps.get(index);
         IChainedMethodCall<IScenarioStep> methodCall =
                  context.getChainedMethodCallForElement(step, xtextStep);
         list.add(new SimpleImmutableEntry<>(step, methodCall));
         index++;
      }
      return new CollectionChainedMethodCall<>(location, steps, list, context);
   }

   @Override
   public IDetailedSourceLocation getLocation() {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextElement);
      return context.getSourceLocation(xtextElement, region);
   }

}
