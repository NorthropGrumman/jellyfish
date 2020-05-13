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
 * Implements the "respond" step verb.  This verb is used to indicate a component is responding to a request.  It's only
 * argument is the output field that represents the response.  Only one response step can appear in a scenario.
 */
public class RespondStepHandler extends AbstractStepHandler {

   public static final ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("haveResponded");
   public static final ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("responding");
   public static final ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willRespond");

   public RespondStepHandler() {
      register(PAST, PRESENT, FUTURE);
   }

   /**
    * Gets the {@code IDataReferenceField} of the output of the model the scenario is associated with that this step.
    * This can be used to determine which output field is used for the response.
    * <p/>
    * Only invoke this method with validated scenario steps.
    *
    * @param step the step that contains a publish verb
    * @return the output field of the model that is used for the response
    */
   public IDataReferenceField getResponse(IScenarioStep step) {
      Preconditions.checkNotNull(step, "step may not be null!");
      String keyword = step.getKeyword();
      Preconditions.checkArgument(
            keyword.equals(PAST.getVerb())
                  || keyword.equals(PRESENT.getVerb())
                  || keyword.equals(FUTURE.getVerb()),
            "the step cannot be processed by this handler!");
      Preconditions.checkArgument(step.getParameters().size() > 1,
                                  "invalid number of parameters in step, requires at least 2!");

      IModel model = step.getParent().getParent();
      String outputName = step.getParameters().get(1);
      return model.getOutputs()
            .getByName(outputName)
            .orElseThrow(() -> new IllegalStateException("model does not contain an output named " + outputName));
   }

   @Override
   protected void doValidateStep(IValidationContext<IScenarioStep> context) {
      requireExactlyNParameters(context,
                                2,
                                "The 'respond' verb requires parameters of the form: with <inputField>");
      requireWithParameter(context);
      PublishStepHandler.requireParameterReferenceAnOutputField(context, 1);
      requireNoOtherRespondStepsInScenario(context);
   }

   @Override
   protected Set<String> doGetSuggestedParameterCompletions(String partialParameter, int parameterIndex,
                                                            IScenarioStep step, ScenarioStepVerb verb) {
      if (step.getParameters().size() > 2) {
         return Collections.emptySet();
      }
      Set<String> suggestions = new TreeSet<>();
      switch (parameterIndex) {
         case 0:
            suggestions.add("with");
            break;
         case 1:
            for (IDataReferenceField field : step.getParent().getParent().getOutputs()) {
               suggestions.add(field.getName());
            }
            break;
         default:
            //ignore
            break;
      }
      return suggestions;
   }

   private static void requireWithParameter(IValidationContext<IScenarioStep> context) {
      IScenarioStep step = context.getObject();
      if (step.getParameters().size() == 2 && !"with".equals(step.getParameters().get(0))) {
         context.declare(Severity.ERROR, "The 'respond' verb requires the first parameter to be 'with'!", step)
               .getParameters();
      }
   }

   private static void requireNoOtherRespondStepsInScenario(IValidationContext<IScenarioStep> context) {
      // Only do this check if the step being validated is the first receive step in the scenario.  This avoids
      // creating multiple errors for the user.
      IScenarioStep step = context.getObject();
      IScenario scenario = step.getParent();

      List<IScenarioStep> respondSteps = scenario.getThens()
            .stream()
            .filter(s -> s.getKeyword().equals(FUTURE.getVerb()))
            .collect(Collectors.toList());

      if (respondSteps.size() > 1 && respondSteps.get(0).equals(step)) {
         String errMsg = "A scenario can only respond once!";
         context.declare(Severity.ERROR, errMsg, respondSteps.get(1)).getKeyword();
      }
   }
}
