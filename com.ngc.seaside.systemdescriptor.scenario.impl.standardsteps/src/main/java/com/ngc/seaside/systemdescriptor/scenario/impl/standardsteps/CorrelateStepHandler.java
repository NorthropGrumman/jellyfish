package com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.AbstractStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

/**
 * Implements the "correlate" verb which is used to indicate multiple pieces of data must be correlated together.
 * It contains a number of arguments and its form is:
 *
 * <pre>
 *    {@code
 *     (correlating|willCorrelate) <inputField|outputField>.<dataField> to <inputField|outputField>.<dataField>
 *    }
 * </pre>
 */
public class CorrelateStepHandler extends AbstractStepHandler {
   public final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("correlating");
   public final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willCorrelate");

   public CorrelateStepHandler() {
      register(PRESENT, FUTURE);
   }

   @Override
   protected void doValidateStep(IValidationContext<IScenarioStep> context) {

      requireStepParameters(context, "The 'correlate' verb requires parameters!");

      IScenarioStep step = context.getObject();
      List<String> parameters = step.getParameters();
      if (parameters.size() != 3) {
         context.declare(Severity.ERROR,
            "Expected parameters of the form: within <number/double> <time unit>",
            step)
                .getKeyword();
      } else {
         
         IDataReferenceField inputs = getInputs(step);
         IDataReferenceField outputs = getOutputs(step);
         // The first parameter should be an operand.
       //  validateOperand(context, step, parameters.get(0));
         // The second parameter should be a number (double).
     //    validateDuration(context, step, parameters.get(1));
         // The third parameter should be a time unit.
       //  validateTimeUnit(context, step, parameters.get(2));
      }

   }

   /**
    * Gets the {@code IDataReferenceField} of the input of the model the scenario is associated with that this step.
    * This can be used to determine which input field is published.
    *
    * <p/>
    *
    * Only invoke this method with validated scenario steps.
    *
    * @param step the step that contains a publish verb.
    * @return the input field of the model that is published
    */
   public IDataReferenceField getInputs(IScenarioStep step) {
      Preconditions.checkNotNull(step, "step may not be null!");
      String keyword = step.getKeyword();
      Preconditions.checkArgument(
         keyword.equals(PRESENT.getVerb())
            || keyword.equals(FUTURE.getVerb()),
         "the step cannot be processed by this handler!");

      IModel model = step.getParent().getParent();
      String inputName = step.getParameters().get(1);
      return model.getInputs()
                  .getByName(inputName)
                  .orElseThrow(() -> new IllegalStateException("model does not contain an input named " + inputName));
   }

   /**
    * Gets the {@code IDataReferenceField} of the output of the model the scenario is associated with that this step.
    * This can be used to determine which output field is published.
    *
    * <p/>
    *
    * Only invoke this method with validated scenario steps.
    *
    * @param step the step that contains a publish verb
    * @return the output field of the model that is published
    */
   public IDataReferenceField getOutputs(IScenarioStep step) {
      Preconditions.checkNotNull(step, "step may not be null!");
      String keyword = step.getKeyword();
      Preconditions.checkArgument(
         keyword.equals(PRESENT.getVerb())
            || keyword.equals(FUTURE.getVerb()),
         "the step cannot be processed by this handler!");

      IModel model = step.getParent().getParent();
      String outputName = step.getParameters().get(0);
      return model.getOutputs()
                  .getByName(outputName)
                  .orElseThrow(() -> new IllegalStateException("model does not contain an output named " + outputName));
   }

}
