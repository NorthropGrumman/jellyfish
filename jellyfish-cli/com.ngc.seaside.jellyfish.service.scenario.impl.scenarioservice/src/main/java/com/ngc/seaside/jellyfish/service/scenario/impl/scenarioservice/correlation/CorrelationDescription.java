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
package com.ngc.seaside.jellyfish.service.scenario.impl.scenarioservice.correlation;

import com.ngc.seaside.jellyfish.service.scenario.correlation.api.ICorrelationDescription;
import com.ngc.seaside.jellyfish.service.scenario.correlation.api.ICorrelationExpression;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.CorrelateStepHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CorrelationDescription implements ICorrelationDescription {

   private List<ICorrelationExpression> completenessExpressions;
   private List<ICorrelationExpression> correlationExpressions;

   /**
    * Constructor.
    *
    * @param scenario the scenario
    * @param handler  the step handler
    */
   public CorrelationDescription(IScenario scenario, CorrelateStepHandler handler) {
      completenessExpressions = new ArrayList<>(scenario.getWhens().size());
      correlationExpressions = new ArrayList<>(scenario.getThens().size());

      for (IScenarioStep step : scenario.getSteps(CorrelateStepHandler.PRESENT.getVerb())) {
         completenessExpressions.add(new CorrelationExpression(handler, step));
      }

      for (IScenarioStep step : scenario.getSteps(CorrelateStepHandler.FUTURE.getVerb())) {
         correlationExpressions.add(new CorrelationExpression(handler, step));
      }

   }

   @Override
   public Collection<ICorrelationExpression> getCompletenessExpressions() {
      return completenessExpressions;
   }

   @Override
   public Collection<ICorrelationExpression> getCompletenessExpressionForInput(IDataReferenceField inputField) {
      return completenessExpressions.stream()
            .filter(expression -> expression.getLeftHandOperand().getStart().equals(inputField)
                  || expression.getRightHandOperand().getStart().equals(inputField))
            .collect(Collectors.toList());
   }

   @Override
   public Collection<ICorrelationExpression> getCorrelationExpressions() {
      return correlationExpressions;
   }

   @Override
   public Collection<ICorrelationExpression> getCorrelationExpressionForOutput(IDataReferenceField outputField) {
      return correlationExpressions.stream()
            .filter(expression -> expression.getLeftHandOperand().getStart().equals(outputField)
                  || expression.getRightHandOperand().getStart().equals(outputField))
            .collect(Collectors.toList());
   }

}
