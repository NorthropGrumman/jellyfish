package com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
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
   public static enum InputOutputEnum {
      INPUT, OUTPUT;
   }
   
   public final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("correlating");
   public final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willCorrelate");
   final Pattern PATTERN = Pattern.compile("((?:[a-z][a-z0-9_]*))(\\.)((?:[a-z][a-z0-9_]*))",
      Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

   private InputOutputDataField leftData;
   private InputOutputDataField rightData;
   
   public CorrelateStepHandler() {
      register(PRESENT, FUTURE);
   }

   public IDataField getLeftData() {
      return leftData.getDataField();
   }

   public IDataField getRightData() {
      return rightData.getDataField();
   }

   @Override
   protected void doValidateStep(IValidationContext<IScenarioStep> context) {
      String leftDataString;
      String rightDataString;

      InputOutputDataField inOutDataType;
      requireStepParameters(context, "The 'correlate' verb requires parameters!");

      IScenarioStep step = context.getObject();
      String keyword = step.getKeyword();

      Preconditions.checkArgument(
         keyword.equals(PRESENT.getVerb())
            || keyword.equals(FUTURE.getVerb()),
         "the step cannot be processed by this handler!");

      List<String> parameters = step.getParameters();
      if (parameters.size() != 3) {
         context.declare(Severity.ERROR,
            "Expected parameters of the form: <inputField|outputField>.<dataField> to <inputField|outputField>.<dataField>",
            step).getKeyword();
      } else {

         leftDataString = getCorrelationArg(context, step, 0);
         validateToArgument(context, step, 1);
         rightDataString = getCorrelationArg(context, step, 2);

         leftData = evaluateDataField(context, step, leftDataString);
         rightData = evaluateDataField(context, step, rightDataString);
         System.out.println(leftData.getDataField().getName());
         System.out.println(leftData.getDataField().getType().name());
         System.out.println();
         
         System.out.println(rightData.getDataField().getName());
         System.out.println(rightData.getDataField().getType().name());
         System.out.println(rightData.getInputOutputLocation());

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

   private InputOutputDataField evaluateDataField(IValidationContext<IScenarioStep> context, IScenarioStep step,
            String leftDataString) {
      Preconditions.checkNotNull(context, "context may not be null!");
      Preconditions.checkNotNull(step, "step may not be null!");
      
      String keyword = step.getKeyword();
      Preconditions.checkArgument(
         keyword.equals(PRESENT.getVerb())
            || keyword.equals(FUTURE.getVerb()),
         "the step cannot be processed by this handler!");

      InputOutputDataField inOutDataField = null;
      IDataField dataField = null;
      String[] splitInOutFieldDataField = leftDataString.split("\\.");
      String inOutFieldStr = splitInOutFieldDataField[0];
      String dataFieldStr = splitInOutFieldDataField[1];

      IModel model = step.getParent().getParent();

      IDataReferenceField dataRefField;

      // Left data field can only be input in present tense
      if (keyword.equals(PRESENT.getVerb())) {

         if (model.getInputs().getByName(inOutFieldStr).isPresent()) {
            dataRefField = model.getInputs().getByName(inOutFieldStr).get();
            dataField = searchModelForDataField(dataRefField, dataFieldStr);
            if (dataField != null) {
               inOutDataField = new InputOutputDataField(dataField, InputOutputEnum.INPUT);    
            }
            
         } else {
            declareOrThrowError(context, step, leftDataString + "isn't an input field");
         }
         
         // Left data field can be input or output in future tense
      } else {

         if (model.getInputs().getByName(inOutFieldStr).isPresent()) {
            dataRefField = model.getInputs().getByName(inOutFieldStr).get();
            dataField = searchModelForDataField(dataRefField, dataFieldStr);
            if (dataField != null) {
               inOutDataField = new InputOutputDataField(dataField, InputOutputEnum.INPUT);   
            }
            
         } else if (model.getOutputs().getByName(inOutFieldStr).isPresent()) {
            dataRefField = model.getOutputs().getByName(inOutFieldStr).get();
            dataField = searchModelForDataField(dataRefField, dataFieldStr);     
            if (dataField != null) {
               inOutDataField = new InputOutputDataField(dataField, InputOutputEnum.OUTPUT);   
            }
            
         } else {
            declareOrThrowError(context, step, leftDataString + "isn't an input or output field");
         } 
      }
      if (inOutDataField == null) {
         declareOrThrowError(context,
            step,
            "First parameter doesn't correspond with a valid data field.");
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
         declareOrThrowError(context, step, argument + "isn't of format <inputField|outputField>.<dataField>");
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
   protected class InputOutputDataField {
      private IDataField dataField;
      private CorrelateStepHandler.InputOutputEnum inputOutputLocation;
      
      public InputOutputDataField(IDataField dataField, InputOutputEnum inputOutputLocation) {
         this.dataField = dataField;
         this.inputOutputLocation = inputOutputLocation;
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
      
   }
}
