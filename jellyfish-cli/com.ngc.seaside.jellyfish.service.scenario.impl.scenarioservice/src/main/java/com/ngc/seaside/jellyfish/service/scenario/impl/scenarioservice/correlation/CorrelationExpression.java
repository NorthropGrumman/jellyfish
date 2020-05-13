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
