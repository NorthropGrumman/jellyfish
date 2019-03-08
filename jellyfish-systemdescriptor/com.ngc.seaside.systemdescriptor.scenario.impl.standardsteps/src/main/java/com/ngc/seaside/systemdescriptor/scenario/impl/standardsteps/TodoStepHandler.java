/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.AbstractStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

/**
 * A placeholder for a step.
 */
public class TodoStepHandler extends AbstractStepHandler {

   private static final String VERB = "TODO";

   public static final ScenarioStepVerb PAST = ScenarioStepVerb.pastTense(VERB);
   public static final ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense(VERB);
   public static final ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense(VERB);

   public TodoStepHandler() {
      register(PAST, PRESENT, FUTURE);
   }

   @Override
   protected void doValidateStep(IValidationContext<IScenarioStep> context) {
      IScenarioStep step = context.getObject();
      context.declare(Severity.WARNING, "TODO: " + String.join(" ", step.getParameters()), step).getKeyword();
   }

}
