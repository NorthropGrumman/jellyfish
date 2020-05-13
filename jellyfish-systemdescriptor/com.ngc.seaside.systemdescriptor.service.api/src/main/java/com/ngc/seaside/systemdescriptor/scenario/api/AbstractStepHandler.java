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
package com.ngc.seaside.systemdescriptor.scenario.api;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.validation.api.AbstractSystemDescriptorValidator;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A base class for {@code IScenarioStepHandler}s.  The different forms of the verb supported by this handler must be
 * {@link #register(ScenarioStepVerb, ScenarioStepVerb...) registered} in the constructor.  Instances that extend this
 * class are also {@code ISystemDescriptorValidator}s.  This allows a step handler to validate the arguments included
 * with the step are valid for the given verb.  Implementations should perform any validation in {@link
 * #doValidateStep(IValidationContext)}.  For convenience, several methods are provided that can reusable validation
 * logic.
 * <p/>
 * The simplest extension of this class might look something like this:
 * <pre>
 *    {@code public class SurfingStepHandler extends AbstractStepHandler {
 *      private final static ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("surfed");
 *      private final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("surfing");
 *      private final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("will surf");
 *      public SurfingStepHandler() {
 *        register(PAST, PRESENT, FUTURE);
 *      }
 *      {@literal @}Override
 *      protected void doValidateStep(IValidationContext<IScenarioStep> context) {
 *        requireStepParameters(context, "The 'surf' verb requires parameters!");
 *      }
 *    }
 *    }
 * </pre>
 */
public abstract class AbstractStepHandler extends AbstractSystemDescriptorValidator implements IScenarioStepHandler {

   /**
    * All the verbs supported by this handler, keyed by tense.
    */
   private Map<VerbTense, ScenarioStepVerb> verbs = new EnumMap<>(VerbTense.class);

   @Override
   public Map<VerbTense, ScenarioStepVerb> getVerbs() {
      return verbs;
   }

   @Override
   public String toString() {
      return "StepHandler for " + verbs;
   }

   /**
    * Invoked to validate this step.  Place validation logic instead of overriding {@link
    * #validateStep(IValidationContext)} directly.  Otherwise, the validation logic may be called for <i>all</i> steps,
    * not just steps that use the verb handled by this handler.
    */
   protected abstract void doValidateStep(IValidationContext<IScenarioStep> context);

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

   /**
    * Delegates to {@code doValidateStep} only if the provided step is a step that is using the verb provided by this
    * handler.  Place validation logic in {@code doValidateStep} instead of overriding this implementation.
    */
   @Override
   protected void validateStep(IValidationContext<IScenarioStep> context) {
      if (shouldStepBeValidated(context.getObject())) {
         doValidateStep(context);
      }
   }

   /**
    * May be invoked during {@link #doValidateStep(IValidationContext)} to verify the step has at least 1 parameter.
    *
    * @param context      the validation context
    * @param errorMessage the error message to use if the scenario step has no parameters
    */
   protected void requireStepParameters(IValidationContext<IScenarioStep> context,
                                        String errorMessage) {
      IScenarioStep step = context.getObject();
      if (step.getParameters().isEmpty()) {
         context.declare(Severity.ERROR, errorMessage, step).getParameters();
      }
   }

   /**
    * May be invoked during {@link #doValidateStep(IValidationContext)} to verify the step has exactly 1 parameter.
    *
    * @param context      the validation context
    * @param errorMessage the error message to use if the scenario step does not have 1 parameter
    */
   protected void requireOnlyOneParameter(IValidationContext<IScenarioStep> context,
                                          String errorMessage) {
      requireStepParameters(context, errorMessage);
      IScenarioStep step = context.getObject();
      if (step.getParameters().size() > 1) {
         context.declare(Severity.ERROR, errorMessage, step).getParameters();
      }
   }

   /**
    * May be invoked during {@link #doValidateStep(IValidationContext)} to verify the step has exactly <i>N</i>
    * parameters.
    *
    * @param context      the validation context
    * @param errorMessage the error message to use if the scenario step does not have exactly <i>N</i> parameters
    */
   protected void requireExactlyNParameters(IValidationContext<IScenarioStep> context,
                                            int parameterCount,
                                            String errorMessage) {
      IScenarioStep step = context.getObject();
      if (parameterCount != step.getParameters().size()) {
         context.declare(Severity.ERROR, errorMessage, step).getParameters();
      }
   }

   /**
    * May be invoked during {@link #doValidateStep(IValidationContext)} to verify the scenario that contains the current
    * step does not contain a given step that uses the given verb.
    *
    * @param context      the validation context
    * @param verb         the verb to check in past tense
    * @param errorMessage the error message to use if the scenario contains a given step with the given verb
    */
   protected void requireNoGivenStepsWithVerbInScenario(IValidationContext<IScenarioStep> context,
                                                        ScenarioStepVerb verb,
                                                        String errorMessage) {
      if (verb.getTense() != VerbTense.PAST_TENSE) {
         throw new IllegalArgumentException("verb " + verb + " should be in past tense!");
      }

      IScenario scenario = context.getObject().getParent();
      for (IScenarioStep step : scenario.getGivens()) {
         if (step.getKeyword().equals(verb.getVerb())) {
            context.declare(Severity.ERROR, errorMessage, step).getKeyword();
         }
      }
   }

   /**
    * May be invoked during {@link #doValidateStep(IValidationContext)} to verify the scenario that contains the current
    * step does not contain a when step that uses the given verb.
    *
    * @param context      the validation context
    * @param verb         the verb to check in present tense
    * @param errorMessage the error message to use if the scenario contains a when step with the given verb
    */
   protected void requireNoWhenStepsWithVerbInScenario(IValidationContext<IScenarioStep> context,
                                                       ScenarioStepVerb verb,
                                                       String errorMessage) {
      if (verb.getTense() != VerbTense.PRESENT_TENSE) {
         throw new IllegalArgumentException("verb " + verb + " should be in present tense!");
      }

      IScenario scenario = context.getObject().getParent();
      for (IScenarioStep step : scenario.getWhens()) {
         if (step.getKeyword().equals(verb.getVerb())) {
            context.declare(Severity.ERROR, errorMessage, step).getKeyword();
         }
      }
   }

   /**
    * May be invoked during {@link #doValidateStep(IValidationContext)} to verify the scenario that contains the current
    * step does not contain a then step that uses the given verb.
    *
    * @param context      the validation context
    * @param verb         the verb to check in future tense
    * @param errorMessage the error message to use if the scenario contains a then step with the given verb
    */
   protected void requireNoThenStepsWithVerbInScenario(IValidationContext<IScenarioStep> context,
                                                       ScenarioStepVerb verb,
                                                       String errorMessage) {
      if (verb.getTense() != VerbTense.FUTURE_TENSE) {
         throw new IllegalArgumentException("verb " + verb + " should be in future tense!");
      }

      IScenario scenario = context.getObject().getParent();
      for (IScenarioStep step : scenario.getThens()) {
         if (step.getKeyword().equals(verb.getVerb())) {
            context.declare(Severity.ERROR, errorMessage, step).getKeyword();
         }
      }
   }

   /**
    * Checks that the given arguments are valid and delegates to
    * {@link #doGetSuggestedParameterCompletions(String, int, IScenarioStep, ScenarioStepVerb)}, filtering the resulting
    * suggestions for those that don't start with the provided partial completion.
    */
   @Override
   public Set<String> getSuggestedParameterCompletions(IScenarioStep step, ScenarioStepVerb verb, int parameterIndex) {
      Objects.requireNonNull(step, "step cannot be null");
      Objects.requireNonNull(verb, "verb cannot be null");
      List<String> partialParameterCompletion = step.getParameters();
      if (partialParameterCompletion.isEmpty()) {
         throw new IllegalArgumentException("step parameter list cannot be empty");
      }
      if (parameterIndex < 0 || parameterIndex >= partialParameterCompletion.size()) {
         throw new IllegalArgumentException(
                  "partial parameter completion at " + parameterIndex + " index cannot be null");
      }
      String partialParameter = partialParameterCompletion.get(parameterIndex);
      Objects.requireNonNull(partialParameter,
               "partial parameter completion at " + parameterIndex + " index cannot be null");
      Set<String> suggestions = doGetSuggestedParameterCompletions(partialParameter, parameterIndex, step, verb);
      return suggestions.stream().filter(suggestion -> suggestion.startsWith(partialParameter))
               .collect(Collectors.toCollection(LinkedHashSet::new));
   }

   /**
    * Returns the suggestions for the given parameter. The suggestions do not need to be prefixed with the partial
    * parameter as this is performed by {@link #getSuggestedParameterCompletions(IScenarioStep, ScenarioStepVerb, int)}.
    *
    * @see IScenarioStepHandler#getSuggestedParameterCompletions(IScenarioStep, ScenarioStepVerb, int)
    *
    * @param partialParameter partially-completed parameter (may be empty)
    * @param parameterIndex   index of parameter
    * @param step scenario    step
    * @param verb             verb
    * @return collection of suggested completions for the given parameter
    */
   protected Set<String> doGetSuggestedParameterCompletions(String partialParameter, int parameterIndex,
            IScenarioStep step, ScenarioStepVerb verb) {
      return Collections.emptySet();
   }

   private boolean shouldStepBeValidated(IScenarioStep step) {
      String keyword = step.getKeyword();
      return verbs.values().stream().filter(v -> v.getVerb().equals(keyword)).count() > 0;
   }

}
