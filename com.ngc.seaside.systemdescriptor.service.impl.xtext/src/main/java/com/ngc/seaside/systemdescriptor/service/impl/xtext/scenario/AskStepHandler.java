package com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario;

import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;

public class AskStepHandler extends AbstractStepHandler {

   public final static ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("haveAsked");
   public final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("asking");
   public final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willAsk");

   public AskStepHandler() {
      register(PAST, PRESENT, FUTURE);
   }
}
