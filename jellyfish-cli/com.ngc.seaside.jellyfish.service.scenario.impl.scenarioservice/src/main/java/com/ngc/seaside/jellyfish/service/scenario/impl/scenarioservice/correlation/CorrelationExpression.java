package com.ngc.seaside.jellyfish.service.scenario.impl.scenarioservice.correlation;

import com.ngc.seaside.jellyfish.service.scenario.correlation.api.ICorrelationExpression;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataPath;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.CorrelateStepHandler;

public class CorrelationExpression implements ICorrelationExpression {

   private final IDataPath left;
   private final IDataPath right;
   
   public CorrelationExpression(CorrelateStepHandler handler, IScenarioStep step) {
      IDataPath left = handler.getLeftPath(step);
      IDataPath right = handler.getRightPath(step);
      if (left.isOutput()) {
         this.left = right;
         this.right = left;
      } else {
         this.left = left;
         this.right = right;
      }
   }
   
   @Override
   public Operator getOperator() {
      return Operator.EQUALS;
   }

   @Override
   public IDataPath getLeftHandOperand() {
      return left;
   }

   @Override
   public IDataPath getRightHandOperand() {
      return right;
   }

   @Override
   public DataTypes getCorrelationEventIdType() {
      return left.getEnd().getType();
   }

   @Override
   public INamedChild<IPackage> getCorrelationEventIdReferenceType() {
      IDataField end = left.getEnd();
      switch(end.getType()) {
      case DATA:
         return end.getReferencedDataType();
      case ENUM:
         return end.getReferencedEnumeration();
      default:
         throw new IllegalStateException("Cannot get reference type for data type " + end.getType());
      }
   }
   
}
