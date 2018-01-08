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
