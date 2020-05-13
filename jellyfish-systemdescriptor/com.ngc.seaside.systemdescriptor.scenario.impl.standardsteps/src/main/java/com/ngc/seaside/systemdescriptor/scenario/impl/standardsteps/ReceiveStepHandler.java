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
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.AbstractStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * Implements the "receive" step verb.  This verb is used to indicate some input is asynchronously received using some
 * pub/sub protocol.  It's only argument is the input field of the model that contains the scenario that is published.
 */
public class ReceiveStepHandler extends AbstractStepHandler {

   public static final ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("haveReceived");
   public static final ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("receiving");
   public static final ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willReceive");

   public ReceiveStepHandler() {
      register(PAST, PRESENT, FUTURE);
   }

   /**
    * Gets the {@code IDataReferenceField} of the input of the model the scenario that is associated with that this
    * step. This can be used to determine which input field is received.
    * <p/>
    * Only invoke this method with validated scenario steps.
    *
    * @param step the step that contains a receive verb
    * @return the input field of the model that is received
    */
   public IDataReferenceField getInputs(IScenarioStep step) {
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
      requireOnlyOneParameter(context, "The 'receive' verb requires exactly one parameter which is an input field!");
      requireParameterReferenceAnInputField(context, 0);
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

   static void requireParameterReferenceAnInputField(IValidationContext<IScenarioStep> context,
                                                     int parameterIndex) {
      IScenarioStep step = context.getObject();
      String
            fieldName =
            step.getParameters().size() <= parameterIndex ? null : step.getParameters().get(parameterIndex);
      IModel model = step.getParent().getParent();
      if (fieldName != null && !model.getInputs().getByName(fieldName).isPresent()) {
         String errMsg = String.format("The model %s contains no input field named '%s'!",
                                       model.getFullyQualifiedName(),
                                       fieldName);
         context.declare(Severity.ERROR, errMsg, step).getParameters();
      }
   }
}
