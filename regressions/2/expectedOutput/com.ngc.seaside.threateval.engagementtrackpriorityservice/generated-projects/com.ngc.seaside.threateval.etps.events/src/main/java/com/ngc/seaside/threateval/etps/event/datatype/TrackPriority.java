/**
***************************************** UNCLASSIFIED ******************************************
*************************************************************************************************
************************* Copyright 2017 Northrop Grumman Corporation ***************************
* Author: Generated
*/
package com.ngc.seaside.threateval.etps.event.datatype;

/**
 * This file has been generated and should not be edited directly.
 * @author Generated
 */
public class TrackPriority {

   public static final String TOPIC_NAME = "/data/com.ngc.seaside.threateval.etps.event.datatype.TrackPriority";

   public static final com.ngc.blocs.service.event.api.IEventTopic<TrackPriority> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, TrackPriority.class);

   private int trackId;
   private java.lang.String sourceId;
   private float priority;

   public int getTrackId() {
      return trackId;
   }

   public TrackPriority setTrackId(int trackId) {
      this.trackId = trackId;
      return this;
   }

   public java.lang.String getSourceId() {
      return sourceId;
   }

   public TrackPriority setSourceId(java.lang.String sourceId) {
      this.sourceId = sourceId;
      return this;
   }

   public float getPriority() {
      return priority;
   }

   public TrackPriority setPriority(float priority) {
      this.priority = priority;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
         this.trackId,         this.sourceId,         this.priority      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof TrackPriority)) {
         return false;
      }

      TrackPriority that = (TrackPriority) obj;

      return          java.util.Objects.equals(this.trackId, that.trackId) &&         java.util.Objects.equals(this.sourceId, that.sourceId) &&         java.util.Objects.equals(this.priority, that.priority)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName()).append(" - ");
      builder.append("[trackId=").append(trackId).append("]");
      builder.append("[sourceId=").append(sourceId).append("]");
      builder.append("[priority=").append(priority).append("]");
      return builder.toString();
   }

}
