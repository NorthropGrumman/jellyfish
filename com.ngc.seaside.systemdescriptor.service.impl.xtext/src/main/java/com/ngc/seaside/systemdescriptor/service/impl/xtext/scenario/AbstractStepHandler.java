package com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario;

import com.ngc.seaside.systemdescriptor.scenario.api.IScenarioStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.scenario.api.VerbTense;
import com.ngc.seaside.systemdescriptor.validation.api.AbstractSystemDescriptorValidator;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * A base class for {@code IScenarioStepHandler}s.  The different forms of the verb supported by this handler must be
 * {@link #register(ScenarioStepVerb, ScenarioStepVerb...) registered} in the constructor.
 */
public abstract class AbstractStepHandler implements IScenarioStepHandler {

   private Map<VerbTense, ScenarioStepVerb> verbs = new EnumMap<>(VerbTense.class);

   @Override
   public Map<VerbTense, ScenarioStepVerb> getVerbs() {
      return verbs;
   }


   /**
    * Registered the given forms of the verb.  Once this method is called additional forms cannot be registered.
    */
   protected void register(ScenarioStepVerb verb, ScenarioStepVerb... more) {
      verbs.put(verb.getTense(), verb);
      if (more != null) {
         for (ScenarioStepVerb v : more) {
            verbs.put(v.getTense(), v);
         }
      }
      verbs = Collections.unmodifiableMap(verbs);
   }

   @Override
   public String toString() {
      return "StepHandler for " + verbs;
   }
}
