package com.ngc.seaside.threateval.tps.connector;

public class TrackPriorityServiceDataConversion {

   public static com.ngc.seaside.threateval.tps.event.datatype.TrackPriority convert(com.ngc.seaside.threateval.tps.datatype.TrackPriority from) {
      com.ngc.seaside.threateval.tps.event.datatype.TrackPriority to = new com.ngc.seaside.threateval.tps.event.datatype.TrackPriority();

      to.setTrackId(from.getTrackId());
      to.setSourceId(from.getSourceId());
      to.setPriority(from.getPriority());

      return to;
   }

   public static com.ngc.seaside.threateval.tps.event.common.datatype.DroppedSystemTrack convert(com.ngc.seaside.threateval.tps.common.datatype.DroppedSystemTrack from) {
      com.ngc.seaside.threateval.tps.event.common.datatype.DroppedSystemTrack to = new com.ngc.seaside.threateval.tps.event.common.datatype.DroppedSystemTrack();

      to.setIdentifier(convert(from.getIdentifier()));

      return to;
   }

   public static com.ngc.seaside.threateval.tps.event.common.datatype.SystemTrackIdentifier convert(com.ngc.seaside.threateval.tps.common.datatype.SystemTrackIdentifier from) {
      com.ngc.seaside.threateval.tps.event.common.datatype.SystemTrackIdentifier to = new com.ngc.seaside.threateval.tps.event.common.datatype.SystemTrackIdentifier();

      to.setIdentifier(from.getIdentifier());

      return to;
   }

   public static com.ngc.seaside.threateval.tps.datatype.PrioritizedSystemTrackIdentifiers convert(com.ngc.seaside.threateval.tps.event.datatype.PrioritizedSystemTrackIdentifiers from) {
      com.ngc.seaside.threateval.tps.datatype.PrioritizedSystemTrackIdentifiers.Builder to = com.ngc.seaside.threateval.tps.datatype.PrioritizedSystemTrackIdentifiers.newBuilder();

      for (com.ngc.seaside.threateval.tps.event.common.datatype.SystemTrackIdentifier value : from.getIdentifiers()) {
         to.addIdentifiers(convert(value));
      }

      return to.build();
   }

   public static com.ngc.seaside.threateval.tps.common.datatype.SystemTrackIdentifier convert(com.ngc.seaside.threateval.tps.event.common.datatype.SystemTrackIdentifier from) {
      com.ngc.seaside.threateval.tps.common.datatype.SystemTrackIdentifier.Builder to = com.ngc.seaside.threateval.tps.common.datatype.SystemTrackIdentifier.newBuilder();

      to.setIdentifier(from.getIdentifier());

      return to.build();
   }

}
