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

   /**
    * Constructor.
    *
    * @param handler the correlate step handler
    * @param step    the scenario step
    */
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
      switch (end.getType()) {
         case DATA:
            return end.getReferencedDataType();
         case ENUM:
            return end.getReferencedEnumeration();
         default:
            throw new IllegalStateException("Cannot get reference type for data type " + end.getType());
      }
   }

}
