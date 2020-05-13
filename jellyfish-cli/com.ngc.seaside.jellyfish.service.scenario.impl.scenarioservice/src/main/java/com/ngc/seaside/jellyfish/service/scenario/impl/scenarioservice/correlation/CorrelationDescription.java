/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
