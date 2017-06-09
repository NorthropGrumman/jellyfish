package com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;


public class AskStepHandler extends AbstractStepHandler {

   public final static ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("haveAsked");
   public final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("asking");
   public final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willAsk");

   public AskStepHandler() {
      register(PAST, PRESENT, FUTURE);
   }

//   @Override
//   protected void validateStep(IValidationContext<IScenarioStep> context) {
//      IScenarioStep step = context.getObject();
//      if(step.getParameters().isEmpty()) {
//         context.declare(Severity.ERROR, "recieved must have parameters!", step).getParameters();
//      }
//   }
}
