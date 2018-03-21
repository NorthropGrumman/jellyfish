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
   public IDataReferenceField getRequest(IScenarioStep step) {
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
      requireOnlyOneParameter(context,
                              "The 'receiveRequest' verb requires exactly one parameter which is an input field!");
      ReceiveStepHandler.requireParameterReferenceAnInputField(context, 0);
      requireNoOtherReceiveRequestStepsInScenario(context);
      requireNoWhenStepsWithVerbInScenario(
            context,
            CorrelateStepHandler.PRESENT,
            "Scenarios that receive requests cannot correlate multiple inputs!");
      requireNoWhenStepsWithVerbInScenario(
            context,
            ReceiveStepHandler.PRESENT,
            "Scenarios that receive requests cannot asynchronously receive multiple inputs!");
      requireWillRespondStep(context);
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

   private static void requireWillRespondStep(IValidationContext<IScenarioStep> context) {
      IScenarioStep step = context.getObject();
      IScenario scenario = step.getParent();

      boolean hasWillRespondStep = scenario.getThens()
            .stream()
            .anyMatch(s -> RespondStepHandler.FUTURE.getVerb().equals(s.getKeyword()));
      if(!hasWillRespondStep) {
         String errMsg = "Scenarios that receive requests must respond using the 'willRespond' verb!";
         context.declare(Severity.ERROR, errMsg, scenario).getThens();
      }
   }

}
