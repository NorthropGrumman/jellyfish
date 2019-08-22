/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.api.AbstractStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;

public class BeginCorrelationEventStepHandler extends AbstractStepHandler {

   public static final ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("hasBeganCorrelationEvent");
   public static final ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("beginningCorrelationEvent");
   public static final ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willBeginCorrelationEvent");

   public BeginCorrelationEventStepHandler() {
      register(PAST, PRESENT, FUTURE);
   }

   // TODO TH: This handler is not implemented completely.  It is just implemented enough to get around build errors.

   @Override
   protected void doValidateStep(IValidationContext<IScenarioStep> context) {
   }
}