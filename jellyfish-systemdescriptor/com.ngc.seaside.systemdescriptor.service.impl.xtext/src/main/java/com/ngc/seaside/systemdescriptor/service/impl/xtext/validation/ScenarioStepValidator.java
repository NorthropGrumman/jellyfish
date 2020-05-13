/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
