package com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario;

import com.google.inject.Inject;

import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.service.help.api.IHelpService;

public class ReceiveStepHandler extends AbstractStepHandler {

   public final static ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("haveReceived");
   public final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("receiving");
   public final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willReceive");

   @Inject
   public ReceiveStepHandler(IHelpService helpService) {
      super(helpService);
      register(PAST, "Indicates that the current model will have already received some message or event as input.")
            .register(PRESENT, "Indicants that the current model is receiving some message or event as input.")
            .register(FUTURE, "Indicates that the current model will eventually receive some message or event as input.")
            .makeFinal();
   }
}
