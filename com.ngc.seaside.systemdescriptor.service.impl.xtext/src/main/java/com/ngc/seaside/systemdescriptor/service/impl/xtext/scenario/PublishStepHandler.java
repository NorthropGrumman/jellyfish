package com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario;

import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;

public class PublishStepHandler extends AbstractStepHandler {

   public final static ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("havePublished");
   public final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("publishing");
   public final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willPublish");

   public PublishStepHandler() {
      register(PAST, PRESENT, FUTURE);
   }
}
