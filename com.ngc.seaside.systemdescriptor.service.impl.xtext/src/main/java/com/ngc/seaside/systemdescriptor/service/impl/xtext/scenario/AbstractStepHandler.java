package com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario;

import com.ngc.seaside.systemdescriptor.scenario.api.IScenarioStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.scenario.api.VerbTense;
import com.ngc.seaside.systemdescriptor.service.help.api.IHelpService;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public abstract class AbstractStepHandler implements IScenarioStepHandler {

   private final IHelpService helpService;
   private Map<VerbTense, ScenarioStepVerb> verbs = new EnumMap<>(VerbTense.class);

   protected AbstractStepHandler(IHelpService helpService) {
      this.helpService = helpService;
   }

   @Override
   public Map<VerbTense, ScenarioStepVerb> getVerbs() {
      return verbs;
   }

   protected AbstractStepHandler register(ScenarioStepVerb verb) {
      verbs.put(verb.getTense(), verb);
      return this;
   }

   protected AbstractStepHandler register(ScenarioStepVerb verb, String description) {
      verbs.put(verb.getTense(), verb);
      helpService.addDescription(verb, description);
      return this;
   }

   protected void makeFinal() {
      verbs = Collections.unmodifiableMap(verbs);
   }

   @Override
   public String toString() {
      return "StepHandler for " + verbs;
   }
}
