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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.codecompletion;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.ngc.seaside.systemdescriptor.extension.IScenarioStepCompletionExtension;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.WrappedSystemDescriptor;
import com.ngc.seaside.systemdescriptor.scenario.api.IScenarioStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.scenario.api.VerbTense;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Step;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.emf.ecore.EObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

public class XtextScenarioStepCodeCompletion implements IScenarioStepCompletionExtension {

   private final Map<VerbTense, Map<String, IScenarioStepHandler>> handlers = new LinkedHashMap<>();

   @Inject
   public XtextScenarioStepCodeCompletion(ScenarioStepsHandlerHolder holder) {
      for (IScenarioStepHandler handler : holder.scenarioStepHandlers) {
         for (ScenarioStepVerb verb : handler.getVerbs().values()) {
            this.handlers.computeIfAbsent(verb.getTense(), __ -> new LinkedHashMap<>()).put(verb.getVerb(), handler);
         }
      }
   }

   private VerbTense getTense(Step step) {
      switch (step.eClass().getClassifierID()) {
         case SystemDescriptorPackage.GIVEN_STEP:
            return VerbTense.PAST_TENSE;
         case SystemDescriptorPackage.WHEN_STEP:
            return VerbTense.PRESENT_TENSE;
         case SystemDescriptorPackage.THEN_STEP:
            return VerbTense.FUTURE_TENSE;
         default:
            throw new IllegalStateException("Cannot determine tense from step " + step);
      }
   }

   @Override
   public Set<String> completeKeyword(Step step, String partialKeyword) {
      Preconditions.checkNotNull(step, "step cannot be null");
      Preconditions.checkNotNull(partialKeyword, "partial keyword cannot be null");
      Set<String> potentialKeywords = new TreeSet<>();
      VerbTense tense = getTense(step);
      Map<String, IScenarioStepHandler> map = handlers.getOrDefault(tense, Collections.emptyMap());
      for (Entry<String, IScenarioStepHandler> entry : map.entrySet()) {
         String keyword = entry.getKey();
         if (keyword.startsWith(partialKeyword)) {
            potentialKeywords.add(keyword);
         }
      }
      return potentialKeywords;
   }

   @Override
   public Set<String> completeStepParameter(Step step, int parameterIndex, boolean insert) {
      Preconditions.checkNotNull(step, "step cannot be null");
      Preconditions.checkArgument(parameterIndex >= 0, "parameter index cannot be negative");
      
      ISystemDescriptor sd = getSystemDescriptor(step);
      VerbTense tense = getTense(step);
      Scenario scenario = (Scenario) step.eContainer().eContainer();
      Model model = (Model) scenario.eContainer();
      Package pkg = (Package) model.eContainer();
      IScenarioStep stepWrapper = sd.findModel(pkg.getName(), model.getName()).map(IModel::getScenarios)
               .flatMap(scenarios -> scenarios.getByName(scenario.getName()))
               .map(scenarioWrapper -> getStep(tense, scenarioWrapper, scenario, step))
               .orElseThrow(() -> new IllegalStateException("Cannot find IScenarioStep wrapper for step " + step));

      Map<String, IScenarioStepHandler> handlers = this.handlers.getOrDefault(tense, Collections.emptyMap());
      IScenarioStepHandler handler = handlers.get(step.getKeyword());
      if (handler == null) {
         return Collections.emptySet();
      } else {
         ScenarioStepVerb verb = ScenarioStepVerb.create(step.getKeyword(), tense);
         List<String> partialParameterCompletion = new ArrayList<>(step.getParameters());
         if (insert) {
            partialParameterCompletion.add(parameterIndex, "");
         }
         IScenarioStep stepWithPartialCompletion = new IScenarioStep() {

            @Override
            public IScenarioStep setKeyword(String keyword) {
               throw new UnsupportedOperationException();
            }

            @Override
            public IScenario getParent() {
               return stepWrapper.getParent();
            }

            @Override
            public List<String> getParameters() {
               return partialParameterCompletion;
            }

            @Override
            public String getKeyword() {
               return stepWrapper.getKeyword();
            }
         };
         return handler.getSuggestedParameterCompletions(stepWithPartialCompletion, verb, parameterIndex);
      }
   }

   private IScenarioStep getStep(VerbTense tense, IScenario scenarioWrapper, Scenario scenario, Step step) {
      List<? extends Step> steps;
      Collection<IScenarioStep> stepWrappers;
      switch (tense) {
         case FUTURE_TENSE:
            steps = scenario.getThen().getSteps();
            stepWrappers = scenarioWrapper.getThens();
            break;
         case PAST_TENSE:
            steps = scenario.getGiven().getSteps();
            stepWrappers = scenarioWrapper.getGivens();
            break;
         case PRESENT_TENSE:
            steps = scenario.getWhen().getSteps();
            stepWrappers = scenarioWrapper.getWhens();
            break;
         default:
            return null;
      }
      int index = steps.indexOf(step);
      if (index >= 0 && index < stepWrappers.size()) {
         return new ArrayList<>(stepWrappers).get(index);
      }
      return null;
   }

   private ISystemDescriptor getSystemDescriptor(Step step) {
      EObject parent = step;
      while (parent != null && !SystemDescriptorPackage.Literals.PACKAGE.isInstance(parent)) {
         parent = parent.eContainer();
      }
      Package pkg = (Package) parent;
      return new WrappedSystemDescriptor(pkg);
   }

   /**
    * A value holder to hold {@link IScenarioStepHandler}s. This is a workaround to Guice to
    * enable optional constructor parameters.
    */
   public static class ScenarioStepsHandlerHolder {
      @Inject(optional = true)
      Set<IScenarioStepHandler> scenarioStepHandlers = Collections.emptySet();
   }

}
