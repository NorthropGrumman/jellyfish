package com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario;

import com.google.inject.Inject;

import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.service.help.api.IHelpService;

public class PublishStepHandler extends AbstractStepHandler {

   public final static ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("havePublished");
   public final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("publishing");
   public final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willPublish");

   @Inject
   public PublishStepHandler(IHelpService helpService) {
      super(helpService);
      register(PAST, "Indicates that the current model will have already published some message or event as output.")
            .register(PRESENT, "Indicants that the current model is publishing some message or event as output.")
            .register(FUTURE, "Indicates that the current model will eventually publish some message or event as output.")
            .makeFinal();
   }
}
