package com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario;

import com.ngc.seaside.systemdescriptor.model.api.SystemDescriptors;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

public class ReceiveStepHandler extends AbstractStepHandler {

   public final static ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("haveReceived");
   public final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("receiving");
   public final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willReceive");

   public ReceiveStepHandler() {
      register(PAST, PRESENT, FUTURE);
   }

   @Override
   protected void doValidateStep(IValidationContext<IScenarioStep> context) {
      requireStepParameters(context, "The 'receive' verb requires parameters!");
      suggestRemovingGivenUsage(context);
   }

   private void suggestRemovingGivenUsage(IValidationContext<IScenarioStep> context) {
      IScenarioStep step = context.getObject();
      if (SystemDescriptors.isGivenStep(step)) {
         context.declare(Severity.SUGGESTION, "Suspicious use of the verb 'receive' in a given step.  Using receive in"
                                              + " a given step may indicate a stateful scenario or a scenario that"
                                              + " should be decomposed into two different scenarios.  Consider replacing this step with a when step.",
                         step).getKeyword();
      }
   }
}
