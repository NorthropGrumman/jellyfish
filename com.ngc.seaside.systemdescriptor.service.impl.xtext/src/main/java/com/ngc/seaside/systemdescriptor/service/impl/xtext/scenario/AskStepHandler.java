package com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.AbstractStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;

/**
 * Implements the "ask" step verb.
 */
public class AskStepHandler extends AbstractStepHandler {

   public final static ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("haveAsked");
   public final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("asking");
   public final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willAsk");

   public AskStepHandler() {
      register(PAST, PRESENT, FUTURE);
   }

   @Override
   protected void doValidateStep(IValidationContext<IScenarioStep> context) {
      requireStepParameters(context, "The 'publish' verb requires parameters!");
   }
}
