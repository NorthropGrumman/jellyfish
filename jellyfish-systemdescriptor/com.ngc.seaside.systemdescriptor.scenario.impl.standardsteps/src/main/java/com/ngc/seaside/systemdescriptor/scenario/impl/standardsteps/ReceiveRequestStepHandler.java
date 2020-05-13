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
package com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.AbstractStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Implements the "receive request" step verb.  This verb is used to indicate a component is receiving a request.  It's
 * only argument is the input field that represents the request.  Only one receive request step can appear in a scenario
 * and no correlation is possible.
 */
public class ReceiveRequestStepHandler extends AbstractStepHandler {

   public static final ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("haveReceivedRequest");
   public static final ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("receivingRequest");
   public static final ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willReceiveRequest");

   public ReceiveRequestStepHandler() {
      register(PAST, PRESENT, FUTURE);
   }

   /**
    * Gets the {@code IDataReferenceField} of the input of the model the scenario  that is associated with that this
    * step. This can be used to determine which input field is received as the request.
    * <p/>
    * Only invoke this method with validated scenario steps.
    *
    * @param step the step that contains a receive verb
    * @return the input field of the model that is received
    */
   public IDataReferenceField getRequest(IScenarioStep step) {
      Preconditions.checkNotNull(step, "step may not be null!");
      String keyword = step.getKeyword();
      Preconditions.checkArgument(
            keyword.equals(PAST.getVerb())
                  || keyword.equals(PRESENT.getVerb())
                  || keyword.equals(FUTURE.getVerb()),
            "the step cannot be processed by this handler!");

      IModel model = step.getParent().getParent();
      String inputName = step.getParameters().get(0);
      return model.getInputs()
            .getByName(inputName)
            .orElseThrow(() -> new IllegalStateException("model does not contain an input named " + inputName));
   }

   @Override
   protected void doValidateStep(IValidationContext<IScenarioStep> context) {
      requireOnlyOneParameter(context,
                              "The 'receiveRequest' verb requires exactly one parameter which is an input field!");
      ReceiveStepHandler.requireParameterReferenceAnInputField(context, 0);
      requireNoOtherReceiveRequestStepsInScenario(context);
      requireNoWhenStepsWithVerbInScenario(
            context,
            CorrelateStepHandler.PRESENT,
            "Scenarios that receive requests cannot correlate multiple inputs!");
      requireNoWhenStepsWithVerbInScenario(
            context,
            ReceiveStepHandler.PRESENT,
            "Scenarios that receive requests cannot asynchronously receive multiple inputs!");
      requireWillRespondStep(context);
   }

   @Override
   protected Set<String> doGetSuggestedParameterCompletions(String partialParameter, int parameterIndex,
                                                            IScenarioStep step, ScenarioStepVerb verb) {
      if (step.getParameters().size() > 1) {
         return Collections.emptySet();
      }
      Set<String> suggestions = new TreeSet<>();
      for (IDataReferenceField field : step.getParent().getParent().getInputs()) {
         suggestions.add(field.getName());
      }
      return suggestions;
   }

   private static void requireNoOtherReceiveRequestStepsInScenario(IValidationContext<IScenarioStep> context) {
      // Only do this check if the step being validated is the first receive step in the scenario.  This avoids
      // creating multiple errors for the user.
      IScenarioStep step = context.getObject();
      IScenario scenario = step.getParent();

      List<IScenarioStep> receiveRequestSteps = scenario.getWhens()
            .stream()
            .filter(s -> s.getKeyword().equals(PRESENT.getVerb()))
            .collect(Collectors.toList());

      if (receiveRequestSteps.size() > 1 && receiveRequestSteps.get(0).equals(step)) {
         String errMsg = "Scenarios that receive requests cannot receive multiple inputs!";
         context.declare(Severity.ERROR, errMsg, receiveRequestSteps.get(1)).getKeyword();
      }
   }

   private static void requireWillRespondStep(IValidationContext<IScenarioStep> context) {
      IScenarioStep step = context.getObject();
      IScenario scenario = step.getParent();

      boolean hasWillRespondStep = scenario.getThens()
            .stream()
            .anyMatch(s -> RespondStepHandler.FUTURE.getVerb().equals(s.getKeyword()));
      if (!hasWillRespondStep) {
         String errMsg = "Scenarios that receive requests must respond using the 'willRespond' verb!";
         context.declare(Severity.ERROR, errMsg, scenario).getThens();
      }
   }

}
