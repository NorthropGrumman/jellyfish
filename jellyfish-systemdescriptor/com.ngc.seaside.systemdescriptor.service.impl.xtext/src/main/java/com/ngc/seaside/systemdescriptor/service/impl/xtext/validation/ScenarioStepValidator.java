/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.service.impl.xtext.validation;

import com.google.inject.Inject;

import com.ngc.seaside.systemdescriptor.model.api.SystemDescriptors;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.IScenarioStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.VerbTense;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.validation.api.AbstractSystemDescriptorValidator;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import java.util.Collection;
import java.util.stream.Collectors;

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
      Collection<IScenarioStepHandler> handlers = service.getScenarioStepHandlers();

      VerbTense tense;
      boolean hasHandler;

      if (SystemDescriptors.isGivenStep(step)) {
         tense = VerbTense.PAST_TENSE;
         hasHandler = handlers
               .stream()
               .anyMatch(h -> h.getVerbs().get(VerbTense.PAST_TENSE).getVerb().equals(keyword));
      } else if (SystemDescriptors.isWhenStep(step)) {
         tense = VerbTense.PRESENT_TENSE;
         hasHandler = handlers
               .stream()
               .anyMatch(h -> h.getVerbs().get(VerbTense.PRESENT_TENSE).getVerb().equals(keyword));
      } else {
         tense = VerbTense.FUTURE_TENSE;
         hasHandler = handlers
               .stream()
               .anyMatch(h -> h.getVerbs().get(VerbTense.FUTURE_TENSE).getVerb().equals(keyword));
      }

      if (!hasHandler) {
         String verbs = handlers
               .stream()
               .map(h -> h.getVerbs().get(tense).getVerb())
               .collect(Collectors.joining(", "));
         String error = String.format(
               "Unrecognized step verb '%s'!  Please use a valid %s verb when describing a %s step."
               + "  Available verbs are: %s",
               keyword,
               prettyTense(tense),
               prettyStep(tense),
               verbs);
         context.declare(Severity.ERROR, error, step).getKeyword();
      }
   }

   private static String prettyTense(VerbTense verbTense) {
      return verbTense.toString().toLowerCase().replace('_', ' ');
   }

   private static String prettyStep(VerbTense verbTense) {
      switch (verbTense) {
         case PAST_TENSE:
            return "given";
         case PRESENT_TENSE:
            return "when";
         default:
            return "then";
      }
   }
}
