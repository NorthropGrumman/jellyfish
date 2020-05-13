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

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.CollectionChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.StringChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.sd.AbstractXtextChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Step;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.util.ITextRegion;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class ScenarioStepChainedMethodCall extends AbstractXtextChainedMethodCall<IScenarioStep, Step> {

   /**
    * @param step step
    * @param xtextStep xtext step
    * @param context context
    */
   public ScenarioStepChainedMethodCall(IScenarioStep step, Step xtextStep, ChainedMethodCallContext context) {
      super(step, xtextStep, context);
      try {
         register(IScenarioStep.class.getMethod("getParent"), this::thenGetParent);
         register(IScenarioStep.class.getMethod("getKeyword"), this::thenGetKeyword);
         register(IScenarioStep.class.getMethod("getParameters"), this::thenGetParameters);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<String> thenGetKeyword() {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextElement,
               SystemDescriptorPackage.Literals.STEP__KEYWORD, 0);
      IDetailedSourceLocation location = context.getSourceLocation(xtextElement, region);
      return new StringChainedMethodCall(location, context);
   }

   private IChainedMethodCall<List<String>> thenGetParameters() {
      EList<String> xtextParameters = xtextElement.getParameters();
      IDetailedSourceLocation location;
      if (xtextParameters.isEmpty()) {
         location = null;
      } else {
         ITextRegion firstRegion = context.getLocationInFileProvider().getFullTextRegion(xtextElement,
                  SystemDescriptorPackage.Literals.STEP__PARAMETERS, 0);
         ITextRegion lastRegion = context.getLocationInFileProvider().getFullTextRegion(xtextElement,
                  SystemDescriptorPackage.Literals.STEP__PARAMETERS, xtextParameters.size() - 1);
         ITextRegion region = firstRegion.merge(lastRegion);
         location = context.getSourceLocation(xtextElement, region);
      }
      List<Entry<String, IChainedMethodCall<String>>> list = new ArrayList<>(xtextParameters.size());
      int index = 0;
      for (String param : xtextParameters) {
         ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextElement,
                  SystemDescriptorPackage.Literals.STEP__PARAMETERS, index);
         IDetailedSourceLocation paramLocation = context.getSourceLocation(xtextElement, region);
         IChainedMethodCall<String> methodCall = new StringChainedMethodCall(paramLocation, context);
         list.add(new SimpleImmutableEntry<>(param, methodCall));
         index++;
      }
      return new CollectionChainedMethodCall<>(location, element.getParameters(), list, context);
   }

   private IChainedMethodCall<IScenario> thenGetParent() {
      return context.getChainedMethodCallForElement(element.getParent());
   }
}
