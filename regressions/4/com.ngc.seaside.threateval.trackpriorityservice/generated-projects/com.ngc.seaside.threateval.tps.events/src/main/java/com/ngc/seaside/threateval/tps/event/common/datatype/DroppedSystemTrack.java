/**
***************************************** UNCLASSIFIED ******************************************
*************************************************************************************************
************************* Copyright 2017 Northrop Grumman Corporation ***************************
* Author: Generated
*/
package com.ngc.seaside.threateval.tps.event.common.datatype;

/**
 * This file has been generated and should not be edited directly.
 * @author Generated
 */
public class DroppedSystemTrack {

   public static final String TOPIC_NAME = "/data/com.ngc.seaside.threateval.tps.event.common.datatype.DroppedSystemTrack";

   public static final com.ngc.blocs.service.event.api.IEventTopic<DroppedSystemTrack> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, DroppedSystemTrack.class);

   private com.ngc.seaside.threateval.tps.event.common.datatype.SystemTrackIdentifier identifier;

   public com.ngc.seaside.threateval.tps.event.common.datatype.SystemTrackIdentifier getIdentifier() {
      return identifier;
   }

   public DroppedSystemTrack setIdentifier(com.ngc.seaside.threateval.tps.event.common.datatype.SystemTrackIdentifier identifier) {
      this.identifier = identifier;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
         this.identifier      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof DroppedSystemTrack)) {
         return false;
      }

      DroppedSystemTrack that = (DroppedSystemTrack) obj;

      return          java.util.Objects.equals(this.identifier, that.identifier)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName()).append(" - ");
      builder.append("[identifier=").append(identifier).append("]");
      return builder.toString();
   }

}
