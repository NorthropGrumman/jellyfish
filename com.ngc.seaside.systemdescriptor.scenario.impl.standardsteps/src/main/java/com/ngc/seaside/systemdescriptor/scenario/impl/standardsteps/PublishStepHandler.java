package com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.AbstractStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;

/**
 * Implements the "publish" step verb.  This verb is used to indicate some output is asynchronously published using some
 * pub/sub protocol.  It's only argument is the output field of the model that contains the scenario that is published.
 */
public class PublishStepHandler extends AbstractStepHandler {

   public final static ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("havePublished");
   public final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("publishing");
   public final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willPublish");

   public PublishStepHandler() {
      register(PAST, PRESENT, FUTURE);
   }

   /**
    * Gets the {@code IDataReferenceField} of the output of the model the scenario is associated with that this step.
    * This can be used to determine which output field is published.
    *
    * <p/>
    *
    * Only invoke this method with validated scenario steps.
    *
    * @param step the step that contains a publish verb
    * @return the output field of the model that is published
    */
   public IDataReferenceField getOutputs(IScenarioStep step) {
      Preconditions.checkNotNull(step, "step may not be null!");
      String keyword = step.getKeyword();
      Preconditions.checkArgument(
            keyword.equals(PAST.getVerb())
            || keyword.equals(PRESENT.getVerb())
            || keyword.equals(FUTURE.getVerb()),
            "the step cannot be processed by this handler!");

      IModel model = step.getParent().getParent();
      String outputName = step.getParameters().get(0);
      return model.getOutputs()
            .getByName(outputName)
            .orElseThrow(() -> new IllegalStateException("model does not contain an output named " + outputName));
   }

   @Override
   protected void doValidateStep(IValidationContext<IScenarioStep> context) {
      requireStepParameters(context, "The 'publish' verb requires parameters!");
   }
}
