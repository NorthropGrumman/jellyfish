package com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario;

import com.ngc.seaside.systemdescriptor.scenario.api.IScenarioStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;

public class ReceiveStepHandler implements IScenarioStepHandler {

   private static final ScenarioStepVerb VERB = ScenarioStepVerb.create("hasReceived", "receiving", "will receive");

   @Override
   public ScenarioStepVerb getVerb() {
      return VERB;
   }
}
