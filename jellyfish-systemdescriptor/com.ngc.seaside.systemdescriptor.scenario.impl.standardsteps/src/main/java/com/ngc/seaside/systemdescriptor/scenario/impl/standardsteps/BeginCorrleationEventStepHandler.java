package com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.AbstractStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;

public class BeginCorrleationEventStepHandler extends AbstractStepHandler {

   public final static ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("hasBeganCorrelationEvent");
   public final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("beginningCorrelationEvent");
   public final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willBeginCorrelationEvent");

   public BeginCorrleationEventStepHandler() {
      register(PAST, PRESENT, FUTURE);
   }

   // TODO TH: This handler is not implemented completely.  It is just implemented enough to get around build errors.

   @Override
   protected void doValidateStep(IValidationContext<IScenarioStep> context) {
   }
}
