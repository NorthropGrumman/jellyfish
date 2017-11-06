package com.ngc.seaside.jellyfish.service.scenario.correlation.api;

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;

import java.util.Collection;

public interface ICorrelationDescription {

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

   /**
    * Gets a collection of the correlation expressions that must be satisfied in order for the correlation to be
    * complete.  These are expressions that are recorded in {@code when} steps of a scenario that use the {@code
    * correlating} verb.  Each expression contains a right and left hand operator which can be investigated to form the
    * complete expression.
    *
    * @return the correlation expressions described in the {@code when} steps of the associated scenario
    */
   Collection<ICorrelationExpression> getCompletenessExpressions();

   /**
    * Gets a collection of correlation expressions which reference the given input field as either the right or left
    * hand operator.
    */
   Collection<ICorrelationExpression> getCompletenessExpressionForInput(IDataReferenceField inputField);

   /**
    * Gets a collection of correlation expressions that describe how published data should be setup before publishing.
    * These are expressions that are recorded in {@code then} steps of a scenario that use the {@code willCorrelate}
    * verb. Unlike {@link #getCompletenessExpressions() completeness expressions}, these expressions describe how to
    * set the correlation event ID on the outgoing message. In this case, the left hand operator is a message that has
    * been received. The right hand operator is the outgoing message that should be updated.
    *
    * @return the correlation expressions described in the {@code then} steps of the associated scenario.
    */
   Collection<ICorrelationExpression> getCorrelationExpressions();

   /**
    * Gets a collection of correlation expressions which reference the given output field in the right hand operator.
    * Note in most cases, this list is either empty or contains only one element.
    */
   Collection<ICorrelationExpression> getCorrelationExpressionForOutput(IDataReferenceField outputField);

}
