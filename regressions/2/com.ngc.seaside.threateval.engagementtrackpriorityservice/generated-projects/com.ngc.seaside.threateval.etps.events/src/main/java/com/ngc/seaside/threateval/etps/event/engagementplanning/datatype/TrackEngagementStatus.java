/**
***************************************** UNCLASSIFIED ******************************************
*************************************************************************************************
************************* Copyright 2017 Northrop Grumman Corporation ***************************
* Author: Generated
*/
package com.ngc.seaside.threateval.etps.event.engagementplanning.datatype;

/**
 * This file has been generated and should not be edited directly.
 * @author Generated
 */
public class TrackEngagementStatus {

   public static final String TOPIC_NAME = "/data/com.ngc.seaside.threateval.etps.event.engagementplanning.datatype.TrackEngagementStatus";

   public static final com.ngc.blocs.service.event.api.IEventTopic<TrackEngagementStatus> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, TrackEngagementStatus.class);

   private int trackId;
   private int plannedEngagementCount;
   private float probabilityOfKill;

   public int getTrackId() {
      return trackId;
   }

   public TrackEngagementStatus setTrackId(int trackId) {
      this.trackId = trackId;
      return this;
   }

   public int getPlannedEngagementCount() {
      return plannedEngagementCount;
   }

   public TrackEngagementStatus setPlannedEngagementCount(int plannedEngagementCount) {
      this.plannedEngagementCount = plannedEngagementCount;
      return this;
   }

   public float getProbabilityOfKill() {
      return probabilityOfKill;
   }

   public TrackEngagementStatus setProbabilityOfKill(float probabilityOfKill) {
      this.probabilityOfKill = probabilityOfKill;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
         this.trackId,         this.plannedEngagementCount,         this.probabilityOfKill      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof TrackEngagementStatus)) {
         return false;
      }

      TrackEngagementStatus that = (TrackEngagementStatus) obj;

      return          java.util.Objects.equals(this.trackId, that.trackId) &&         java.util.Objects.equals(this.plannedEngagementCount, that.plannedEngagementCount) &&         java.util.Objects.equals(this.probabilityOfKill, that.probabilityOfKill)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName()).append(" - ");
      builder.append("[trackId=").append(trackId).append("]");
      builder.append("[plannedEngagementCount=").append(plannedEngagementCount).append("]");
      builder.append("[probabilityOfKill=").append(probabilityOfKill).append("]");
      return builder.toString();
   }

}
