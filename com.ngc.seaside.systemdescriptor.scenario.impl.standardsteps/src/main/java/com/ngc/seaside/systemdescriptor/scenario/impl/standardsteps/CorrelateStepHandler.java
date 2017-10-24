package com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.AbstractStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import java.util.List;

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

   private String leftData;
   private String rightData;

   public CorrelateStepHandler() {
      register(PRESENT, FUTURE);
   }

   public String getLeftData() {
      return leftData;
   }

   public String getRightData() {
      return rightData;
   }

   @Override
   protected void doValidateStep(IValidationContext<IScenarioStep> context) {

      requireStepParameters(context, "The 'correlate' verb requires parameters!");

      IScenarioStep step = context.getObject();
      String keyword = step.getKeyword();

      List<String> parameters = step.getParameters();
      if (parameters.size() != 3) {
         context.declare(Severity.ERROR,
            "Expected parameters of the form: within <number/double> <time unit>",
            step)
                .getKeyword();
      } else {

         leftData = getCorrelationArg(step, 0);
         validateToArgument(context, step, 1);
         rightData = getCorrelationArg(step, 2);

         // TODO Ensure the data is in of format <inputField|outputField>.<dataField>
         // validateLeftDataFormat(context, step, leftData);
         // validateRightDataFormat(context, step, rightData);

         // TODO Validate that vboth field types are the same
         // validateFieldType(context, step, leftData, rightData);

         if (keyword.equals(PRESENT.getVerb())) {
            // TODO With PRESENT verb, data is required to be an input
            // verifyDataIsOnlyInput(context, step, leftData);
            // verifyDataIsOnlyInput(context, step, rightData);
         } else if (keyword.equals(FUTURE.getVerb())) {
            // TODO With FUTURE verb, the data has to reference exactly one input field and one output field
            // The order does not matter.
            // verifyDataIsExclusive(context, step, leftData, rightData);
         } else {
            declareOrThrowError(context, step, "PAST verb hasn't been implemented yet.");
         }
      }
   }
   
//   public IDataField getCorrelationDataField(IScenarioStep step, int argPosition) {
//      Preconditions.checkNotNull(step, "step may not be null!");
//      String keyword = step.getKeyword();
//      Preconditions.checkArgument(
//         keyword.equals(PRESENT.getVerb())
//            || keyword.equals(FUTURE.getVerb()),
//         "the step cannot be processed by this handler!");
//
//      
//      
//      
//   }

   private String getCorrelationArg(IScenarioStep step, int argPosition) {
      Preconditions.checkNotNull(step, "step may not be null!");
      String keyword = step.getKeyword();
      Preconditions.checkArgument(
         keyword.equals(PRESENT.getVerb())
            || keyword.equals(FUTURE.getVerb()),
         "the step cannot be processed by this handler!");
      
      return step.getParameters().get(argPosition);
   }

   private void validateToArgument(IValidationContext<IScenarioStep> context, IScenarioStep step, int argPosition) {
      Preconditions.checkNotNull(step, "step may not be null!");
      if (step.getParameters().get(argPosition) != "to") {
         declareOrThrowError(context,
            step,
            String.format("Expected parameter to be 'to'"));
      }

   }

   public INamedChildCollection<IModel, IDataReferenceField> getInputs(IScenarioStep step) {
      Preconditions.checkNotNull(step, "step may not be null!");
      String keyword = step.getKeyword();
      Preconditions.checkArgument(
         keyword.equals(PRESENT.getVerb())
            || keyword.equals(FUTURE.getVerb()),
         "the step cannot be processed by this handler!");

      IModel model = step.getParent().getParent();
      return model.getInputs();
   }

   public INamedChildCollection<IModel, IDataReferenceField> getOutputs(IScenarioStep step) {
      Preconditions.checkNotNull(step, "step may not be null!");
      String keyword = step.getKeyword();
      Preconditions.checkArgument(
         keyword.equals(PRESENT.getVerb())
            || keyword.equals(FUTURE.getVerb()),
         "the step cannot be processed by this handler!");

      IModel model = step.getParent().getParent();
      return model.getOutputs();
   }

   private static void declareOrThrowError(IValidationContext<IScenarioStep> context,
            IScenarioStep step,
            String errMessage) {
      if (context != null) {
         context.declare(Severity.ERROR, errMessage, step).getKeyword();
      } else {
         throw new IllegalArgumentException(errMessage);
      }
   }
}
