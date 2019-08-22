/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataPath;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.AbstractStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.scenario.api.VerbTense;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * Implements the "correlate" verb which is used to indicate multiple pieces of data must be correlated together. It
 * contains a number of arguments and its form is:
 * <pre>
 *    {@code
 *     (correlating|willCorrelate) <inputField|outputField>.<dataField> to <inputField|outputField>.<dataField>
 *    }
 * </pre>
 * NOTE: The "PAST" tense is not currently supported.
 */
public class CorrelateStepHandler extends AbstractStepHandler {

   public static final ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("hasCorrelated");
   public static final ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("correlating");
   public static final ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willCorrelate");

   private static final Pattern PATTERN = Pattern.compile("(?:[a-z][a-z0-9_]*)(\\.(?:[a-z][a-z0-9_]*))+",
                                                          Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

   /**
    * Default constructor
    */
   public CorrelateStepHandler() {
      register(PAST, PRESENT, FUTURE);
   }

   /**
    * Get the data path referenced on the left side of the "to".
    *
    * @param step the scenario step
    * @return the left data path
    */
   public IDataPath getLeftPath(IScenarioStep step) {
      requireStepUsesHandlerVerb(step);
      List<String> parameters = step.getParameters();
      Preconditions.checkArgument(parameters.size() == 3,
                                  "invalid step!");
      String leftData = getCorrelationArg(null, step, 0);
      return evaluatePath(null, step, leftData);
   }

   /**
    * Get the data path referenced on the right side of the "to".
    *
    * @param step the scenario step
    * @return the right data path
    */
   public IDataPath getRightPath(IScenarioStep step) {
      requireStepUsesHandlerVerb(step);
      List<String> parameters = step.getParameters();
      Preconditions.checkArgument(parameters.size() == 3,
                                  "invalid step!");
      String rightData = getCorrelationArg(null, step, 2);
      return evaluatePath(null, step, rightData);
   }

   /**
    * Get the data field referenced on the left side of the "to".
    *
    * @param step the scenario step
    * @return the left data field
    */
   public IDataField getLeftData(IScenarioStep step) {
      return getLeftPath(step).getEnd();
   }

   /**
    * Get the data field referenced on the right side of the "to".
    *
    * @param step the scenario step
    * @return the right data field
    */
   public IDataField getRightData(IScenarioStep step) {
      return getRightPath(step).getEnd();
   }

   @Override
   protected void doValidateStep(IValidationContext<IScenarioStep> context) {
      String leftDataString;
      String rightDataString;
      IDataPath leftData;
      IDataPath rightData;

      requireStepParameters(context, "The 'correlate' verb requires parameters!");

      IScenarioStep step = context.getObject();
      requireStepUsesHandlerVerb(step);

      List<String> parameters = step.getParameters();
      if (parameters.size() != 3) {
         context.declare(
               Severity.ERROR,
               "Expected parameters of the form: <inputField|outputField>(.<dataField>)+ to"
                     + " <inputField|outputField>(.<dataField>)+",
               step).getKeyword();
      } else {
         leftDataString = getCorrelationArg(null, step, 0);
         validateToArgument(context, step, 1);
         rightDataString = getCorrelationArg(null, step, 2);

         verifyDataStringsDontMatch(context, step, leftDataString, rightDataString);

         // Retrieve data fields and whether they are input or output
         leftData = evaluatePath(context, step, leftDataString);
         rightData = evaluatePath(context, step, rightDataString);

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

   @Override
   protected Set<String> doGetSuggestedParameterCompletions(String partialParameter, int parameterIndex,
            IScenarioStep step, ScenarioStepVerb verb) {
      if (step.getParameters().size() > 3 || verb.getTense() == VerbTense.PAST_TENSE) {
         return Collections.emptySet();
      }
      switch (parameterIndex) {
         case 0:
            return getParameterCompletions(partialParameter, step.getParent().getParent(), false);
         case 1:
            return Collections.singleton("to");
         case 2:
            return getParameterCompletions(partialParameter, step.getParent().getParent(),
                     verb.getTense() == VerbTense.FUTURE_TENSE);
         default:
            return Collections.emptySet();
      }
   }

   private Set<String> getParameterCompletions(String partialParameter, IModel model, boolean output) {
      Set<String> suggestions = new TreeSet<>();
      int index = partialParameter.indexOf('.');
      if (index >= 0) {
         IDataReferenceField field;
         if (output) {
            field = model.getOutputs().getByName(partialParameter.substring(0, index)).orElse(null);
         } else {
            field = model.getInputs().getByName(partialParameter.substring(0, index)).orElse(null);
         }
         int startIndex = index + 1;
         IData data = field == null ? null : field.getType();
         while (data != null) {
            index = partialParameter.indexOf('.', startIndex);
            if (index < 0) {
               break;
            } else {
               String part = partialParameter.substring(startIndex, index);
               IDataField dataField = data.getFields().getByName(part).orElse(null);
               data = dataField == null || dataField.getType() != DataTypes.DATA ? null
                        : dataField.getReferencedDataType();
            }
            startIndex = index + 1;
         }

         String prefix = partialParameter.substring(0, partialParameter.lastIndexOf('.') + 1);
         if (data != null) {
            for (IDataField dataField : data.getFields()) {
               suggestions.add(prefix + dataField.getName());
            }
         }
      } else {
         for (IDataReferenceField field : model.getInputs()) {
            suggestions.add(field.getName());
         }
         for (IDataReferenceField field : model.getOutputs()) {
            suggestions.add(field.getName());
         }
      }
      return suggestions;
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
    * This method retrieves the data path from the model using the provided step arguments. The {@link IDataField}
    * object is wrapped by an {@link IDataPath} which contains more information about the data field
    * pertaining to how it was used in the context of the step.
    *
    * @param context         the context
    * @param step            the current step
    * @param dataFieldString the argument in the step in format {@code <inputField|outputField>(.<dataField>)+ to
    *                        <inputField|outputField>(.<dataField>)+}
    * @return the evaluated data path
    */
   private IDataPath evaluatePath(IValidationContext<IScenarioStep> context, IScenarioStep step,
                                  String dataFieldString) {
      Preconditions.checkNotNull(step, "step may not be null!");
      Preconditions.checkNotNull(dataFieldString, "dataFieldString may not be null!");

      IModel model = step.getParent().getParent();

      IDataPath path;
      try {
         path = IDataPath.of(model, dataFieldString);
      } catch (RuntimeException e) {
         declareOrThrowError(context,
                             step,
                             "Invalid field references!  " + e.getMessage());
         return null;
      }

      return path;
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
      Preconditions.checkArgument(keyword.equals(PRESENT.getVerb()) || keyword.equals(FUTURE.getVerb()),
                                  "the step cannot be processed by this handler!");
   }

   /**
    * This method ensures that the types of two data paths are the same otherwise correlation cannot occur.
    *
    * @param context   the context
    * @param step      the current step
    * @param leftPath  the data path from the left of the "to"
    * @param rightPath the data path from the right of the "to"
    */
   private void validateFieldType(IValidationContext<IScenarioStep> context, IScenarioStep step,
                                  IDataPath leftPath, IDataPath rightPath) {
      if (leftPath.getEnd().getType() != rightPath.getEnd().getType()) {
         declareOrThrowError(context,
                             step,
                             "Argument types don't match. Left argument is of type: "
                                   + leftPath.getEnd().getType() + ". Right data is of type: " + rightPath
                                   .getEnd().getType());
      }
   }

   /**
    * Verify that the {@code <inputField|outputField>} arguments on each side of the "to" are not the same.
    *
    * @param context   the context
    * @param step      the current step
    * @param leftPath  the data path from the left of the "to"
    * @param rightPath the data path from the right of the "to"
    */
   private void validateInOutFieldDifferent(IValidationContext<IScenarioStep> context, IScenarioStep step,
                                            IDataPath leftPath, IDataPath rightPath) {
      if (leftPath.isOutput() == rightPath.isOutput()) {
         String inOutFieldLeft = leftPath.getStart().getName();
         String inOutFieldRight = rightPath.getStart().getName();

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
    * If the verb is PRESENT, only model inputs may be used. If the verb is FUTURE, exactly one model input and one
    * model output must be referenced.
    *
    * @param context   the context
    * @param step      the current step
    * @param leftPath  the data path from the left of the "to"
    * @param rightPath the data path from the right of the "to"
    */
   private void validateInputOutputTense(IValidationContext<IScenarioStep> context, IScenarioStep step,
                                         IDataPath leftPath, IDataPath rightPath) {
      String keyword = step.getKeyword();

      if (keyword.equals(PRESENT.getVerb())) {
         if (leftPath.isOutput() || rightPath.isOutput()) {
            declareOrThrowError(context,
                                step,
                                "In present tense of correlation verb, both arguments must be input types.");
         }
      } else if (keyword.equals(FUTURE.getVerb())) {
         if (leftPath.isOutput() == rightPath.isOutput()) {
            declareOrThrowError(context,
                                step,
                                "In future tense of correlation verb, one argument must be an input type and the"
                                      + " other must be of output type.");
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
}
