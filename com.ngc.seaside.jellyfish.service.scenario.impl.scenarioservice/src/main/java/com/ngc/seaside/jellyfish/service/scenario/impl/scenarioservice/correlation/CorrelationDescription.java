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
