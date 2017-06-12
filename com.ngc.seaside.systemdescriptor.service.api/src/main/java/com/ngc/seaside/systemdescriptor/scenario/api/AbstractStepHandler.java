package com.ngc.seaside.systemdescriptor.scenario.api;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.validation.api.AbstractSystemDescriptorValidator;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * A base class for {@code IScenarioStepHandler}s.  The different forms of the verb supported by this handler must be
 * {@link #register(ScenarioStepVerb, ScenarioStepVerb...) registered} in the constructor.  Instances that extend this
 * class are also {@code ISystemDescriptorValidator}s.  This allows a step handler to validate the arguments included
 * with the step are valid for the given verb.  Implementations should perform any validation in {@link
 * #doValidateStep(IValidationContext)}.  For convenience, {@link #requireStepParameters(IValidationContext, String)} is
 * provided to indicate that a verb requires at least 1 argument.
 *
 * <p/>
 *
 * The simplest extension of this class might look something like this:
 *
 * <pre>
 *    {@code
 *    public class SurfingStepHandler extends AbstractStepHandler {
 *      private final static ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("surfed");
 *      private final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("surfing");
 *      private final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("will surf");
 *
 *      public SurfingStepHandler() {
 *        register(PAST, PRESENT, FUTURE);
 *      }
 *
 *      @Override
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
    * May be invoked during {@link #doValidateStep(IValidationContext)} to verify the step has at least 1 argument.
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

   private boolean shouldStepBeValidated(IScenarioStep step) {
      String keyword = step.getKeyword();
      return verbs.values().stream().filter(v -> v.getVerb().equals(keyword)).count() > 0;
   }

}
