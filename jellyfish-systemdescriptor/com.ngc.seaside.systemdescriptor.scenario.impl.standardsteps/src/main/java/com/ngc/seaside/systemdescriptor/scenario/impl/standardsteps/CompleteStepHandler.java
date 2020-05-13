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
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.AbstractStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

/**
 * Implements the "complete" verb which is used to declare timing constraints.  It contains a number of arguments and
 * its form is:
 * <pre>
 *    {@code
 *     <verb> (within|atLeast) <double> <TimeUnit>
 *    }
 * </pre>
 */
public class CompleteStepHandler extends AbstractStepHandler {

   /**
    * The different operand that define how the duration should be interpreted.
    */
   public enum Operand {
      LESS_THAN_OR_EQUAL("within"),
      GREATER_THAN_OR_EQUAL("atLeast");

      private final String keyword;

      Operand(String keyword) {
         this.keyword = keyword;
      }
   }

   public static final ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("hasCompleted");
   public static final ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("completing");
   public static final ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willBeCompleted");

   public CompleteStepHandler() {
      register(PAST, PRESENT, FUTURE);
   }

   /**
    * Gets the operand referenced in the step.
    * Only invoke this method with validated scenario steps.
    *
    * @param step the step that contains the complete verb
    * @return the operand
    */
   public Operand getOperand(IScenarioStep step) {
      requireStepUsesHandlerVerb(step);
      List<String> parameters = step.getParameters();
      Preconditions.checkArgument(parameters.size() == 3,
                                  "invalid step!");
      return validateOperand(null, step, parameters.get(0));
   }

   /**
    * Gets the duration referenced in the step.
    * Only invoke this method with validated scenario steps.
    *
    * @param step the step that contains the complete verb
    * @return the duration
    */
   public double getDuration(IScenarioStep step) {
      requireStepUsesHandlerVerb(step);
      List<String> parameters = step.getParameters();
      Preconditions.checkArgument(parameters.size() == 3,
                                  "invalid step!");
      return validateDuration(null, step, parameters.get(1));
   }

   /**
    * Gets the time unit referenced in the step.
    * Only invoke this method with validated scenario steps.
    *
    * @param step the step that contains the complete verb
    * @return the time unit
    */
   public TimeUnit getTimeUnit(IScenarioStep step) {
      requireStepUsesHandlerVerb(step);
      List<String> parameters = step.getParameters();
      Preconditions.checkArgument(parameters.size() == 3,
                                  "invalid step!");
      return validateTimeUnit(null, step, parameters.get(2));
   }

   @Override
   protected Set<String> doGetSuggestedParameterCompletions(String partialParameter, int parameterIndex,
                                                            IScenarioStep step, ScenarioStepVerb verb) {
      if (step.getParameters().size() > 3) {
         return Collections.emptySet();
      }
      Set<String> suggestions = new TreeSet<>();
      switch (parameterIndex) {
         case 0:
            for (Operand operand : Operand.values()) {
               String keyword = operand.keyword;
               if (keyword.startsWith(partialParameter)) {
                  suggestions.add(keyword);
               }
            }
            break;
         case 2:
            for (TimeUnit unit : TimeUnit.values()) {
               String keyword = unit.name().toLowerCase();
               if (keyword.startsWith(partialParameter)) {
                  suggestions.add(keyword);
               }
            }
            break;
         default:
            // ignore
            break;
      }
      return suggestions;
   }

   @Override
   protected void doValidateStep(IValidationContext<IScenarioStep> context) {
      requireStepParameters(context, "The 'complete' verb requires parameters!");

      IScenarioStep step = context.getObject();
      List<String> parameters = step.getParameters();
      if (parameters.size() != 3) {
         context.declare(Severity.ERROR,
                         "Expected parameters of the form: within <number/double> <time unit>",
                         step)
               .getKeyword();
      } else {
         // The first parameter should be an operand.
         validateOperand(context, step, parameters.get(0));
         // The second parameter should be a number (double).
         validateDuration(context, step, parameters.get(1));
         // The third parameter should be a time unit.
         validateTimeUnit(context, step, parameters.get(2));
      }
   }

   private static Operand validateOperand(IValidationContext<IScenarioStep> context,
                                          IScenarioStep step,
                                          String parameter) {
      int length = Operand.values().length;
      StringJoiner joiner = new StringJoiner(", ");
      Operand operand = null;
      for (int i = 0; i < length; i++) {
         String label = Operand.values()[i].keyword;
         joiner.add(label);
         if (label.equals(parameter)) {
            operand = Operand.values()[i];
         }
      }

      if (operand == null) {
         declareOrThrowError(context,
                             step,
                             String.format("Expected first parameter to be one of '%s'.", joiner.toString()));
      }
      return operand;
   }

   private static double validateDuration(IValidationContext<IScenarioStep> context,
                                          IScenarioStep step,
                                          String parameter) {
      double value;
      try {
         value = Double.parseDouble(parameter);
      } catch (NumberFormatException e) {
         declareOrThrowError(context, step, "Expected second parameter to be double number.");
         value = 0;
      }
      return value;
   }

   private static TimeUnit validateTimeUnit(IValidationContext<IScenarioStep> context,
                                            IScenarioStep step,
                                            String parameter) {
      int length = TimeUnit.values().length;
      StringJoiner joiner = new StringJoiner(", ");
      TimeUnit unit = null;
      for (int i = 0; i < length; i++) {
         String label = TimeUnit.values()[i].toString().toLowerCase();
         joiner.add(label);
         if (label.equals(parameter)) {
            unit = TimeUnit.values()[i];
         }
      }

      if (unit == null) {
         declareOrThrowError(context,
                             step,
                             String.format("Expected third parameter to be a time unit (%s).", joiner.toString()));
      }
      return unit;
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

   private static void requireStepUsesHandlerVerb(IScenarioStep step) {
      Preconditions.checkNotNull(step, "step may not be null!");
      String keyword = step.getKeyword();
      Preconditions.checkArgument(
            keyword.equals(PAST.getVerb())
                  || keyword.equals(PRESENT.getVerb())
                  || keyword.equals(FUTURE.getVerb()),
            "the step cannot be processed by this handler!");
   }
}
