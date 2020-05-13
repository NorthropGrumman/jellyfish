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
 * Implements the "publish" step verb.  This verb is used to indicate some output is asynchronously published using some
 * pub/sub protocol.  It's only argument is the output field of the model that contains the scenario that is published.
 */
public class PublishStepHandler extends AbstractStepHandler {

   public static final ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("havePublished");
   public static final ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("publishing");
   public static final ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willPublish");

   public PublishStepHandler() {
      register(PAST, PRESENT, FUTURE);
   }

   /**
    * Gets the {@code IDataReferenceField} of the output of the model the scenario is associated with that this step.
    * This can be used to determine which output field is published.
    * <p/>
    * Only invoke this method with validated scenario steps.
    *
    * @param step the step that contains a publish verb
    * @return the output field of the model that is published
    */
   public IDataReferenceField getOutputs(IScenarioStep step) {
      Preconditions.checkNotNull(step, "step may not be null!");
      String keyword = step.getKeyword();
      Preconditions.checkArgument(
            keyword.equals(PAST.getVerb())
                  || keyword.equals(PRESENT.getVerb())
                  || keyword.equals(FUTURE.getVerb()),
            "the step cannot be processed by this handler!");

      IModel model = step.getParent().getParent();
      String outputName = step.getParameters().get(0);
      return model.getOutputs()
            .getByName(outputName)
            .orElseThrow(() -> new IllegalStateException("model does not contain an output named " + outputName));
   }

   @Override
   protected void doValidateStep(IValidationContext<IScenarioStep> context) {
      requireOnlyOneParameter(context, "The 'publish' verb requires exactly one parameter which is an output field!");
      requireParameterReferenceAnOutputField(context, 0);
   }

   @Override
   protected Set<String> doGetSuggestedParameterCompletions(String partialParameter, int parameterIndex,
                                                            IScenarioStep step, ScenarioStepVerb verb) {
      if (step.getParameters().size() > 1) {
         return Collections.emptySet();
      }
      Set<String> suggestions = new TreeSet<>();
      for (IDataReferenceField field : step.getParent().getParent().getOutputs()) {
         suggestions.add(field.getName());
      }
      return suggestions;
   }

   static void requireParameterReferenceAnOutputField(IValidationContext<IScenarioStep> context,
                                                      int parameterIndex) {
      IScenarioStep step = context.getObject();
      String
            fieldName =
            step.getParameters().size() <= parameterIndex ? null : step.getParameters().get(parameterIndex);
      IModel model = step.getParent().getParent();
      if (fieldName != null && !model.getOutputs().getByName(fieldName).isPresent()) {
         String errMsg = String.format("The model %s contains no output field named '%s'!",
                                       model.getFullyQualifiedName(),
                                       fieldName);
         context.declare(Severity.ERROR, errMsg, step).getParameters();
      }
   }
}
