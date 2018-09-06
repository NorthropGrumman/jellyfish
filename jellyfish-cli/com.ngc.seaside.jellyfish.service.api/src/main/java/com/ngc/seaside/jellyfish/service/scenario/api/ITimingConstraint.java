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
package com.ngc.seaside.jellyfish.service.scenario.api;

import java.util.concurrent.TimeUnit;

/**
 * A timing constraint is a constraint that is applied to {@link IMessagingFlow}s that limits the duration of those
 * flows.
 */
public interface ITimingConstraint {

   /**
    * Gets the max time a {@link IMessagingFlow} is allowed to take.
    *
    * @return the max time a {@link IMessagingFlow} is allowed to take
    */
   double getMaxTime();

   /**
    * Gets the time unit associated with the duration.
    *
    * @return the time unit associated with the duration
    */
   TimeUnit getTimeUnit();

}
