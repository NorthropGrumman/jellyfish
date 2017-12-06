/**
***************************************** UNCLASSIFIED ******************************************
*************************************************************************************************
************************* Copyright 2017 Northrop Grumman Corporation ***************************
* Author: Generated
*/
package com.ngc.seaside.threateval.ctps.event.classifier.datatype;

/**
 * This file has been generated and should not be edited directly.
 * @author Generated
 */
public class Classification {

   public static final String TOPIC_NAME = "/data/com.ngc.seaside.threateval.ctps.event.classifier.datatype.Classification";

   public static final com.ngc.blocs.service.event.api.IEventTopic<Classification> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, Classification.class);

   private int trackId;
   private com.ngc.seaside.threateval.ctps.event.classifier.datatype.ClassificationEnum objectType;

   public int getTrackId() {
      return trackId;
   }

   public Classification setTrackId(int trackId) {
      this.trackId = trackId;
      return this;
   }

   public com.ngc.seaside.threateval.ctps.event.classifier.datatype.ClassificationEnum getObjectType() {
      return objectType;
   }

   public Classification setObjectType(com.ngc.seaside.threateval.ctps.event.classifier.datatype.ClassificationEnum objectType) {
      this.objectType = objectType;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
         this.trackId,         this.objectType      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof Classification)) {
         return false;
      }

      Classification that = (Classification) obj;

      return          java.util.Objects.equals(this.trackId, that.trackId) &&         java.util.Objects.equals(this.objectType, that.objectType)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName()).append(" - ");
      builder.append("[trackId=").append(trackId).append("]");
      builder.append("[objectType=").append(objectType).append("]");
      return builder.toString();
   }

}
