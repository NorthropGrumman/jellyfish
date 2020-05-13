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
