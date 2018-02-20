package com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.AbstractStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;

public class ReceiveRequestStepHandler extends AbstractStepHandler {

   public final static ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("haveReceivedRequest");
   public final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("receivingRequest");
   public final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willReceiveRequest");

   public ReceiveRequestStepHandler() {
      register(PAST, PRESENT, FUTURE);
   }

   @Override
   protected void doValidateStep(IValidationContext<IScenarioStep> context) {
      // TODO TH: implement this
   }
}
