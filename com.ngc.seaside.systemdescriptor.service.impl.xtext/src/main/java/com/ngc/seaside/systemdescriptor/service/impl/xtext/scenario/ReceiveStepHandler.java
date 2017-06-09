package com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario;

import com.google.inject.Inject;

import com.ngc.seaside.systemdescriptor.scenario.api.IScenarioStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.scenario.api.VerbTense;
import com.ngc.seaside.systemdescriptor.service.help.api.IHelpService;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class ReceiveStepHandler implements IScenarioStepHandler {

   public final static ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("haveReceived");
   public final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("receiving");
   public final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willReceive");

   private final IHelpService helpService;
   private Map<VerbTense, ScenarioStepVerb> verbs = new EnumMap<>(VerbTense.class);


   @Inject
   public ReceiveStepHandler(IHelpService helpService) {
      this.helpService = helpService;
      register(PAST, "Indicates that the current model will have already received some message or event as input.")
            .register(PRESENT, "Indicants that the current model is receiving some message or event as input.")
            .register(FUTURE, "Indicates that the current model will eventually receiving some message or event as input.")
            .makeFinal();
   }

   @Override
   public Map<VerbTense, ScenarioStepVerb> getVerbs() {
      return verbs;
   }

   protected ReceiveStepHandler register(ScenarioStepVerb verb) {
      verbs.put(verb.getTense(), verb);
      return this;
   }

   protected ReceiveStepHandler register(ScenarioStepVerb verb, String description) {
      verbs.put(verb.getTense(), verb);
      helpService.addDescription(verb, description);
      return this;
   }

   protected void makeFinal() {
      verbs = Collections.unmodifiableMap(verbs);
   }

   @Override
   public String toString() {
      return "ReceiveStepHandler{" +
             "verbs=" + verbs +
             '}';
   }
}
