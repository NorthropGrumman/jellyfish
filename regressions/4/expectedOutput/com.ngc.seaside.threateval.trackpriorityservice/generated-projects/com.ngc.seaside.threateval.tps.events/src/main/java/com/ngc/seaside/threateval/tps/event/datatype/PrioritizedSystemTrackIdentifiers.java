/**
***************************************** UNCLASSIFIED ******************************************
*************************************************************************************************
************************* Copyright 2017 Northrop Grumman Corporation ***************************
* Author: Generated
*/
package com.ngc.seaside.threateval.tps.event.datatype;

/**
 * This file has been generated and should not be edited directly.
 * @author Generated
 */
public class PrioritizedSystemTrackIdentifiers {

   public static final String TOPIC_NAME = "/data/com.ngc.seaside.threateval.tps.event.datatype.PrioritizedSystemTrackIdentifiers";

   public static final com.ngc.blocs.service.event.api.IEventTopic<PrioritizedSystemTrackIdentifiers> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, PrioritizedSystemTrackIdentifiers.class);

   private java.util.List<com.ngc.seaside.threateval.tps.event.common.datatype.SystemTrackIdentifier> identifiers;

   public java.util.List<com.ngc.seaside.threateval.tps.event.common.datatype.SystemTrackIdentifier> getIdentifiers() {
      return identifiers;
   }

   public PrioritizedSystemTrackIdentifiers setIdentifiers(java.util.List<com.ngc.seaside.threateval.tps.event.common.datatype.SystemTrackIdentifier> identifiers) {
      this.identifiers = identifiers;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
         this.identifiers      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof PrioritizedSystemTrackIdentifiers)) {
         return false;
      }

      PrioritizedSystemTrackIdentifiers that = (PrioritizedSystemTrackIdentifiers) obj;

      return          java.util.Objects.equals(this.identifiers, that.identifiers)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName()).append(" - ");
      builder.append("[identifiers=").append(identifiers).append("]");
      return builder.toString();
   }

}
