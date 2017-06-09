package com.ngc.seaside.systemdescriptor.service.help.impl.defaultz;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.service.help.api.IHelpService;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultHelpService implements IHelpService {

   private final Map<ScenarioStepVerb, String> stepVerbDescriptions = new ConcurrentHashMap<>();

   @Override
   public Optional<String> getDescription(ScenarioStepVerb verb) {
      Preconditions.checkNotNull(verb, "verb may not be null!");
      return Optional.ofNullable(stepVerbDescriptions.get(verb));
   }

   @Override
   public void addDescription(ScenarioStepVerb verb, String description) {
      Preconditions.checkNotNull(verb, "verb may not be null!");
      Preconditions.checkNotNull(description, "description may not be null!");
      stepVerbDescriptions.put(verb, description);
   }

   @Override
   public boolean removeDescription(ScenarioStepVerb verb) {
      Preconditions.checkNotNull(verb, "verb may not be null!");
      return stepVerbDescriptions.remove(verb) != null;
   }
}
