package com.ngc.seaside.threateval.datps.connector;

public class DefendedAreaTrackPriorityServiceDataConversion {

   public static com.ngc.seaside.threateval.datps.event.common.datatype.SystemTrack convert(com.ngc.seaside.threateval.datps.common.datatype.SystemTrack from) {
      com.ngc.seaside.threateval.datps.event.common.datatype.SystemTrack to = new com.ngc.seaside.threateval.datps.event.common.datatype.SystemTrack();

      to.setIdentifier(convert(from.getIdentifier()));
      to.setState(convert(from.getState()));
      to.setCov(convert(from.getCov()));

      return to;
   }

   public static com.ngc.seaside.threateval.datps.event.common.datatype.SystemTrackIdentifier convert(com.ngc.seaside.threateval.datps.common.datatype.SystemTrackIdentifier from) {
      com.ngc.seaside.threateval.datps.event.common.datatype.SystemTrackIdentifier to = new com.ngc.seaside.threateval.datps.event.common.datatype.SystemTrackIdentifier();

      to.setIdentifier(from.getIdentifier());

      return to;
   }

   public static com.ngc.seaside.threateval.datps.event.common.datatype.StateVector convert(com.ngc.seaside.threateval.datps.common.datatype.StateVector from) {
      com.ngc.seaside.threateval.datps.event.common.datatype.StateVector to = new com.ngc.seaside.threateval.datps.event.common.datatype.StateVector();

      to.setEcefPosition(convert(from.getEcefPosition()));
      to.setValidityTime(convert(from.getValidityTime()));
      to.setEcefVelocity(convert(from.getEcefVelocity()));
      to.setEcefAcc(convert(from.getEcefAcc()));

      return to;
   }

   public static com.ngc.seaside.threateval.datps.event.common.datatype.Covariance convert(com.ngc.seaside.threateval.datps.common.datatype.Covariance from) {
      com.ngc.seaside.threateval.datps.event.common.datatype.Covariance to = new com.ngc.seaside.threateval.datps.event.common.datatype.Covariance();


      return to;
   }

   public static com.ngc.seaside.threateval.datps.event.common.datatype.GPSTime convert(com.ngc.seaside.threateval.datps.common.datatype.GPSTime from) {
      com.ngc.seaside.threateval.datps.event.common.datatype.GPSTime to = new com.ngc.seaside.threateval.datps.event.common.datatype.GPSTime();

      to.setSecondsFromGPS(from.getSecondsFromGPS());
      to.setMicroseconds(from.getMicroseconds());

      return to;
   }

   public static com.ngc.seaside.threateval.datps.event.common.datatype.Vector3 convert(com.ngc.seaside.threateval.datps.common.datatype.Vector3 from) {
      com.ngc.seaside.threateval.datps.event.common.datatype.Vector3 to = new com.ngc.seaside.threateval.datps.event.common.datatype.Vector3();

      to.setY(from.getY());
      to.setZ(from.getZ());
      to.setX(from.getX());

      return to;
   }

   public static com.ngc.seaside.threateval.datps.atype.TrackPriority convert(com.ngc.seaside.threateval.datps.event.atype.TrackPriority from) {
      com.ngc.seaside.threateval.datps.atype.TrackPriority.Builder to = com.ngc.seaside.threateval.datps.atype.TrackPriority.newBuilder();

      to.setSourceId(from.getSourceId());
      to.setTrackId(from.getTrackId());
      to.setPriority(from.getPriority());

      return to.build();
   }

}
