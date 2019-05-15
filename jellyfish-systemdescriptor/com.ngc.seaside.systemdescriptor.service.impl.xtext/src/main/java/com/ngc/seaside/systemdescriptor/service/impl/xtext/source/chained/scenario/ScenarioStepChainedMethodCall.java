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
