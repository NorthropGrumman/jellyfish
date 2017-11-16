package com.ngc.seaside.jellyfish.service.scenario.correlation.api;

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataPath;

/**
 * Represents a correlation expression as described in a System Descriptor scenario.  An expression is two operands and
 * an operator.  This interface represents both expressions that are part of a {@code when} step as well as expressions
 * that part of a {@code then} step.
 *
 * <p/>
 *
 * As an example, consider the following scenario:
 * <pre>
 *    scenario calculateTrackPriority {
 *      when receiving systemTrack
 *      and receiving impactAssessment
 *      and correlating systemTrack.header.correlationEventId to impactAssessment.header.correlationEventId
 *      then willCorrelate systemTrack.header.correlationEventId to trackPriority.header.correlationEventId
 *      and willPublish trackPriority
 * }
 * </pre>
 *
 * In this case, there is one expression in a when step and and one expression in a then step.  The when step expression
 * is:
 * <pre>
 *    correlating systemTrack.header.correlationEventId to impactAssessment.header.correlationEventId
 * </pre>
 * The left hand operand is the data path {@code systemTrack.header.correlationEventId} and the right hand operand is
 * the data path {@code impactAssessment.header.correlationEventId}.  The operator is equals.
 *
 * The then step expression is:
 * <pre>
 *    willCorrelate systemTrack.header.correlationEventId to trackPriority.header.correlationEventId
 * </pre>
 * The left hand operand is {@code systemTrack.header.correlationEventId} and the right hand operand is {@code
 * trackPriority.header.correlationEventId}.  Again, the operator is equals.
 */
public interface ICorrelationExpression {

   /**
    * Defines the operators that can be used form expressions.
    */
   enum Operator {
      /**
       * Indicates that the left and right hand operands must be equals.
       */
      EQUALS
   }

   /**
    * Gets the operator used by this expression.
    */
   Operator getOperator();

   /**
    * Gets the left hand operand used by this expression.
    */
   IDataPath getLeftHandOperand();

   /**
    * Gets the right hand operand used by this expression.
    */
   IDataPath getRightHandOperand();

   /**
    * Gets the data type of the correlation event ID as defined by this correlation.  If the value is {@link
    * DataTypes#DATA} or {@link DataTypes#ENUM}, {@link #getCorrelationEventIdReferenceType()} can be used to obtain the
    * details of the complex type.  Otherwise, the type of the correlation event ID is a primitive.
    *
    * @return the data type of the correlation event ID used by this correlation
    */
   DataTypes getCorrelationEventIdType();

   /**
    * Gets the complex type of the ID of the correlation event used by this correlation.  This value is only set if
    * {@link #getCorrelationEventIdType()} returns {@link DataTypes#DATA} or {@link DataTypes#ENUM}.
    *
    * @return the complex type of this correlation event's ID
    */
   INamedChild<IPackage> getCorrelationEventIdReferenceType();

}
