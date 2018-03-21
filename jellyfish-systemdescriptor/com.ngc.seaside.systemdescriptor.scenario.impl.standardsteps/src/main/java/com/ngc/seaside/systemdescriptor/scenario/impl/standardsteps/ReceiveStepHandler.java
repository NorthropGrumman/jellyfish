package com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.AbstractStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

/**
 * Implements the "receive" step verb.  This verb is used to indicate some input is asynchronously received using some
 * pub/sub protocol.  It's only argument is the input field of the model that contains the scenario that is published.
 */
public class ReceiveStepHandler extends AbstractStepHandler {

   public final static ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("haveReceived");
   public final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("receiving");
   public final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willReceive");

   public ReceiveStepHandler() {
      register(PAST, PRESENT, FUTURE);
   }

   /**
    * Gets the {@code IDataReferenceField} of the input of the model the scenario that is associated with that this
    * step. This can be used to determine which input field is received.
    *
    * <p/>
    *
    * Only invoke this method with validated scenario steps.
    *
    * @param step the step that contains a receive verb
    * @return the input field of the model that is received
    */
   public IDataReferenceField getInputs(IScenarioStep step) {
      Preconditions.checkNotNull(step, "step may not be null!");
      String keyword = step.getKeyword();
      Preconditions.checkArgument(
            keyword.equals(PAST.getVerb())
            || keyword.equals(PRESENT.getVerb())
            || keyword.equals(FUTURE.getVerb()),
            "the step cannot be processed by this handler!");

      IModel model = step.getParent().getParent();
      String inputName = step.getParameters().get(0);
      return model.getInputs()
            .getByName(inputName)
            .orElseThrow(() -> new IllegalStateException("model does not contain an input named " + inputName));
   }

   @Override
   protected void doValidateStep(IValidationContext<IScenarioStep> context) {
      requireStepParameters(context, "The 'receive' verb requires parameters!");
      requireOnlyOneParameter(context);
      requireParameterReferenceAnInputField(context);
   }

   private static void requireOnlyOneParameter(IValidationContext<IScenarioStep> context) {
      IScenarioStep step = context.getObject();
      if (step.getParameters().size() > 1) {
         context.declare(Severity.ERROR, "Only one field can be referenced with this verb!", step).getParameters();
      }
   }

   private static void requireParameterReferenceAnInputField(IValidationContext<IScenarioStep> context) {
      IScenarioStep step = context.getObject();
      String fieldName = step.getParameters().stream().findFirst().orElse(null);
      IModel model = step.getParent().getParent();
      if (fieldName != null && !model.getInputs().getByName(fieldName).isPresent()) {
         String errMsg = String.format("The model %s contains no input field named '%s'!",
                                       model.getFullyQualifiedName(),
                                       fieldName);
         context.declare(Severity.ERROR, errMsg, step).getParameters();
      }
   }
}
