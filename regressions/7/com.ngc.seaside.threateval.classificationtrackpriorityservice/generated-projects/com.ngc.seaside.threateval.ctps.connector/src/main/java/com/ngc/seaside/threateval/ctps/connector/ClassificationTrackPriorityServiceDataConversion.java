package com.ngc.seaside.threateval.ctps.connector;

public class ClassificationTrackPriorityServiceDataConversion {

   public static com.ngc.seaside.threateval.ctps.event.classifier.datatype.Classification convert(com.ngc.seaside.threateval.ctps.classifier.datatype.Classification from) {
      com.ngc.seaside.threateval.ctps.event.classifier.datatype.Classification to = new com.ngc.seaside.threateval.ctps.event.classifier.datatype.Classification();

      to.setTrackId(from.getTrackId());
      to.setObjectType(convert(from.getObjectType()));
      to.setHeader(convert(from.getHeader()));

      return to;
   }

   public static com.ngc.seaside.threateval.ctps.event.classifier.datatype.ClassificationEnum convert(com.ngc.seaside.threateval.ctps.classifier.datatype.ClassificationEnum from) {
      final com.ngc.seaside.threateval.ctps.event.classifier.datatype.ClassificationEnum to;

      switch (from) {
      case FISH_HEAD:
         to = com.ngc.seaside.threateval.ctps.event.classifier.datatype.ClassificationEnum.FISH_HEAD;
         break;
      case BACKBONE:
         to = com.ngc.seaside.threateval.ctps.event.classifier.datatype.ClassificationEnum.BACKBONE;
         break;
      case FIN:
         to = com.ngc.seaside.threateval.ctps.event.classifier.datatype.ClassificationEnum.FIN;
         break;
      case TAIL:
         to = com.ngc.seaside.threateval.ctps.event.classifier.datatype.ClassificationEnum.TAIL;
         break;
      default:
         throw new IllegalArgumentException("Unknown enum: " + from);
      }

      return to;
   }

   public static com.ngc.seaside.threateval.ctps.event.common.datatype.Metadata convert(com.ngc.seaside.threateval.ctps.common.datatype.Metadata from) {
      com.ngc.seaside.threateval.ctps.event.common.datatype.Metadata to = new com.ngc.seaside.threateval.ctps.event.common.datatype.Metadata();

      to.setCorrelationEventId(from.getCorrelationEventId());

      return to;
   }

   public static com.ngc.seaside.threateval.ctps.datatype.TrackPriority convert(com.ngc.seaside.threateval.ctps.event.datatype.TrackPriority from) {
      com.ngc.seaside.threateval.ctps.datatype.TrackPriority.Builder to = com.ngc.seaside.threateval.ctps.datatype.TrackPriority.newBuilder();

      to.setTrackId(from.getTrackId());
      to.setSourceId(from.getSourceId());
      to.setPriority(from.getPriority());
      to.setHeader(convert(from.getHeader()));

      return to.build();
   }

   public static com.ngc.seaside.threateval.ctps.common.datatype.Metadata convert(com.ngc.seaside.threateval.ctps.event.common.datatype.Metadata from) {
      com.ngc.seaside.threateval.ctps.common.datatype.Metadata.Builder to = com.ngc.seaside.threateval.ctps.common.datatype.Metadata.newBuilder();

      to.setCorrelationEventId(from.getCorrelationEventId());

      return to.build();
   }

}
