package com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.AbstractStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;

/**
 * Implements the "respond" step verb.  This verb is used to indicate a component is responding to a request.  It's only
 * argument is the output field that represents the response.  Only one response step can appear in a scenario.
 */
public class RespondStepHandler extends AbstractStepHandler {

   public final static ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("haveResponded");
   public final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("responding");
   public final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willRespond");

   public RespondStepHandler() {
      register(PAST, PRESENT, FUTURE);
   }

   @Override
   protected void doValidateStep(IValidationContext<IScenarioStep> context) {
      // TODO TH: implement this
   }
}
