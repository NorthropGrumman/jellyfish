package com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.AbstractStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

public class CompleteStepHandler extends AbstractStepHandler {

   public final static ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("hasCompleted");
   public final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("completing");
   public final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willBeCompleted");

   public CompleteStepHandler() {
      register(PAST, PRESENT, FUTURE);
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
         // The first parameter should be "within" right now.
         validateOperand(context, step, parameters.get(0));
         // The second parameter should be a number (double).
         validateDuration(context, step, parameters.get(1));
         // The third parameter should be a time unit.
         validateTimeUnit(context, step, parameters.get(2));
      }
   }

   private static void validateOperand(IValidationContext<IScenarioStep> context,
                                       IScenarioStep step,
                                       String parameter) {
      // We just support within right now, need to word on this.
      if (!"within".equals(parameter)) {
         context.declare(Severity.ERROR,
                         "Expected first parameter to be 'within'.",
                         step)
               .getKeyword();
      }
   }

   private static void validateDuration(IValidationContext<IScenarioStep> context,
                                        IScenarioStep step,
                                        String parameter) {
      try {
         Double.parseDouble(parameter);
      } catch (NumberFormatException e) {
         context.declare(Severity.ERROR,
                         "Expected second parameter to be double number.",
                         step)
               .getKeyword();
      }
   }

   private static void validateTimeUnit(IValidationContext<IScenarioStep> context,
                                        IScenarioStep step,
                                        String parameter) {
      int length = TimeUnit.values().length;
      StringJoiner joiner = new StringJoiner(", ");
      boolean valid = false;
      for (int i = 0; i < length; i++) {
         String label = TimeUnit.values()[i].toString().toLowerCase();
         joiner.add(label);
         valid |= label.equals(parameter);
      }

      if (!valid) {
         context.declare(Severity.ERROR,
                         String.format("Expected third parameter to be a time unit (%s).", joiner.toString()),
                         step)
               .getKeyword();
      }
   }
}
