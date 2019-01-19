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
package com.ngc.seaside.jellyfish.service.scenario.correlation.api;

import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;

import java.util.Collection;

public interface ICorrelationDescription {

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
