package com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.AbstractStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;

/**
 * Implements the "receive" step verb.
 */
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
   }
}
