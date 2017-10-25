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
public class SystemTrackIdentifier {

   public static final String TOPIC_NAME = "/data/com.ngc.seaside.threateval.tps.event.common.datatype.SystemTrackIdentifier";

   public static final com.ngc.blocs.service.event.api.IEventTopic<SystemTrackIdentifier> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, SystemTrackIdentifier.class);

   private int identifier;

   public int getIdentifier() {
      return identifier;
   }

   public SystemTrackIdentifier setIdentifier(int identifier) {
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

      if(!(obj instanceof SystemTrackIdentifier)) {
         return false;
      }

      SystemTrackIdentifier that = (SystemTrackIdentifier) obj;

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
