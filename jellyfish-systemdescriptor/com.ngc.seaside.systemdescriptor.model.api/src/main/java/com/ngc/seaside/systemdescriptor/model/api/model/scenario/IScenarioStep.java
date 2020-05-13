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
package com.ngc.seaside.systemdescriptor.model.api.model.scenario;

import java.util.List;

/**
 * Represents an individual step or statement in an {@link IScenario}.  Operations that change the state of this object
 * may throw {@code UnsupportedOperationException}s if the object is immutable.
 */
public interface IScenarioStep {

   /**
    * Gets the keyword for this scenario step.  This is usually a verb.  Its tense may vary depending on the type of the
    * step this is.
    *
    * @return the keyword for this scenario step
    * @see IScenario#getGivens()
    * @see IScenario#getWhens()
    * @see IScenario#getThens()
    */
   String getKeyword();

   /**
    * Sets the keyword for this scenario step.  This is usually a verb.  Its tense may vary depending on the type of the
    * step this is.
    *
    * @param keyword the keyword for this scenario step
    * @return this step
    * @see IScenario#getGivens()
    * @see IScenario#getWhens()
    * @see IScenario#getThens()
    */
   IScenarioStep setKeyword(String keyword);

   /**
    * Gets the parameters that follow the keyword in the order they occur.  The returned list may not be modifiable if
    * this object is immutable.
    *
    * @return the parameters that follow the keyword
    */
   List<String> getParameters();

   /**
    * Gets the parent scenario that contains this step.
    *
    * @return the parent scenario that contains this step
    */
   IScenario getParent();
}
