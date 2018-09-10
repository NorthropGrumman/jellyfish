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
