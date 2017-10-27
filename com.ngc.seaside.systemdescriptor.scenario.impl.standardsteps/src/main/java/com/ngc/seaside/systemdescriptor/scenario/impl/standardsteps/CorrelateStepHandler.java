package com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.AbstractStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import java.util.List;
import java.util.regex.Pattern;

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
   final Pattern PATTERN = Pattern.compile("((?:[a-z][a-z0-9_]*))(\\.)((?:[a-z][a-z0-9_]*))",
      Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

   public CorrelateStepHandler() {
      register(PRESENT, FUTURE);
   }

   public IDataField getLeftData(IScenarioStep step) {
      requireStepUsesHandlerVerb(step);
      List<String> parameters = step.getParameters();
      Preconditions.checkArgument(parameters.size() == 3,
         "invalid step!");
      String leftData = getCorrelationArg(null, step, 0);
      return evaluateDataField(null, step, leftData).getDataField();
   }

   public IDataField getRightData(IScenarioStep step) {
      requireStepUsesHandlerVerb(step);
      List<String> parameters = step.getParameters();
      Preconditions.checkArgument(parameters.size() == 3,
         "invalid step!");
      String leftData = getCorrelationArg(null, step, 2);
      return evaluateDataField(null, step, leftData).getDataField();
   }

   @Override
   protected void doValidateStep(IValidationContext<IScenarioStep> context) {
      String leftDataString;
      String rightDataString;
      InputOutputDataField leftData;
      InputOutputDataField rightData;

      requireStepParameters(context, "The 'correlate' verb requires parameters!");

      IScenarioStep step = context.getObject();
      requireStepUsesHandlerVerb(step);

      List<String> parameters = step.getParameters();
      if (parameters.size() != 3) {
         context.declare(Severity.ERROR,
            "Expected parameters of the form: <inputField|outputField>.<dataField> to <inputField|outputField>.<dataField>",
            step).getKeyword();
      } else {
         leftDataString = getCorrelationArg(null, step, 0);
         validateToArgument(context, step, 1);
         rightDataString = getCorrelationArg(null, step, 2);

         verifyDataStringsDontMatch(context, step, leftDataString, rightDataString);

         // Retrieve data fields and whether they are input or output
         leftData = evaluateDataField(context, step, leftDataString);
         rightData = evaluateDataField(context, step, rightDataString);

         if (leftData != null && rightData != null) {

            // Validate input/output fields are different
            validateInOutFieldDifferent(context, step, leftData, rightData);

            // Validate that both field types are the same
            validateFieldType(context, step, leftData, rightData);

            // Verify the correct combination of input and output based on verb tense
            validateInputOutputTense(context, step, leftData, rightData);
         }
      }
   }

   private void validateInOutFieldDifferent(IValidationContext<IScenarioStep> context, IScenarioStep step,
            InputOutputDataField leftData, InputOutputDataField rightData) {
      if (leftData.getInputOutputLocation() == rightData.getInputOutputLocation()) {
         String inOutFieldLeft = leftData.getDataFieldArg().split("\\.")[0];
         String inOutFieldRight = rightData.getDataFieldArg().split("\\.")[0];

         if (inOutFieldLeft.equals(inOutFieldRight)) {
            declareOrThrowError(context,
               step,
               "Can't reference the same field.");
         }
      }
   }

   private void verifyDataStringsDontMatch(IValidationContext<IScenarioStep> context, IScenarioStep step,
            String leftDataString, String rightDataString) {
      if (leftDataString.equals(rightDataString)) {
         declareOrThrowError(context,
            step,
            "Can't correlate a data field to itself");
      }
   }

   private void validateInputOutputTense(IValidationContext<IScenarioStep> context, IScenarioStep step,
            InputOutputDataField leftData, InputOutputDataField rightData) {
      String keyword = step.getKeyword();

      if (keyword.equals(PRESENT.getVerb())) {
         if (leftData.getInputOutputLocation() != InputOutputEnum.INPUT
            || rightData.getInputOutputLocation() != InputOutputEnum.INPUT) {
            declareOrThrowError(context,
               step,
               "In present tense of correlation verb, both arguments must be input types.");
         }
      } else if (keyword.equals(FUTURE.getVerb())) {
         if ((leftData.getInputOutputLocation() == InputOutputEnum.INPUT
            && rightData.getInputOutputLocation() == InputOutputEnum.INPUT)
            || leftData.getInputOutputLocation() == InputOutputEnum.OUTPUT
               && rightData.getInputOutputLocation() == InputOutputEnum.OUTPUT) {
            declareOrThrowError(context,
               step,
               "In future tense of correlation verb, one argument must be an input type and the other must be of output type.");
         }
      } else {
         declareOrThrowError(context,
            step,
            keyword + " is not a valid verb");
      }
   }

   private void validateFieldType(IValidationContext<IScenarioStep> context, IScenarioStep step,
            InputOutputDataField leftData, InputOutputDataField rightData) {
      if (leftData.getDataField().getType() != rightData.getDataField().getType()) {
         declareOrThrowError(context,
            step,
            "Argument types don't match. Left argument is of type: "
               + leftData.getDataField().getType() + ". Right data is of type: " + rightData.getDataField().getType());
      }
   }

   private InputOutputDataField evaluateDataField(IValidationContext<IScenarioStep> context, IScenarioStep step,
            String dataFieldString) {
      Preconditions.checkNotNull(step, "step may not be null!");

      InputOutputDataField inOutDataField = null;
      IDataField dataField = null;
      String[] splitInOutFieldDataField = dataFieldString.split("\\.");
      String inOutFieldStr = splitInOutFieldDataField[0];
      String dataFieldStr = splitInOutFieldDataField[1];
      String keyword = step.getKeyword();
      IModel model = step.getParent().getParent();
      IDataReferenceField dataRefField;
      String errorMessage = "";

      // Data field can only be input in present tense
      if (keyword.equals(PRESENT.getVerb())) {

         if (model.getInputs().getByName(inOutFieldStr).isPresent()) {
            dataRefField = model.getInputs().getByName(inOutFieldStr).get();
            dataField = searchModelForDataField(dataRefField, dataFieldStr);
            if (dataField != null) {
               inOutDataField = new InputOutputDataField(dataField, InputOutputEnum.INPUT, dataFieldString);
            } else {
               errorMessage = "The " + dataFieldStr
                  + " can't be found in the model inputs for the present tense correlation";
            }

         } else {
            errorMessage = dataFieldString + " isn't a valid input field";
         }

         // Data field can be input or output in future tense
      } else {

         if (model.getInputs().getByName(inOutFieldStr).isPresent()) {
            dataRefField = model.getInputs().getByName(inOutFieldStr).get();
            dataField = searchModelForDataField(dataRefField, dataFieldStr);
            if (dataField != null) {
               inOutDataField = new InputOutputDataField(dataField, InputOutputEnum.INPUT, dataFieldString);
            } else {
               errorMessage = "The " + dataFieldStr
                  + " can't be found in the model inputs for the future tense correlation.";
            }

         } else if (model.getOutputs().getByName(inOutFieldStr).isPresent()) {
            dataRefField = model.getOutputs().getByName(inOutFieldStr).get();
            dataField = searchModelForDataField(dataRefField, dataFieldStr);
            if (dataField != null) {
               inOutDataField = new InputOutputDataField(dataField, InputOutputEnum.OUTPUT, dataFieldString);
            } else {
               errorMessage = "The " + dataFieldStr
                  + " can't be found in the model outputs for the future tense correlation.";
            }

         } else {
            errorMessage = dataFieldString + " isn't an input or output field.";
         }
      }
      if (inOutDataField == null) {
         declareOrThrowError(context,
            step,
            errorMessage);
      }
      return inOutDataField;
   }

   private IDataField searchModelForDataField(IDataReferenceField dataRefField, String dataFieldStr) {
      IDataField dataField = null;
      IData dataType = dataRefField.getType();

      // Check data type for field
      if (dataType.getFields().getByName(dataFieldStr).isPresent()) {
         dataField = dataType.getFields().getByName(dataFieldStr).get();
      } else {

         // Check super data types if original data type didn't contain field
         boolean found = false;
         while (dataType.getSuperDataType().isPresent() && !found) {
            dataType = dataType.getSuperDataType().get();
            if (dataType.getFields().getByName(dataFieldStr).isPresent()) {
               dataField = dataType.getFields().getByName(dataFieldStr).get();
               found = true;
            }
         }
      }
      return dataField;
   }

   private String getCorrelationArg(IValidationContext<IScenarioStep> context, IScenarioStep step, int argPosition) {
      Preconditions.checkNotNull(step, "step may not be null!");
      String argument = step.getParameters().get(argPosition).trim();
      if (!PATTERN.matcher(argument).matches()) {
         declareOrThrowError(context, step, argument + " isn't of format <inputField|outputField>.<dataField>");
      }
      return argument;
   }

   private void validateToArgument(IValidationContext<IScenarioStep> context, IScenarioStep step, int argPosition) {
      Preconditions.checkNotNull(step, "step may not be null!");
      if (step.getParameters().get(argPosition) != "to") {
         declareOrThrowError(context,
            step,
            String.format("Expected parameter to be 'to'"));
      }
   }

   private static void requireStepUsesHandlerVerb(IScenarioStep step) {
      Preconditions.checkNotNull(step, "step may not be null!");
      String keyword = step.getKeyword();
      Preconditions.checkArgument(keyword.equals(PRESENT.getVerb())
         || keyword.equals(FUTURE.getVerb()),
         "the step cannot be processed by this handler!");
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

   /*
    * Enum to designate what is an input or output
    */
   private static enum InputOutputEnum {
      INPUT, OUTPUT;
   }

   /*
    * This is a helper class to store an IDataField, whether it was input or output, and the original argument
    * supplied to retrieve the IDataField.
    */
   protected class InputOutputDataField {
      private IDataField dataField;
      private CorrelateStepHandler.InputOutputEnum inputOutputLocation;
      private String dataFieldArg;

      public InputOutputDataField(IDataField dataField, InputOutputEnum inputOutputLocation, String dataFieldArg) {
         this.dataField = dataField;
         this.inputOutputLocation = inputOutputLocation;
         this.dataFieldArg = dataFieldArg;
      }

      public IDataField getDataField() {
         return dataField;
      }

      public void setDataField(IDataField dataField) {
         this.dataField = dataField;
      }

      public CorrelateStepHandler.InputOutputEnum getInputOutputLocation() {
         return inputOutputLocation;
      }

      public void setInputOutputLocation(CorrelateStepHandler.InputOutputEnum inputOutputLocation) {
         this.inputOutputLocation = inputOutputLocation;
      }

      public String getDataFieldArg() {
         return dataFieldArg;
      }

      public void setDataFieldArg(String dataFieldArg) {
         this.dataFieldArg = dataFieldArg;
      }
   }
}
