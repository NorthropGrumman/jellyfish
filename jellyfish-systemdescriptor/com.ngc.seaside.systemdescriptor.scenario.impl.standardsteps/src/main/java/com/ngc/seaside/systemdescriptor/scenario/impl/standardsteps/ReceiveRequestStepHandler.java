package com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps;

import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.AbstractStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;

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
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   protected void doValidateStep(IValidationContext<IScenarioStep> context) {
      // TODO TH: implement this
   }
}
