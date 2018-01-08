package com.ngc.seaside.systemdescriptor.service.impl.xtext.validation;

import com.google.inject.Inject;

import com.ngc.seaside.systemdescriptor.model.api.SystemDescriptors;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.VerbTense;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.validation.api.AbstractSystemDescriptorValidator;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

/**
 * A validator that requires a {@link com.ngc.seaside.systemdescriptor.scenario.api.IScenarioStepHandler} be registered
 * to handle a {@link IScenarioStep#getKeyword() step keyword} that is referenced in a step.
 */
public class ScenarioStepValidator extends AbstractSystemDescriptorValidator {

   private final ISystemDescriptorService service;

   @Inject
   public ScenarioStepValidator(ISystemDescriptorService service) {
      this.service = service;
   }

   @Override
   protected void validateStep(IValidationContext<IScenarioStep> context) {
      IScenarioStep step = context.getObject();
      String keyword = step.getKeyword();
      boolean hasHandler;

      // TODO TH: update this so the user gets a better error message if they use a verb in the wrong tense.

      if (SystemDescriptors.isGivenStep(step)) {
         hasHandler = service.getScenarioStepHandlers()
                            .stream()
                            .filter(h -> h.getVerbs().get(VerbTense.PAST_TENSE).getVerb().equals(keyword))
                            .count() > 0;
      } else if (SystemDescriptors.isWhenStep(step)) {
         hasHandler = service.getScenarioStepHandlers()
                            .stream()
                            .filter(h -> h.getVerbs().get(VerbTense.PRESENT_TENSE).getVerb().equals(keyword))
                            .count() > 0;
      } else {
         hasHandler = service.getScenarioStepHandlers()
                            .stream()
                            .filter(h -> h.getVerbs().get(VerbTense.FUTURE_TENSE).getVerb().equals(keyword))
                            .count() > 0;
      }

      if (!hasHandler) {
         String error = String.format(
               "Unrecognized step keyword '%s'!  Please use a valid keyword when describing a scenario.",
               keyword);
         context.declare(Severity.ERROR, error, step).getKeyword();
      }
   }
}
