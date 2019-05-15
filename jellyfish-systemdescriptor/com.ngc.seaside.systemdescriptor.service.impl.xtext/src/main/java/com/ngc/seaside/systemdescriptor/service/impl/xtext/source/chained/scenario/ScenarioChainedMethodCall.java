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
