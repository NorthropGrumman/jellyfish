/**
***************************************** UNCLASSIFIED ******************************************
*************************************************************************************************
************************* Copyright 2017 Northrop Grumman Corporation ***************************
* Author: Generated
*/
package com.ngc.seaside.threateval.ctps.event.common.datatype;

/**
 * This file has been generated and should not be edited directly.
 * @author Generated
 */
public class Metadata {

   public static final String TOPIC_NAME = "/data/com.ngc.seaside.threateval.ctps.event.common.datatype.Metadata";

   public static final com.ngc.blocs.service.event.api.IEventTopic<Metadata> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, Metadata.class);

   private java.lang.String correlationEventId;

   public java.lang.String getCorrelationEventId() {
      return correlationEventId;
   }

   public Metadata setCorrelationEventId(java.lang.String correlationEventId) {
      this.correlationEventId = correlationEventId;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
         this.correlationEventId      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof Metadata)) {
         return false;
      }

      Metadata that = (Metadata) obj;

      return          java.util.Objects.equals(this.correlationEventId, that.correlationEventId)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName()).append(" - ");
      builder.append("[correlationEventId=").append(correlationEventId).append("]");
      return builder.toString();
   }

}
