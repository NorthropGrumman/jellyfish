package com.ngc.seaside.threateval.etps.connector;

public class EngagementTrackPriorityServiceDataConversion {

   public static com.ngc.seaside.threateval.etps.event.engagementplanning.datatype.TrackEngagementStatus convert(com.ngc.seaside.threateval.etps.engagementplanning.datatype.TrackEngagementStatus from) {
      com.ngc.seaside.threateval.etps.event.engagementplanning.datatype.TrackEngagementStatus to = new com.ngc.seaside.threateval.etps.event.engagementplanning.datatype.TrackEngagementStatus();

      to.setPlannedEngagementCount(from.getPlannedEngagementCount());
      to.setProbabilityOfKill(from.getProbabilityOfKill());
      to.setTrackId(from.getTrackId());

      return to;
   }

   public static com.ngc.seaside.threateval.etps.datatype.TrackPriority convert(com.ngc.seaside.threateval.etps.event.datatype.TrackPriority from) {
      com.ngc.seaside.threateval.etps.datatype.TrackPriority.Builder to = com.ngc.seaside.threateval.etps.datatype.TrackPriority.newBuilder();

      to.setSourceId(from.getSourceId());
      to.setTrackId(from.getTrackId());
      to.setPriority(from.getPriority());

      return to.build();
   }

}
