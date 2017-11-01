package com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.SystemDescriptors;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.AbstractStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Implements the "correlate" verb which is used to indicate multiple pieces of data must be correlated together. It
 * contains a number of arguments and its form is:
 *
 * <pre>
 *    {@code
 *     (correlating|willCorrelate) <inputField|outputField>.<dataField> to <inputField|outputField>.<dataField>
 *    }
 * </pre>
 *
 * NOTE: The "PAST" tense is not currently supported.
 */
public class CorrelateStepHandler extends AbstractStepHandler {

   public final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("correlating");
   public final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willCorrelate");

   // Regular expression representing <inputField|outputField>.<dataField>
//   private final Pattern PATTERN = Pattern.compile("((?:[a-z][a-z0-9_]*))(\\.)((?:[a-z][a-z0-9_]*))",
//      Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

   private final Pattern PATTERN = Pattern.compile("(?:[a-z][a-z0-9_]*)(\\.(?:[a-z][a-z0-9_]*))+",
                                                   Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

   /**
    * Default constructor
    */
   public CorrelateStepHandler() {
      register(PRESENT, FUTURE);
   }

   /**
    * Get the the data field referenced on the left side of the "to".
    *
    * @param step the scenario step
    * @return the left data field
    */
   public IDataField getLeftData(IScenarioStep step) {
      requireStepUsesHandlerVerb(step);
      List<String> parameters = step.getParameters();
      Preconditions.checkArgument(parameters.size() == 3,
                                  "invalid step!");
      String leftData = getCorrelationArg(null, step, 0);
      return evaluateDataField(null, step, leftData).getDataField();
   }

   /**
    * Get the the data field referenced on the right side of the "to".
    *
    * @param step the scenario step
    * @return the right data field
    */
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
         context.declare(
               Severity.ERROR,
               "Expected parameters of the form: <inputField|outputField>(.<dataField>)+ to <inputField|outputField>(.<dataField>)+",
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

   /**
    * This method should be used to declare errors (if processing can continue) or throw and exception if an illegal
    * argument is used.
    *
    * @param context    the context
    * @param step       the current step
    * @param errMessage the error message to display
    */
   private static void declareOrThrowError(IValidationContext<IScenarioStep> context,
                                           IScenarioStep step,
                                           String errMessage) {
      if (context != null) {
         context.declare(Severity.ERROR, errMessage, step).getKeyword();
      } else {
         throw new IllegalArgumentException(errMessage);
      }
   }

   /**
    * This method retrieves the data field from the model using the provided step arguments. The {@link IDataField}
    * object is wrapped by an {@link InputOutputDataField} which contains more information about the data field
    * pertaining to how it was used in the context of the step.
    *
    * @param context         the context
    * @param step            the current step
    * @param dataFieldString the argument in the step in format {@code <inputField|outputField>(.<dataField>)+ to
    *                        <inputField|outputField>(.<dataField>)+}
    * @return the evaluated data field
    */
   private InputOutputDataField evaluateDataField(IValidationContext<IScenarioStep> context, IScenarioStep step,
                                                  String dataFieldString) {
      Preconditions.checkNotNull(step, "step may not be null!");

      IDataField dataField;
      InputOutputDataField inOutDataField = null;
      String[] splitInOutFieldDataField = dataFieldString.split("\\.");

      String keyword = step.getKeyword();
      IModel model = step.getParent().getParent();
      String errorMessage = "";

      // Data field can only be input in present tense
      if (keyword.equals(PRESENT.getVerb())) {
         dataField = resolve(model.getInputs(), splitInOutFieldDataField);
         if (dataField != null) {
            inOutDataField = new InputOutputDataField(dataField, InputOutputEnum.INPUT, dataFieldString);
         } else {
            errorMessage = String.format("Unable to resolve '%s'; is '%s' an input?",
                                         dataFieldString,
                                         splitInOutFieldDataField[0]);
         }
      } else {
         // Data field can be input or output in future tense
         // Try inputs.
         dataField = resolve(model.getInputs(), splitInOutFieldDataField);
         if (dataField != null) {
            inOutDataField = new InputOutputDataField(dataField, InputOutputEnum.INPUT, dataFieldString);
         } else {
            // Try outputs.
            dataField = resolve(model.getOutputs(), splitInOutFieldDataField);
            if (dataField != null) {
               inOutDataField = new InputOutputDataField(dataField, InputOutputEnum.OUTPUT, dataFieldString);
            } else {
               // Give up.
               errorMessage = String.format("Unable to resolve '%s'; is '%s' an input or output?",
                                            dataFieldString,
                                            splitInOutFieldDataField[0]);
            }
         }

//         if (model.getInputs().getByName(inOutFieldStr).isPresent()) {
//            dataRefField = model.getInputs().getByName(inOutFieldStr).get();
//            dataField = searchModelForDataField(dataRefField, dataFieldStr);
//            if (dataField != null) {
//               inOutDataField = new InputOutputDataField(dataField, InputOutputEnum.INPUT, dataFieldString);
//            } else {
//               errorMessage = "The " + dataFieldStr
//                              + " can't be found in the model inputs for the future tense correlation.";
//            }
//
//         } else if (model.getOutputs().getByName(inOutFieldStr).isPresent()) {
//            dataRefField = model.getOutputs().getByName(inOutFieldStr).get();
//            dataField = searchModelForDataField(dataRefField, dataFieldStr);
//            if (dataField != null) {
//               inOutDataField = new InputOutputDataField(dataField, InputOutputEnum.OUTPUT, dataFieldString);
//            } else {
//               errorMessage = "The " + dataFieldStr
//                              + " can't be found in the model outputs for the future tense correlation.";
//            }
//
//         } else {
//            errorMessage = dataFieldString + " isn't an input or output field.";
//         }
      }
      if (inOutDataField == null) {
         declareOrThrowError(context,
                             step,
                             errorMessage);
      }
      return inOutDataField;
   }

   private static IDataField resolve(INamedChildCollection<?, IDataReferenceField> dataRefFields, String[] path) {
      IDataField field = null;
      IDataReferenceField ref = dataRefFields.getByName(path[0]).orElse(null);
      if (ref != null) {
         field = resolve(ref.getType(), Arrays.copyOfRange(path, 1, path.length));
      }
      return field;
   }

   private static IDataField resolve(IData dataType, String[] path) {
      Deque<IDataField> fields = new ArrayDeque<>(path.length);
      IData currentDataType = dataType;

      for (String fieldName : path) {
         if (currentDataType != null) {
            IDataField field = getDataField(currentDataType, fieldName);
            if (field == null) {
               currentDataType = null;
            } else {
               fields.push(field);
               currentDataType = SystemDescriptors.isPrimitiveDataFieldDeclaration(field)
                                 ? null
                                 : field.getReferencedDataType();
            }
         }
      }

      return fields.size() == path.length ? fields.pop() : null;
   }

   /**
    * Gets the correlation string argument provided in the step based on position
    *
    * @param context     the context
    * @param step        the current step
    * @param argPosition the position of the argument
    * @return the correlation string argument
    */
   private String getCorrelationArg(IValidationContext<IScenarioStep> context, IScenarioStep step, int argPosition) {
      Preconditions.checkNotNull(step, "step may not be null!");
      String argument = step.getParameters().get(argPosition).trim();
      if (!PATTERN.matcher(argument).matches()) {
         declareOrThrowError(context, step, argument + " isn't of format <inputField|outputField>(.<dataField>)+");
      }
      return argument;
   }

   /**
    * Verifies that the step uses the correct verb
    *
    * @param step the current step
    */
   private static void requireStepUsesHandlerVerb(IScenarioStep step) {
      Preconditions.checkNotNull(step, "step may not be null!");
      String keyword = step.getKeyword();
      Preconditions.checkArgument(keyword.equals(PRESENT.getVerb())
                                  || keyword.equals(FUTURE.getVerb()),
                                  "the step cannot be processed by this handler!");
   }

   /**
    * This method searches the model for the referenced data field. If the data field does not match the initial data
    * type provided in the input, the super data types are searched to support inheritance. If the data field is never
    * found, null is returned.
    *
    * @param dataType     the data type to search
    * @param dataFieldStr the data field key to search for
    * @return the data field
    */
   private static IDataField getDataField(IData dataType, String dataFieldStr) {
      IDataField dataField = null;

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

   /**
    * This method ensures that the types of two data fields are the same otherwise correlation cannot occur.
    *
    * @param context   the context
    * @param step      the current step
    * @param leftData  the datafield from the left of the "to"
    * @param rightData the datafield from the right of the "to"
    */
   private void validateFieldType(IValidationContext<IScenarioStep> context, IScenarioStep step,
                                  InputOutputDataField leftData, InputOutputDataField rightData) {
      if (leftData.getDataField().getType() != rightData.getDataField().getType()) {
         declareOrThrowError(context,
                             step,
                             "Argument types don't match. Left argument is of type: "
                             + leftData.getDataField().getType() + ". Right data is of type: " + rightData
                                   .getDataField().getType());
      }
   }

   /**
    * Verify that the <inputField|outputField> arguments on each side of the "to" are not the same.
    *
    * @param context   the context
    * @param step      the current step
    * @param leftData  the datafield from the left of the "to"
    * @param rightData the datafield from the right of the "to"
    */
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

   /**
    * This method verifies that the correct combination of input and output fields are used depending on the verb
    * tense.
    *
    * If the verb is PRESENT, only model inputs may be used. If the verb is FUTURE, exactly one model input and one
    * model output must be referenced.
    *
    * @param context   the context
    * @param step      the current step
    * @param leftData  the datafield from the left of the "to"
    * @param rightData the datafield from the right of the "to"
    */
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
             || (leftData.getInputOutputLocation() == InputOutputEnum.OUTPUT
                 && rightData.getInputOutputLocation() == InputOutputEnum.OUTPUT)) {
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

   /**
    * Validates that the second argument is "to"
    *
    * @param context     the context
    * @param step        the current step
    * @param argPosition the position of the argument
    */
   private void validateToArgument(IValidationContext<IScenarioStep> context, IScenarioStep step, int argPosition) {
      Preconditions.checkNotNull(step, "step may not be null!");
      if (!"to".equals(step.getParameters().get(argPosition))) {
         declareOrThrowError(context,
                             step,
                             "Expected parameter to be 'to'");
      }
   }

   /**
    * This method ensures that correlation cannot occur between the same fields.
    *
    * @param context         the context
    * @param step            the current step
    * @param leftDataString  the data string from the left of the "to"
    * @param rightDataString the data string from the right of the "to"
    */
   private void verifyDataStringsDontMatch(IValidationContext<IScenarioStep> context, IScenarioStep step,
                                           String leftDataString, String rightDataString) {
      if (leftDataString.equals(rightDataString)) {
         declareOrThrowError(context,
                             step,
                             "Can't correlate a data field to itself");
      }
   }

   /**
    * Enum to designate what is an input or output
    */
   private enum InputOutputEnum {
      INPUT, OUTPUT
   }

   /**
    * This is a helper class to store an IDataField, whether it was input or output, and the original argument supplied
    * to retrieve the IDataField.
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
