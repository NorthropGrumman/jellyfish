package com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario;

import com.google.inject.Inject;

import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.service.help.api.IHelpService;

public class AskStepHandler extends AbstractStepHandler {

   public final static ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("haveAsked");
   public final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("asking");
   public final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willAsk");

   @Inject
   public AskStepHandler(IHelpService helpService) {
      super(helpService);
      register(PAST, "Indicates that the current model will have already asked some model to perform some scenario.")
            .register(PRESENT, "Indicants that the current model is asking some model to perform some scenario.")
            .register(FUTURE, "Indicates that the current model will eventually ask some model to perform some scenario.")
            .makeFinal();
   }
}
