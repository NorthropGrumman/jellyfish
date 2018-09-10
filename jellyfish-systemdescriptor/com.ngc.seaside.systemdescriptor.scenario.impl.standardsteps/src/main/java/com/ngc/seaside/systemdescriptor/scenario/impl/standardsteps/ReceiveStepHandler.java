/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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

   public static final ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("haveReceived");
   public static final ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("receiving");
   public static final ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willReceive");

   public ReceiveStepHandler() {
      register(PAST, PRESENT, FUTURE);
   }

   /**
    * Gets the {@code IDataReferenceField} of the input of the model the scenario that is associated with that this
    * step. This can be used to determine which input field is received.
    * <p/>
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
      requireOnlyOneParameter(context, "The 'receive' verb requires exactly one parameter which is an input field!");
      requireParameterReferenceAnInputField(context, 0);
   }

   static void requireParameterReferenceAnInputField(IValidationContext<IScenarioStep> context,
                                                     int parameterIndex) {
      IScenarioStep step = context.getObject();
      String
            fieldName =
            step.getParameters().size() <= parameterIndex ? null : step.getParameters().get(parameterIndex);
      IModel model = step.getParent().getParent();
      if (fieldName != null && !model.getInputs().getByName(fieldName).isPresent()) {
         String errMsg = String.format("The model %s contains no input field named '%s'!",
                                       model.getFullyQualifiedName(),
                                       fieldName);
         context.declare(Severity.ERROR, errMsg, step).getParameters();
      }
   }
}
