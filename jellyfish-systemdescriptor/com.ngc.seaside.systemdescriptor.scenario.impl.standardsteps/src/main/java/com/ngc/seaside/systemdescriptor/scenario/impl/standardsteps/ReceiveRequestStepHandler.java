package com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.AbstractStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implements the "receive request" step verb.  This verb is used to indicate a component is receiving a request.  It's
 * only argument is the input field that represents the request.  Only one receive request step can appear in a scenario
 * and no correlation is possible.
 */
public class ReceiveRequestStepHandler extends AbstractStepHandler {

   public final static ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("haveReceivedRequest");
   public final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("receivingRequest");
   public final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willReceiveRequest");

   public ReceiveRequestStepHandler() {
      register(PAST, PRESENT, FUTURE);
   }

   /**
    * Gets the {@code IDataReferenceField} of the input of the model the scenario  that is associated with that this
    * step. This can be used to determine which input field is received as the request.
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
      requireOnlyOneParameter(context, "The 'receiveRequest' verb requires exactly one parameter!");
      requireParameterReferenceAnInputField(context);
      requireNoOtherReceiveRequestStepsInScenario(context);
      requireNoWhenStepsWithVerbInScenario(
            context,
            CorrelateStepHandler.PRESENT,
            "Scenarios that receive requests cannot correlate multiple inputs!");
      requireNoWhenStepsWithVerbInScenario(
            context,
            ReceiveStepHandler.PRESENT,
            "Scenarios that receive requests cannot asynchronously receive multiple inputs!");
   }

   protected void requireOnlyOneParameter(IValidationContext<IScenarioStep> context,
                                          String errorMessage) {
      requireStepParameters(context, errorMessage);
      IScenarioStep step = context.getObject();
      if (step.getParameters().size() > 1) {
         context.declare(Severity.ERROR, errorMessage, step).getParameters();
      }
   }

   protected void requireNoGivenStepsWithVerbInScenario(IValidationContext<IScenarioStep> context,
                                                        ScenarioStepVerb verb,
                                                        String errorMessage) {
      IScenario scenario = context.getObject().getParent();
      for (IScenarioStep step : scenario.getGivens()) {
         if (step.getKeyword().equals(verb.getVerb())) {
            context.declare(Severity.ERROR, errorMessage, step).getKeyword();
         }
      }
   }

   protected void requireNoWhenStepsWithVerbInScenario(IValidationContext<IScenarioStep> context,
                                                       ScenarioStepVerb verb,
                                                       String errorMessage) {
      IScenario scenario = context.getObject().getParent();
      for (IScenarioStep step : scenario.getWhens()) {
         if (step.getKeyword().equals(verb.getVerb())) {
            context.declare(Severity.ERROR, errorMessage, step).getKeyword();
         }
      }
   }

   protected void requireNoThenStepsWithVerbInScenario(IValidationContext<IScenarioStep> context,
                                                       ScenarioStepVerb verb,
                                                       String errorMessage) {
      IScenario scenario = context.getObject().getParent();
      for (IScenarioStep step : scenario.getThens()) {
         if (step.getKeyword().equals(verb.getVerb())) {
            context.declare(Severity.ERROR, errorMessage, step).getKeyword();
         }
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

   private static void requireNoOtherReceiveRequestStepsInScenario(IValidationContext<IScenarioStep> context) {
      // Only do this check if the step being validated is the first receive step in the scenario.  This avoids
      // creating multiple errors for the user.
      IScenarioStep step = context.getObject();
      IScenario scenario = step.getParent();

      List<IScenarioStep> receiveRequestSteps = scenario.getWhens()
            .stream()
            .filter(s -> s.getKeyword().equals(PRESENT.getVerb()))
            .collect(Collectors.toList());

      if (receiveRequestSteps.size() > 1 && receiveRequestSteps.get(0).equals(step)) {
         String errMsg = "Scenarios that receive requests cannot receive multiple inputs!";
         context.declare(Severity.ERROR, errMsg, receiveRequestSteps.get(1)).getKeyword();
      }
   }
}
